package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface FireStationRepository extends CrudRepository<FireStation, Long> {

    Optional<FireStation> findByAddress(String address);
    //A partir du nom de la methode, JPA va créer l'implémentation de la methode tt seul
    //Attention aux fautes de frappe (True Story)


   /* public void save (FireStation firestation){
        // list.add de ma liste FireStation (a créer a partir du data.json) voir jackson

        Suggestion Vincent.
        Changement de piste, mais je garde l'idée pour plus tard.
    }*/
}
