package com.coyotesong.microservice.rss.service.rome.converters;

import com.coyotesong.microservice.rss.model.Person;
import com.rometools.rome.feed.synd.SyndPerson;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Convert SyndPerson to Person
 */
public class SyndEnclosureToEnclosureConverter extends AbstractConverter implements SyndConverter<SyndPerson, Person> {

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
