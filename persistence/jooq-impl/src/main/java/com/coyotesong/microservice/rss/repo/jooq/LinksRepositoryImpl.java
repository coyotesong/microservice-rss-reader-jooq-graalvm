package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.model.Link;
import com.coyotesong.microservice.rss.repo.LinksRepository;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.LinksRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Links.LINKS;

public class LinksRepositoryImpl extends AbstractRepositoryImpl<LinksRecord, Link, Long> implements LinksRepository {

    public LinksRepositoryImpl(DSLContext dsl) {
        super(dsl, LINKS, Link.class);
    }

    public LinksRepositoryImpl(DSLContext dsl, Configuration configuration) {
        super(dsl, LINKS, Link.class, configuration);
    }

    @Override
    public Long getId(Link link) {
        return link.getId();
    }

    @Override
    public void clear() {
        dsl.deleteFrom(LINKS).execute();
    }
}
