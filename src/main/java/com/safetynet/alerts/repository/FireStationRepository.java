package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.*;
import org.springframework.stereotype.Repository;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public class FireStationRepository {
    private static List<FireStation> allfireStations;

    public void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //Trouvé dans la doc Jackson.
            InputData data = objectMapper.readValue(new File("src/main/resources/data.json"), InputData.class);

            allfireStations = data.getFirestations();
            System.out.println(allfireStations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<FireStation> findByAddress(String address) {
        for (FireStation fireStation : allfireStations) {
            if (fireStation.getAddress().equals(address)) {
                return Optional.of(fireStation);
            }
        }
        return Optional.empty();
    }

    public List<FireStation> findAllByStation(Integer station) {
        loadData();
        List<FireStation> fireStationByStation = new ArrayList<>();

        for (FireStation fireStation : allfireStations) {
            if (fireStation.getStation().equals(station)) {
                fireStationByStation.add(fireStation);
            }
        }
        return fireStationByStation;
    }

    public List<FireStation> findAll() {
        return allfireStations;
    }

    public void deleteByAddress(String address) {
        Iterator<FireStation> iterator = allfireStations.iterator();
        while (iterator.hasNext()) {
            FireStation fireStation = iterator.next();
            if (fireStation.getAddress().equals(address)) {
                iterator.remove();

            }
        }
    }

    public FireStation save (FireStation fireStation){
        allfireStations.add(fireStation);
        return fireStation;
    }

    public void deleteAll(){
        allfireStations.clear();
    }

}