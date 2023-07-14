package com.safetynet.alerts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.Data;

@Data
public class FireStation {

    @NotNull(message = "Champ obligatoire")
    private String address;

    @NotNull(message = "Champ obligatoire")
    private Integer station;
}