package com.coyotesong.microservice.rss.service.rome.converters;

import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Convert OPML outline to Feed, ignoring children.
 */
public class RomeOpmlToOpmlConverter extends AbstractConverter implements SyndConverter<Opml, com.coyotesong.microservice.rss.model.Opml> {
    private final RomeOutlineToOutlineConverter romeOutlineToOutlineConverter;

    public RomeOpmlToOpmlConverter(RomeOutlineToOutlineConverter romeOutlineToOutlineConverter ) {
        this.romeOutlineToOutlineConverter = romeOutlineToOutlineConverter;
    }

    /**
     * Convert contents of file containing ROME OPML to OPML
     * @param reader RSS object
     * @return our RSS object
     */
    @NotNull
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
        opml.setModifiedDate(toInstant(romeOpml.getModified()));
        opml.setModules(fromModules(romeOpml.getModules()));
        romeOpml.getOutlines();
        opml.setOwnerEmail(romeOpml.getOwnerEmail());
        opml.setOwnerName(romeOpml.getOwnerName());
        opml.setOwnerId(romeOpml.getOwnerId());
        opml.setStylesheet(romeOpml.getStyleSheet());
        opml.setTitle(romeOpml.getTitle());

        if (romeOpml.getOutlines() != null && !romeOpml.getOutlines().isEmpty()) {
            final List<com.coyotesong.microservice.rss.model.Outline> outlines = new ArrayList<>();
            flattenOutlines(outlines, romeOpml.getOutlines());
            opml.setOutlines(outlines);
        }

        // romeOpml.getExpansionState();
        // romeOpml.getForeignMarkup();
        // romeOpml.getWindowX()...

        return opml;
    }

    void flattenOutlines(List<com.coyotesong.microservice.rss.model.Outline> outlines, List<Outline> romeOutlines) {
        if (romeOutlines != null && !romeOutlines.isEmpty()) {
            for (Outline romeOutline : romeOutlines) {
                final com.coyotesong.microservice.rss.model.Outline outline =
                        romeOutlineToOutlineConverter.from(romeOutline);
                if (StringUtils.isNotBlank(outline.getType())) {
                    outlines.add(outline);
                }
                if (!romeOutline.getChildren().isEmpty()) {
                    flattenOutlines(outlines, romeOutline.getChildren());
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
