package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.EmergencySheet;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmergencySheetServiceImpl implements EmergencySheetService {
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private PersonRepository personRepository;

    public List <EmergencySheet> findPersonsCoveredByFireStation (Integer station){
        List<FireStation> fireStations = fireStationRepository.findAllByStation(station);
        List<String> addressList = new ArrayList<String>();
        List<EmergencySheet> emergencyList = new ArrayList<EmergencySheet>();
        EmergencySheet emergencySheet = new EmergencySheet();

        for(FireStation f : fireStations){
            addressList.add(f.getAddress());
        }

        for (String s: addressList){
           List<Person> personsAtThisAdress = personRepository.findAllByAddress(s);
           for (Person p: personsAtThisAdress){
               emergencySheet.setFirstName(p.getFirstName());
               emergencySheet.setLastName(p.getLastName());
               emergencySheet.setAddress(p.getAddress());
               emergencySheet.setPhone(p.getPhone());
               emergencyList.add(emergencySheet);
           }

            //findPersonne by (s)
        }

        return emergencyList;
    }
    ////find  By Firestation + for each address : find Person by adress + compteur adulte + compteur enfants

}
