package com.safetynet.alerts.service;

import com.safetynet.alerts.model.EmergencySheet;

import java.util.List;

public interface EmergencySheetService {
    public List<EmergencySheet> findPersonsCoveredByFireStation (Integer station);
}
