package com.safetynet.alerts.service;

import com.safetynet.alerts.model.EmergencyInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

}
