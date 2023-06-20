package com.safetynet.alerts.controller;

import com.safetynet.alerts.exceptions.FireStationNotFoundException;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
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
    public ResponseEntity<FireStation> createFireStation(@Valid @RequestBody FireStation fireStation) {
        //RequestBody va servir a Spring pour convertir
        // le resultat de la requete http en objet Java Firestation

        FireStation fireStationAdded = fireStationService.saveFireStation(fireStation);
        if (Objects.isNull(fireStationAdded)) {
            return ResponseEntity.noContent().build();// Je ne comprends pas cette condition
            // qui ne peut pas être atteinte. (si firestation null alors code 400, on n'entre pas dans la methode)
            //De plus si l'objet Firestation est null, le code 204 ne me semble pas approprié.
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
        /*URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{Address}")
                .buildAndExpand(fireStationAdded.getAddress())
                .toUri();
        return ResponseEntity.created(location).build(); remplacer par noContent?
        // Pour pouvoir utiliser created , il faut avoir un endpoint get? pas demandé dans le projet.*/

        //TODO: Ajouter un code 409 conflit apres avoir eu le detail du fonctionnement de cette partie
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
        //RequestParam = les parametres a renseigner dans la partie param, en clé/valeur ou clé = "address" et valeur = "10 rue de la paix"
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
            throw new FireStationNotFoundException("La Caserne de pompier avec l'address " + address + " est introuvable.");
            // le message s'affiche uniquement dans le terminal? comment remédier à ça pour qu'un message apparaisse dans le body de la requete?
        }
            //ResponseEntity objet qui renvoie les codes de retour.
    }

    //● supprimer le mapping d'une caserne ou d'une adresse.
    //TODO: vérifier si il ne faudrais pas rechercher par adresse plutot que par id.
    // La consigne supprimer caserne OU adresse est mystérieuse...

    /**
     * Delete - Delete a fire station
     *
     * @param address - The address of the fire station to delete
     */
    @Transactional// sans ça, code 500. A comprendre
    @DeleteMapping("/firestation")
    public ResponseEntity deleteFireStation(@RequestParam("address") final String address) {
        Optional <FireStation> f = fireStationService.getFireStation(address);
        if (f.isPresent()) {
            fireStationService.deleteFireStation(address);
            return ResponseEntity.noContent().build();
        } else{
            throw new FireStationNotFoundException ("La Caserne de pompier avec l'address " + address + " est introuvable.");
        }
    }

    @GetMapping("/firestations") //pour les tests, a supprimer plus tard
    public Iterable<FireStation> getFireStations() {
        return fireStationService.getFireStations();
    }

    /* Pas demandé mais au cas ou...
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