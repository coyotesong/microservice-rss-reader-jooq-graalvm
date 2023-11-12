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

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RSS OPML outline
 */
@Entity(name = "rome.outlines")
public class Outline implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Embeddable
    public static class OutlineKey implements Serializable {
        // @Serial
        private static final long serialVersionUID = 1L;

        @Column(nullable = false)
        private Long id;
        @Column(nullable = false)
        private int position;

        public OutlineKey() {
        }

        public OutlineKey(Long id, int position) {
            this.id = id;
            this.position = position;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    @EmbeddedId
    private OutlineKey key;
    @ManyToOne
    //@MapsId("opmlId");
    @JoinColumn(name = "opml_id")
    private Entry opml;
    ;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdDate;
    @Column
    private String encoding;
    @Column
    private boolean isBreakpoint;
    @Column
    private boolean isComment;
    @Column
    private URL htmlUrl;
    @Column
    private String text;
    @Column
    private String title;
    @Column
    private String type;
    @Column
    private URL xmlUrl;

    // these are concrete classes for serialization
    @Transient
    private LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
    @Transient
    private ArrayList<String> categories = new ArrayList<>();
    @Transient
    private ArrayList<String> modules = new ArrayList<>();

    public Outline() {
        this.key = new OutlineKey();
    }

    public Outline(Long id, int position) {
        this.key = new OutlineKey(id, position);
    }

    public OutlineKey getKey() {
        return key;
    }

    public void setKey(OutlineKey key) {
        this.key = key;
    }

    public void setKey(Long id, int position) {
        if (this.key == null) {
            this.key = new OutlineKey(id, position);
        } else {
            this.key.id = id;
            this.key.position = position;
        }
    }

    public Long getId() {
        return key.id;
    }

    public void setId(Long id) {
        this.key.id = id;
    }

    public int getPosition() {
        return key.position;
    }

    public void setPosition(int position) {
        this.key.position = position;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isBreakpoint() {
        return isBreakpoint;
    }

    public void setBreakpoint(boolean breakpoint) {
        isBreakpoint = breakpoint;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }

    public URL getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(URL htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URL getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(URL xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        if (attributes == null) {
            this.attributes = new LinkedHashMap<>();
        } else if (attributes instanceof LinkedHashMap) {
            this.attributes = (LinkedHashMap<String, String>) attributes;
        } else {
            this.attributes = new LinkedHashMap<>(attributes);
        }
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        if (categories == null) {
            this.categories = new ArrayList<>();
        } else if (categories instanceof ArrayList) {
            this.categories = (ArrayList<String>) categories;
        } else {
            this.categories = new ArrayList<>(categories);
        }
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        if (modules == null) {
            this.modules = new ArrayList<>();
        } else if (modules instanceof ArrayList) {
            this.modules = (ArrayList<String>) modules;
        } else {
            this.modules = new ArrayList<>(modules);
        }
    }

    @Override
    public String toString() {
        // it appears 'title' and 'text' will always be the same.
        return String.format("Outline[type: '%s', xmlUrl: '%s', title: '%s')", getType(), getXmlUrl(), getTitle());
    }
}