package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.exceptions.NotFoundException;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.EmergencyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmergencyInfoController {

    @Autowired
    private EmergencyInfoService emergencyInfoService;
    @Autowired
    FireStationRepository fireStationRepository;

    /*Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers correspondante.
    Donc, si le numéro de station = 1, elle doit renvoyer les habitants couverts par la station numéro 1. La liste
    doit inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone. De plus,
    elle doit fournir un décompte du nombre d'adultes et du nombre d'enfants (tout individu âgé de 18 ans ou
    moins) dans la zone desservie.*/
    @GetMapping("/firestation")
    public ResponseEntity findPersonsCoveredByFireStation(@RequestParam("stationNumber") final Integer stationNumber) {
        if (emergencyInfoService.getEmergencyInfoByStation(stationNumber).isEmpty()) {
            throw new NotFoundException("Il n'existe aucun centre de secours n° " + stationNumber + ".");
        }
        Map<String, Object> personCoveredByFireStation = emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(stationNumber);
        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName", "lastName", "address", "phone");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencySheetFilter = new MappingJacksonValue(personCoveredByFireStation);
        emergencySheetFilter.setFilters(filter);
        return ResponseEntity.status(HttpStatus.OK).body(emergencySheetFilter);
        // https://openclassrooms.com/fr/courses/4668056-construisez-des-microservices/7652183-renvoyez-les-bons-codes-et-filtrez-les-reponses
    }


    /*Cette url doit retourner une liste d'enfants (tout individu âgé de 18 ans ou moins) habitant à cette adresse.
    La liste doit comprendre le prénom et le nom de famille de chaque enfant, son âge et une liste des autres
    membres du foyer. S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide.*/
    @GetMapping("/childAlert")
    public ResponseEntity findChildrenByAddress(@RequestParam("address") final String address) {
        if (emergencyInfoService.getEmergencyInfoByAddress(address).isEmpty()) {
            throw new NotFoundException("Le " + address + " ne correspond à aucune adresse enregistrée.");
        }
        Map<String, Object> childrenByAddress = emergencyInfoService.findEmergencyinfoOfCHildrenByAddress(address);

        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName", "lastName", "age");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(childrenByAddress);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
    }

    /*
    Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de
    pompiers. Nous l'utiliserons pour envoyer des messages texte d'urgence à des foyers spécifiques.
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity findPhoneNumbersServedByFirestation(@RequestParam("firestation") final Integer firestationNumber) {
        return null;
    }

    @GetMapping("/fire")
    public ResponseEntity findEmergencyInformationByAddress(@RequestParam("address") final String address) {
        return null;
    }

    @GetMapping("/flood/stations")
    public ResponseEntity findEmergencyInformationByFireStationCoverage(@RequestParam("stations") final List<Integer> firestations) {
        return null;
    }

    @GetMapping("/personInfo")
    public ResponseEntity findEmergencyInformationByPerson(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName) {
        return null;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity findEmailByCity(@RequestParam("city") final String city) {
        return null;
    }
}
