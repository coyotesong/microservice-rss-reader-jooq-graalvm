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

package com.coyotesong.microservice.rss.model.util;

import com.coyotesong.microservice.rss.model.Feed;
import com.coyotesong.microservice.rss.model.util.flyway.GenerateInitialDataScript;
import com.coyotesong.microservice.rss.model.util.flyway.GenerateTablesScript;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lightweight utility to create Flyway DB configuration files.
 * <p>
 * FIXME: add relationships
 */
public class FlywayGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(FlywayGenerator.class);

    // I think we can check for the 'embedded' annotation instead of this package in
    // the code generation phase.
    private static final String ENTITIES_PACKAGE_NAME = Feed.class.getPackageName();

    public static void main(String[] args) throws IOException {
        try {
            new FlywayGenerator().generate();
        } catch (IOException e) {
            LOG.warn("IOException {}", e.getMessage());
        }
    }

    void generate() throws IOException {
        final Reflections reflections = new Reflections(ENTITIES_PACKAGE_NAME, Scanners.SubTypes);

        final File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        final File root = new File(tmpdir, "db/migration");

        if (root.exists()) {
            LOG.error("target directory '{}' already exists", root.getAbsolutePath());
            return;
        }

        if (!root.mkdirs()) {
            LOG.error("unable to create target directory '{}'", root.getAbsolutePath());
            return;
        }

        final Comparator<Class<? extends Serializable>> comparator = new EntityComparator();
        final List<Class<? extends Serializable>> entities = reflections.getSubTypesOf(Serializable.class)
                .stream()
                .filter(clz -> clz.getAnnotation(Entity.class) != null)
                .sorted(comparator)
                .collect(Collectors.toList());

        new GenerateTablesScript(root).generate(entities);
        new GenerateInitialDataScript(root).generate();
    }

    static class EntityComparator implements Comparator<Class<? extends Serializable>> {
        @Override
        public int compare(Class<? extends Serializable> p, Class<? extends Serializable> q) {
            Entity pEntity = p.getAnnotation(Entity.class);
            Entity qEntity = q.getAnnotation(Entity.class);
            if ((p == null) || (q == null)) {
                throw new IllegalArgumentException("one or both objects are lacking required annotation!");
            }

            return pEntity.name().compareTo(qEntity.name());
        }
    }
}