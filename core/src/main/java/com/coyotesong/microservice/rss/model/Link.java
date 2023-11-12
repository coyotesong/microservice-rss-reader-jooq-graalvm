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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * RSS Link, based on Rome's SyndLink
 */
@Entity(name = "rss.links")
public class Link extends AbstractEntity implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "bigserial")
    private Long id;
    @Column
    private Long entryId;
    @Column
    private int position;

    @Column
    private String title;
    @Column
    private String type;
    @NotNull
    @Column(nullable = false)
    private String href = ConversionUtilities.DUMMY_URL.toString();
    @Column
    private String hrefLang = null;
    @Column
    private Long length;
    @Column
    private String rel; // alternate, service.edit

    /**
     * Default constructor
     */
    public Link() {
        href = "-";
    }

    public Link(@NotNull String href) {
        this.href = href;
    }

    /**
     * Copy constructor
     *
     * @param link
     */
    public Link(@NotNull Link link) {
        this.id = link.getId();
        this.title = link.getTitle();
        this.type = link.getType();
        this.href = link.getHref();
        this.hrefLang = link.getHrefLang();
        this.length = link.getLength();
        this.rel = link.getRel();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    @NotNull
    public String getHref() {
        return href;
    }

    public void setHref(@NotNull String href) {
        this.href = href;
    }

    public String getHrefLang() {
        return hrefLang;
    }

    public void setHrefLang(String hrefLang) {
        this.hrefLang = hrefLang;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }
}
