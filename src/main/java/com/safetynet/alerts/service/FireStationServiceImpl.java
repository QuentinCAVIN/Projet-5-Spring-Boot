package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class FireStationServiceImpl implements FireStationService{

    /*
http://localhost:8080/firestation
        Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete avec HTTP :
        ● ajout d'un mapping caserne/adresse ;
        ● mettre à jour le numéro de la caserne de pompiers d'une adresse ;
        ● supprimer le mapping d'une caserne ou d'une adresse.
        */

// Les methodes ci-dessous sont utilisé par FireStationController.
    @Autowired
    private FireStationRepository fireStationRepository;

    public Optional<FireStation> getFireStation(final String address) {
        return fireStationRepository.findByAddress(address);
    }

    public Iterable<FireStation> getFireStations() {
        return fireStationRepository.findAll();
    }

    public void deleteFireStation(final Long id) {
        fireStationRepository.deleteById(id);
    }
    public void deleteFireStation(final String address) {fireStationRepository.deleteByAddress(address);}

    public FireStation saveFireStation(FireStation fireStation) {
        FireStation savedFireStation = fireStationRepository.save(fireStation);
        return savedFireStation;
    }

    public List<String> getAddressesCoveredByStation(Integer station){

        List<String> addressCoveredByStation = new ArrayList<>();
        for (FireStation fireStation : fireStationRepository.findAllByStation(station)){
            String address = fireStation.getAddress();
            addressCoveredByStation.add(address);
        }

        return addressCoveredByStation;
    }

    public List<String> getAddressesCoveredByStations(List<Integer>stations){
        List<String> allAddresses = new ArrayList<>();
        for (Integer station : stations) {
            List<String> addresses = getAddressesCoveredByStation(station);
            for (String address : addresses) {
                allAddresses.add(address);
            }
        }
        return  allAddresses;
    }
}