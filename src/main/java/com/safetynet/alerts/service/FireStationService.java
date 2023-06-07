package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class FireStationService {

    @Autowired
    private FireStationRepository fireStationRepository;

    public Optional<FireStation> getFireStation(final Long id) {
        return fireStationRepository.findById(id);
    }

    public Iterable<FireStation> getFireStation() {
        return fireStationRepository.findAll();
    }

    public void deleteFireStation(final Long id) {
        fireStationRepository.deleteById(id);
    }

    public FireStation saveFireStation(FireStation fireStation) {
        FireStation savedFireStation = fireStationRepository.save(fireStation);
        return savedFireStation;
    }
}