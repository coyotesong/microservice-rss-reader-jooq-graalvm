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

import com.coyotesong.microservice.rss.repo.*;
import com.coyotesong.microservice.rss.repo.spi.RssRepositoryFactoryFactory;
import com.coyotesong.microservice.rss.service.RssService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.Optional;
import java.util.ServiceLoader;
import javax.sql.DataSource;

/**
 * Configures RSS persistence implementation via ServiceLoader
 */
@Configuration
public class RssRepositoryConfiguration implements ApplicationContextAware {

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    /**
     * Define metaFactory
     * @return
     */
    @Bean
    public RssRepositoryFactoryFactory rssRepositoryFactoryFactory() {
        Optional<RssRepositoryFactoryFactory> metaFactory = ServiceLoader.load(RssRepositoryFactoryFactory.class).findFirst();
        if (metaFactory.isEmpty()) {
            throw new IllegalStateException("No RssRepositoryFactoryFactory could be found!");
        }

        return metaFactory.get();
    }

    /**
     * Create individual factory
     * @param metaFactory
     * @return
     */
    @Bean
    @Autowired
    public RssRepositoryFactory rssRepositoryFactory(RssRepositoryFactoryFactory metaFactory) throws SQLException {
        // this allows us to compile and build without providing a placeholder DataSource.
        final DataSource dataSource = ctx.getBean(DataSource.class);
        if (dataSource == null) {
            throw new IllegalStateException("DataSource bean must be defined!");
        }

        return metaFactory.newInstance(dataSource);
    }

    @Bean
    @Autowired
    public CategoryRepository categoryRepository(@Autowired RssRepositoryFactory factory) {
        return factory.newCategoryRepositoryInstance();
    }

    @Bean
    @Autowired
    public EnclosureRepository enclosureRepository(RssRepositoryFactory factory) {
        return factory.newEnclosureRepositoryInstance();
    }

    @Bean
    @Autowired
    public EntryRepository entryRepository(RssRepositoryFactory factory, CategoryRepository categoryRepository,
                                           LinksRepository linksRepository, PersonRepository personRepository) {
        return factory.newEntryRepositoryInstance(categoryRepository, linksRepository, personRepository);
    }

    @Bean
    @Autowired
    public FeedRepository feedRepository(RssRepositoryFactory factory, RssService rssService,
                                         CategoryRepository categoryRepository, EntryRepository entryRepository,
                                         ImageRepository imageRepository, LinksRepository linkRepository,
                                         PersonRepository personRepository) {
        return factory.newFeedRepositoryInstance(rssService, categoryRepository, entryRepository, imageRepository,
                linkRepository, personRepository);
    }

    @Bean
    @Autowired
    public ImageRepository imageRepository(RssRepositoryFactory factory) {
        return factory.newImageRepositoryInstance();
    }

    @Bean
    @Autowired
    public LinksRepository linksRepository(RssRepositoryFactory factory) {
        return factory.newLinksRepositoryInstance();
    }

    @Bean
    @Autowired
    public OpmlRepository opmlRepository(RssRepositoryFactory factory, RssService rssService) {
        return factory.newOpmlRepositoryInstance(rssService);
    }

    @Bean
    @Autowired
    public PersonRepository personRepository(RssRepositoryFactory factory) {
        return factory.newPersonRepositoryInstance();
    }
}
