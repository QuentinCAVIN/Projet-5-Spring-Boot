package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
http://localhost:8080/firestation
        Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete avec HTTP :
        ● ajout d'un mapping caserne/adresse ;
        ● mettre à jour le numéro de la caserne de pompiers d'une adresse ;
        ● supprimer le mapping d'une caserne ou d'une adresse.
        */

@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    //● ajout d'un mapping caserne/adresse

    /**
     * Create - Add a new fire station
     *
     * @param fireStation An object fire station
     * @return The fire station object saved
     */
    @PostMapping("/firestation")
    public FireStation createFireStation(@RequestBody FireStation fireStation) {
        //RequestBody va servir a Spring pour convertir
        // le resultat de la requete http en objet Java Firestation
        return fireStationService.saveFireStation(fireStation);
    }

    //● mettre à jour le numéro de la caserne de pompiers d'une adresse

    /**
     * Update - Update an existing fire station
     *
     * @param address     - The address of the fire station to update
     * @param fireStation - The fire station object updated
     * @return
     */
    @PutMapping("/firestation")
    public FireStation updateFireStation(@RequestParam("address") final String address, @RequestBody FireStation fireStation) {
        //RequestParam = les parametres a renseigner dans la partie param, en clé/valeur ou clé = address et valeur = 10 rue de la paix
        // http://localhost:8080/firestation?address=29 15th St
        // c'est mieux d'utiliser ce system plutot que d'utiliser /firestation/{address} (sauf pour les ID)
        Optional<FireStation> f = fireStationService.getFireStation(address);
        //Optional<FireStation> est un container qui peut contenir soit un Firestation soit une valeur vide
        if (f.isPresent()) {
            FireStation currentFireStation = f.get();

            int station = fireStation.getStation();
            if (station != 0) {
                currentFireStation.setStation(station);
            }
            fireStationService.saveFireStation(currentFireStation);
            return currentFireStation;
        } else {
            return null; //a vérifier
            //ResponseEntity objet qui renvoie les codes de retour.
            //Regarder ça sur https://openclassrooms.com/fr/courses/4668056-construisez-des-microservices/7652183-renvoyez-les-bons-codes-et-filtrez-les-reponses
        }
    }

    //● supprimer le mapping d'une caserne ou d'une adresse.
    //TODO: vérifier si il ne faudrais pas rechercher par adresse plutot que par id.
    // La consigne supprimer caserne OU adresse est mystérieuse...

    /**
     * Delete - Delete a fire station
     *
     * @param id - The id of the fire station to delete
     */
    @DeleteMapping("/firestation/{id}")
    public void deleteFireStation(@PathVariable("id") final Long id) {
        fireStationService.deleteFireStation(id);
    }

    //● utile à l'enregistrement du fichier json en BDD au démarrage de l'application.

    /**
     * Read - Get all firestations
     *
     * @return - An Iterable object of FireStation full filled
     */
    @GetMapping("/firestations")
    public Iterable<FireStation> getFireStations() {
        return fireStationService.getFireStations();
    }

    // Au cas ou...
    /**
     * Read - Get one fire station
     * @param address The address of the fire station
     * @return An FireStation object full filled
     */
    /*
    @GetMapping(value="/firestation/{id}",produces = "application/json")// sert a spécifier le format
    // de sortie. Doit le faire en json par default normalement.
    public FireStation getFireStation(@PathVariable("address") final String address) {
        //@PathVariable va associer la valeur de l'identifiant "id" passé dans la requéte à Long id
        Optional<FireStation> fireStation = fireStationService.getFireStation(address);
        if(fireStation.isPresent()) {
            return fireStation.get();
        } else {
            return null;
        }
    }
    */
}