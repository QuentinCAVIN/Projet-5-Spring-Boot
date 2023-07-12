package com.safetynet.alerts.controller;

/*
http://localhost:8080/person
Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete avec HTTP :
● ajouter une nouvelle personne ;
● mettre à jour une personne existante (pour le moment, supposons que le prénom et le nom de
famille ne changent pas, mais que les autres champs peuvent être modifiés) ;
● supprimer une personne (utilisez une combinaison de prénom et de nom comme identificateur
unique).
 */

import com.safetynet.alerts.exceptions.AlreadyPresentException;
import com.safetynet.alerts.exceptions.NotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    //● ajouter une nouvelle personne ;
    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        logger.info("une requête Http POST à été reçue à l'url /person avec le body " + person);

        Optional<Person> personAlreadyPresent = personService.getPerson(person.getFirstName(), person.getLastName());
        if (personAlreadyPresent.isPresent()) {
            throw new AlreadyPresentException(person.getFirstName() + " " + person.getLastName() +" est déjà enregistré: \""+ personAlreadyPresent.orElse(null) +"\"");
        }
        Person personSaved = personService.savePerson(person);
        logger.info("L'objet {} à été créé", personSaved);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
    //prénom et le nom de famille ne changent pas) ;
    @PutMapping("/person")
    public ResponseEntity<Person> updatePerson(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName, @RequestBody Person person) {
        logger.info("une requête Http PUT à été reçue à l'url /person avec les paramètres {} {} et le body {}.", firstName, lastName, person);

        Optional<Person> personAlreadyPresent = personService.getPerson(firstName, lastName);
        if (personAlreadyPresent.isPresent()) {

            Person currentPerson = personAlreadyPresent.get();
            String address = person.getAddress();
            String city = person.getCity();
            int zip = person.getZip();
            String phone = person.getPhone();
            String email = person.getEmail();

            if (address != null) {
                currentPerson.setAddress(address);
            }
            if (city != null) {
                currentPerson.setCity(city);
            }
            if (zip != 0) {
                currentPerson.setZip(zip);
            }
            if (phone != null) {
                currentPerson.setPhone(phone);
            }
            if (email != null) {
                currentPerson.setEmail(email);
            }
            // TODO voir si il serais mieux d'utiliser un for each pour vérifier que les attributs sont null
            // https://stackoverflow.com/questions/1038308/how-to-get-the-list-of-all-attributes-of-a-java-object-using-beanutils-introspec
            Person personSaved = personService.savePerson(currentPerson);
            logger.info("L'objet à été modifié: " +  personSaved);
            return ResponseEntity.status(HttpStatus.OK).body(personSaved);
        } else {
            throw new NotFoundException("Il n'y a pas de données associé à " + firstName + " " + lastName + ".");
        }
    }

    //● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
    //identificateur unique).
    @Transactional
    @DeleteMapping("/person")
    public ResponseEntity deletePerson(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName) {
        logger.info("une requête Http DELETE à été reçue à l'url /person avec les paramètre {} {}.", firstName, lastName);
        Optional<Person> personAlreadyPresent = personService.getPerson(firstName, lastName);
        if (personAlreadyPresent.isPresent()) {
            personService.deletePerson(firstName,lastName);
            logger.info("L'objet à été supprimé.");
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException ("Il n'y a pas de données associé à " + firstName + " " + lastName + ".");
        }
    }
}