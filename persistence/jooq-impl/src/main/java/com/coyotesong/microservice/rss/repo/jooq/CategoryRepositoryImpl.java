package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.model.Category;
import com.coyotesong.microservice.rss.repo.CategoryRepository;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.CategoriesRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Categories.CATEGORIES;

public class CategoryRepositoryImpl extends AbstractRepositoryImpl<CategoriesRecord, Category, Long> implements CategoryRepository {

    public CategoryRepositoryImpl(DSLContext dsl) {
        super(dsl, CATEGORIES, Category.class);
    }

    public CategoryRepositoryImpl(DSLContext dsl, Configuration configuration) {
        super(dsl, CATEGORIES, Category.class, configuration);
    }

    @Override
    public Long getId(Category category) {
        return category.getId();
    }

    @Override
    public void clear() {
        dsl.deleteFrom(CATEGORIES).execute();
    }
}