package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Content;
import com.rometools.rome.feed.synd.SyndContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Convert SyndContent to Content
 */
public class SyndContentToContentConverter implements SyndConverter<SyndContent, Content> {

    /**
     * Convert SyndContent to Content
     *
     * @param syndContent may be null
     * @return
     */
    @Nullable
    @Override
    public Content from(@Nullable SyndContent syndContent) {
        if (syndContent == null) {
            return null;
        }

        final Content content = new Content();

        content.setMode(syndContent.getMode());
        if ("html".equals(syndContent.getType())) {
            content.setType("text/html");
        } else {
            content.setType(syndContent.getType());
        }
        content.setValue(syndContent.getValue());

        return content;
    }

    /**
     * Convert Content to SyndContent
     *
     * @param content
     * @return
     */
    @Nullable
    @Override
    public SyndContent to(@Nullable Content content) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndContent> fromType() {
        return SyndContent.class;
    }

    /**
     * Return the 'to' Type Class
     */
    // @Override
    @NotNull
    @Override
    public Class<Content> toType() {
        return Content.class;
    }
}

