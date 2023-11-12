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
package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.repo.*;
import com.coyotesong.microservice.rss.repo.spi.RssRepositoryFactoryFactory;
import com.coyotesong.microservice.rss.service.RssService;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class JooqRssRepositoryFactoryFactory implements RssRepositoryFactoryFactory {

    SQLDialect getDialect(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            final DatabaseMetaData md = conn.getMetaData();
            final String productName = md.getDatabaseProductName();
            if ("Postgresql".equals(productName)) {
                return SQLDialect.POSTGRES;
            }
        }

        return SQLDialect.DEFAULT;
    }

    @Override
    public RssRepositoryFactory newInstance(DataSource dataSource) throws SQLException {
        final Configuration configuration = new DefaultConfiguration();
        // TODO - instigate how to handle a Spring object here...
        // configuration.set(new DefaultExecuteListenerProvider(exceptionTransformer()));
        configuration.set(getDialect(dataSource));
        return new JooqRssRepositoryFactory(new DefaultDSLContext(configuration), configuration);
    }

    public static class JooqRssRepositoryFactory implements RssRepositoryFactory {
        private final DSLContext dsl;
        private final Configuration configuration;

        public JooqRssRepositoryFactory(DSLContext dsl, Configuration configuration) {
            this.dsl = dsl;
            this.configuration = configuration;
        }

        @Override
        public CategoryRepository newCategoryRepositoryInstance() {
            return new CategoryRepositoryImpl(dsl, configuration);
        }

        @Override
        public EnclosureRepository newEnclosureRepositoryInstance() {
            return new EnclosureRepositoryImpl(dsl, configuration);
        }

        @Override
        public EntryRepository newEntryRepositoryInstance(CategoryRepository categoryRepository, LinksRepository linksRepository, PersonRepository personRepository) {
            return new EntryRepositoryImpl(dsl, configuration, categoryRepository, linksRepository, personRepository);
        }

        @Override
        public FeedRepository newFeedRepositoryInstance(RssService rssService, CategoryRepository categoryRepository, EntryRepository entryRepository, ImageRepository imageRepository, LinksRepository linkRepository, PersonRepository personRepository) {
            return new FeedRepositoryImpl(dsl, configuration, rssService, categoryRepository, entryRepository, imageRepository, linkRepository, personRepository);
        }

        @Override
        public ImageRepository newImageRepositoryInstance() {
            return new ImageRepositoryImpl(dsl, configuration);
        }

        @Override
        public LinksRepository newLinksRepositoryInstance() {
            return new LinksRepositoryImpl(dsl, configuration);
        }

        @Override
        public OpmlRepository newOpmlRepositoryInstance(RssService rssService) {
            return new OpmlRepositoryImpl(dsl, configuration, rssService);
        }

        @Override
        public PersonRepository newPersonRepositoryInstance() {
            return new PersonRepositoryImpl(dsl, configuration);
        }
    }
}
