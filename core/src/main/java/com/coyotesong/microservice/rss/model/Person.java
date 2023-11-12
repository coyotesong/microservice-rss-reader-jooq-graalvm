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

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

/**
 * RSS person, based on Rome's SyndPerson
 */
@Entity(name = "rss.persons")
public class Person implements Serializable, Comparable<Person> {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "bigserial")
    private Long id;
    @Column(precision = 6)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant timestamp = Instant.now();
    @Column(nullable = false)
    private String name;
    @Column
    private String email;
    @Column
    private URL url;

    // this class is the owner of the relationship
    @ManyToMany(targetEntity = Entry.class)
    @JoinTable(schema = "rss", name = "authors_x_entries",
            joinColumns =
            @JoinColumn(name = "person_id", referencedColumnName = "person_id"),
            inverseJoinColumns =
            @JoinColumn(name = "entity_id", referencedColumnName = "entity_id")
    )
    private ArrayList<Person> authorOf = new ArrayList<>();

    // this class is the owner of the relationship
    @ManyToMany(targetEntity = Entry.class)
    @JoinTable(schema = "rss", name = "contributors_x_entries",
            joinColumns =
            @JoinColumn(name = "person_id", referencedColumnName = "person_id"),
            inverseJoinColumns =
            @JoinColumn(name = "entity_id", referencedColumnName = "entity_id")
    )
    private ArrayList<Person> contributorTo = new ArrayList<>();

    /**
     * Default constructor
     */
    public Person() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public ArrayList<Person> getAuthorOf() {
        return authorOf;
    }

    public void setAuthorOf(ArrayList<Person> authorOf) {
        this.authorOf = authorOf;
    }

    public ArrayList<Person> getContributorTo() {
        return contributorTo;
    }

    public void setContributorTo(ArrayList<Person> contributorTo) {
        this.contributorTo = contributorTo;
    }

    @Override
    public int compareTo(@NotNull Person that) {
        if (this == that) {
            return 0;
        }

        if (this.name == null) {
            return 1;
        } else if (that.name == null) {
            return -1;
        }

        int r = this.name.compareTo(that.name);
        if (r != 0) {
            return r;
        }

        if (this.url == null) {
            return 1;
        } else if (that.url == null) {
            return -1;
        }

        r = this.url.toExternalForm().compareTo(that.url.toExternalForm());
        if (r != 0) {
            return r;
        }

        if (this.email == null) {
            return 1;
        } else if (that.email == null) {
            return -1;
        }

        r = this.email.compareTo(that.email);
        if (r != 0) {
            return r;
        }

        // We don't care about these since we ignore the remaining fields in the
        // equals() method but we may break the general contract on compareTo()
        // if we don't include them.
        if (this.timestamp == null) {
            return 1;
        } else if (that.timestamp == null) {
            return -1;
        }

        r = this.timestamp.compareTo(that.timestamp);
        if (r != 0) {
            return r;
        }

        if (this.id == null) {
            return 1;
        } else if (that.id == null) {
            return -1;
        }

        return this.id.compareTo(that.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(email, person.email) && Objects.equals(url, person.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, email);
    }

    @Override
    public String toString() {
        return String.format("Person[id = %s, name: '%s', url: '%s', email: '%s']", getId(),
                getName(), getUrl(), getEmail());
    }
}