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

// import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;
import java.net.URL;

public class FeedEntry implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Key key;
    @Column
    private URL url;

    public FeedEntry() {
        this.key = new Key();
    }

    public FeedEntry(Feed feed, Entry entry) {
        this.key = new Key(feed, entry);
        this.url = entry.getUrl();
    }

    public FeedEntry(Long feedId, Long entryId) {
        this.key = new Key(feedId, entryId);
    }

    public FeedEntry(Long feedId, Long entryId, URL url) {
        this(feedId, entryId);
        this.url = url;
    }

    public Long getEntryId() {
        return key.entryId;
    }

    public void setEntryId(Long entryId) {
        this.key.entryId = entryId;
    }

    public Long getFeedId() {
        return key.feedId;
    }

    public void setFeedId(Long feedId) {
        this.key.feedId = feedId;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    @Embeddable
    public static class Key implements Serializable {
        // @Serial
        private static final long serialVersionUID = 1L;

        private Long entryId;
        private Long feedId;

        public Key() {
        }

        public Key(Feed feed, Entry entry) {
            this.feedId = feed.getId();
            this.entryId = entry.getId();
        }

        public Key(Long feedId, Long entryId) {
            this.feedId = feedId;
            this.entryId = entryId;
        }

        public Long getEntryId() {
            return entryId;
        }

        public void setEntryId(Long entryId) {
            this.entryId = entryId;
        }

        public Long getFeedId() {
            return feedId;
        }

        public void setFeedId(Long feedId) {
            this.feedId = feedId;
        }
    }
}
