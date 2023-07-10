package com.safetynet.alerts.service;

import com.safetynet.alerts.model.EmergencyInfo;

import java.util.List;

public interface EmergencyInfoService {

    public List <EmergencyInfo> findEmergencyInfoOfPeopleCoveredByFirestation(Integer station);

    public int numberOfAdultCoveredByFirestation (Integer station);

    public int numberOfChildrenCoveredByFirestation (Integer station);

    public List<EmergencyInfo> findEmergencyInfoOfPeopleByAddress(String address);

    public List<EmergencyInfo>findEmergencyinfoOfCHildrenByAddress(String address);

    public List<String> findAdultByAddress(String address);

    public List<EmergencyInfo> getAllEmergencyInfo();

    public List <EmergencyInfo> getEmergencyInfoByAddress(String address);

    public List <EmergencyInfo> getEmergencyInfoByStation(Integer station);

    public List<EmergencyInfo> findEmergencyInfoByFirstNameAndLastName(String firstName, String LastName);

    public List<EmergencyInfo> findEmergencyInfoByCity(String city);
}
