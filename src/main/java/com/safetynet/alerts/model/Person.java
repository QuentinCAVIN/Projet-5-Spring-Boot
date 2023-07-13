package com.safetynet.alerts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data //Lombock pour éviter getters et setters
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Champ obligatoire.")
    private String firstName;

    @NotBlank(message = "Champ obligatoire.")
    private String lastName;

    private String address;

    private String city;

    private int zip;

    private String phone;

    private String email;
}
