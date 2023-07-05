package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.*;
import lombok.Data;

import org.springframework.stereotype.Repository;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Repository
public class EmergencyInfoRepository {

   private static List<EmergencyInfo> emergencyInfos = new ArrayList<>();

    public void fileEmergencyInfo(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            InputData data = objectMapper.readValue(new File("src/main/resources/data.json"), InputData.class);

            List<FireStation> fireStations = data.getFirestations();
            List<MedicalRecord> medicalRecords = data.getMedicalrecords();
            List<Person> persons = data.getPersons();

            for (Person person : persons ){
                EmergencyInfo emergencyInfo = new EmergencyInfo();
                emergencyInfo.setFirstName(person.getFirstName());
                emergencyInfo.setLastName(person.getLastName());
                emergencyInfo.setAddress(person.getAddress());
                emergencyInfo.setCity(person.getCity());
                emergencyInfo.setZip(person.getZip());
                emergencyInfo.setPhone(person.getPhone());
                emergencyInfo.setEmail(person.getEmail());

                for(MedicalRecord medicalRecord : medicalRecords){
                    if (person.getFirstName().equals(medicalRecord.getFirstName()) && person.getLastName().equals(medicalRecord.getLastName())){
                        emergencyInfo.setBirthdate(medicalRecord.getBirthdate());
                        emergencyInfo.setMedications(medicalRecord.getMedications());
                        emergencyInfo.setAllergies(medicalRecord.getAllergies());
                    }
                }

                for (FireStation fireStation : fireStations){
                    if (emergencyInfo.getAddress().equals(fireStation.getAddress())){
                        emergencyInfo.setStation(fireStation.getStation());
                    }
                }

                emergencyInfos.add(emergencyInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<EmergencyInfo> getEmergencyInfos(){
        return emergencyInfos;
    }

    public void add(EmergencyInfo emergencyInfo){
        emergencyInfos.add(emergencyInfo);
    }
}
