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
import java.util.List;

@Entity(name = "rome.opml")
public class Opml implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "bigserial")
    private Long id;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp = Instant.now();
    @Column
    private URL url;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdDate;
    @Column
    private String docs;
    @Column
    private String encoding;
    @Column
    private String feedType;
    @Column
    private Instant modifiedDate;
    @Column
    private String ownerEmail;
    @Column
    private String ownerName;
    @Column
    private String ownerId;
    @Column
    private String stylesheet;
    @Column
    private String title;

    // these are concrete classes for serialization
    @Transient
    private ArrayList<String> modules = new ArrayList<>();
    @Transient
    private ArrayList<Outline> outlines = new ArrayList<>();

    @OneToOne
    private OpmlBody body;

    public Opml() {
        this.body = new OpmlBody();
    }

    public Opml(URL url) {
        this.url = url;
        this.body = new OpmlBody();
        this.body.setUrl(this.url);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Outline> getOutlines() {
        return outlines;
    }

    public void setOutlines(List<Outline> outlines) {
        if (outlines == null) {
            this.outlines = new ArrayList<>();
        } else if (outlines instanceof ArrayList) {
            this.outlines = (ArrayList<Outline>) outlines;
        } else {
            this.outlines = new ArrayList<>(outlines);
        }
    }

    public OpmlBody getBody() {
        return body;
    }

    public void setBody(OpmlBody body) {
        this.body = body;
    }

    public String toString() {
        return String.format("Opml('%s', '%s', '%s', '%s <%s>', %d)", getFeedType(), getTitle(), getCreatedDate(), getOwnerName(), getOwnerEmail(), getOutlines().size());
    }
}