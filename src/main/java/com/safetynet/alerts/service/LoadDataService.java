package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.InputData;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Data
@Service
public class LoadDataService {
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private PersonRepository personRepository;

    public void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //Trouvé dans la doc Jackson.
            InputData data = objectMapper.readValue(new File("src/main/resources/data.json"), InputData.class);

            //On utilise la methode abstraite de CrudRepository
            // "Spring Data JPA générera automatiquement l'implémentation des méthodes pour vous,
            // en se basant sur les conventions de nommage et la configuration de l'entité et de
            // la source de données."
            List<FireStation> fireStations = data.getFirestations();
            fireStationRepository.saveAll(fireStations);

            List<MedicalRecord> medicalRecords = data.getMedicalrecords();
            medicalRecordRepository.saveAll(medicalRecords);

            List<Person> persons = data.getPersons();
            personRepository.saveAll(persons);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}