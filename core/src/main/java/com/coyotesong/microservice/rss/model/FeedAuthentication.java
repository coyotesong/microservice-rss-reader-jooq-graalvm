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
import com.coyotesong.microservice.rss.model.annotations.Sensitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.time.Instant;

/**
 * RSS Feed authentication
 */
@Entity(name = "rss.feed_authentications")
public class FeedAuthentication extends AbstractEntity implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(FeedAuthentication.class);

    @Nullable
    @Id
    private Long id;
    @NotNull
    @Column(nullable = false, precision = 6)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp = Instant.now();
    // Authentication URL may be different from primary URL
    @NotNull
    @Column(nullable = false)
    private URL authenticationUrl;
    @Column
    private String username;
    @Column
    @Sensitive
    private String password;

    /**
     * Default constructor
     */
    public FeedAuthentication() {
        // this should always be overwritten later
        this.authenticationUrl = ConversionUtilities.DUMMY_URL;
    }

    /**
     * Constructor taking URL
     *
     * @param authenticationUrl link to RSS feed
     */
    public FeedAuthentication(@NotNull URL authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    public FeedAuthentication(@NotNull FeedAuthentication feed) {
        this.id = feed.id;
        this.timestamp = (feed.timestamp == null) ? Instant.now() : feed.timestamp;
        this.authenticationUrl = feed.authenticationUrl;
        this.username = feed.username;
        this.password = feed.password;
    }

    public FeedAuthentication(
            @Nullable Long id,
            @NotNull Instant timestamp,
            @NotNull URL authenticationUrl,
            @Nullable String username,
            @Nullable String password) {
        this.id = id;
        this.timestamp = (timestamp == null) ? Instant.now() : timestamp;
        this.authenticationUrl = authenticationUrl;
        this.username = username;
        this.password = password;
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
    public URL getAuthenticationUrl() {
        return authenticationUrl;
    }

    public void setAuthenticationUrl(@NotNull URL authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
