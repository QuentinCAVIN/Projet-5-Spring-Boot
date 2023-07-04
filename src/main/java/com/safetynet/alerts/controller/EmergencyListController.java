package com.safetynet.alerts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public class EmergencyListController {
    @GetMapping("/firestation")
    public ResponseEntity findPersonCoveredByFireStation(@RequestParam("stationNumber") final Integer stationNumber) {
        return null;
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
