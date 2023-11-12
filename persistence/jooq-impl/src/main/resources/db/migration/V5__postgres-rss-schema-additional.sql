--
-- Additional things that aren't automated yet
--

-- alter table rome.opml_content add constraint rome_opml_opml_content_fkey foreign key(id) references rome.opml(id);

-- alter table rss.feeds_content add constraint rss_feeds_feed_content_fkey foreign key(id) references rss.feeds(id);

create table rss.feed_response (
    id                    int8 not null,
    timestamp             timestamp(6) without time zone not null default now()::timestamp(6),
    url                   text not null,
    status_code           int4,
    requires_authentication boolean,
    content_type          text,
    charset               text,
    etag                  text,
    expires               timestamp(0) without time zone,
    last_modified         timestamp(0) without time zone,
    cache_control         text,
    pragma                text,

    constraint rss_feed_response_pkey primary key(id)
);

-- alter table rss.feed_response add constraint rss_feeds_feed_response_fkey foreign key(id) references rss.feeds(id);

create table rss.feeds_entries (
    feed_id               int8 not null,
    entry_id              int8 not null,
    url                   text,

    constraint rss_feeds_entries_pkey primary key(feed_id, entry_id)
);

alter table rss.feeds_entries add constraint feeds_entries_feeds_fkey foreign key (feed_id) references rss.feeds(id) on delete cascade;
alter table rss.feeds_entries add constraint feeds_entries_entries_fkey foreign key (entry_id) references rss.entries(id) on delete cascade;
