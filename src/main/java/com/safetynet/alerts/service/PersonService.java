package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Data
@Service
public class PersonService {

        /*
http://localhost:8080/person
Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete avec HTTP :
● ajouter une nouvelle personne ;
● mettre à jour une personne existante (pour le moment, supposons que le prénom et le nom de
famille ne changent pas, mais que les autres champs peuvent être modifiés) ;
● supprimer une personne (utilisez une combinaison de prénom et de nom comme identificateur
unique).
 */
    //TODO: implémenter les méthodes necessaires pour coller au endpoint ci-dessus.
    // Les methodes ci-dessous ont été recopiées ici a titre d'exemple.
/*
    @Autowired
    private PersonRepository personRepository;

    public Optional<Person> getPerson(final Long id) {
        return personRepository.findById(id);
    }

    public Iterable<Person> getPerson() {
        return personRepository.findAll();
    }

    public void deletePerson(final Long id) {
        personRepository.deleteById(id);
    }

    public Person savePerson(Person person) {
        Person savedPerson = personRepository.save(person);
        return savedPerson;
    }*/
}

