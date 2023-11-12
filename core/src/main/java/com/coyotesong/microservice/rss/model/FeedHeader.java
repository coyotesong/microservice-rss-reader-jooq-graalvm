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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "rss.feed_headers")
public class FeedHeader implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Embeddable
    public class HeaderKey implements Serializable {
        // @Serial
        private static final long serialVersionUID = 1L;

        @Column(nullable = false)
        private Long id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private int position;

        public HeaderKey() {
        }

        public HeaderKey(Long id, String name, int position) {
            this.id = id;
            this.name = name;
            this.position = position;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    @EmbeddedId
    private HeaderKey key;

    @Nullable
    @Column
    private String value;

    public FeedHeader() {
        this.key = new HeaderKey();
    }

    public FeedHeader(@Nullable Long id, @NotNull String name, int position, @Nullable String value) {
        this.key = new HeaderKey(id, name, position);
        this.value = value;
    }

    public FeedHeader(@NotNull String name, int position, @Nullable String value) {
        this(null, name, position, value);
    }

    public HeaderKey getKey() {
        return key;
    }

    public void setKey(HeaderKey key) {
        this.key = key;
    }

    @Nullable
    public Long getId() {
        return key.id;
    }

    public void setId(@Nullable Long id) {
        this.key.id = id;
    }

    @NotNull
    public String getName() {
        return key.name;
    }

    public void setName(@NotNull String name) {
        this.key.name = name;
    }

    public int getPosition() {
        return key.position;
    }

    public void setPosition(int position) {
        this.key.position = position;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public void setValue(@Nullable String value) {
        this.value = value;
    }
}