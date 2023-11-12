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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * RSS Entry, based on Rome's SyndEntry
 */
@Entity(name = "rss.entries")
public class Entry implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(nullable = false, precision = 6)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp = Instant.now();
    @Nullable
    @Id
    @Column(columnDefinition = "bigserial")
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String title;
    @Transient
    private Content titleEx;
    @Column
    private String author;
    @NotNull
    @Column(nullable = false)
    private URL url;
    @Column
    private int authorCount;
    @Nullable
    @Column(precision = 0)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant publishedDate;
    @Nullable
    @Column(precision = 0)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedDate;
    @Column
    private int linkCount;
    @Transient
    // @OneToOne
    private Content description;
    @Transient
    private Feed source;

    // these are concrete classes for serialization
    @ManyToMany(mappedBy = "entryId", targetEntity = Person.class, fetch = FetchType.LAZY)
    private ArrayList<Person> authors = new ArrayList<>();
    @ManyToMany(mappedBy = "entryId", targetEntity = Person.class, fetch = FetchType.LAZY)
    private ArrayList<Person> contributors = new ArrayList<>();
    // @ManyToMany
    @ManyToMany
    private ArrayList<Category> categories = new ArrayList<>();
    // @ManyToMany
    @OneToMany
    private ArrayList<Content> contents = new ArrayList<>();
    // @ManyToMany
    @OneToMany
    private ArrayList<Link> links = new ArrayList<>();
    // @ManyToMany
    @OneToMany
    private ArrayList<Enclosure> enclosures = new ArrayList<>();

    /**
     * Default constructor
     */
    public Entry() {
    }

    @NotNull
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public Content getTitleEx() {
        return titleEx;
    }

    public void setTitleEx(Content titleEx) {
        this.titleEx = titleEx;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @NotNull
    public URL getUrl() {
        return url;
    }

    public void setUrl(@NotNull URL url) {
        this.url = url;
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

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Nullable
    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(@Nullable Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

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

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        if (contents == null) {
            this.contents = new ArrayList<>();
        } else if (contents instanceof ArrayList) {
            this.contents = (ArrayList<Content>) contents;
        } else {
            this.contents = new ArrayList<>(contents);
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

    public Content getDescription() {
        return description;
    }

    public void setDescription(Content description) {
        this.description = description;
    }

    public List<Enclosure> getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(List<Enclosure> enclosures) {
        if (enclosures == null) {
            this.enclosures = new ArrayList<>();
        } else if (enclosures instanceof ArrayList) {
            this.enclosures = (ArrayList<Enclosure>) enclosures;
        } else {
            this.enclosures = new ArrayList<>(enclosures);
        }
    }

    public Feed getSource() {
        return source;
    }

    public void setSource(Feed source) {
        this.source = source;
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

    @Override
    public String toString() {
        return String.format("Entry[id: %s, url: '%s', title: '%s']", id, url, title);
    }
}
