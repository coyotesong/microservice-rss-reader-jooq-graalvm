package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.model.Enclosure;
import com.coyotesong.microservice.rss.repo.EnclosureRepository;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.EnclosuresRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Enclosures.ENCLOSURES;

public class EnclosureRepositoryImpl extends AbstractRepositoryImpl<EnclosuresRecord, Enclosure, Long> implements EnclosureRepository {

    public EnclosureRepositoryImpl(DSLContext dsl) {
        super(dsl, ENCLOSURES, Enclosure.class);
    }

    public EnclosureRepositoryImpl(DSLContext dsl, Configuration configuration) {
        super(dsl, ENCLOSURES, Enclosure.class, configuration);
    }

    @Override
    public Long getId(Enclosure enclosure) {
        return enclosure.getId();
    }

    @Override
    public void clear() {
        dsl.deleteFrom(ENCLOSURES).execute();
    }
}