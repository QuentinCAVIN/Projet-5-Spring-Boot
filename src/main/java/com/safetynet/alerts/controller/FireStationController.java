package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
http://localhost:8080/firestation
        Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete avec HTTP :
        ● ajout d'un mapping caserne/adresse ;
        ● mettre à jour le numéro de la caserne de pompiers d'une adresse ;
        ● supprimer le mapping d'une caserne ou d'une adresse.
        */

//TODO: Les methodes ci-dessous devrait être fonctionelles mais ne respectent pas
// les consignent du projet (voir ci-dessus)
@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    /**
     * Create - Add a new fire station
     * @param fireStation An object fire station
     * @return The fire station object saved
     */
    @PostMapping("/firestation")
    public FireStation createFireStation(@RequestBody FireStation fireStation) {
        return fireStationService.saveFireStation(fireStation);
    }


    /**
     * Read - Get one fire station
     * @param id The id of the fire station
     * @return An FireStation object full filled
     */
    @GetMapping(value="/firestation/{id}",produces = "application/json")// sert a spécifier le format
    // de sortie. Doit le faire en json par default normalement.
    public FireStation getFireStation(@PathVariable("id") final Long id) {
        Optional<FireStation> fireStation = fireStationService.getFireStation(id);
        if(fireStation.isPresent()) {
            return fireStation.get();
        } else {
            return null;
        }
    }

    /**
     * Read - Get all firestations
     * @return - An Iterable object of FireStation full filled
     */
    @GetMapping("/firestations")
    public Iterable<FireStation> getFireStations() {
        return fireStationService.getFireStations();
    }

    /**
     * Update - Update an existing fire station
     * @param id - The id of the fire station to update
     * @param fireStation - The fire station object updated
     * @return
     */
    @PutMapping("/firestation/{id}")
    public FireStation updateFireStation(@PathVariable("id") final Long id, @RequestBody FireStation fireStation) {
        Optional<FireStation> f = fireStationService.getFireStation(id);
        if(f.isPresent()) {
            FireStation currentFireStation = f.get();

            String address = fireStation.getAddress();
            if(address != null) {
                currentFireStation.setAddress(address);
            }
            int station = fireStation.getStation();
            if(station != 0) {
                currentFireStation.setStation(station);
            }
            fireStationService.saveFireStation(currentFireStation);
            return currentFireStation;
        } else {
            return null;
        }
    }


    /**
     * Delete - Delete an fire station
     * @param id - The id of the fire station to delete
     */
    @DeleteMapping("/firestation/{id}")
    public void deleteFireStation(@PathVariable("id") final Long id) {
        fireStationService.deleteFireStation(id);
    }
}