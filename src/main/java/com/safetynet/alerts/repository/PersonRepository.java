package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.InputData;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepository {
    List<Person> allPersons;

    public void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InputData data = objectMapper.readValue(new File("src/main/resources/data.json"), InputData.class);
            allPersons = data.getPersons();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        for (Person person : allPersons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return Optional.of(person);
            }
        }
        return Optional.empty();
    }


    public void deleteByFirstNameAndLastName(String firstName, String lastName) {
        Iterator<Person> iterator = allPersons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                iterator.remove();
            }
        }
    }

    public List<Person> findAll() {
        return allPersons;
    }

    public Person save(Person person) {
        allPersons.add(person);
        return person;
    }

    public void deleteAll() {
        allPersons.clear();
    }
}