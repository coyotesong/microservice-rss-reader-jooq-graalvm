package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.model.Image;
import com.coyotesong.microservice.rss.repo.ImageRepository;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.ImagesRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Images.IMAGES;

public class ImageRepositoryImpl extends AbstractRepositoryImpl<ImagesRecord, Image, Long> implements ImageRepository {

    public ImageRepositoryImpl(DSLContext dsl) {
        super(dsl, IMAGES, Image.class);
    }

    public ImageRepositoryImpl(DSLContext dsl, Configuration configuration) {
        super(dsl, IMAGES, Image.class, configuration);
    }

    @Override
    public Long getId(Image image) {
        return image.getId();
    }

    @Override
    public void clear() {
        dsl.deleteFrom(IMAGES).execute();
    }
}