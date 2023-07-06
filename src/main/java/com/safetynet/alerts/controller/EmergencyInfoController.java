package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
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

    @GetMapping("/firestation")
    public ResponseEntity findPersonsCoveredByFireStation(@RequestParam("stationNumber") final Integer stationNumber) {
        Map<String,Object> personCoveredByFireStation = emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(stationNumber);
        SimpleBeanPropertyFilter filterProprety = SimpleBeanPropertyFilter
                .filterOutAllExcept("firstName","lastName","address","phone");//TODO retirer age quand finis
        FilterProvider filter = new SimpleFilterProvider().addFilter("filter", filterProprety);
        MappingJacksonValue emergencySheetFilter = new MappingJacksonValue(personCoveredByFireStation);
        emergencySheetFilter.setFilters(filter);
        return ResponseEntity.status(HttpStatus.OK).body(emergencySheetFilter);

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
