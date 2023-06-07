package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FireStationRepository extends CrudRepository<FireStation, Long> {

}
