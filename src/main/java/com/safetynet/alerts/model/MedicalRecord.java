package com.safetynet.alerts.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data //Lombock pour éviter getters et setters
@Entity
@Table(name = "medicalrecords")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String birthdate;

    private List<String> medications;

    private List<String> allergies;
}