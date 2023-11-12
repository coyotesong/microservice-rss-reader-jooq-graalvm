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

package com.coyotesong.microservice.rss.model;

import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;

/**
 * RSS image, based on Rome's SyndImage
 */
@Entity(name = "rss.images")
public class Image implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    @Id
    @Column(columnDefinition = "bigserial")
    private Long id;
    @Column
    private String title;
    @Column(nullable = false)
    private URL url;
    @Column
    private String description;
    @Column
    private Integer height;
    @Column
    private Integer width;
    @Column
    private String link;
    // @Transient
    private byte[] data;

    /**
     * Default constructor
     */
    public Image() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(title, image.title) && Objects.equals(url, image.url) && Objects.equals(description, image.description) && Objects.equals(height, image.height) && Objects.equals(width, image.width) && Objects.equals(link, image.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, url, description, height, width, link);
    }
}
