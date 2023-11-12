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

package com.coyotesong.microservice.rss.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks a field as sensitive
 * <p>
 * Log files often contain sensitive information that should be hidden. It's
 * possible to use pattern matching in the logging framework but that's both
 * incomplete and can have false positives.
 * </p>
 * <p>
 * It is far better to use an explicit annotation that can be checked anywhere
 * we provide information to the user and/or write it to the logs.
 * </p>
 * <p>
 * Only a single sensitivity level is defined at this time. It could be extended
 * in the future, e.g., as a flag that a field should be encrypted withih the
 * database, or even that it should never be stored in the database at all.
 * </p>
 * <p>
 * This annotation is created a stopgap - there must be a standard annotation
 * for this purpose... right?
 * </p>
 * <p>
 * Potential enhancement: use content-aware patterns, e.g., an email pattern
 * that shows the first three characters of the email address and domain.
 * </p>
 * <p>
 * Documentation on using pattern matching in a logger configuration.
 *
 * @see(<a href="https://www.baeldung.com/logback-mask-sensitive-data">Mark
 * Sensitive Data in Logs With Logback</a>)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sensitive {
    enum Level {
        SENSITIVE
    }

    Level level() default Level.SENSITIVE;

    String pattern() default "****";
}
