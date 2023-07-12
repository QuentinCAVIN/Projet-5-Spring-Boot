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

    public List<EmergencyInfo> findEmergencyInfoOfPeopleCoveredByFirestation(Integer station) {
        return emergencyInfoRepository.findAllByStation(station);
    }

    public List<EmergencyInfo> findEmergencyInfoOfPeopleByAddress(String address) {
        return emergencyInfoRepository.findAllByAddress(address);
    }

    public List<EmergencyInfo> findEmergencyinfoOfCHildrenByAddress(String address) {
        return emergencyInfoRepository.findEmergencyinfoOfCHildrenByAddress(address);
    }

    public List<String> findAdultByAddress(String address) {
        return emergencyInfoRepository.findAdultByAddress(address);
    }

    public List<EmergencyInfo> findEmergencyInfoByFirstNameAndLastName(String firstName, String lastName) {
        return emergencyInfoRepository.findEmergencyInfoByFirstNameAndLastName(firstName, lastName);
    }

    public List<EmergencyInfo> findEmergencyInfoByCity(String city) {
        return emergencyInfoRepository.findAllByCity(city);
    }

    public int numberOfAdultCoveredByFirestation(Integer station) {
        return emergencyInfoRepository.numberOfAdultCoveredByFirestation(station);
    }

    public int numberOfChildrenCoveredByFirestation(Integer station) {
        return emergencyInfoRepository.numberOfChildrenCoveredByFirestation(station);
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
