package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.exceptions.NotFoundException;
import com.safetynet.alerts.model.EmergencyInfo;
import com.safetynet.alerts.service.EmergencyInfoService;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmergencyInfoController {
    @Autowired
    private EmergencyInfoService emergencyInfoService;
    @Autowired
    FireStationService fireStationService;

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

        Map <String,Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("Personnes couvertes par le centre de secours n°" + stationNumber + ":", emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(stationNumber));
        endpointExpected.put("Adultes présents: ",emergencyInfoService.numberOfAdultCoveredByFirestation(stationNumber));
        endpointExpected.put("Enfants présents: ",emergencyInfoService.numberOfChildrenCoveredByFirestation(stationNumber));

        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName", "lastName", "address", "phone");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(endpointExpected);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
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

        Map <String,Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("Enfants présents au " + address + ":", emergencyInfoService.findEmergencyinfoOfCHildrenByAddress(address));
        endpointExpected.put("Adultes présents à cette adresse:",emergencyInfoService.findAdultByAddress(address));

        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName", "lastName", "age");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(endpointExpected);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
    }

    /*
    Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de
    pompiers. Nous l'utiliserons pour envoyer des messages texte d'urgence à des foyers spécifiques.
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity findPhoneNumbersServedByFirestation(@RequestParam("firestation") final Integer firestationNumber) {
        if (emergencyInfoService.getEmergencyInfoByStation(firestationNumber).isEmpty()) {
            throw new NotFoundException("Il n'existe aucun centre de secours n° " +firestationNumber + ".");
        }
        List<EmergencyInfo> phoneNumberCoveredByFirestation = emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(firestationNumber);

        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept( "phone");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(phoneNumberCoveredByFirestation);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
    }

    /*
    Cette url doit retourner la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne
    de pompiers la desservant. La liste doit inclure le nom, le numéro de téléphone, l'âge et les antécédents
    médicaux (médicaments, posologie et allergies) de chaque personne.
     */
    @GetMapping("/fire")
    public ResponseEntity findEmergencyInformationByAddress(@RequestParam("address") final String address) {
        if (emergencyInfoService.getEmergencyInfoByAddress(address).isEmpty()) {
            throw new NotFoundException("Le " + address + " ne correspond à aucune adresse enregistrée.");
        }

        Map <String,Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("N° du centre de secours couvrant le " + address, fireStationService.getFireStation(address).get().getStation());
        endpointExpected.put("Personnes présente au " + address + ":",emergencyInfoService.findEmergencyInfoOfPeopleByAddress(address));

        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName", "lastName", "phone", "age", "medications", "allergies");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(endpointExpected);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
    }

    /*
    Cette url doit retourner une liste de tous les foyers desservis par la caserne. Cette liste doit regrouper les
    personnes par adresse. Elle doit aussi inclure le nom, le numéro de téléphone et l'âge des habitants, et
    faire figurer leurs antécédents médicaux (médicaments, posologie et allergies) à côté de chaque nom.
     */
    @GetMapping("/flood/stations")
    public ResponseEntity findEmergencyInformationByFireStationsCoverage(@RequestParam("stations") final List<Integer> firestations) {
        for (Integer firestation : firestations){
            if (emergencyInfoService.getEmergencyInfoByStation(firestation).isEmpty()) {
                throw new NotFoundException("Il n'existe aucun centre de secours n° " + firestation + ".");
            }
        }

        Map <String,Object> endpointExpected = new LinkedHashMap<>();
        for (String address : fireStationService.getAddressesCoveredByStations(firestations)){

            endpointExpected.put(address,emergencyInfoService.findEmergencyInfoOfPeopleByAddress(address));
        }

        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName", "lastName", "phone", "age", "medications", "allergies");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(endpointExpected);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
    }

    /*
    Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail et les antécédents médicaux (médicaments,
    posologie, allergies) de chaque habitant. Si plusieurs personnes portent le même nom, elles doivent
    toutes apparaître.
     */
    @GetMapping("/personInfo")
    public ResponseEntity findEmergencyInformationByPerson(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName) {
        List <EmergencyInfo> endpointExpected = emergencyInfoService.findEmergencyInfoByFirstNameAndLastName(firstName,lastName);

        if (endpointExpected.isEmpty()){
            throw new NotFoundException("Il n'y a pas de données associé à " + firstName + " " + lastName + ".");
        }

        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName", "lastName", "age", "email", "medications", "allergies");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(endpointExpected);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
    }

    /*
    Cette url doit retourner les adresses mail de tous les habitants de la ville.
    */
    @GetMapping("/communityEmail")
    public ResponseEntity findEmailByCity(@RequestParam("city") final String city) {

        List<EmergencyInfo> endpointExpected = emergencyInfoService.findEmergencyInfoByCity(city);

        if (endpointExpected.isEmpty()){
            throw new NotFoundException("Il n'y a pas de donnée associée à " + city + ".");
        }
        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept( "email");
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencyInfoFilter = new MappingJacksonValue(endpointExpected);
        emergencyInfoFilter.setFilters(filter);

        return ResponseEntity.status(HttpStatus.OK).body(emergencyInfoFilter);
    }
}