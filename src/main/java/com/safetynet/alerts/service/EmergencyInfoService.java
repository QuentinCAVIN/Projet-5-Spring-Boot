package com.safetynet.alerts.service;

import com.safetynet.alerts.model.EmergencyInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface EmergencyInfoService {
    public Map <String,Object> findEmergencyInfoOfPeopleCoveredByFirestation(Integer station);
    public Map<String,Object> findEmergencyinfoOfCHildrenByAddress(String address);
    public List<EmergencyInfo> getAllEmergencyInfo();

    public List <EmergencyInfo> getEmergencyInfoByAddress(String address);

    public List <EmergencyInfo> getEmergencyInfoByStation(Integer station);

}
