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

    public Map<String,Object> findEmergencyInfoOfPeopleCoveredByFirestation(Integer station){
        int numberOfChilden = 0;
        int numberofAdult = 0;

        emergencyInfoRepository.fileEmergencyInfo();

        List<EmergencyInfo>emergencyInfoOfPeopleCoveredByFirestation = new ArrayList<>();
        for (EmergencyInfo emergencyInfo: emergencyInfoRepository.getListOfAllEmergencyInfo()) {
            if(emergencyInfo.getStation() == station){
                emergencyInfoOfPeopleCoveredByFirestation.add(emergencyInfo);
                if (emergencyInfo.getAge() < 19){
                    numberOfChilden ++;
                } else {
                    numberofAdult ++;
                }
            }
        }

        Map<String,Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("Personnes couvertes par le centre de secours n°" + station + ":", emergencyInfoOfPeopleCoveredByFirestation);
        endpointExpected.put("Nombre d'aldutes présent:",numberofAdult);
        endpointExpected.put("Nombre d'enfants présent:",numberOfChilden);

        return endpointExpected;
    }
}
