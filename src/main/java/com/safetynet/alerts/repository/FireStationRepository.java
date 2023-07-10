package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface FireStationRepository extends CrudRepository<FireStation, Long> {

    Optional<FireStation> findByAddress(String address);
    Optional<FireStation> findByStation(Integer station);

    List<FireStation> findAllByStation(Integer station);
    List<FireStation> findAll();
     void deleteByAddress (String address);
    //A partir du nom de la methode, JPA va créer l'implémentation de la methode tt seul
    //Attention aux fautes de frappe (True Story)
}

