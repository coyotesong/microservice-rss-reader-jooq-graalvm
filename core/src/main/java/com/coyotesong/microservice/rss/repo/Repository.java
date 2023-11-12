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

package com.coyotesong.microservice.rss.repo;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Methods common to all repositories.
 *
 * @param <P> The generic POJO type.
 * @param <T> The generic primary key type. This is a regular &lt;T&gt; type for single-column keys, or a Record subtype for composite keys.
 */
public interface Repository<P, T> {

    T getId(P object);

    long count();

    boolean exists(P object);

    boolean existsById(T id);

    @NotNull
    List<@NotNull P> findAll();

    P findById(T id);

    @NotNull
    Optional<@NotNull P> findOptionalById(@NotNull T id);

    void clear();
}