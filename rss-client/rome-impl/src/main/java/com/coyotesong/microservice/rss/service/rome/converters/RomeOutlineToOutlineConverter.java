/*
 * Copyright (c) 2023. Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.internal.ConversionUtilities;
import com.rometools.opml.feed.opml.Outline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Instant;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
        outline.setCreatedDate(ConversionUtilities.toInstant(romeOutline.getCreated()));
        outline.setHtmlUrl(ConversionUtilities.toUrl(romeOutline.getHtmlUrl()));
        outline.setText(romeOutline.getText());
        outline.setTitle(romeOutline.getTitle());
        outline.setType(romeOutline.getType());
        outline.setXmlUrl(ConversionUtilities.toUrl(romeOutline.getXmlUrl()));

        // this value seems to always be null.
        // romeOutline.getUrl();

        outline.setAttributes(fromAttributes(romeOutline.getAttributes()));
        outline.setCategories(romeOutline.getCategories());
        outline.setModules(fromModules(romeOutline.getModules()));

        return outline;
    }

    @Nullable
    private URL toUrl(@NotNull Outline romeOutline) {
        return ConversionUtilities.toUrl(isBlank(romeOutline.getUrl()) ? romeOutline.getXmlUrl() : romeOutline.getUrl());
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
