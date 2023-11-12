package com.coyotesong.microservice.rss.service.rome.converters;

import com.rometools.opml.feed.opml.Attribute;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.rss.Category;
/*
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Enclosure;
import com.rometools.rome.feed.rss.Cloud;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Guid;
import com.rometools.rome.feed.rss.Image;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Category;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Generator;
import com.rometools.rome.feed.atom.Person;
import com.rometools.rome.feed.rss.Source
import com.rometools.opml.feed.opml.Opml;
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class AbstractConverter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractConverter.class);

    @Nullable
    protected Instant toInstant(@Nullable Date date) {
        if (date == null || date.getTime() == 0) {
            return null;
        }

        return Instant.ofEpochMilli(date.getTime());
    }

    @Nullable
    protected URL toUrl(@Nullable String url) {
        if (isBlank(url)) {
            return null;
        }

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            LOG.info("MalformedURLException: {}, {}", url, e.getMessage());
            return null;
        }
    }

    /**
     * This provides a map of attributes.
     */
    @NotNull
    protected Map<String, String> fromAttributes(List<Attribute> attributes) {
        // ttrss = popular docker-based rss reader
        @SuppressWarnings("unused")
        final List<String> knownAttr = Arrays.asList("xmlUrl", "htmlUrl", "ttrssSortOrder", "ttrssUpdateInterval");

        if (attributes == null) {
            return Collections.emptyMap();
        }

        return attributes.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
    }

    /**
     * This provides a map of categories. (domain -> value)
     * @param categories
     * @return
     */
    protected Map<String, String> fromCategories(List<Category> categories) {
        if (categories == null) {
            return Collections.emptyMap();
        }

        return categories.stream().collect(Collectors.toMap(Category::getDomain, Category::getValue));
    }

    /**
     * This provides a list of URIs
     * @param modules
     * @return List of URIs
     */
    @NotNull
    protected List<String> fromModules(List<Module> modules) {
        if (modules == null) {
            return Collections.emptyList();
        }

        return modules.stream().map(Module::getUri).collect(Collectors.toList());
    }
}
