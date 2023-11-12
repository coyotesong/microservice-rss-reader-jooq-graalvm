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

import com.coyotesong.microservice.rss.model.Link;
import com.rometools.rome.feed.synd.SyndLink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
        if (isNotBlank(syndLink.getHref())) {
            link.setHref(syndLink.getHref());
        }
        if (isNotBlank(syndLink.getHreflang())) {
            link.setHrefLang(syndLink.getHreflang());
        }
        if (isNotBlank(syndLink.getTitle())) {
            link.setTitle(syndLink.getTitle());
        }
        if (isNotBlank(syndLink.getType())) {
            link.setType(syndLink.getType());
        }
        if (isNotBlank(syndLink.getRel())) {
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