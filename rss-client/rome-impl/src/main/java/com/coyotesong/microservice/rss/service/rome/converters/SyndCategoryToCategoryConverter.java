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

import com.coyotesong.microservice.rss.model.Category;
import com.rometools.rome.feed.synd.SyndCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Convert SyndCategory to Category
 */
public class SyndCategoryToCategoryConverter extends AbstractConverter implements SyndConverter<SyndCategory, Category> {

    /**
     * Convert SyndCategory to Category
     *
     * @param syndCategory
     * @return
     */
    @Nullable
    @Override
    public Category from(@Nullable SyndCategory syndCategory) {
        if (syndCategory == null) {
            return null;
        }

        final Category category = new Category();

        if (syndCategory.getName() != null) {
            category.setName(syndCategory.getName().trim());
        }

        // we want to ensure any blank content remains a 'null' value
        if (isNotBlank(syndCategory.getLabel())) {
            category.setLabel(syndCategory.getLabel().trim());
        }

        // this is not always a URL
        if (isNotBlank(syndCategory.getTaxonomyUri())) {
            category.setTaxonomyUri(syndCategory.getTaxonomyUri());
        }

        return category;
    }

    /**
     * Convert Category to SyndCategory
     *
     * @param category
     * @return
     */
    @Nullable
    @Override
    public SyndCategory to(@Nullable Category category) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndCategory> fromType() {
        return SyndCategory.class;
    }

    /**
     * Return the 'to' Type Class
     */
    @NotNull
    @Override
    public Class<Category> toType() {
        return Category.class;
    }
}
