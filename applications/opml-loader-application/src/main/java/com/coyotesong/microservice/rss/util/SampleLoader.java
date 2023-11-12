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

package com.coyotesong.microservice.rss.util;

import com.coyotesong.microservice.rss.model.Opml;
import com.coyotesong.microservice.rss.repo.EntryRepository;
import com.coyotesong.microservice.rss.repo.FeedRepository;
import com.coyotesong.microservice.rss.repo.OpmlRepository;
import com.coyotesong.microservice.rss.repo.PersonRepository;
import com.coyotesong.microservice.rss.service.RssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ErrorMvcAutoConfiguration.class  })
@EntityScan(basePackages = { "com.coyotesong.microservice.rss.model" })
@ComponentScan(basePackages = {
        "com.coyotesong.microservice.rss.config",
        "com.coyotesong.microservice.rss.repo",
        "com.coyotesong.microservice.rss.service",
        "com.coyotesong.microservice.rss.service.rome" })
public class SampleLoader {
    private static final Logger LOG = LoggerFactory.getLogger(SampleLoader.class);

    private final RssService rssService;


    private final EntryRepository entryRepository;
    private final FeedRepository feedRepository;
    private final PersonRepository personRepository;
    private final OpmlRepository opmlRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleLoader.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            final SampleLoader loader = ctx.getBean(SampleLoader.class);
            loader.load();
        };
    }

    @Autowired
    public SampleLoader(RssService rssService, OpmlRepository opmlRepository, EntryRepository entryRepository,
                        FeedRepository feedRepository, PersonRepository personRepository) {
        this.rssService = rssService;
        this.opmlRepository = opmlRepository;
        this.entryRepository = entryRepository;
        this.feedRepository = feedRepository;
        this.personRepository = personRepository;
    }

    void load() throws IOException, ExecutionException {
        opmlRepository.clear();
        personRepository.clear();
        entryRepository.clear();
        feedRepository.clear();

        Path path = new File("/home/bgiles/Downloads/feedly-254a721a-d332-42d5-97de-8435b5896a41-2023-10-02.opml").toPath();
        final Optional<Opml> o = rssService.importOpml(path);
        if (o.isPresent()) {
            final Opml opml = o.get();
            opmlRepository.save(opml);
            // do this in a single step in order to facilitate deduplication
            feedRepository.saveAll(rssService.importFeeds(opml));
        }
    }
}