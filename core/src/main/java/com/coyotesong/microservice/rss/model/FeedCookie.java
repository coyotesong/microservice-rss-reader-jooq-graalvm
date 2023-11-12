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

// import org.apache.commons.lang3.StringUtils; -- minor issue - why isn't this seen in classpath?

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * HTTP Cookies
 * <p>
 * We can use the java.net cookie classes for now, but this would be useful if we need
 * to support more than a single listener.
 */
public class FeedCookie implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(FeedCookie.class);

    // see also java.net.CookieHandler
    // see also CookieManager
    // see also CookieStore
    // see also CookiePolicy
    // see HttpCookie

    @SuppressWarnings("unused")
    public enum Priority {
        HIGH
    }

    @SuppressWarnings("unused")
    public enum SameSite {
        NONE,
        LAX,
        STRICT
    }

    @Nullable
    private Long id;
    @NotNull
    private Instant timestamp = Instant.now();
    @NotNull
    private String host;
    @NotNull
    private String name;
    @NotNull
    private String value;
    // a single exception???
    private String path;
    @Nullable
    private Instant expires;
    private String domain;
    @Nullable
    private Boolean httpOnly;
    @Nullable
    private Boolean secure;
    @Nullable
    private String sameSite;
    @Nullable
    private Integer maxAge;
    @Nullable
    private String priority;
    @Nullable
    private Integer version;
    @NotNull
    private String content;

    public FeedCookie() {
        this.host = "-";
        this.name = "_";
        this.value = "-";
        this.content = "-";
    }

    @SuppressWarnings("unused")
    public FeedCookie(FeedCookie cookie) {
        this.id = cookie.id;
        this.timestamp = cookie.getTimestamp();
        this.name = cookie.name;
        this.value = cookie.value;
        this.path = cookie.path;
        this.expires = cookie.expires;
        this.domain = cookie.domain;
        this.httpOnly = cookie.httpOnly;
        this.secure = cookie.secure;
        this.sameSite = cookie.sameSite;
        this.maxAge = cookie.maxAge;
        this.content = cookie.content;
        this.priority = cookie.priority;
        this.version = cookie.version;
        this.host = cookie.host;
    }

    @SuppressWarnings("unused")
    public FeedCookie(
            @Nullable Long feedId,
            @NotNull Instant timestamp,
            @NotNull String host,
            @NotNull String name,
            @NotNull String value,
            String path,
            @Nullable Instant expires,
            String domain,
            @Nullable Boolean httpOnly,
            @Nullable Boolean secure,
            @Nullable String sameSite,
            @Nullable Integer maxAge,
            @Nullable String priority,
            @Nullable Integer version,
            @NotNull
            String content
    ) {
        this.id = feedId;
        this.timestamp = timestamp;
        this.name = name;
        this.value = value;
        this.path = path;
        this.expires = expires;
        this.domain = domain;
        this.httpOnly = httpOnly;
        this.secure = secure;
        this.sameSite = sameSite;
        this.maxAge = maxAge;
        this.content = content;
        this.priority = priority;
        this.version = version;
        this.host = host;
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
    public String getHost() {
        return host;
    }

    public void setHost(@NotNull String host) {
        this.host = host;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    public void setValue(@NotNull String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Nullable
    public Instant getExpires() {
        return expires;
    }

    public void setExpires(@Nullable Instant expires) {
        this.expires = expires;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Nullable
    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(@Nullable Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Nullable
    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(@Nullable Boolean secure) {
        this.secure = secure;
    }

    @Nullable
    public String getSameSite() {
        return sameSite;
    }

    public void setSameSite(@Nullable String sameSite) {
        /*
        if (StringUtils.isNotBlank(sameSite)) {
            if (sameSite.equalsIgnoreCase("none")) {
                this.sameSite = "None";
            } else if (sameSite.equalsIgnoreCase("lax")) {
                this.sameSite = "Lax";
            } else if (sameSite.equalsIgnoreCase("strict")) {
                this.sameSite = "Strict";
            } else {
                this.sameSite = sameSite;
            }
        }
         */
    }

    @Nullable
    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(@Nullable Integer maxAge) {
        this.maxAge = maxAge;
    }

    @Nullable
    public String getPriority() {
        return priority;
    }

    public void setPriority(@Nullable String priority) {
        this.priority = priority;
    }

    @Nullable
    public Integer getVersion() {
        return version;
    }

    public void setVersion(@Nullable Integer version) {
        this.version = version;
    }

    /**
     * Get actual cookie returned by host
     *
     * @return actual content return by host
     */
    @NotNull
    public String getContent() {
        return content;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
    }

    /**
     * Is this a session cookie? If so it should not be written to the database.
     *
     * @return
     */
    public boolean isSessionCookie() {
        return expires == null;
    }

    @NotNull
    public static FeedCookie valueOf(@NotNull String content) {
        final FeedCookie cookie = new FeedCookie();
        cookie.setContent(content);

        final String[] properties = content.split(";");
        for (int i = 0; i < properties.length; i++) {
            final String[] components = properties[i].trim().split("=", 2);
            if (i == 0) {
                cookie.setName(components[0].trim());
                if (components.length > 1) {
                    cookie.setValue(components[1].trim());
                }
            } else {
                final String name = components[0].trim().toLowerCase();
                final String value = (components.length > 1) ? components[1].trim() : null;
                switch (name) {
                    case "domain":
                        cookie.setDomain(value);
                        break;
                    case "expires":
                        // if (StringUtils.isNotBlank(value)) {
                        //    cookie.setExpires(toInstant(value));
                        //}
                        break;
                    case "httponly":
                        cookie.setHttpOnly(Boolean.TRUE);
                        break;
                    case "max-age":
                        // if (StringUtils.isNotBlank(value) && value.matches("[0-9]+")) {
                        //    cookie.setMaxAge(Integer.valueOf(value, 10));
                        //}
                        break;
                    case "path":
                        cookie.setPath(value);
                        break;
                    case "priority":
                        cookie.setPriority(value);
                        break;
                    case "samesite":
                        cookie.setSameSite(value);
                        break;
                    case "secure":
                        cookie.setSecure(Boolean.TRUE);
                        break;
                    case "version":
                        //if (StringUtils.isNotBlank(value) && value.matches("[0-9]+")) {
                        //    cookie.setVersion(Integer.valueOf(value, 10));
                        //}
                        break;

                    default:
                        LOG.info("unhandled cookie property: {} -> {}", name, value);
                }
            }
        }

        return cookie;
    }

    @Nullable
    private static Instant toInstant(@NotNull String value) {
        try {
            // just to make life hard some sites add dashes between the date's components.
            return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(value.replace("-", " ")));
        } catch (DateTimeParseException e) {
            LOG.info("Unable to parse datetime: {}({}): {}", e.getClass().getName(), value, e.getMessage());
        }
        return null;
    }
}
