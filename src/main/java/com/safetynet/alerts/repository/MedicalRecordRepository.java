package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.InputData;
import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordRepository {
    private static List<MedicalRecord> allMedicalRecords;

    public void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InputData data = objectMapper.readValue(new File("src/main/resources/data.json"), InputData.class);
            allMedicalRecords = data.getMedicalrecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName){

        for (MedicalRecord medicalRecord : allMedicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                return Optional.of(medicalRecord);
            }
        }
        return Optional.empty();
    }

    public void deleteByFirstNameAndLastName(String firstName, String lastName) {
        Iterator<MedicalRecord> iterator = allMedicalRecords.iterator();
        while(iterator.hasNext()){
            MedicalRecord medicalRecord = iterator.next();
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)){
                iterator.remove();
            }
        }
    }

    public List<MedicalRecord> findAll(){
        return allMedicalRecords;
    }

    public MedicalRecord save (MedicalRecord medicalRecord){
        allMedicalRecords.add(medicalRecord);
        return medicalRecord;
    }

    public void deleteAll(){
        allMedicalRecords.clear();
    }
}
