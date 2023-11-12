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

package com.coyotesong.microservice.rss.model;

import com.coyotesong.microservice.rss.internal.ConversionUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RSS Feeds, based on Rome's SyndFeed
 * <p>
 * TODO: Authentication information?
 * </p>
 */
@Entity(name = "rss.feeds")
public class Feed extends AbstractEntity implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(Feed.class);

    @Nullable
    @Id
    @Column(columnDefinition = "bigserial")
    private Long id;
    @NotNull
    @Column(nullable = false, precision = 6)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp = Instant.now();
    @NotNull
    @Column(nullable = false)
    private URL url;
    @NotNull
    @Column
    private URL originalUrl;
    @Column(nullable = false)
    private boolean isValid;
    @Column(nullable = false)
    private boolean isLive;

    // fairly static content
    @Column
    private String title;
    @Transient
    private Content titleEx;
    @Column
    private String xmlProtocol;
    @Column
    private String xmlLink;
    @NotNull
    @Column(nullable = false)
    private String feedType = "unspecified";
    @Nullable
    // @ManyToOne
    //@JoinColumn(name = "feed_id", nullable = true)
    private Image image;
    @Nullable
    //@ManyToOne
    //@JoinColumn(name = "feed_id", nullable = true)
    private Image icon;
    // @Nullable
    // private String description;
    @Transient
    private Content descriptionEx;
    @Nullable
    @Column(nullable = true)
    private String copyright;
    @Nullable
    @Column(nullable = true)
    private String encoding;
    @Nullable
    @Column(nullable = true)
    private String language;
    @Nullable
    @Column(nullable = true)
    private String managingEditor;
    @Nullable
    @Column(nullable = true)
    private String webMaster;
    @Nullable
    @Column(nullable = true)
    private String author;
    @Column(nullable = false)
    private boolean requiresAuthentication;

    @Column
    private String error;

    // dynamic content
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Instant publishedDate;

    //@OneToOne
    private FeedBody body;

    // temporary - while figuring out ROME schema
    @Column
    private int authorCount;
    @Column
    private int linkCount;

    @Column
    private URL htmlUrl;

    // these are concrete classes for serialization
    //@ManyToMany
    private ArrayList<Person> authors = new ArrayList<>();
    //@ManyToMany
    private ArrayList<Category> categories = new ArrayList<>();
    //@ManyToMany
    private ArrayList<Person> contributors = new ArrayList<>();
    //@ManyToMany
    private ArrayList<Entry> entries = new ArrayList<>();
    //@ManyToMany
    private ArrayList<Link> links = new ArrayList<>();
    //@OneToOne
    private FeedHttpResponse httpResponse = new FeedHttpResponse();

    /**
     * Default constructor
     */
    public Feed() {
        // this should always be overwritten later
        this(ConversionUtilities.DUMMY_URL);
        this.isValid = false;
        this.isLive = false;
        this.requiresAuthentication = false;
        this.body = new FeedBody();
        this.body.setUrl(this.url);
    }

    public Feed(Path path) {
        this.url = ConversionUtilities.toUrl(path);
        this.httpResponse.setUrl(this.url);
        this.body = new FeedBody();
        this.body.setUrl(this.url);

        this.isValid = false;
        this.isLive = false;
        this.requiresAuthentication = false;
    }

    /**
     * Constructor taking URL
     *
     * @param url link to RSS feed
     */
    public Feed(@NotNull URL url) {
        this.url = url;
        this.originalUrl = url;
        this.httpResponse.setUrl(this.url);
        this.body = new FeedBody();
        this.body.setUrl(this.url);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @NotNull
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Instant timestamp) {
        this.timestamp = timestamp;
    }

    @NotNull
    public URL getUrl() {
        return url;
    }

    public void setUrl(@NotNull URL url) {
        this.url = url;
    }

    public URL getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(URL originalUrl) {
        this.originalUrl = originalUrl;
    }

    /**
     * Returns 'true' if this URL provides a valid RSS stream.
     *
     * @return
     */
    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * Returns 'true' if this URL provides an invalid RSS stream.
     * <p>
     * A common reason for a site to be 'live' but not 'valid' is if returns
     * an improperly formatted document.
     * </p>
     *
     * @return
     */
    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Content getTitleEx() {
        return titleEx;
    }

    public void setTitleEx(Content titleEx) {
        this.titleEx = titleEx;
    }

    /**
     * Get feed's 'xml' url. This may not be a proper URL.
     * <p>
     * Some problems may be a missing URL handler, e.g., 'yt:' for YouTube videos.
     * But some of the other tags are harder to understand, e.g., 'tag:' or even
     * a Reddit subreddit name.
     * </p>
     * <p>
     * So this field is named 'xmlLink' instead of 'xmlUrl' in order to avoid
     * problems with jOOQ's code autogeneration.
     * </p>
     */
    public String getXmlLink() {
        return xmlLink;
    }

    public void setXmlLink(String xmlLink) {
        this.xmlLink = xmlLink;
    }

    /**
     * Get protocol for feed's 'xml' url.
     * <p>
     * This is a convenience method - it makes it easier to identify unsupported
     * protocols and to see how widely used they are.
     * </p>
     *
     * @return
     */
    public String getXmlProtocol() {
        return xmlProtocol;
    }

    public void setXmlProtocol(String xmlProtocol) {
        this.xmlProtocol = xmlProtocol;
    }

    /**
     * Get type of RSS feed.
     * <p>
     * This could probably be split into two components - the first would be RSS|ATOM
     * and the second would be the version number.
     * </p>
     *
     * @return
     */
    @NotNull
    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(@NotNull String feedType) {
        this.feedType = feedType;
    }

    @Nullable
    public Image getImage() {
        return image;
    }

    public void setImage(@Nullable Image image) {
        this.image = image;
    }

    @Nullable
    public Image getIcon() {
        return icon;
    }

    public void setIcon(@Nullable Image icon) {
        this.icon = icon;
    }

    /*
    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
     */

    public Content getDescriptionEx() {
        return descriptionEx;
    }

    public void setDescriptionEx(Content descriptionEx) {
        this.descriptionEx = descriptionEx;
    }

    @Nullable
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(@Nullable String copyright) {
        this.copyright = copyright;
    }

    @Nullable
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(@Nullable String encoding) {
        this.encoding = encoding;
    }

    @Nullable
    public String getLanguage() {
        return language;
    }

    public void setLanguage(@Nullable String language) {
        this.language = language;
    }

    @Nullable
    public String getManagingEditor() {
        return managingEditor;
    }

    public void setManagingEditor(@Nullable String managingEditor) {
        this.managingEditor = managingEditor;
    }

    @Nullable
    public String getWebMaster() {
        return webMaster;
    }

    public void setWebMaster(@Nullable String webMaster) {
        this.webMaster = webMaster;
    }

    @Nullable
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@Nullable String author) {
        this.author = author;
    }

    public boolean requiresAuthentication() {
        return requiresAuthentication;
    }

    public void requiresAuthentication(boolean requiresAuthentication) {
        this.requiresAuthentication = requiresAuthentication;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        if (authors == null) {
            this.authors = new ArrayList<>();
        } else if (authors instanceof ArrayList) {
            this.authors = (ArrayList<Person>) authors;
        } else {
            this.authors = new ArrayList<>(authors);
        }
    }

    public List<Person> getContributors() {
        return contributors;
    }

    public void setContributors(List<Person> contributors) {
        if (contributors == null) {
            this.contributors = new ArrayList<>();
        } else if (contributors instanceof ArrayList) {
            this.contributors = (ArrayList<Person>) contributors;
        } else {
            this.contributors = new ArrayList<>(contributors);
        }
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        if (links == null) {
            this.links = new ArrayList<>();
        } else if (links instanceof ArrayList) {
            this.links = (ArrayList<Link>) links;
        } else {
            this.links = new ArrayList<>(links);
        }
    }

    /**
     * Latest articles
     *
     * @return
     */
    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        if (entries == null) {
            this.entries = new ArrayList<>();
        } else if (entries instanceof ArrayList) {
            this.entries = (ArrayList<Entry>) entries;
        } else {
            this.entries = new ArrayList<>(entries);
        }
    }

    /**
     * Get primary categories for this feed.
     *
     * @return
     */
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        if (categories == null) {
            this.categories = new ArrayList<>();
        } else if (categories instanceof ArrayList) {
            this.categories = (ArrayList<Category>) categories;
        } else {
            this.categories = new ArrayList<>(categories);
        }
    }

    public int getAuthorCount() {
        return authorCount;
    }

    public void setAuthorCount(int authorCount) {
        this.authorCount = authorCount;
    }

    public int getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(int linkCount) {
        this.linkCount = linkCount;
    }

    /**
     * HTTP error, if any.
     *
     * @return
     */
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        this.isValid = false;
    }

    public FeedBody getBody() {
        return body;
    }

    public void setBody(FeedBody body) {
        this.body = body;
    }

    public URL getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(URL htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public boolean isRequiresAuthentication() {
        return requiresAuthentication;
    }

    public void setRequiresAuthentication(boolean requiresAuthentication) {
        this.requiresAuthentication = requiresAuthentication;
    }

    public FeedHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(FeedHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Feed addResponse(HttpResponse<String> response) {
        this.isLive = true;
        this.requiresAuthentication = (response.statusCode() == 403);
        this.url = ConversionUtilities.toUrl(response.uri());

        this.httpResponse.setUrl(this.url);
        this.httpResponse.setStatusCode(response.statusCode());

        final HttpHeaders headers = response.headers();
        final List<String> keys = new ArrayList<>(headers.map().keySet());
        Collections.sort(keys);

        for (String key : keys) {
            @NotNull final List<String> values = headers.allValues(key);
            @NotNull final String lKey = key.toLowerCase();
            // does it make sense to store cookies here?
            if ("set-cookie".equals(lKey)) {
                // values.stream().map(java.net.HttpCookie::parse)
                //         .forEach(x -> x.forEach(cookie -> cookieStore.add(response.uri(), cookie)));
            } else if ("set-cookie2".equals(lKey)) {
                // does it make sense to
                // values.stream().map(java.net.HttpCookie::parse)
                //         .forEach(x -> x.forEach(cookie -> cookieStore.add(response.uri(), cookie)));
            } else {
                for (int position = 0; position < values.size(); position++) {
                    this.httpResponse.getHeaders().add(new FeedHeader(id, lKey, position, values.get(position)));
                }
            }

            switch (lKey) {
                case "cache-control":
                    this.httpResponse.setCacheControl(values.get(0));
                    break;
                case "charset":
                    this.httpResponse.setCharset(values.get(0));
                    this.getBody().setCharset(values.get(0));
                    break;
                case "content-type":
                    this.httpResponse.setContentType(values.get(0));
                    this.getBody().setContentType(values.get(0));
                    break;
                case "etag":
                    this.httpResponse.setEtag(values.get(0));
                    this.getBody().setEtag(values.get(0));
                    break;
                case "pragma":
                    this.httpResponse.setPragma(values.get(0));
                    break;

                case "expires":
                    this.httpResponse.setExpires(ConversionUtilities.toInstant(values.get(0)));
                    break;
                case "last-modified":
                    this.httpResponse.setLastModified(ConversionUtilities.toInstant(values.get(0)));
                    break;
            }
        }

        /*
        // finally a bit of an override due to things I've seen in the wild...
        if ((isBlank(getContentType()) || (getContentType().contains("html"))
                && getBody().toLowerCase().startsWith("<?xml "))) {
            setContentType("application/xml");
        }
         */

        return this;
    }

    @Override
    public String toString() {
        return String.format("Feed[id: %s, feedType: '%s', url: '%s', origUrl: '%s', title: '%s', pubDate: '%s']",
                getId(), getFeedType(), getUrl(), getOriginalUrl(), getTitle(), (getPublishedDate() == null) ? null
                        : DateTimeFormatter.ISO_INSTANT.format(getPublishedDate()));
    }
}