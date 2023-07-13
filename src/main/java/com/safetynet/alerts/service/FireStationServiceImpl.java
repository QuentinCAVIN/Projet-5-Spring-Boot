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

    @Autowired
    private FireStationRepository fireStationRepository;

    public Optional<FireStation> getFireStation(final String address) {
        return fireStationRepository.findByAddress(address);
    }

    public Iterable<FireStation> getFireStations() {
        return fireStationRepository.findAll();
    }

    public void deleteFireStation(final String address) {fireStationRepository.deleteByAddress(address);}

    public FireStation saveFireStation(FireStation fireStation) {
        return fireStationRepository.save(fireStation);
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