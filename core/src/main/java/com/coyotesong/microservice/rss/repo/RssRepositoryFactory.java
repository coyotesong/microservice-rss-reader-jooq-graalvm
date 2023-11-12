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

package com.coyotesong.microservice.rss.repo;

import com.coyotesong.microservice.rss.service.RssService;

/**
 * Factory for RSS Repository objects
 */
public interface RssRepositoryFactory {

    CategoryRepository newCategoryRepositoryInstance();

    EnclosureRepository newEnclosureRepositoryInstance();

    EntryRepository newEntryRepositoryInstance(CategoryRepository categoryRepository, LinksRepository linksRepository,
                                               PersonRepository personRepository);

    FeedRepository newFeedRepositoryInstance(RssService rssService, CategoryRepository categoryRepository,
                                             EntryRepository entryRepository, ImageRepository imageRepository,
                                             LinksRepository linkRepository,
                                             PersonRepository personRepository);

    ImageRepository newImageRepositoryInstance();

    LinksRepository newLinksRepositoryInstance();

    OpmlRepository newOpmlRepositoryInstance(RssService rssService);

    PersonRepository newPersonRepositoryInstance();
}
