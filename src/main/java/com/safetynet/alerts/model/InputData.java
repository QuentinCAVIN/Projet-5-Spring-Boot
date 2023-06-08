package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;
@Data
public class InputData {

    private List<FireStation> firestations;


    private List<Person> persons;


    private List<MedicalRecord> medicalrecords;
}
