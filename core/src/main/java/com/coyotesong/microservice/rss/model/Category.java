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
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * RSS Category, based on Rome's SyndCategory
 */
@Entity(name = "rss.categories")
public class Category extends AbstractEntity implements Serializable, Comparable<Category> {
    // @Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    @Id
    @Column(columnDefinition = "bigserial")
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String name;
    @Column
    private String label;
    @Column
    private String taxonomyUri;

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = toTitleCase(name);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTaxonomyUri() {
        return taxonomyUri;
    }

    public void setTaxonomyUri(String taxonomyUri) {
        this.taxonomyUri = taxonomyUri;
    }

    @Override
    public int compareTo(@NotNull Category that) {
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

        if (this.label == null) {
            return 1;
        } else if (that.label == null) {
            return -1;
        }

        r = this.label.compareTo(that.label);
        if (r != 0) {
            return r;
        }

        if (this.taxonomyUri == null) {
            return 1;
        } else if (that.taxonomyUri == null) {
            return -1;
        }

        r = this.taxonomyUri.compareTo(that.taxonomyUri);
        if (r != 0) {
            return r;
        }

        // We don't care about these since we ignore the remaining fields in the
        // equals() method but we may break the general contract on compareTo()
        // if we don't include them.
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
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name) && Objects.equals(label, category.label) && Objects.equals(taxonomyUri, category.taxonomyUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, label, taxonomyUri);
    }
}