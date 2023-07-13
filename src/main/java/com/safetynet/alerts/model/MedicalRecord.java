package com.safetynet.alerts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data //Lombock pour éviter getters et setters
@Entity
@Table(name = "medicalrecords")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Obligatoire pour créer un nouveau dossier médical.")
    private String firstName;
    @NotBlank(message = "Obligatoire pour créer un nouveau dossier médical.")
    private String lastName;

    private String birthdate;

    private List<String> medications;

    private List<String> allergies;
}