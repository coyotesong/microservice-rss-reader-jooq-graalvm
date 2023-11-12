package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.model.Opml;
import com.coyotesong.microservice.rss.model.OpmlBody;
import com.coyotesong.microservice.rss.model.Outline;
import com.coyotesong.microservice.rss.repo.OpmlRepository;
import com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables.records.OpmlBodyRecord;
import com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables.records.OpmlRecord;
import com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables.records.OutlinesRecord;
import com.coyotesong.microservice.rss.service.RssService;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import java.util.Collection;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables.Opml.OPML;
import static com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables.OpmlBody.OPML_BODY;
import static com.coyotesong.microservice.rss.repo.jooq.generated.rome.tables.Outlines.OUTLINES;

public class OpmlRepositoryImpl extends AbstractRepositoryImpl<OpmlRecord, Opml, Long> implements OpmlRepository {
    private final RssService rssService;
    private final OutlineRepository outlineRepository;
    private final OpmlBodyRepository bodyRepository;

    public OpmlRepositoryImpl(DSLContext dsl, Configuration configuration, RssService rssService) {
        super(dsl, OPML, Opml.class, configuration);
        this.rssService = rssService;
        this.bodyRepository = new OpmlBodyRepository(dsl, configuration);
        this.outlineRepository = new OutlineRepository(dsl, configuration);
    }

    @Override
    public Long getId(Opml opml) {
        return opml.getId();
    }

    @Override
    public void clear() {
        // we shouldn't need this with a 'CASCADE' on the foreign key
        outlineRepository.clear();
        bodyRepository.clear();
        dsl.deleteFrom(OPML).execute();
    }

    @Override
    public void save(Opml opml) {
        // TODO - check if entry already exists
        dsl.insertInto(OPML).set(dsl.newRecord(OPML, opml)).execute();
    }

    public static class OpmlBodyRepository extends AbstractRepositoryImpl<OpmlBodyRecord, OpmlBody, Long> {
        public OpmlBodyRepository(DSLContext dsl, Configuration configuration) {
            super(dsl, OPML_BODY, OpmlBody.class, configuration);
        }

        @Override
        public Long getId(OpmlBody opmlBody) {
            return opmlBody.getId();
        }

        @Override
        public void clear() {
            dsl.deleteFrom(OPML_BODY).execute();
        }

        public void ssve(OpmlBody opmlBody) {
            super.insert(opmlBody);
        }
    }

    public static class OutlineRepository extends AbstractRepositoryImpl<OutlinesRecord, Outline, Long> {
        public OutlineRepository(DSLContext dsl, Configuration configuration) {
            super(dsl, OUTLINES, Outline.class, configuration);
        }

        @Override
        public Long getId(Outline outline) {
            return outline.getId();
        }

        @Override
        public void clear() {
            dsl.deleteFrom(OUTLINES).execute();
        }

        public void saveAll(Collection<Outline> outlines) {
            // TODO - restore
        }
    }
}
