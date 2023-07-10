package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmergencyInfoRepository {
    @Autowired
    FireStationRepository fireStationRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    private static List<EmergencyInfo> listOfAllEmergencyInfo = new ArrayList<>();

    private void fileEmergencyInfo() {

        listOfAllEmergencyInfo.clear();

        List<FireStation> allFireStations = fireStationRepository.findAll();
        List<MedicalRecord> allMedicalRecords = medicalRecordRepository.findAll();
        List<Person> allPersons = personRepository.findAll();

        for (Person person : allPersons) {
            EmergencyInfo emergencyInfo = new EmergencyInfo();
            emergencyInfo.setFirstName(person.getFirstName());
            emergencyInfo.setLastName(person.getLastName());
            emergencyInfo.setAddress(person.getAddress());
            emergencyInfo.setCity(person.getCity());
            emergencyInfo.setZip(person.getZip());
            emergencyInfo.setPhone(person.getPhone());
            emergencyInfo.setEmail(person.getEmail());

            for (MedicalRecord medicalRecord : allMedicalRecords) {
                if (person.getFirstName().equals(medicalRecord.getFirstName()) && person.getLastName().equals(medicalRecord.getLastName())) {
                    emergencyInfo.setBirthdate(medicalRecord.getBirthdate());
                    emergencyInfo.setAge();
                    emergencyInfo.setMedications(medicalRecord.getMedications());
                    emergencyInfo.setAllergies(medicalRecord.getAllergies());
                }
            }

            for (FireStation fireStation : allFireStations) {
                if (emergencyInfo.getAddress().equals(fireStation.getAddress())) {
                    emergencyInfo.setStation(fireStation.getStation());
                }
            }
            listOfAllEmergencyInfo.add(emergencyInfo);
        }
    }

    public List<EmergencyInfo> getListOfAllEmergencyInfo() {
        fileEmergencyInfo();
        return listOfAllEmergencyInfo;
    }

    public List<EmergencyInfo> findAllByStation (Integer station){
        fileEmergencyInfo();
        List<EmergencyInfo> emergencyInfoByStation= new ArrayList<>();
        for (EmergencyInfo emergencyInfo : listOfAllEmergencyInfo){
            if (emergencyInfo.getStation().equals(station)){
                emergencyInfoByStation.add(emergencyInfo);
            }
        }
        return emergencyInfoByStation;
    }

    public List<EmergencyInfo> findAllByAddress (String address){
        fileEmergencyInfo();
        List<EmergencyInfo> emergencyInfoByAddress = new ArrayList<>();
        for (EmergencyInfo emergencyInfo : listOfAllEmergencyInfo){
            if (emergencyInfo.getAddress().equals(address)){
                emergencyInfoByAddress.add(emergencyInfo);
            }
        }
        return emergencyInfoByAddress;
    }

    public List<EmergencyInfo> findAllByCity (String city){
        fileEmergencyInfo();
        List<EmergencyInfo> emergencyInfoByCity = new ArrayList<>();
        for (EmergencyInfo emergencyInfo : listOfAllEmergencyInfo){
            if (emergencyInfo.getCity().equals(city)){
                emergencyInfoByCity.add(emergencyInfo);
            }
        }
        return emergencyInfoByCity;
    }
}
