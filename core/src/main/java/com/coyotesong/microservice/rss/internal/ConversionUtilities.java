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
package com.coyotesong.microservice.rss.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Conversion utilities
 */
public final class ConversionUtilities {
    private static final Logger LOG = LoggerFactory.getLogger(ConversionUtilities.class);

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final File USER_HOME_DIRECTORY = new File(System.getProperty("user.home"));
    private static final File HOME_DIRECTORY = USER_HOME_DIRECTORY.getParentFile();

    // dummy url - should only be used as a placeholder when the 'setUrl()' method fails.
    public static final URL DUMMY_URL;

    static {
        try {
            DUMMY_URL = URI.create("http://example.com/dummy").toURL();
        } catch (MalformedURLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Convert String to URL
     *
     * @param url URL (as string)
     * @return URL for resource
     */
    @NotNull
    public static URL toUrl(@NotNull String url) {
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException e) {
            // TODO - we should never write untrusted contents to the log!
            LOG.info("MalformedURLException: {}, {}", url, e.getMessage());
            return DUMMY_URL;
        }
    }

    /**
     * Convert Path to URL
     * <p>
     * TODO: unit tests shows this creates 'file:/path', not 'file:///path'.
     * </p>
     *
     * @param path path to file
     * @return URL for file
     */
    @NotNull
    public static URL toUrl(@NotNull Path path) {
        final String pathString = path.toString();
        if (pathString.startsWith("~/")) {
            final File file = new File(USER_HOME_DIRECTORY, pathString.substring(2));
            return toUrl(file.toPath().toUri());
        } else if (pathString.startsWith("~")) {
            // TODO - we should check for homedir in /etc/passwd, etc.
            int idx = pathString.indexOf(FILE_SEPARATOR);
            if (idx < 0) {
                final File file = new File(HOME_DIRECTORY, FILE_SEPARATOR + pathString.substring(1));
                return toUrl(file.toPath().toUri());
            }

            final String username = pathString.substring(1, idx);
            final File file = new File(HOME_DIRECTORY, FILE_SEPARATOR + username + FILE_SEPARATOR + pathString.substring(idx + 1));
            return toUrl(file.toPath().toUri());
        }

        return toUrl(path.toUri());
    }

    /**
     * Convert URI to URL
     *
     * @param uri URI resource
     * @return URL for resource
     */
    @NotNull
    public static URL toUrl(@NotNull URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            // TODO - we should never write untrusted contents to the log!
            LOG.info("MalformedURLException: {}, {}", uri.toString(), e.getMessage());
            return DUMMY_URL;
        }
    }

    /**
     * Convert string to Instant
     *
     * @param value date and time in RFC_1123 format. (This is what RSS uses)
     * @return corresponding Instant
     */
    @Nullable
    public static Instant toInstant(@Nullable String value) throws DateTimeParseException {
        if (value == null) {
            return null;
        }

        return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(value));
    }

    /**
     * Convert Date to Instant
     *
     * @param date java.util.Date
     * @return corresponding Instant
     */
    @Nullable
    public static Instant toInstant(@Nullable Date date) throws DateTimeParseException {
        if (date == null) {
            return null;
        }

        // I've seen problems with using java.util.Date#toInstance()
        return Instant.ofEpochMilli(date.getTime());
    }
}
