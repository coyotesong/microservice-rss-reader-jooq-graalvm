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

public class PersonsEntries implements Serializable {
    // @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Key key;

    public PersonsEntries() {
        this.key = new Key();
    }

    public PersonsEntries(Long personId, Long entryId) {
        this.key = new Key(personId, entryId);
    }

    public Long getEntryId() {
        return key.entryId;
    }

    public void setEntryId(Long entryId) {
        this.key.entryId = entryId;
    }

    public Long getPersonId() {
        return key.personId;
    }

    public void setPersonId(Long personId) {
        this.key.personId = personId;
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

        @Column(nullable = false)
        private Long entryId;
        @Column(nullable = false)
        private Long personId;

        public Key() {
        }

        public Key(Person person, Entry entry) {
            this.personId = person.getId();
            this.entryId = entry.getId();
        }

        public Key(Long personId, Long entryId) {
            this.personId = personId;
            this.entryId = entryId;
        }

        public Long getEntryId() {
            return entryId;
        }

        public void setEntryId(Long entryId) {
            this.entryId = entryId;
        }

        public Long getPersonId() {
            return personId;
        }

        public void setPersonId(Long personId) {
            this.personId = personId;
        }
    }
}
