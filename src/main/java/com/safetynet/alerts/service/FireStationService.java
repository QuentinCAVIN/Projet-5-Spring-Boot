package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import java.util.Optional;

public interface FireStationService {

    public Optional<FireStation> getFireStation(final String address);

    public Iterable<FireStation> getFireStations();

    public void deleteFireStation(final Long id);

    public void deleteFireStation(final String address);

    public FireStation saveFireStation(FireStation fireStation);
}