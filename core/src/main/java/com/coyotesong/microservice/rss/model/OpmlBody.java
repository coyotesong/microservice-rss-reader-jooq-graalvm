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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.net.URL;

/**
 * Original-ish contents of OPML document
 * <p>
 * Notes:
 * <ul>
 * <li>The table is slightly denormalized for performance</li>
 * <li>The contents may have been slightly modified, e.g., to remove a &lt;!DOCTYPE header.</li>
 * </ul>
 * </p>
 */
@Entity(name = "rome.opml_body")
public class OpmlBody implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    @Id
    @Column(nullable = false)
    private Long id;
    @Column(nullable = true)
    private URL url;
    @Column(nullable = true)
    private String contentType;
    @Column(nullable = true)
    private Integer contentLength;
    @Column(nullable = true)
    private String charset;
    @Column(nullable = true)
    private String etag;
    @Column(nullable = false)
    private String body;

    public OpmlBody() {
    }

    public OpmlBody(@Nullable Long id, @NotNull URL url, @NotNull String body) {
        this.id = id;
        this.url = url;
        this.body = body;
        this.contentLength = body.length();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}