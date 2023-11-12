/*
 * Copyright (c) 2023. Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Entry;
import com.coyotesong.microservice.rss.model.Feed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.coyotesong.microservice.rss.internal.ConversionUtilities.toUrl;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Convert SyndFeed to Feed
 */
public class SyndFeedToFeedConverter extends AbstractConverter implements SyndConverter<SyndFeed, Feed> {
    private static final Logger LOG = LoggerFactory.getLogger(SyndFeedToFeedConverter.class);

    public static final SyndFeedToFeedConverter INSTANCE = new SyndFeedToFeedConverter();

    private final SyndCategoryToCategoryConverter categoryConverter = new SyndCategoryToCategoryConverter();
    private final SyndContentToContentConverter contentConverter = new SyndContentToContentConverter();
    private final SyndEnclosureToEnclosureConverter enclosureConverter = new SyndEnclosureToEnclosureConverter();
    private final SyndImageToImageConverter imageConverter = new SyndImageToImageConverter();
    private final SyndLinkToLinkConverter linkConverter = new SyndLinkToLinkConverter();
    private final SyndPersonToPersonConverter personConverter = new SyndPersonToPersonConverter();

    private final SyndEntryToEntryConverter entryConverter = new SyndEntryToEntryConverter(
            categoryConverter, contentConverter, enclosureConverter, linkConverter, personConverter);

    private SyndFeedToFeedConverter() {
    }

    /**
     * Convert contents of file containing ROME OPML to OPML
     * @param reader RSS object
     * @return our RSS object
     */
    @Nullable
    public Feed from(@NotNull Reader reader) throws ExecutionException {
        final SyndFeedInput input = new SyndFeedInput();
        input.setXmlHealerOn(true);

        try {
            final SyndFeed syndFeed = input.build(reader);
            return from(syndFeed);
        } catch (FeedException e) {
            throw new ExecutionException(e);
        }
    }

    /**
     * Convert SyndFeed to Feed
     * @param syndFeed
     */
    @Nullable
    @Override
    public Feed from(@Nullable SyndFeed syndFeed) {
        if (syndFeed == null) {
            return null;
        }

        if (isBlank(syndFeed.getFeedType())) {
            LOG.error("required field is blank: feedType");
            // throw IllegalArgumentException?
        }

        // note: url is set later

        final Feed feed = new Feed();
        feed.setLive(true);
        if (isNotBlank(syndFeed.getTitle())) {
            feed.setTitle(syndFeed.getTitle());
        }
        if (isNotBlank(syndFeed.getFeedType())) {
            feed.setFeedType(syndFeed.getFeedType());
        }
        if (isNotBlank(syndFeed.getUri())) {
            feed.setXmlLink(syndFeed.getUri());
            feed.setXmlProtocol(syndFeed.getUri().split(":")[0]);
        }
        if (isNotBlank(syndFeed.getLink())) {
            feed.setHtmlUrl(toUrl(syndFeed.getLink()));
        }

        // may be null!
        // feed.setDescription(syndFeed.getDescription());

        if (isNotBlank(syndFeed.getCopyright())) {
            feed.setCopyright(syndFeed.getCopyright());
        }
        if (isNotBlank(syndFeed.getEncoding())) {
            feed.setEncoding(syndFeed.getEncoding());
        }
        if (isNotBlank(syndFeed.getLanguage())) {
            final String[] components = syndFeed.getLanguage().trim().split("-");
            if (components.length == 1) {
                feed.setLanguage(syndFeed.getLanguage());
            } else {
                feed.setLanguage(components[0] + "-" + components[1].toUpperCase());
            }
        }

        if (isNotBlank(syndFeed.getManagingEditor())) {
            feed.setManagingEditor(syndFeed.getManagingEditor());
        }
        if (isNotBlank(syndFeed.getWebMaster())) {
            feed.setWebMaster(syndFeed.getWebMaster());
        }

        // I have errors if I use java.util.Date#toInstant().
        if (syndFeed.getPublishedDate() != null) {
            feed.setPublishedDate(Instant.ofEpochMilli(syndFeed.getPublishedDate().getTime()));
        }

        feed.setAuthorCount(syndFeed.getAuthors().size());
        if (isNotBlank(syndFeed.getAuthor())) {
            feed.setAuthor(syndFeed.getAuthor());
        }

        /*
        List<Module> modules = syndFeed.getModules();
        for (Module module : modules) {
            module.getUri();
        }
         */

        // unimplemented for the moment
        // SyndContent syndFeed.getDescriptionEx();
        // SyndContent syndFeed.getTitleEx()));

        // unused
        // List<Element> syndFeed.getForeignMarkup();
        // List<String> syndFeed.getSupportedFeedTypes();
        // String syndFeed.getStyleSheet();

        // String syndFeed.getDocs();
        // String syndFeed.getGenerator();

        //
        // everything below this requires additional converters
        //

        if (!syndFeed.getAuthors().isEmpty()) {
            feed.setAuthors(syndFeed.getAuthors().stream().map(personConverter::from).collect(Collectors.toList()));
        }

        // sometimes we see a category without a name.
        if (!syndFeed.getCategories().isEmpty()) {
            feed.setCategories(syndFeed.getCategories().stream().map(categoryConverter::from).filter(Objects::nonNull).filter(s -> isNotBlank(s.getName())).collect(Collectors.toList()));
        }

        if (!syndFeed.getContributors().isEmpty()) {
            feed.setContributors(syndFeed.getContributors().stream().map(personConverter::from).collect(Collectors.toList()));
        }

        final List<Entry> entries = new ArrayList<>();
        if (!syndFeed.getEntries().isEmpty()) {
            for (SyndEntry syndEntry : syndFeed.getEntries()) {
                final Entry entry = entryConverter.from(syndEntry);
                entry.setSource(feed);
                entries.add(entry);
            }
            feed.setEntries(entries);
        }

        if (syndFeed.getIcon() != null) {
            feed.setIcon(imageConverter.from(syndFeed.getIcon()));
        }

        if (syndFeed.getImage() != null) {
            feed.setIcon(imageConverter.from(syndFeed.getImage()));
        }

        feed.setLinkCount(syndFeed.getLinks().size());
        if (!syndFeed.getLinks().isEmpty()) {
            feed.setLinks(syndFeed.getLinks().stream().map(linkConverter::from).collect(Collectors.toList()));
        }

        // WireFeed wire = feed.originalWireFeed();
        // wire.getFeedType();
        // wire.getEncoding();
        // wire.getForeignMarkup();
        // wire.getStyleSheet();

        feed.setValid(true);

        return feed;
    }

    /**
     * Convert Feed to SyndFeed
     * @param feed
     * @return
     */
    @Nullable
    @Override
    public SyndFeed to(@Nullable Feed feed) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndFeed> fromType() {
        return SyndFeed.class;
    }

    /**
     * Return the 'to' Type Class
     */
    @NotNull
    @Override
    public Class<Feed> toType() {
        return Feed.class;
    }
}