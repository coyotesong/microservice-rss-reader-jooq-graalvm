--
--
--

alter table rss.contents add constraint contents_entries_fkey foreign key (id) references rss.entries(id) on delete cascade;

alter table rome.outlines add constraint outlines_opml_fkey foreign key (id) references rome.opml(id) on delete cascade;
