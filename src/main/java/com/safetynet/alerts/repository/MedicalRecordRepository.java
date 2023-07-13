package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName);
    //Et oui ça marche! "And" est un mot clé JPA ("Or" aussi).

    void deleteByFirstNameAndLastName(String firstName, String lastName);

    List<MedicalRecord> findAll();
}
