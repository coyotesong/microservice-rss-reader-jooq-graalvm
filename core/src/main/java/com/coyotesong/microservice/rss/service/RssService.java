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

package com.coyotesong.microservice.rss.service;

import com.coyotesong.microservice.rss.model.Feed;
import com.coyotesong.microservice.rss.model.Opml;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service that imports RSS OPML and RSS documents
 */
public interface RssService {

    /**
     * Import OPML document
     *
     * @param path
     * @return List of ROME Outline objects
     * @throws IOException
     */
    Optional<Opml> importOpml(@NotNull Path path) throws IOException, ExecutionException;

    /**
     * Import OPML document
     *
     * @param url path to OPML document
     * @return List of ROME Outline objects
     * @throws IOException
     */
    Optional<Opml> importOpml(@NotNull URL url) throws IOException, ExecutionException;

    /**
     * Import Feed document
     *
     * @param path
     * @return Feed
     * @throws IOException
     */
    Feed importFeed(@NotNull Path path);

    /**
     * Import Feed document
     *
     * @param url
     * @return Feed
     * @throws IOException
     */
    Optional<Feed> importFeed(@NotNull URL url) throws IOException, ExecutionException;

    /**
     * Create deduplicated Feeds from Opml
     *
     * @param opml
     * @return
     */
    @NotNull
    List<Feed> importFeeds(@NotNull Opml opml);
}
