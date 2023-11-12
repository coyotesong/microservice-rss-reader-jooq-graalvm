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

package com.coyotesong.microservice.rss.service.rome.converters;

import com.rometools.opml.feed.opml.Attribute;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.rss.Category;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractConverter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractConverter.class);

    /**
     * This provides a map of attributes.
     */
    @NotNull
    protected Map<String, String> fromAttributes(List<Attribute> attributes) {
        // ttrss = popular docker-based rss reader
        @SuppressWarnings("unused")
        final List<String> knownAttr = Arrays.asList("xmlUrl", "htmlUrl", "ttrssSortOrder", "ttrssUpdateInterval");

        if (attributes == null) {
            return Collections.emptyMap();
        }

        return attributes.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
    }

    /**
     * This provides a map of categories. (domain -> value)
     * @param categories
     * @return
     */
    protected Map<String, String> fromCategories(List<Category> categories) {
        if (categories == null) {
            return Collections.emptyMap();
        }

        return categories.stream().collect(Collectors.toMap(Category::getDomain, Category::getValue));
    }

    /**
     * This provides a list of URIs
     * @param modules
     * @return List of URIs
     */
    @NotNull
    protected List<String> fromModules(List<Module> modules) {
        if (modules == null) {
            return Collections.emptyList();
        }

        return modules.stream().map(Module::getUri).collect(Collectors.toList());
    }
}
