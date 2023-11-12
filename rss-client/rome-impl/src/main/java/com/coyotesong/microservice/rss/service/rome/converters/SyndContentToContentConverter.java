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

