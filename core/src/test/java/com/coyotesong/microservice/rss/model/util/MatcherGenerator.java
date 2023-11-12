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

import com.coyotesong.microservice.rss.model.Opml;
import com.coyotesong.microservice.rss.model.annotations.Sensitive;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

//import static org.apache.commons.lang3.StringUtils.*;

/**
 * Generate the Hamcrest Matchers
 * <p>
 * These matchers are similar to the Hamcrest "SamePropertyValuesAs" matcher. The biggest differences
 * are that these matchers inspect the JPA annotations and perform checks on '@Sensitive' fields but
 * don't show the actual contents in the message.
 * </p>
 * <p>
 * TODO: the matchers do not currently check '@embedded' values.
 * </p>
 * The matchers do not currently support Collections or Maps.
 * <p>
 * Implementation notes: we could ensure consistency with the actual classes if we used
 * reflection or implemented this as a maven plugin that runs during the 'generate-sources' phase
 * </p>
 *
 * @see <a href="https://hamcrest.org/JavaHamcrest/javadoc/2.2/index.html?org/hamcrest/Matchers.html">SamePropertyValuesAs&lt;T&gt;</a>
 */
public class MatcherGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(MatcherGenerator.class);

    // I think we can check for the 'embedded' annotation instead of this package in
    // the code generation phase.
    private static final String ENTITIES_PACKAGE_NAME = Opml.class.getPackageName();

    private static final List<String> JPA_COLUMN_ANNOTATIONS = Arrays.asList("Column", "Id", "ManyToMany",
            "ManyToOne", "OneToMany", "OneToOne");

    private static final List<String> SAFE_PACKAGES = Arrays.asList("java.lang", "java.net", "java.time");

    public static void main(String[] args) {
        new MatcherGenerator().generate();
    }

    void generate() {
        final Reflections reflections = new Reflections(ENTITIES_PACKAGE_NAME, Scanners.SubTypes);

        final File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        File root = new File(tmpdir, "matchers");

        if (root.exists()) {
            LOG.error("target directory '{}' already exists", root.getAbsolutePath());
            return;
        }

        if (!root.mkdirs()) {
            LOG.error("unable to create target directory '{}'", root.getAbsolutePath());
            return;
        }

        final List<Class<? extends Serializable>> entities =
                reflections.getSubTypesOf(Serializable.class)
                        .stream()
                        .collect(Collectors.toList());
        for (Class<? extends Serializable> entity : entities) {
            generate(root, entity);
        }
    }

    void generate(@NotNull File root, @NotNull Class<? extends Serializable> clz) {
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

        final Map<String, Field> fields = new LinkedHashMap<>();
        for (Field field : clz.getDeclaredFields()) {
            if (isJpaColumn(field)) {
                fields.put(field.getName(), field);
            }
        }

        final List<String> unsupportedColumnNames = new ArrayList<>();
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            if (!isSupported(entry.getValue())) {
                unsupportedColumnNames.add(entry.getKey());
            }
        }

        // is it worth using a templating engine for this?
        final File file = new File(root, simpleName + "Matcher.java");
        try (Writer w = new FileWriter(file);
             PrintWriter pw = new PrintWriter(w)) {
            pw.printf("package %s.matchers;\n", ENTITIES_PACKAGE_NAME);
            pw.println();
            pw.printf("import %s;\n", clz.getName());
            pw.println("");
            pw.println("import org.hamcrest.Description;");
            pw.println("import org.hamcrest.Matcher;");
            pw.println("import org.hamcrest.TypeSafeMatcher;");
            pw.println("import org.hamcrest.core.IsEqual;");
            pw.println("import org.hamcrest.core.IsNull;");
            pw.println();
            pw.println("import java.util.ArrayList;");
            pw.println("import java.util.LinkedHashMap;");
            pw.println("import java.util.List;");
            pw.println("import java.util.Map;");
            pw.println();
            pw.println("import static java.time.format.DateTimeFormatter.ISO_INSTANT;");
            pw.println();
            pw.println("/**");
            pw.printf(" * Hamcrest Matcher for %s objects\n", simpleName);
            pw.println(" * <p>");
            pw.println(" * This file was autogenerated.");
            if (!unsupportedColumnNames.isEmpty()) {
                pw.println(" * </p><p>");
                pw.println(" * Note: it can not (yet) handle these columns:");
                pw.println(" * <ul>");
                for (String name : unsupportedColumnNames) {
                    pw.printf(" *   <li>%s %s</li>\n", fields.get(name).getType().getSimpleName(), name);
                }
                pw.println(" * </ul>");
            }
            unsupportedColumnNames.forEach(fields::remove);
            pw.println(" * </p>");
            pw.println(" */");
            pw.printf("public class %sMatcher extends TypeSafeMatcher<%s> {\n", simpleName, simpleName);
            pw.println("    private final Map<String, Matcher<?>> matchers = new LinkedHashMap<>();");
            pw.println("    private final List<String> expectedContent = new ArrayList<>();");
            pw.printf("    private final %s expected;\n", simpleName);
            pw.println();
            defineMatchers(pw, simpleName, fields);
            pw.println();
            defineDescribeTo(pw, simpleName);
            pw.println();
            defineDescribeMismatchSafely(pw, simpleName, fields);
            pw.println();
            defineMatchesSafely(pw, simpleName, fields);
            pw.println();
            defineStaticFactories(pw, simpleName);
            pw.println("}");
            pw.println();

            pw.flush();
        } catch (IOException e) {
            LOG.error("generate({}): {}", clz.getName(), e.getMessage());
        }
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
     * Can we support this field yet?
     *
     * @return
     */
    boolean isSupported(Field field) {
        final Class<?> cl = field.getType();
        if (Collections.class.isAssignableFrom(cl) || Map.class.isAssignableFrom(cl)) {
            return false;
        }

        return SAFE_PACKAGES.contains(cl.getPackageName());
    }

    /**
     * Is this field sensitive?
     *
     * @param field
     * @return
     */
    boolean isSensitive(Field field) {
        final Sensitive sensitive = field.getType().getAnnotation(Sensitive.class);
        return sensitive != null;
    }

    /**
     * Get pattern to replace field's value.
     *
     * @param field
     * @return
     */
    String getSecurityPattern(Field field) {
        final Sensitive sensitive = field.getType().getAnnotation(Sensitive.class);
        // if ((sensitive == null) || StringUtils.isBlank(sensitive.pattern())) {
        //    return "****";
        //}

        return sensitive.pattern();
    }

    /**
     * Define the 'matchers' that will perform the actual tests
     *
     * @param pw
     * @param simpleName
     * @param fields
     */
    public void defineMatchers(PrintWriter pw, String simpleName, Map<String, Field> fields) {
        pw.println("    /**");
        pw.println("     * Define the matchers that will perform the actual tests\n");
        pw.println("     */");
        pw.printf("    public %sMatcher(%s expected) {\n", simpleName, simpleName);
        pw.println("        this.expected = expected;");
        pw.println();
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            // if embedded...
            //    pw.printf("        matchers.put(\"%s: \" + %sMatcher.matches(expected.%s())));\n", entry.getKey(),
            //            toPascalIdentifierCase(entry.getKey()), entry.getValue());
            //} else {
            if (Instant.class.isAssignableFrom(entry.getValue().getType())) {
                pw.printf("        if (expected.get%s() != null) {\n", capitalize(entry.getKey()));
                pw.printf("            matchers.put(\"%s\", IsEqual.equalTo(ISO_INSTANT.format(expected.get%s())));\n", entry.getKey(),
                        capitalize(entry.getKey()));
                pw.println("        } else {");
                pw.printf("            matchers.put(\"%s\", IsNull.nullValue());\n", entry.getKey());
                pw.println("        }");
            } else if ("boolean".equals(entry.getValue().getType().getSimpleName())) {
                pw.printf("        matchers.put(\"%s\", IsEqual.equalTo(expected.%s()));\n", entry.getKey(),
                        entry.getKey());
            } else {
                if (entry.getValue().getType().getName().contains(".")) {
                    pw.printf("        if (expected.get%s() != null) {\n", capitalize(entry.getKey()));
                    pw.printf("            matchers.put(\"%s\", IsEqual.equalTo(expected.get%s()));\n", entry.getKey(),
                            capitalize(entry.getKey()));
                    pw.println("        } else {");
                    pw.printf("            matchers.put(\"%s\", IsNull.nullValue());\n", entry.getKey());
                    pw.println("        }");
                } else {
                    pw.printf("        matchers.put(\"%s\", IsEqual.equalTo(expected.get%s()));\n", entry.getKey(),
                            capitalize(entry.getKey()));
                }
            }
        }

        pw.println();
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            final Field field = entry.getValue();
            if (Instant.class.isAssignableFrom(entry.getValue().getType())) {
                pw.printf("        if (expected.get%s() != null) {\n", capitalize(entry.getKey()));
                pw.printf("            expectedContent.add(\"%s: \" + ISO_INSTANT.format(expected.get%s()));\n", entry.getKey(),
                        capitalize(entry.getKey()));
                pw.println("        } else {");
                pw.printf("            expectedContent.add(\"%s: null\");\n", entry.getKey());
                pw.println("        }");
            } else if ("boolean".equals(field.getType().getSimpleName())) {
                pw.printf("        expectedContent.add(\"%s: \" + expected.%s());\n", entry.getKey(), entry.getKey());
            } else if (isSensitive(field)) {
                pw.printf("        expectedContent.add(\"%s: \\\"%s\\\"\");\n", entry.getKey(), getSecurityPattern(field));
            } else {
                pw.printf("        expectedContent.add(\"%s: \" + expected.get%s());\n", entry.getKey(), capitalize(entry.getKey()));
            }
        }
        pw.println("    }");
    }

    /**
     * Describe the differences between the actual and expected values
     *
     * @param pw
     * @param simpleName
     * @param fields
     */
    public void defineDescribeMismatchSafely(PrintWriter pw, String simpleName, Map<String, Field> fields) {
        pw.println("    /**");
        pw.println("     * Describe difference between actual and expected values");
        pw.println("     */");
        pw.println("    @Override");
        pw.printf("    public void describeMismatchSafely(%s actual, Description description) {\n", simpleName);
        pw.println("        final List<String> actualContent = new ArrayList<>();");
        pw.println();
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            final Field field = entry.getValue();
            if (isSensitive(field)) {
                pw.printf("            actualContent.add(\"%s: \\\"%s\\\"\");\n", entry.getKey(), getSecurityPattern(field));
            } else if (Instant.class.isAssignableFrom(field.getType())) {
                pw.printf("        if (actual.get%s() != null) {\n", capitalize(entry.getKey()));
                pw.printf("            if (!matchers.get(\"%s\").matches(ISO_INSTANT.format(actual.get%s()))) {\n", entry.getKey(),
                        capitalize(entry.getKey()));
                pw.printf("                actualContent.add(\"%s: \" + ISO_INSTANT.format(actual.get%s()));\n",
                        entry.getKey(), capitalize(entry.getKey()));
                pw.println("            }");
                pw.printf("        } else if (expected.get%s() != null) {\n", capitalize(entry.getKey()));
                pw.printf("            actualContent.add(\"%s: null\");\n", entry.getKey());
                pw.println("        }");
            } else if ("boolean".equals(field.getType().getSimpleName())) {
                pw.printf("        if (!matchers.get(\"%s\").matches(actual.%s())) {\n", entry.getKey(),
                        entry.getKey());
                pw.printf("            actualContent.add(\"%s: \" + actual.%s());\n", entry.getKey(),
                        entry.getKey());
                pw.println("        }");
            } else {
                pw.printf("        if (!matchers.get(\"%s\").matches(actual.get%s())) {\n", entry.getKey(),
                        capitalize(entry.getKey()));
                pw.printf("            actualContent.add(\"%s: \" + actual.get%s());\n", entry.getKey(),
                        capitalize(entry.getKey()));
                pw.println("        }");
            }
        }
        pw.println();
        pw.println("        if (!actualContent.isEmpty()) {");
        pw.printf("            description.appendValueList(\"%s [ \", \", \", \" ]\", actualContent);\n", simpleName);
        pw.println("        }");
        pw.println("    }");
    }

    /**
     * Create the method that describes the <b>expected</b> value.
     *
     * @param pw
     * @param simpleName
     */
    public void defineDescribeTo(PrintWriter pw, String simpleName) {
        pw.println("    /**");
        pw.println("     * Describe the expected value");
        pw.println("     */");
        pw.println("    @Override");
        pw.println("    public void describeTo(Description description) {");
        pw.printf("        description.appendValueList(\"%s: [ \", \", \", \" ]\", expectedContent);\n", simpleName);
        pw.println("    }");
    }

    /**
     * Determine the differences between the specified and actual values.
     *
     * @param pw
     * @param simpleName
     * @param fields
     */
    public void defineMatchesSafely(PrintWriter pw, String simpleName, Map<String, Field> fields) {
        pw.println("    /**");
        pw.println("     * Determine the differences between the expected and actual values.");
        pw.println("     */");
        pw.println("    @Override");
        pw.printf("    protected boolean matchesSafely(%s actual) {\n", simpleName);
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            final Field field = entry.getValue();
            // if embedded...
            //    pw.printf("        if (!matchers.get(\"%s\").matches(actual.%s())) {\n", entry.getKey(), entry.getValue());
            //    pw.println("            return false;");
            //    pw.println("        }");
            // } else {
            if (Instant.class.isAssignableFrom(field.getType())) {
                pw.printf("        if (actual.get%s() != null) {\n", capitalize(entry.getKey()));
                pw.printf("            if (!matchers.get(\"%s\").matches(ISO_INSTANT.format(actual.get%s()))) {\n", entry.getKey(),
                        capitalize(entry.getKey()));
                pw.println("                return false;");
                pw.println("            }");
                pw.printf("        } else if (expected.get%s() != null) {\n", capitalize(entry.getKey()));
                pw.println("            return false;");
                pw.println("        }");
            } else if ("boolean".equals(field.getType().getSimpleName())) {
                pw.printf("        if (!matchers.get(\"%s\").matches(actual.%s())) {\n", entry.getKey(),
                        entry.getKey());
                pw.println("            return false;");
                pw.println("        }");
            } else {
                pw.printf("        if (!matchers.get(\"%s\").matches(actual.get%s())) {\n", entry.getKey(),
                        capitalize(entry.getKey()));
                pw.println("            return false;");
                pw.println("        }");
            }
        }
        pw.println();
        pw.println("        return true;");
        pw.println("    }");
    }

    /**
     * Define static factories
     *
     * @param pw
     * @param simpleName
     */
    public void defineStaticFactories(PrintWriter pw, String simpleName) {
        pw.println("    /**");
        pw.println("      * Static factory");
        pw.println("      * @param expected");
        pw.println("      */");
        pw.printf("    public static %sMatcher equalTo(%s expected) {\n", simpleName, simpleName);
        pw.printf("        return new %sMatcher(expected);\n", simpleName);
        pw.println("    }");
        pw.println();
        pw.println("    /**");
        pw.println("      * Static factory");
        pw.println("      * @param expected");
        pw.println("      */");
        pw.printf("    public static %sMatcher deepEqualTo(%s expected) {\n", simpleName, simpleName);
        pw.println("        throw new AssertionError(\"unimplemented method\");");
        pw.println("    }");
    }

    private String capitalize(String s) {
        if (s.length() <= 1) {
            return s.toUpperCase();
        }

        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
