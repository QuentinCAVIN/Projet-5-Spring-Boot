package com.safetynet.alerts.service;

import com.safetynet.alerts.model.EmergencyInfo;

import java.util.LinkedHashMap;
import java.util.Map;

public interface EmergencyInfoService {
    public Map <String,Object> findEmergencyInfoOfPeopleCoveredByFirestation(Integer station);

}
