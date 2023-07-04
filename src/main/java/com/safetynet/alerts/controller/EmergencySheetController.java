package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.alerts.model.EmergencySheet;
import com.safetynet.alerts.service.EmergencySheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class EmergencySheetController {

    @Autowired
    private EmergencySheetService emergencySheetService;

    @GetMapping("/firestation")
    public MappingJacksonValue findPersonsCoveredByFireStation(@RequestParam("stationNumber") final Integer stationNumber) {
      List<EmergencySheet> personCoveredByFireStation = emergencySheetService.findPersonsCoveredByFireStation(stationNumber);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .serializeAllExcept("city","zip","medications","email","allergies","station");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("filter", filter);
        MappingJacksonValue emergencyListFiltres = new MappingJacksonValue(personCoveredByFireStation);
        emergencyListFiltres.setFilters(listDeNosFiltres);
        return emergencyListFiltres;
        //TODO: à étudier:
        // https://openclassrooms.com/fr/courses/4668056-construisez-des-microservices/7652183-renvoyez-les-bons-codes-et-filtrez-les-reponses
    }

    @GetMapping("/childAlert")
    public ResponseEntity findChildrenByAddress(@RequestParam("address") final String address) {
        return null;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity findPhoneNumbersServedByFirestation(@RequestParam("firestation") final Integer firestationNumber) {
        return null;
    }

    @GetMapping("/fire")
    public ResponseEntity findEmergencyInformationByAddress(@RequestParam("address") final String address) {
        return null;
    }

    @GetMapping("/flood/stations")
    public ResponseEntity findEmergencyInformationByFireStationCoverage (@RequestParam("stations") final List<Integer> firestations) {
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
