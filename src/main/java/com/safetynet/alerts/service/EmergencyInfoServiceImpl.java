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
    public Map <String,Object> findEmergencyInfoOfPeopleCoveredByFirestation(Integer station){
        int numberOfChilden = 0;
        int numberOfAdult = 0;

        List<EmergencyInfo>emergencyInfoOfPeopleCoveredByFirestation = new ArrayList<>();
        for (EmergencyInfo emergencyInfo: getAllEmergencyInfo()) {
            if(emergencyInfo.getStation() == station){
                emergencyInfoOfPeopleCoveredByFirestation.add(emergencyInfo);
                if (emergencyInfo.getAge() < 19){
                    numberOfChilden ++;
                } else {
                    numberOfAdult ++;
                }
            }
        }

        Map <String,Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("Personnes couvertes par le centre de secours n°" + station + ":", emergencyInfoOfPeopleCoveredByFirestation);
        endpointExpected.put("Nombre d'adultes présent:",numberOfAdult);
        endpointExpected.put("Nombre d'enfants présent:",numberOfChilden);

        return endpointExpected;
    }

    public Map<String,Object> findEmergencyinfoOfCHildrenByAddress(String address){

        List<EmergencyInfo>emergencyInfoOfChildAtThisAddress = new ArrayList<>();
        List<String> adultAtThisAddress = new ArrayList<>();
        for (EmergencyInfo emergencyInfo:getAllEmergencyInfo()) {
            if(emergencyInfo.getAddress().equals(address)) {
                if (emergencyInfo.getAge() < 19) {
                    emergencyInfoOfChildAtThisAddress.add(emergencyInfo);
                } else {
                    if (!adultAtThisAddress.contains(emergencyInfo.getFirstName() + " " + emergencyInfo.getLastName()))
                    adultAtThisAddress.add(emergencyInfo.getFirstName() + " " + emergencyInfo.getLastName());
                }
            }
        }

        Map <String,Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("Enfants présents au " + address + ":", emergencyInfoOfChildAtThisAddress);
        endpointExpected.put("Adultes présents à cette adresse:",adultAtThisAddress);

        return endpointExpected;
    }

    public List <EmergencyInfo> getAllEmergencyInfo(){
       return emergencyInfoRepository.getListOfAllEmergencyInfo();
    }

    public List <EmergencyInfo> getEmergencyInfoByAddress(String address){
        return emergencyInfoRepository.findAllByAddress(address);
    }

    public List <EmergencyInfo> getEmergencyInfoByStation(Integer station){
        return emergencyInfoRepository.findAllByStation(station);
    }
}
