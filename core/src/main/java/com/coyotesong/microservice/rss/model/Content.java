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

import javax.persistence.*;
import java.io.Serializable;

/**
 * RSS Content, based on Rome's SyndContent
 */
@Entity(name = "rss.contents")
public class Content implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Embeddable
    public static class ContentKey implements Serializable {
        // @Serial
        private static final long serialVersionUID = 1L;

        @Column(nullable = false)
        private Long id;
        @Column(nullable = false)
        private int position;

        public ContentKey() {
        }

        public ContentKey(Long id, int position) {
            this.id = id;
            this.position = position;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    @EmbeddedId
    private ContentKey key;

    @ManyToOne(fetch = FetchType.LAZY)
    //@MapsId("entryId");
    @JoinColumn(name = "entry_id")
    private Entry entry;

    @Column
    private String mode;
    @Column
    private String type;
    @Column
    private String value;
    @Column
    private String contentType;
    @Column
    private Integer contentLength;
    @Column
    private String charset;

    public Content() {
        this.key = new ContentKey();
    }

    public Content(Long id, int position) {
        this.key = new ContentKey(id, position);
    }

    public Content(Content value) {
        this.key = value.key;
        this.mode = value.mode;
        this.type = value.type;
        this.value = value.value;
        this.contentType = value.contentType;
        this.contentLength = value.contentLength;
        this.charset = value.charset;
    }

    public Content(
            Long id,
            int position,
            String mode,
            String type,
            String value,
            String contentType,
            Integer contentLength,
            String charset
    ) {
        this.key = new ContentKey(id, position);
        this.mode = mode;
        this.type = type;
        this.value = value;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.charset = charset;
    }

    public ContentKey getKey() {
        return key;
    }

    public void setKey(ContentKey key) {
        this.key = key;
    }

    public void setKey(Long id, int position) {
        if (this.key == null) {
            this.key = new ContentKey(id, position);
        } else {
            this.key.id = id;
            this.key.position = position;
        }
    }

    public Long getId() {
        return key.id;
    }

    public void setId(Long id) {
        this.key.id = id;
    }

    public int getPosition() {
        return key.position;
    }

    public void setPosition(int position) {
        this.key.position = position;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
