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

package com.coyotesong.microservice.rss.config;

import com.coyotesong.microservice.rss.model.Feed;
import com.coyotesong.microservice.rss.model.Opml;
import com.coyotesong.microservice.rss.service.RssService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

// see https://www.baeldung.com/spring-boot-support-for-jooq
// see https://www.baeldung.com/jooq-with-spring

@Configuration
@ComponentScan({"com.coyotesong.microservice.rss.repo.jooq"})
// @EnableTransactionManagement
//@PropertySource("classpath:rss.datasource")
public class TestConfiguration {
    @Bean
    public RssService rssService() {
        return new RssService() {
            public Optional<Opml> importOpml(@NotNull Path path) throws IOException, ExecutionException {
                return Optional.empty();
            }

            public Optional<Opml> importOpml(@NotNull URL url) throws IOException, ExecutionException {
                return Optional.empty();
            }

            public Feed importFeed(@NotNull Path path) {
                return new Feed();
            }

            public Optional<Feed> importFeed(@NotNull URL url) throws IOException, ExecutionException {
                return Optional.empty();
            }

            public List<Feed> importFeeds(@NotNull Opml opml) {
                return Collections.emptyList();
            }
        };
    }
}
