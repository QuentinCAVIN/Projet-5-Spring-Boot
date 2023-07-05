package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.EmergencyInfo;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmergencyInfoServiceImpl implements EmergencyInfoService {
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private PersonRepository personRepository;

    public Map<String,Object> findPersonsCoveredByFireStation (Integer station){
        List<FireStation> fireStations = fireStationRepository.findAllByStation(station);

        List<String> addressesCoveredByFireStation = new ArrayList<String>();
        for(FireStation f : fireStations){
            addressesCoveredByFireStation.add(f.getAddress());
        }

        List<EmergencyInfo> infoAboutPeopleCoveredByFireStation = new ArrayList<EmergencyInfo>();
        for (String address: addressesCoveredByFireStation){

           List<Person> personsAtThisAdress = personRepository.findAllByAddress(address);
           for (Person person: personsAtThisAdress){
               EmergencyInfo infoAboutAPersonCoveredByFireStation = new EmergencyInfo();
               infoAboutAPersonCoveredByFireStation.setFirstName(person.getFirstName());
               infoAboutAPersonCoveredByFireStation.setLastName(person.getLastName());
               infoAboutAPersonCoveredByFireStation.setAddress(person.getAddress());
               infoAboutAPersonCoveredByFireStation.setPhone(person.getPhone());
               //TODO: récupérer Birthday dans MedicalRecord
               // peut-être grouper toutes les informations commune en mémoire
               setAge(infoAboutAPersonCoveredByFireStation);
               infoAboutPeopleCoveredByFireStation.add(infoAboutAPersonCoveredByFireStation);
           }
        }


        Map <String,Object> personsCoveredByFireStation = new HashMap<>();
        personsCoveredByFireStation.put("Personnes couvertes par le centre de secours n°" + station + ":", infoAboutPeopleCoveredByFireStation);
        personsCoveredByFireStation.put("nombre d'aldutes présent:",station);//TEST
        personsCoveredByFireStation.put("nombre d'enfants présent:",station);

        return personsCoveredByFireStation;
    }

    public void setAge(EmergencyInfo emergencyInfo) {

            if (emergencyInfo.getBirthdate() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate =LocalDate.parse(emergencyInfo.getBirthdate(),formatter);
            LocalDate currentDate = LocalDate.now();
            emergencyInfo.setAge((Period.between(birthDate,currentDate)).getYears());
        }
    }
    ////find  By Firestation + for each address : find Person by adress + compteur adulte + compteur enfants

}
