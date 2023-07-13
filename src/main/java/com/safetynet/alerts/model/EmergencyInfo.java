package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data //Lombock pour éviter getters et setters
@JsonFilter("filter")
public class EmergencyInfo {

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private int zip;

    private String phone;

    private String email;

    private String birthdate;

    private int age;

    private List<String> medications;

    private List<String> allergies;

    private Integer station;

    public void setAge() {

        if (birthdate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(birthdate, formatter);
            LocalDate currentDate = LocalDate.now();
            this.age = ((Period.between(birthDate, currentDate)).getYears());
        }
    }
}
