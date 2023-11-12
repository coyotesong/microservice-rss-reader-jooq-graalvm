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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Contents of HttpResponse
 * <p>
 * Implementation note - this code currently uses a single CookieStore but we'll need
 * individual CookieStores if we ever add "users". This may happen when adding support
 * for authenticated feeds.
 * </p>
 */
@Entity(name = "rss.feed_response")
public class FeedHttpResponse extends AbstractEntity implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(FeedHttpResponse.class);

    @Nullable
    @Id
    private Long id;
    @Column(precision = 6)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp = Instant.now();
    @Column(nullable = false)
    private URL url;
    @Column
    private int statusCode;
    @Column
    private boolean requiresAuthentication;
    @Column
    private String contentType;
    @Column
    private String charset = StandardCharsets.UTF_8.name();
    @Column
    private String etag;
    @Column(precision = 0)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant expires;
    @Column(precision = 0)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant lastModified;

    @Column
    private String pragma;
    @Column
    private String cacheControl;

    // these are concrete classes for serialization
    // private List<FeedCookie> cookies = Collections.emptyList();
    @Transient
    //@OneToMany
    private ArrayList<FeedHeader> headers = new ArrayList<>();

    public FeedHttpResponse() {
    }

    public FeedHttpResponse(@NotNull Path path) {
        try {
            this.url = path.toUri().toURL();
        } catch (MalformedURLException e) {
            // this should never happen
            LOG.info("{} on {}: {}", e.getClass().getName(), path.toAbsolutePath(), e.getMessage());
        }
    }

    public FeedHttpResponse(@NotNull URL url) {
        this.url = url;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isRequiresAuthentication() {
        return requiresAuthentication;
    }

    public void setRequiresAuthentication(boolean requiresAuthentication) {
        this.requiresAuthentication = requiresAuthentication;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public ArrayList<FeedHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<FeedHeader> headers) {
        this.headers = headers;
    }

    public String getPragma() {
        return pragma;
    }

    public void setPragma(String pragma) {
        this.pragma = pragma;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String newString(ByteBuffer buffer) {
        return new String(buffer.array(), Charset.forName(this.charset));
    }

    public String newString(byte[] bytes) {
        return new String(bytes, Charset.forName(this.charset));
    }
}
