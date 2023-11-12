package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Feed;
// import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.SyndFeed;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Convert SyndFeed to Feed
 */
public class SyndFeedToFeedConverter implements SyndConverter<SyndFeed, Feed> {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(SyndFeedToFeedConverter.class);

    private final SyndCategoryToCategoryConverter categoryConverter;
    private final SyndEntryToEntryConverter entryConverter;
    private final SyndImageToImageConverter imageConverter;
    private final SyndLinkToLinkConverter linkConverter;
    private final SyndPersonToPersonConverter personConverter;

    /**
     * Constructor
     *
     * @param entryConverter
     */
    public SyndFeedToFeedConverter(SyndCategoryToCategoryConverter categoryConverter, SyndEntryToEntryConverter entryConverter,
                                   SyndImageToImageConverter imageConverter,
                                   SyndLinkToLinkConverter linkConverter, SyndPersonToPersonConverter personConverter) {
        this.categoryConverter = categoryConverter;
        this.entryConverter = entryConverter;
        this.imageConverter = imageConverter;
        this.linkConverter = linkConverter;
        this.personConverter = personConverter;
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

        if (StringUtils.isBlank(syndFeed.getFeedType())) {
            LOG.error("required field is blank: feedType");
            // throw IllegalArgumentException?
        }

        // note: url is set later

        final Feed feed = new Feed();
        feed.setLive(true);
        if (StringUtils.isNotBlank(syndFeed.getTitle())) {
            feed.setTitle(syndFeed.getTitle());
        }
        if (StringUtils.isNotBlank(syndFeed.getFeedType())) {
            feed.setFeedType(syndFeed.getFeedType());
        }
        if (StringUtils.isNotBlank(syndFeed.getUri())) {
            feed.setXmlLink(syndFeed.getUri());
            feed.setXmlProtocol(syndFeed.getUri().split(":")[0]);
        }

        // may be null!
        // feed.setDescription(syndFeed.getDescription());

        if (StringUtils.isNotBlank(syndFeed.getCopyright())) {
            feed.setCopyright(syndFeed.getCopyright());
        }
        if (StringUtils.isNotBlank(syndFeed.getEncoding())) {
            feed.setEncoding(syndFeed.getEncoding());
        }
        if (StringUtils.isNotBlank(syndFeed.getLanguage())) {
            final String[] components = syndFeed.getLanguage().trim().split("-");
            if (components.length == 1) {
                feed.setLanguage(syndFeed.getLanguage());
            } else {
                feed.setLanguage(components[0] + "-" + components[1].toUpperCase());
            }
        }

        if (StringUtils.isNotBlank(syndFeed.getManagingEditor())) {
            feed.setManagingEditor(syndFeed.getManagingEditor());
        }
        if (StringUtils.isNotBlank(syndFeed.getWebMaster())) {
            feed.setWebMaster(syndFeed.getWebMaster());
        }

        // I have errors if I use java.util.Date#toInstant().
        if (syndFeed.getPublishedDate() != null) {
            feed.setPublishedDate(Instant.ofEpochMilli(syndFeed.getPublishedDate().getTime()));
        }

        feed.setAuthorCount(syndFeed.getAuthors().size());
        if (StringUtils.isNotBlank(syndFeed.getAuthor())) {
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

        if (!syndFeed.getCategories().isEmpty()) {
            feed.setCategories(syndFeed.getCategories().stream().map(categoryConverter::from).collect(Collectors.toList()));
        }

        if (!syndFeed.getContributors().isEmpty()) {
            feed.setContributors(syndFeed.getContributors().stream().map(personConverter::from).collect(Collectors.toList()));
        }

        if (!syndFeed.getEntries().isEmpty()) {
            feed.setEntries(syndFeed.getEntries().stream().map(entryConverter::from).collect(Collectors.toList()));
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