package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;

import java.util.List;
import java.util.Optional;

public interface FireStationService {

    public Optional<FireStation> getFireStation(final String address);

    public Iterable<FireStation> getFireStations();

    public void deleteFireStation(final String address);

    public FireStation saveFireStation(FireStation fireStation);

    public List<String> getAddressesCoveredByStation(Integer station);

    public List<String> getAddressesCoveredByStations(List<Integer> stations);
}