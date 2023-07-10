package com.safetynet.alerts.service;

import com.safetynet.alerts.model.EmergencyInfo;
import com.safetynet.alerts.repository.EmergencyInfoRepository;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@Service
public class EmergencyInfoServiceImpl implements EmergencyInfoService {
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    EmergencyInfoRepository emergencyInfoRepository;

    //TODO: Placer les findEmergency dans le repository ou dans le service?
    public List<EmergencyInfo> findEmergencyInfoOfPeopleCoveredByFirestation(Integer station) {

        List<EmergencyInfo> emergencyInfoOfPeopleCoveredByFirestation = emergencyInfoRepository.findAllByStation(station);
        return emergencyInfoOfPeopleCoveredByFirestation;
    }

    public List<EmergencyInfo> findEmergencyInfoOfPeopleByAddress(String address) {

        List<EmergencyInfo> emergencyInfoOfPeopleAtThisAddress = emergencyInfoRepository.findAllByAddress(address);
        return emergencyInfoOfPeopleAtThisAddress;
    }

    public List<EmergencyInfo> findEmergencyinfoOfCHildrenByAddress(String address) {

        List<EmergencyInfo> emergencyInfoOfChildrenAtThisAddress = new ArrayList<>();
        for (EmergencyInfo emergencyInfo : findEmergencyInfoOfPeopleByAddress(address)) {
                if (emergencyInfo.getAge() < 19) {
                    emergencyInfoOfChildrenAtThisAddress.add(emergencyInfo);
                }
        }
        return emergencyInfoOfChildrenAtThisAddress;
    }

    public List<String> findAdultByAddress(String address) {
        List<String> adultAtThisAddress = new ArrayList<>();
        for (EmergencyInfo emergencyInfo : findEmergencyInfoOfPeopleByAddress(address)) {
            if (emergencyInfo.getAge() > 18) {
                adultAtThisAddress.add(emergencyInfo.getFirstName() + " " + emergencyInfo.getLastName());
            }
        }
        return adultAtThisAddress;
    }

    public List<EmergencyInfo> findEmergencyInfoByFirstNameAndLastName(String firstName, String lastName){
        List<EmergencyInfo> emergencyInfoOfThisPersonWithNamesake = new ArrayList<>(); //TODO voir si necessaire de gérer les homonymes dans les endpoints précédents.
        for (EmergencyInfo emergencyInfo : emergencyInfoRepository.getListOfAllEmergencyInfo()) {
            if (emergencyInfo.getFirstName().equals(firstName) && emergencyInfo.getLastName().equals(lastName)) {
                emergencyInfoOfThisPersonWithNamesake.add(emergencyInfo);
            }
        }
        return emergencyInfoOfThisPersonWithNamesake;
    }

    public List<EmergencyInfo> findEmergencyInfoByCity(String city){
        List<EmergencyInfo> emergencyInfoByCity = emergencyInfoRepository.findAllByCity(city);
        return emergencyInfoByCity;
    }

    public int numberOfAdultCoveredByFirestation(Integer station) {
        int numberOfAdult = 0;

        for (EmergencyInfo emergencyInfo : findEmergencyInfoOfPeopleCoveredByFirestation(station)) {
            if (emergencyInfo.getAge() > 18) {
                numberOfAdult++;
            }
        }
        return numberOfAdult;
    }

    public int numberOfChildrenCoveredByFirestation(Integer station) {
        int numberOfChildren = 0;

        for (EmergencyInfo emergencyInfo : findEmergencyInfoOfPeopleCoveredByFirestation(station)) {
            if (emergencyInfo.getAge() < 19) {
                numberOfChildren++;
            }
        }
        return numberOfChildren;
    }

    public List<EmergencyInfo> getAllEmergencyInfo() {
        return emergencyInfoRepository.getListOfAllEmergencyInfo();
    }

    public List<EmergencyInfo> getEmergencyInfoByAddress(String address) {
        return emergencyInfoRepository.findAllByAddress(address);
    }

    public List<EmergencyInfo> getEmergencyInfoByStation(Integer station) {
        return emergencyInfoRepository.findAllByStation(station);
    }
}
