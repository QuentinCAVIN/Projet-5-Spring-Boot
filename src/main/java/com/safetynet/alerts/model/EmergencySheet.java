package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import java.util.List;

@Data //Lombock pour éviter getters et setters
@JsonFilter("filter")
public class EmergencySheet {

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private int zip;

    private String phone;

    private String email;

    private String birthdate;

    private List<String> medications;

    private List<String> allergies;

    private Integer station;

}
