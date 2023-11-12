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
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.WireFeedOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Convert OPML outline to Feed, ignoring children.
 */
public class RomeOpmlToOpmlConverter extends AbstractConverter implements SyndConverter<Opml, com.coyotesong.microservice.rss.model.Opml> {
    public static final RomeOpmlToOpmlConverter INSTANCE = new RomeOpmlToOpmlConverter(new RomeOutlineToOutlineConverter());

    private final RomeOutlineToOutlineConverter outlineConverter;

    public RomeOpmlToOpmlConverter(RomeOutlineToOutlineConverter outlineConverter) {
        this.outlineConverter = outlineConverter;
    }

    /**
     * Convert contents of file containing ROME OPML to OPML
     * @param reader RSS object
     * @return our RSS object
     */
    @Nullable
    public com.coyotesong.microservice.rss.model.Opml from(@NotNull Reader reader) throws ExecutionException {
        final WireFeedInput input = new WireFeedInput();
        input.setXmlHealerOn(true);

        try {
            final WireFeed wireFeed = input.build(reader);

            return from((com.rometools.opml.feed.opml.Opml) wireFeed);
        } catch (FeedException e) {
            throw new ExecutionException(e);
        }
    }
    /**
     * Convert contents of file containing ROME OPML to OPML
     * @param opml ROME opml object
     * @return string containing OPML
     */
    @Nullable
    public String toString(@NotNull com.coyotesong.microservice.rss.model.Opml opml) throws ExecutionException {
        final WireFeedOutput output = new WireFeedOutput();

        try {
            final com.rometools.opml.feed.opml.Opml romeOpml = to(opml);
            return output.outputString(romeOpml, true);
        } catch (FeedException e) {
            throw new ExecutionException(e);
        }
    }

    /**
     * Convert ROME OPML to OPML
     * @param romeOpml RSS object
     * @return our RSS object
     */
    @Nullable
    @Override
    public com.coyotesong.microservice.rss.model.Opml from(@Nullable Opml romeOpml) {
        if (romeOpml == null) {
            return null;
        }

        final com.coyotesong.microservice.rss.model.Opml opml =
            new com.coyotesong.microservice.rss.model.Opml();

        if (romeOpml.getCreated() != null && romeOpml.getCreated().getTime() != 0) {
            opml.setCreatedDate(Instant.ofEpochMilli(romeOpml.getCreated().getTime()));
        }
        opml.setDocs(romeOpml.getDocs());
        opml.setEncoding(romeOpml.getEncoding());
        opml.setFeedType(romeOpml.getFeedType());
        opml.setModifiedDate(ConversionUtilities.toInstant(romeOpml.getModified()));
        opml.setModules(fromModules(romeOpml.getModules()));
        romeOpml.getOutlines();
        opml.setOwnerEmail(romeOpml.getOwnerEmail());
        opml.setOwnerName(romeOpml.getOwnerName());
        opml.setOwnerId(romeOpml.getOwnerId());
        opml.setStylesheet(romeOpml.getStyleSheet());
        opml.setTitle(romeOpml.getTitle());


        if (romeOpml.getOutlines() != null && !romeOpml.getOutlines().isEmpty()) {
            // ensure outlines are unique (per URL)
            // must do this after conversion since we index on the actual URL.
            final Map<String, com.coyotesong.microservice.rss.model.Outline> outlines = new HashMap<>();
            flattenOutlines(outlines, romeOpml.getOutlines());

            final List<String> keys = new ArrayList<>(outlines.keySet());
            Collections.sort(keys);
            opml.setOutlines(keys.stream().map(outlines::get).collect(Collectors.toList()));
        }

        // romeOpml.getExpansionState();
        // romeOpml.getForeignMarkup();
        // romeOpml.getWindowX()...

        return opml;
    }

    void flattenOutlines(Map<String, com.coyotesong.microservice.rss.model.Outline> outlineMap, List<Outline> romeOutlines) {
        if (romeOutlines != null && !romeOutlines.isEmpty()) {
            for (Outline romeOutline : romeOutlines) {
                if (romeOutline != null) {
                    if (isNotBlank(romeOutline.getType())) {
                        final com.coyotesong.microservice.rss.model.Outline outline = outlineConverter.from(romeOutline);
                        final String key = outline.getXmlUrl().toString();
                        if (!outlineMap.containsKey(key)) {
                            outlineMap.put(key, outline);
                        }
                    }
                    if (!romeOutline.getChildren().isEmpty()) {
                        flattenOutlines(outlineMap, romeOutline.getChildren());
                    }
                }
            }
        }
    }

    /**
     * Produce OPML outline
     */
    @Nullable
    @Override
    public Opml to(@Nullable com.coyotesong.microservice.rss.model.Opml opml) {
        if (opml == null) {
            return null;
        }
        // final List<Outline> outlines = feeds.stream().map(outlineConverter::to).collect(Collectors.toList());
        /*
        try {
            Opml opml = new Opml();
            opml.setOutlines(outlines);
            opml.setFeedType("opml_2.0");
            opml.setCreated(new Date());
            // opml.setModified(Date);
            // opml.setOwnerEmail();
            // opml.setOwnerId();
            // opml.setOwnerName();
            // opml.setDocs(String);
            // opml.setTitle(String);
            // opml.setEncoding();
            // opml.setForeignMarkup();
            // opml.setStyleSheet();
            final boolean prettyPrint = true;
            //return output.outputString(opml, prettyPrint);
        }
         */

        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<Opml> fromType() {
        return Opml.class;
    }

    /**
     * Return the 'to' Type Class
     */
    // @Override
    @NotNull
    @Override
    public Class<com.coyotesong.microservice.rss.model.Opml> toType() {
        return com.coyotesong.microservice.rss.model.Opml.class;
    }
}
