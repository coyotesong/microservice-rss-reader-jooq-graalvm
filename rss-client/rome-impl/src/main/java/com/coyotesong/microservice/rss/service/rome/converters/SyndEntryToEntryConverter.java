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
import com.coyotesong.microservice.rss.model.Link;
import com.rometools.rome.feed.synd.SyndEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.coyotesong.microservice.rss.internal.ConversionUtilities.toUrl;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Convert SyndEntry to Entry
 */
public class SyndEntryToEntryConverter extends AbstractConverter  implements SyndConverter<SyndEntry, Entry> {

    private final SyndCategoryToCategoryConverter categoryConverter;
    private final SyndContentToContentConverter contentConverter;
    private final SyndEnclosureToEnclosureConverter enclosureConverter;
    private final SyndLinkToLinkConverter linkConverter;
    private final SyndPersonToPersonConverter personConverter;

    /**
     * Constructor
     */
    public SyndEntryToEntryConverter(SyndCategoryToCategoryConverter categoryConverter,
                                     SyndContentToContentConverter contentConverter,
                                     SyndEnclosureToEnclosureConverter enclosureConverter,
                                     SyndLinkToLinkConverter linkConverter,
                                     SyndPersonToPersonConverter personConverter) {
        this.categoryConverter = categoryConverter;
        this.contentConverter = contentConverter;
        this.enclosureConverter = enclosureConverter;
        this.linkConverter = linkConverter;
        this.personConverter = personConverter;
    }

    /**
     * Convert SyndEntry to Entry
     * @param syndEntry
     */
    @Nullable
    @Override
    public Entry from(@Nullable SyndEntry syndEntry) {
        if (syndEntry == null) {
            return null;
        }

        final Entry entry = new Entry();

        entry.setTitle(syndEntry.getTitle());
        // entry.setTitleEx(new Content(syndEntry.getTitleEx()));
        if (isNotBlank(syndEntry.getLink()) && syndEntry.getLink().toLowerCase().startsWith("http")) {
           entry.setUrl(toUrl(syndEntry.getLink()));
        }

        entry.setLinkCount(syndEntry.getLinks().size());
        if (!syndEntry.getLinks().isEmpty()) {
            entry.setLinks(syndEntry.getLinks().stream().map(linkConverter::from).collect(Collectors.toList()));
        } else if (isNotBlank(syndEntry.getLink())) {
            entry.setLinks(Collections.singletonList(new Link(syndEntry.getLink())));
        } else {
            entry.setLinks(Collections.singletonList(new Link()));
        }

        // I see errors when I use java.util.Date#toInstant()
        if (syndEntry.getPublishedDate() != null) {
            entry.setPublishedDate(Instant.ofEpochMilli(syndEntry.getPublishedDate().getTime()));
        }
        if (syndEntry.getUpdatedDate() != null) {
            entry.setUpdatedDate(Instant.ofEpochMilli(syndEntry.getUpdatedDate().getTime()));
        }

        if (syndEntry.getDescription() != null) {
            entry.setDescription(contentConverter.from(syndEntry.getDescription()));
        }

        if (isNotBlank(syndEntry.getAuthor())) {
            entry.setAuthor(syndEntry.getAuthor());
        }

        if (!syndEntry.getAuthors().isEmpty()) {
            entry.setAuthors(syndEntry.getAuthors().stream().map(personConverter::from).collect(Collectors.toList()));
        }

        if (!syndEntry.getContributors().isEmpty()) {
            entry.setContributors(syndEntry.getContributors().stream().map(personConverter::from).collect(Collectors.toList()));
        }

        if (!syndEntry.getContents().isEmpty()) {
            entry.setContents(syndEntry.getContents().stream().map(contentConverter::from).collect(Collectors.toList()));
        }

        if (!syndEntry.getCategories().isEmpty()) {
            entry.setCategories(syndEntry.getCategories().stream().map(categoryConverter::from).collect(Collectors.toList()));
        }

        if (!syndEntry.getEnclosures().isEmpty()) {
            entry.setEnclosures(syndEntry.getEnclosures().stream().map(enclosureConverter::from).collect(Collectors.toList()));
        }

        // unused
        // syndEntry.getSource();
        // syndEntry.getForeignMarkup();
        // syndEntry.getWireEntry();  // object, not necessarily 'SyndWriteEntry'

        return entry;
    }

    /**
     * Convert Entry to SyndEntry
     * @param entry
     * @return
     */
    @Nullable
    @Override
    public SyndEntry to(@Nullable Entry entry) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndEntry> fromType() {
        return SyndEntry.class;
    }

    /**
     * Return the 'to' Type Class
     */
    // @Override
    @NotNull
    @Override
    public Class<Entry> toType() {
        return Entry.class;
    }
}
