package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Entry;
import com.coyotesong.microservice.rss.model.Link;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Convert SyndEntry to Entry
 */
public class SyndEntryToEntryConverter implements SyndConverter<SyndEntry, Entry> {
    private static final Logger LOG = LoggerFactory.getLogger(SyndEntryToEntryConverter.class);

    private final SyndCategoryToCategoryConverter categoryConverter;
    private final SyndContentToContentConverter contentConverter;
    private final SyndLinkToLinkConverter linkConverter;
    private final SyndPersonToPersonConverter personConverter;

    /**
     * Constructor
     */
    public SyndEntryToEntryConverter(SyndCategoryToCategoryConverter categoryConverter,
                                     SyndContentToContentConverter contentConverter,
                                     SyndLinkToLinkConverter linkConverter,
                                     SyndPersonToPersonConverter personConverter) {
        this.categoryConverter = categoryConverter;
        this.contentConverter = contentConverter;
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
        /*
        entry.setTitleEx(new Content(syndEntry.getTitleEx()));
         */
        if (StringUtils.isNotBlank(syndEntry.getLink())) {
            try {
                entry.setUrl(new URL(syndEntry.getLink()));
            } catch (MalformedURLException e) {
                LOG.warn("malformed url: {}", e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(syndEntry.getUri())) {
            entry.setUri(syndEntry.getUri());
        }

        entry.setLinkCount(syndEntry.getLinks().size());
        if (!syndEntry.getLinks().isEmpty()) {
            entry.setLinks(syndEntry.getLinks().stream().map(linkConverter::from).collect(Collectors.toList()));
        } else if (StringUtils.isNotBlank(syndEntry.getLink())) {
            entry.setLinks(Collections.singletonList(new Link(syndEntry.getLink())));
        } else {
            // LOG.info("no links provided:   {}", entry.getTitle());
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

        if (StringUtils.isNotBlank(syndEntry.getAuthor())) {
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

        /*
        if (!syndEntry.getEnclosures().isEmpty()) {
            entry.setenclosures = new ArrayList<>();
            entry.getEnclosures().stream().map(Enclosure::new).forEach(enclosures::add);
        }
         */

        // unused
        // syndEntry.getSource();
        // syndEntry.getForeignMarkup();
        // syndEntry.getWireEntry();  // object
        // syndEntry.getForeignMarkup();

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

    /**
     * Convert SyndFeed to list of Entries
     */
    public List<Entry> of(SyndFeed feed, Integer feedId) {
        if (!feed.getEntries().isEmpty()) {
            return Collections.emptyList();
        }

        // return feed.getEntries().stream().map(this::of).map(e -> { e.setFeedId(feedId); return e; }).collect(Collectors.toList());
        return Collections.emptyList();
        // return feed.getEntries().stream().map(this::of).map(e -> { e.setFeedId(feedId); return e; }).collect(Collectors.toList());
    }
}
