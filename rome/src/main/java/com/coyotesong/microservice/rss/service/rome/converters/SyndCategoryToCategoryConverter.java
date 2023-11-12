package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Category;
import com.rometools.rome.feed.synd.SyndCategory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Convert SyndCategory to Category
 */
public class SyndCategoryToCategoryConverter implements SyndConverter<SyndCategory, Category> {

    /**
     * Convert SyndCategory to Category
     *
     * @param syndCategory
     * @return
     */
    @Nullable
    @Override
    public Category from(@Nullable SyndCategory syndCategory) {
        final Category category = new Category();

        if (StringUtils.isNotBlank(syndCategory.getName())) {
            category.setName(syndCategory.getName());
        }
        if (StringUtils.isNotBlank(syndCategory.getLabel())) {
            category.setLabel(syndCategory.getLabel());
        }
        if (StringUtils.isNotBlank(syndCategory.getTaxonomyUri())) {
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
