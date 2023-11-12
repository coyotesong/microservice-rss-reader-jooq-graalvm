package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Image;
import com.rometools.rome.feed.synd.SyndImage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Convert SyndImage to Image
 */
public class SyndImageToImageConverter implements SyndConverter<SyndImage, Image> {
    private static final Logger LOG = LoggerFactory.getLogger(SyndImageToImageConverter.class);

    /**
     * Convert SyndImage to Image
     *
     * @param syndImage
     * @return
     */
    @Nullable
    @Override
    public Image from(@Nullable SyndImage syndImage) {
        final Image image = new Image();

        image.setDescription(syndImage.getDescription());
        image.setHeight(syndImage.getHeight());
        image.setTitle(syndImage.getTitle());
        image.setWidth(image.getWidth());

        if (StringUtils.isNotBlank(syndImage.getLink())) {
            image.setLink(syndImage.getLink());
            //image.setLink(new URL(syndImage.getLink()))
        }

        if (StringUtils.isNotBlank(syndImage.getUrl())) {
            try {
                image.setUrl(new URL(syndImage.getUrl()));
            } catch (MalformedURLException e) {
                LOG.info("malformed url: {}", e.getMessage());
            }
        }

        return image;
    }

    /**
     * Convert Image to SyndImage
     *
     * @param image
     * @return
     */
    @Nullable
    @Override
    public SyndImage to(@Nullable Image image) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndImage> fromType() {
        return SyndImage.class;
    }

    /**
     * Return the 'to' Type Class
     */
    @NotNull
    @Override
    public Class<Image> toType() {
        return Image.class;
    }
}
