package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import java.util.Optional;

public interface PersonService{

    public Person savePerson(Person person);

    public Optional<Person> getPerson (String firstName, String lastName);

    public void deletePerson(String firstName,String lastName);
}