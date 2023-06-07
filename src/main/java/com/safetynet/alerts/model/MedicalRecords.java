package com.safetynet.alerts.model;

import jakarta.persistence.*;
import lombok.Data;

@Data //Lombock pour éviter getters et setters
@Entity
@Table(name = "firestations")
public class MedicalRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String birthdate;

    private String medications;

    private String allergies;
}
