package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.model.Person;
import com.coyotesong.microservice.rss.repo.PersonRepository;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.records.PersonsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import static com.coyotesong.microservice.rss.repo.jooq.generated.rss.tables.Persons.PERSONS;

public class PersonRepositoryImpl extends AbstractRepositoryImpl<PersonsRecord, Person, Long> implements PersonRepository {

    public PersonRepositoryImpl(DSLContext dsl) {
        super(dsl, PERSONS, Person.class);
    }

    public PersonRepositoryImpl(DSLContext dsl, Configuration configuration) {
        super(dsl, PERSONS, Person.class, configuration);
    }

    @Override
    public Long getId(Person person) {
        return person.getId();
    }

    public void clear() {
        dsl.deleteFrom(PERSONS).execute();
    }
}
