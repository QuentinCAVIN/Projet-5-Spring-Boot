package com.safetynet.alerts.controller;

import com.safetynet.alerts.exceptions.FireStationNotFoundException;
import com.safetynet.alerts.exceptions.FireStationNotValidException;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    //● ajout d'un mapping caserne/adresse
    @PostMapping("/firestation")
    public ResponseEntity createFireStation(@Valid @RequestBody FireStation fireStation){
        //RequestBody va servir a Spring pour convertir
        // le resultat de la requete http en objet Java Firestation
        Optional<FireStation> firestationAlreadyPresent = fireStationService.getFireStation(fireStation.getAddress());
        String addressAlreadyPresent = firestationAlreadyPresent.map(FireStation::getAddress).orElse(null);
        //cette ligne de code extrait l'adresse (String) de l'objet FireStation contenu dans l'Optional<FireStation>,
        // en utilisant la méthode de référence FireStation::getAddress. Si l'Optional est vide, la valeur null sera renvoyée.
        if (addressAlreadyPresent != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        FireStation fireStationAdded = fireStationService.saveFireStation(fireStation);

        //return new ResponseEntity<> ("La caserne" + fireStationAdded + "est créé ",HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //● mettre à jour le numéro de la caserne de pompiers d'une adresse
    @PutMapping("/firestation")
    public FireStation updateFireStation(@RequestParam("address") final String address, @RequestBody FireStation fireStation) {
        //RequestParam = les parametres a renseigner dans la partie param, en clé/valeur ou clé = "address" et valeur = "10 rue de la paix"
        // http://localhost:8080/firestation?address=29 15th St
        // c'est mieux d'utiliser ce system plutot que d'utiliser /firestation/{address} (sauf pour les ID)
        //@PathVariable va associer la valeur de l'identifiant "id" passé dans la requéte à Long id
        Optional<FireStation> f = fireStationService.getFireStation(address);
        //Optional<FireStation> est un container qui peut contenir soit un Firestation soit une valeur vide
        if (f.isPresent()) {
            FireStation currentFireStation = f.get();

            String station = fireStation.getStation();
            if (station != null) {
                currentFireStation.setStation(station);
            }
            fireStationService.saveFireStation(currentFireStation);
            return currentFireStation;
        } else {
            throw new FireStationNotFoundException("Le centre de secours avec l'adresse " + address + " est introuvable.");
        }
    }

    //● supprimer le mapping d'une caserne ou d'une adresse.
    @Transactional// sans ça, code 500. A comprendre
    @DeleteMapping("/firestation")
    public ResponseEntity deleteFireStation(@RequestParam("address") final String address) {
        Optional <FireStation> f = fireStationService.getFireStation(address);
        if (f.isPresent()) {
            fireStationService.deleteFireStation(address);
            return ResponseEntity.noContent().build();
        } else{
            throw new FireStationNotFoundException ("La centre de secours avec l'adresse " + address + " est introuvable.");
        }
    }
}