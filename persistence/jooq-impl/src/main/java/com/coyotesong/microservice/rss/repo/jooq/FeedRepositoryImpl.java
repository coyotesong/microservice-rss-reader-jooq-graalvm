package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.model.Feed;
import com.coyotesong.microservice.rss.repo.*;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.FeedsRecord;
import com.coyotesong.microservice.rss.service.RssService;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import java.util.Collection;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Feeds.FEEDS;

public class FeedRepositoryImpl extends AbstractRepositoryImpl<FeedsRecord, Feed, Long> implements FeedRepository {

    private final RssService rssService;
    private final CategoryRepository categoryRepository;
    private final EntryRepository entryRepository;
    private final ImageRepository imageRepository;
    private final LinksRepository linkRepository;
    private final PersonRepository personRepository;

    public FeedRepositoryImpl(DSLContext dsl, RssService rssService, CategoryRepository categoryRepository,
                              EntryRepository entryRepository,
                              ImageRepository imageRepository,
                              LinksRepository linkRepository,
                              PersonRepository personRepository) {
        super(dsl, FEEDS, Feed.class);
        this.rssService = rssService;
        this.categoryRepository = categoryRepository;
        this.entryRepository = entryRepository;
        this.linkRepository = linkRepository;
        this.imageRepository = imageRepository;
        this.personRepository = personRepository;
    }

    public FeedRepositoryImpl(DSLContext dsl, Configuration configuration, RssService rssService,
                              CategoryRepository categoryRepository,
                              EntryRepository entryRepository,
                              ImageRepository imageRepository, LinksRepository linkRepository,
                              PersonRepository personRepository) {
        super(dsl, FEEDS, Feed.class, configuration);
        this.rssService = rssService;
        this.categoryRepository = categoryRepository;
        this.entryRepository = entryRepository;
        this.linkRepository = linkRepository;
        this.imageRepository = imageRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Long getId(Feed feed) {
        return feed.getId();
    }

    @Override
    public void clear() {
        dsl.deleteFrom(FEEDS).execute();
    }

    @Override
    public void saveAll(Collection<Feed> feeds) {
        // FIXME - reimplement...
    }
}