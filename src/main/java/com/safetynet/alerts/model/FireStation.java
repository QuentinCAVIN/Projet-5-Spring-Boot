package com.safetynet.alerts.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "firestations")
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String address;

    private int station;
}