package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Link;
import com.rometools.rome.feed.synd.SyndLink;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SyndLinkToLinkConverter implements SyndConverter<SyndLink, Link> {

    /**
     * Convert SyndLink to SLink
     * @param syndLink
     * @return
     */
    @Nullable
    public Link from(@Nullable SyndLink syndLink) {
        if (syndLink == null) {
            return null;
        }

        final Link link = new Link();
        if (StringUtils.isNotBlank(syndLink.getHref())) {
            link.setHref(syndLink.getHref());
        }
        if (StringUtils.isNotBlank(syndLink.getHreflang())) {
            link.setHrefLang(syndLink.getHreflang());
        }
        if (StringUtils.isNotBlank(syndLink.getTitle())) {
            link.setTitle(syndLink.getTitle());
        }
        if (StringUtils.isNotBlank(syndLink.getType())) {
            link.setType(syndLink.getType());
        }
        if (StringUtils.isNotBlank(syndLink.getRel())) {
            link.setRel(syndLink.getRel());
        }
        link.setLength(syndLink.getLength());

        return link;
    }

    /**
     * Convert Link to SyndLink
     * @param link
     * @return
     */
    @Nullable
    @Override
    public SyndLink to(@Nullable Link link) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndLink> fromType() {
        return SyndLink.class;
    }

    /**
     * Return the 'to' Type Class
     */
    @NotNull
    @Override
    public Class<Link> toType() {
        return Link.class;

    }
}