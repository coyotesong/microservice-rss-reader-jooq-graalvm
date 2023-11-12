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

import com.coyotesong.microservice.rss.model.Image;
import com.rometools.rome.feed.synd.SyndImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.coyotesong.microservice.rss.internal.ConversionUtilities.toUrl;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Convert SyndImage to Image
 */
public class SyndImageToImageConverter extends AbstractConverter implements SyndConverter<SyndImage, Image> {

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

        if (isNotBlank(syndImage.getLink())) {
            image.setLink(syndImage.getLink());
        }

        if (isNotBlank(syndImage.getUrl())) {
            image.setUrl(toUrl(syndImage.getUrl()));
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
