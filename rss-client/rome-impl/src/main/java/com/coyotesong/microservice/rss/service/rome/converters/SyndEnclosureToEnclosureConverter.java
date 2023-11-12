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

import com.coyotesong.microservice.rss.model.Enclosure;
import com.rometools.rome.feed.synd.SyndEnclosure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.coyotesong.microservice.rss.internal.ConversionUtilities.toUrl;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Convert SyndEnclosure to Ensclosure
 */
public class SyndEnclosureToEnclosureConverter extends AbstractConverter implements SyndConverter<SyndEnclosure, Enclosure> {

    /**
     * Convert SyndEnclosure to Enclosure
     *
     * @param syndEnclosure
     * @return
     */
    @Nullable
    @Override
    public Enclosure from(@Nullable SyndEnclosure syndEnclosure) {
        final Enclosure enclosure = new Enclosure();

        if (syndEnclosure != null) {
            enclosure.setLength(syndEnclosure.getLength());
            enclosure.setType(syndEnclosure.getType());
            if (isNotBlank(syndEnclosure.getUrl())) {
                enclosure.setUrl(toUrl(syndEnclosure.getUrl()));
            }
        }

        return enclosure;
    }

    /**
     * Convert Enclosure to Enclosure
     *
     * @param enclosure
     * @return
     */
    @Nullable
    @Override
    public SyndEnclosure to(@Nullable Enclosure enclosure) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndEnclosure> fromType() {
        return SyndEnclosure.class;
    }

    /**
     * Return the 'to' Type Class
     */
    @NotNull
    @Override
    public Class<Enclosure> toType() {
        return Enclosure.class;
    }
}
