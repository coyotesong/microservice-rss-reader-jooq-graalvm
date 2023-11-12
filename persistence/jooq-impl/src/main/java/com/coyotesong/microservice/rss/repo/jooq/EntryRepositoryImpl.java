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

import com.coyotesong.microservice.rss.model.Entry;
import com.coyotesong.microservice.rss.repo.CategoryRepository;
import com.coyotesong.microservice.rss.repo.EntryRepository;
import com.coyotesong.microservice.rss.repo.LinksRepository;
import com.coyotesong.microservice.rss.repo.PersonRepository;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.EntriesRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Entries.ENTRIES;

public class EntryRepositoryImpl extends AbstractRepositoryImpl<EntriesRecord, Entry, Long> implements EntryRepository {
    @SuppressWarnings("unused")
    private final CategoryRepository categoryRepository;
    private final LinksRepository linkRepository;
    private final PersonRepository personRepository;

    public EntryRepositoryImpl(DSLContext dsl, Configuration configuration, CategoryRepository categoryRepository, LinksRepository linksRepository, PersonRepository personRepository) {
        super(dsl, ENTRIES, Entry.class, configuration);
        this.categoryRepository = categoryRepository;
        this.linkRepository = linksRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Long getId(Entry entry) {
        return entry.getId();
    }

    @Override
    public void clear() {
        dsl.deleteFrom(ENTRIES).execute();
    }
}