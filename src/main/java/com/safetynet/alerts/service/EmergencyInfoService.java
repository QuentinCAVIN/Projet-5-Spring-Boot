package com.safetynet.alerts.service;

import com.safetynet.alerts.model.EmergencyInfo;

import java.util.Map;

public interface EmergencyInfoService {
    public Map<String,Object> findPersonsCoveredByFireStation (Integer station);

    public void setAge(EmergencyInfo emergencyInfo);
}
