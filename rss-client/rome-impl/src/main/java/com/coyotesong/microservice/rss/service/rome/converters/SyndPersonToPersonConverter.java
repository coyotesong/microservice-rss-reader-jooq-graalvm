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

package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Person;
import com.rometools.rome.feed.synd.SyndPerson;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.coyotesong.microservice.rss.internal.ConversionUtilities.toUrl;

/**
 * Convert SyndPerson to Person
 */
public class SyndPersonToPersonConverter extends AbstractConverter implements SyndConverter<SyndPerson, Person> {

    /**
     * Convert SyndPeson to Person
     *
     * @param syndPerson
     * @return
     */
    @Nullable
    @Override
    public Person from(@Nullable SyndPerson syndPerson) {
        final Person person = new Person();

        if (syndPerson != null) {
            if (StringUtils.isNotBlank(syndPerson.getName())) {
                person.setName(syndPerson.getName());
            }
            if (StringUtils.isNotBlank(syndPerson.getEmail())) {
                person.setEmail(syndPerson.getEmail());
            }
            if (StringUtils.isNotBlank(syndPerson.getUri())) {
                person.setUrl(toUrl(syndPerson.getUri()));
            }
        }

        return person;
    }

    /**
     * Convert Person to SyndPerson
     *
     * @param person
     * @return
     */
    @Nullable
    @Override
    public SyndPerson to(@Nullable Person person) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    @Override
    public Class<SyndPerson> fromType() {
        return SyndPerson.class;
    }

    /**
     * Return the 'to' Type Class
     */
    @NotNull
    @Override
    public Class<Person> toType() {
        return Person.class;
    }
}
