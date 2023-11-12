--
-- PostgreSQL tables
--
create table rome.opml (
    id                    bigserial not null,
    created_date          timestamp(0) without time zone,
    docs                  text,
    encoding              text,
    feed_type             text,
    modified_date         timestamp(0) without time zone,
    owner_email           text,
    owner_name            text,
    owner_id              text,
    stylesheet            text,
    title                 text,

    constraint rome_opml_pkey primary key(id)
);


create table rome.outlines (
    id                    int8 not null,
    position              int4 not null,
--  opml                  (many-to-one: optional),
    timestamp             timestamp(0) without time zone,
    created_date          timestamp(0) without time zone,
    encoding              text,
    is_breakpoint         boolean,
    is_comment            boolean,
    html_url              text,
    original_url          text,
    text                  text,
    title                 text,
    type                  text,
    url                   text,
    xml_protocol          text,
    xml_link              text,

    constraint rome_outlines_pkey primary key(id, position)
);


create table rome.wire_feeds (
    id                    bigserial not null,
    encoding              text,
    feed_type             text,
    stylesheet            text,

    constraint rome_wire_feeds_pkey primary key(id)
);


create table rss.categories (
    id                    bigserial not null,
    name                  text not null,
    label                 text,
    taxonomy_uri          text,

    constraint rss_categories_pkey primary key(id)
);


create table rss.contents (
    id                    int8 not null,
    position              int4 not null,
--  entry                 (many-to-one: optional),
    mode                  text,
    type                  text,
    value                 text,

    constraint rss_contents_pkey primary key(id, position)
);


create table rss.enclosures (
    id                    bigserial not null,
    type                  text,
    url                   text,
    length                int8,

    constraint rss_enclosures_pkey primary key(id)
);


create table rss.entries (
    timestamp             timestamp(6) without time zone not null default now()::timestamp(6),
    id                    bigserial not null,
    title                 text not null,
    author                text,
    url                   text not null,
    uri                   text not null,
    author_count          int4,
    published_date        timestamp(0) without time zone,
    updated_date          timestamp(0) without time zone,
    link_count            int4,
--  authors               (many-to-many: mapped by entryId),
--  contributors          (many-to-many: mapped by entryId),
--  categories            (many-to-many: parent),
--  contents              (one-to-many: parent),
--  links                 (one-to-many: parent),
--  enclosures            (one-to-many: parent),

    constraint rss_entries_pkey primary key(id)
);


create table rss.feed_authentications (
    feed_id               int8,
    timestamp             timestamp(6) without time zone not null default now()::timestamp(6),
    authentication_url    text not null,
    username              text,
    password              text,

    constraint rss_feed_authentications_pkey primary key(feed_id)
);


create table rss.feed_headers (
    id                    int8 not null,
    name                  text not null,
    position              int4 not null,
    value                 text,

    constraint rss_feed_headers_pkey primary key(id, name, position)
);


create table rss.feeds (
    id                    bigserial not null,
    timestamp             timestamp(6) without time zone not null default now()::timestamp(6),
    url                   text not null,
    original_url          text,
    is_valid              boolean not null default false,
    is_live               boolean not null default false,
    title                 text,
    xml_protocol          text,
    xml_link              text,
    feed_type             text not null,
--  image                 (many-to-one: optional),
--  icon                  (many-to-one: optional),
    copyright             text,
    encoding              text,
    language              text,
    managing_editor       text,
    web_master            text,
    author                text,
    requires_authenticat  boolean not null default false,
    error                 text,
    published_date        timestamp(0) without time zone,
    etag                  text,
    last_modified         timestamp(0) without time zone,
    expires               timestamp(0) without time zone,
    content_type          text,
    status_code           int4,
    charset               text,
    body                  text,
    author_count          int4,
    link_count            int4,
    html_url              text,
--  authors               (many-to-many: parent),
--  categories            (many-to-many: parent),
--  contributors          (many-to-many: parent),
--  entries               (many-to-many: parent),
--  links                 (many-to-many: parent),
--  headers               (many-to-many: parent),

    constraint rss_feeds_pkey primary key(id)
);


create table rss.images (
    id                    bigserial not null,
    title                 text,
    url                   text not null,
    description           text,
    height                int4,
    width                 int4,
    link                  text,

    constraint rss_images_pkey primary key(id)
);


create table rss.links (
    id                    bigserial not null,
    entry_id              int8,
    position              int4,
    title                 text,
    type                  text,
    href                  text not null,
    href_lang             text,
    length                int8,
    rel                   text,

    constraint rss_links_pkey primary key(id)
);


create table rss.persons (
    id                    bigserial not null,
    timestamp             timestamp(6) without time zone,
    name                  text not null,
    email                 text,
    uri                   text,
--  author_of             (many-to-many: parent),
--  contributor_to        (many-to-many: parent),

    constraint rss_persons_pkey primary key(id)
);

