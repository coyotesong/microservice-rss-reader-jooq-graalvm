package com.coyotesong.microservice.rss.service.rome.converters;

import com.rometools.opml.feed.opml.Outline;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Instant;

/**
 * Convert OPML outline to Feed, ignoring children.
 */
public class RomeOutlineToOutlineConverter extends AbstractConverter implements SyndConverter<Outline, com.coyotesong.microservice.rss.model.Outline> {

    @Nullable
    @Override
    public com.coyotesong.microservice.rss.model.Outline from(@Nullable Outline romeOutline) {
        if (romeOutline == null) {
            return null;
        }

        final com.coyotesong.microservice.rss.model.Outline outline = new com.coyotesong.microservice.rss.model.Outline();

        outline.setTimestamp(Instant.now());
        outline.setBreakpoint(romeOutline.isBreakpoint());
        outline.setComment(romeOutline.isComment());
        outline.setCreatedDate(toInstant(romeOutline.getCreated()));
        outline.setHtmlUrl(toUrl(romeOutline.getHtmlUrl()));
        outline.setText(romeOutline.getText());
        outline.setTitle(romeOutline.getTitle());
        outline.setType(romeOutline.getType());
        outline.setUrl(toUrl(romeOutline.getUrl()));
        outline.setXmlLink(romeOutline.getXmlUrl());
        //outline.setXmlUrl(toUrl(romeOutline.getXmlUrl()));
        // outline.setUrl(toUrl(romeOutline));
        //if (StringUtils.isNotBlank(romeOutline.getXmlUrl())) {
        //    outline.setXmlLink(romeOutline.getXmlUrl());
        //   outline.setXmlProtocol(romeOutline.getXmlUrl().split(":")[0]);
        //}

        outline.setAttributes(fromAttributes(romeOutline.getAttributes()));
        outline.setCategories(romeOutline.getCategories());
        outline.setModules(fromModules(romeOutline.getModules()));
        // romeOutline.getChildren();

        return outline;
    }

    @Nullable
    private URL toUrl(@NotNull Outline romeOutline) {
        return toUrl(StringUtils.isBlank(romeOutline.getUrl()) ? romeOutline.getXmlUrl() : romeOutline.getUrl());
    }

    /**
     * Produce OPML outline
     */
    @Nullable
    @Override
    public Outline to(@Nullable com.coyotesong.microservice.rss.model.Outline outline) {
        if (outline == null) {
            return null;
        }

        throw new AssertionError("unimplemented");
    }


    /**
     * Return the 'to' Type Class
     */
    @NotNull
    @Override
    public Class<Outline> fromType() {
        return Outline.class;
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<com.coyotesong.microservice.rss.model.Outline> toType() {
        return com.coyotesong.microservice.rss.model.Outline.class;
    }
}
