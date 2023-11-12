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

package com.coyotesong.microservice.rss.model.util.flyway;

// import org.apache.commons.lang3.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static java.lang.String.format;

/**
 * Generate 'tables' script
 */
public class GenerateTablesScript {
    private static final Logger LOG = LoggerFactory.getLogger(GenerateTablesScript.class);

    private static final Map<String, String> DEFAULT_COLUMN_DEFINITIONS = new LinkedHashMap<>();

    private static final List<String> JPA_COLUMN_ANNOTATIONS = Arrays.asList("Column", "Id", "ManyToMany",
            "ManyToOne", "OneToMany", "OneToOne", "Embeddable");

    static {
        DEFAULT_COLUMN_DEFINITIONS.put("boolean", "boolean");
        DEFAULT_COLUMN_DEFINITIONS.put("int", "int4");
        DEFAULT_COLUMN_DEFINITIONS.put("long", "int8");

        DEFAULT_COLUMN_DEFINITIONS.put("Boolean", "boolean");
        DEFAULT_COLUMN_DEFINITIONS.put("Integer", "int4");
        DEFAULT_COLUMN_DEFINITIONS.put("Long", "int8");
        DEFAULT_COLUMN_DEFINITIONS.put("String", "text");

        // local extensions (with jOOQ mappers, JPA?)
        DEFAULT_COLUMN_DEFINITIONS.put("URL", "text");
        DEFAULT_COLUMN_DEFINITIONS.put("Instant", "timestamp");
    }

    private final File root;

    public GenerateTablesScript(File root) {
        this.root = root;
    }

    public void generate(List<Class<? extends Serializable>> entities) throws IOException {
        final File tablesFile = new File(root, "V2__postgres-rss-schema-tables.sql");
        try (Writer w = new FileWriter(tablesFile);
             PrintWriter pw = new PrintWriter(w)) {
            pw.print("--\n-- PostgreSQL tables\n--\n");

            for (Class<? extends Serializable> entity : entities) {
                generateTables(pw, entity);
            }
        }
    }

    /**
     * Generate table definition file
     *
     * @param clz
     * @parqm pw
     */
    void generateTables(PrintWriter pw, @NotNull Class<? extends Serializable> clz) throws IOException {
        // requires JPA annotations
        final Entity entityAnnotation = clz.getAnnotation(Entity.class);
        if (entityAnnotation == null) {
            return;
        }

        // unlikely but possible if not using JPA annotations
        if ((clz.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT) {
            return;
        }

        final String simpleName = clz.getSimpleName();

        // use a string builder so we can back out the final comma.
        final List<String> columns = new ArrayList<>();
        for (Field field : clz.getDeclaredFields()) {
            if (field.getAnnotation(Embedded.class) != null) {
                columns.addAll(getEmbeddedColumns(field));
            } else if (field.getAnnotation(EmbeddedId.class) != null) {
                columns.addAll(getEmbeddedColumns(field));
            } else {
                final String columnDefinition = getFullColumnDefinition(field);
                /*
                if (StringUtils.isNotBlank(columnDefinition)) {
                    columns.add(columnDefinition);
                } else {
                    final String relationalDefinition = getRelationalColumn(field);
                    if (StringUtils.isNotBlank(relationalDefinition)) {
                        columns.add(relationalDefinition);
                    }
                }
                */
            }
        }

        if (columns.isEmpty()) {
            return;
        }

        // composite key?
        final String primaryKeyConstraint = getPrimaryKeyConstraint(clz, entityAnnotation.name().replace(".", "_"));

        pw.println();
        pw.printf("create table %s (\n", entityAnnotation.name());
        pw.print(String.join(",\n", columns));

        /*
        final boolean hasPrimaryKeyConstraint = StringUtils.isNotBlank(primaryKeyConstraint);

        if (hasPrimaryKeyConstraint) {
            pw.println(",");
            pw.println();
            pw.print(primaryKeyConstraint);
        }
         */

        pw.println(");");
        pw.println();
    }

    List<String> getEmbeddedColumns(@NotNull Field parentField) {
        final List<String> columns = new ArrayList<>();
        final Class<?> clz = parentField.getType();
        for (Field field : clz.getDeclaredFields()) {
            if (field.getAnnotation(Embeddable.class) != null) {
                columns.addAll(getEmbeddedColumns(field));
            } else {
                final String columnDefinition = getFullColumnDefinition(field);
                /*
                if (StringUtils.isNotBlank(columnDefinition)) {
                    columns.add(columnDefinition);
                } else {
                    final String relationalDefinition = getRelationalColumn(field);
                    if (StringUtils.isNotBlank(relationalDefinition)) {
                        columns.add(relationalDefinition);
                    }
                }
                 */
            }
        }

        return columns;
    }

    /**
     * Get full column definition
     *
     * @param field
     * @return
     */
    String getFullColumnDefinition(@NotNull Field field) {
        final String format = "    %-20.20s  %s%s";

        final StringBuilder sb = new StringBuilder();
        final StringBuilder attributes = new StringBuilder();

        final String columnName = getColumnName(field);
        if (isJpaColumn(field)) {
            final String columnDefinition = getColumnDefinition(field);
            if (columnDefinition == null) {
                return getRelationalColumn(field);
            }

            final Id idAnnotation = field.getAnnotation(Id.class);
            final Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                if ("Instant".equals(field.getType().getSimpleName())) {
                    attributes.append(format("(%d) without time zone", columnAnnotation.precision()));
                }
                if ((idAnnotation != null) || !columnAnnotation.nullable()) {
                    attributes.append(" not null");
                }
                // where is 'default' value defined?
                if (!columnAnnotation.nullable()) {
                    final String simpleName = field.getType().getSimpleName();
                    if ("boolean".equalsIgnoreCase(simpleName)) {
                        attributes.append(" default false");
                    } else if ("Instant".equals(simpleName)) {
                        attributes.append(" default now()::timestamp(" + columnAnnotation.precision() + ")");
                    }
                }
            }

            sb.append(format(format, columnName, columnDefinition, attributes.toString()));
        }

        return sb.toString();
    }

    /**
     * Get primary key constraint, if any
     *
     * @param clz
     * @param tableName
     * @return
     */
    String getPrimaryKeyConstraint(@NotNull Class<?> clz, String tableName) {
        for (Field field : clz.getDeclaredFields()) {
            final Id id = field.getAnnotation(Id.class);
            if (id != null) {
                return format("    constraint %s_pkey primary key(%s)\n", tableName, getColumnName(field));
            }

            final EmbeddedId embeddedId = field.getAnnotation(EmbeddedId.class);
            if (embeddedId != null) {
                final List<String> columns = new ArrayList<>();
                for (Field eField : field.getType().getDeclaredFields()) {
                    if (eField.getAnnotation(Column.class) != null) {
                        columns.add(getColumnName(eField));
                    }
                }
                return format("    constraint %s_pkey primary key(%s)\n", tableName, String.join(", ", columns));
            }
        }

        return null;
    }

    /**
     * Get relational column
     *
     * @param field
     * @return
     */
    String getRelationalColumn(@NotNull Field field) {
        final String format = "--  %-20.20s  (%s%s)";

        final StringBuilder sb = new StringBuilder();
        final String columnName = getColumnName(field);
        if (isJpaColumn(field)) {
            // TODO - check for relationship annotations
            final ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
            final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            final OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            /*
            if (manyToMany != null) {
                final String mappedBy = manyToMany.mappedBy();
                sb.append(format(format, columnName, "many-to-many: ",
                        StringUtils.isBlank(mappedBy) ? "parent" : "mapped by " + mappedBy));
            } else if (manyToOne != null) {
                final boolean optional = manyToOne.optional();
                sb.append(format(format, columnName, "many-to-one",
                        optional ? ": optional" : ""));
            } else if (oneToMany != null) {
                final String mappedBy = oneToMany.mappedBy();
                sb.append(format(format, columnName, "one-to-many: ",
                        StringUtils.isBlank(mappedBy) ? "parent" : "mapped by " + mappedBy));
            } else if (oneToOne != null) {
                final String mappedBy = oneToOne.mappedBy();
                sb.append(format(format, columnName, "one-to-many: ",
                        StringUtils.isBlank(mappedBy) ? "parent" : "mapped by " + mappedBy));
            }
             */
        }

        return sb.toString();
    }

    /**
     * Does this field have a JPA annotation that indicates it's a column?
     *
     * @param field
     * @return
     */
    boolean isJpaColumn(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            if (JPA_COLUMN_ANNOTATIONS.contains(annotation.annotationType().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get column definition from JPA tag or field type.
     * <p>
     * Note: this does not (yet) handle Collections or Maps.
     * </p>
     *
     * @param field
     * @return
     */
    String getColumnDefinition(Field field) {
        final Column columnAnnotation = field.getAnnotation(Column.class);
        /*
        if (columnAnnotation != null && StringUtils.isNotBlank(columnAnnotation.columnDefinition())) {
            return columnAnnotation.columnDefinition();
        }
         */

        final String simpleTypeName = field.getType().getSimpleName();
        if (DEFAULT_COLUMN_DEFINITIONS.containsKey(simpleTypeName)) {
            return DEFAULT_COLUMN_DEFINITIONS.get(simpleTypeName);
        }

        return null;
    }

    /**
     * Get column name from JPA tag or field
     *
     * @param field
     * @return
     */
    String getColumnName(Field field) {
        final Column columnAnnotation = field.getAnnotation(Column.class);
        /*
        if (columnAnnotation != null && StringUtils.isNotBlank(columnAnnotation.name())) {
            return columnAnnotation.name();
        }
         */

        final StringBuffer sb = new StringBuffer();
        for (char ch : field.getName().toCharArray()) {
            if (Character.isUpperCase(ch)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(ch));
        }
        return sb.toString();
    }
}
