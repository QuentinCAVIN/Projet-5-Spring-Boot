package com.safetynet.alerts.controller;

import com.safetynet.alerts.exceptions.AlreadyPresentException;
import com.safetynet.alerts.exceptions.NotFoundException;
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

import java.util.Optional;

@RestController
public class FireStationController {
    @Autowired
    private FireStationService fireStationService;

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    //● ajout d'un mapping caserne/adresse
    @PostMapping("/firestation")
    public ResponseEntity createFireStation(@Valid @RequestBody FireStation fireStation) {
        logger.info("une requête Http POST à été reçue à l'url /firestation avec le body " + fireStation);
        //RequestBody va servir a Spring pour convertir
        // le resultat de la requete http en objet Java Firestation

        Optional<FireStation> firestationAlreadyPresent = fireStationService.getFireStation(fireStation.getAddress());
        String addressAlreadyPresent = firestationAlreadyPresent.map(FireStation::getAddress).orElse(null);

        //Cette ligne de code extrait l'adresse (String) de l'objet FireStation contenu dans l'Optional<FireStation>,
        // en utilisant la méthode de référence FireStation::getAddress. Si l'Optional est vide, la valeur null sera renvoyée.
        if (addressAlreadyPresent != null) {
            throw new AlreadyPresentException("Ce centre de secours à déja été créé: \"" + firestationAlreadyPresent.orElse(null) + "\"");
        }

        FireStation fireStationSaved = fireStationService.saveFireStation(fireStation);
        logger.info("L'objet {} à été créé", fireStationSaved);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //● mettre à jour le numéro de la caserne de pompiers d'une adresse
    @PutMapping("/firestation")
    public ResponseEntity<FireStation> updateFireStation(@RequestParam("address") final String address, @RequestBody FireStation fireStation) {
        logger.info("une requête Http PUT à été reçue à l'url /firestation avec le paramètre {} et le body {}.", address, fireStation);
        //RequestParam = les parametres à renseigner dans la partie param, en clé/valeur ou clé = "address" et valeur = "10 rue de la paix"
        // http://localhost:8080/firestation?address=29 15th St
        // c'est mieux d'utiliser ce system plutôt que d'utiliser /firestation/{address} (sauf pour les ID)
        //@PathVariable va associer la valeur de l'identifiant "id" passé dans la requéte à Long id

        Optional<FireStation> firestationAlreadyPresent = fireStationService.getFireStation(address);
        //Optional<FireStation> est un container qui peut contenir soit un Firestation, soit une valeur vide.

        if (firestationAlreadyPresent.isPresent()) {

            FireStation currentFireStation = firestationAlreadyPresent.get();
            Integer station = fireStation.getStation();
            if (station != null) {
                currentFireStation.setStation(station);
            }
            FireStation fireStationSaved = fireStationService.saveFireStation(currentFireStation);
            logger.info("L'objet à été modifié: " + fireStationSaved);
            return ResponseEntity.status(HttpStatus.OK).body(fireStationSaved);
        } else {
            throw new NotFoundException("L'adresse \"" + address + "\" ne correspond à aucun centre de secours.");
        }
    }

    //● supprimer le mapping d'une caserne ou d'une adresse.
    @Transactional// sans ça, code 500. A comprendre
    @DeleteMapping("/firestation")

    public ResponseEntity deleteFireStation(@RequestParam("address") final String address) {
        logger.info("une requête Http DELETE à été reçue à l'url /firestation avec le paramètre {}.", address);

        Optional<FireStation> firestationAlreadyPresent = fireStationService.getFireStation(address);
        if (firestationAlreadyPresent.isPresent()) {
            fireStationService.deleteFireStation(address);
            logger.info("L'objet à été supprimé.");
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("L'adresse \"" + address + "\" ne correspond à aucun centre de secours.");
        }
    }
}