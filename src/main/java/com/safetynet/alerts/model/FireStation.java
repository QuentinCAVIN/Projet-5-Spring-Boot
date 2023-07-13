package com.safetynet.alerts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.Data;

@Data
@Entity
@Table(name = "firestations")
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Champ obligatoire")
    private String address;

    @NotNull(message = "Champ obligatoire")
    private Integer station;
}