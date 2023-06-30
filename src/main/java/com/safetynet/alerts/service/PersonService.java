package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import java.util.Optional;

public interface PersonService{
    public Optional<Person> getPerson(final Long id);

    public Iterable<Person> getPersons();

    public void deletePerson(final Long id);

    public Person savePerson(Person person);

    public Optional<Person> getPerson (String firstName, String lastName);

    void deletePerson(String firstName,String lastName);
}

