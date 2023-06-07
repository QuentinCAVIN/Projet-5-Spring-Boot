package com.safetynet.alerts.model;

import jakarta.persistence.*;
import lombok.Data;

@Data //Lombock pour éviter getters et setters
@Entity
@Table(name = "firestations")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private int zip;

    private String phone;

    private String email;
}
