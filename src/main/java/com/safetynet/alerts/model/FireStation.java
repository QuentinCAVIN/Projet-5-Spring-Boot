package com.safetynet.alerts.model;

import jakarta.persistence.*;
import lombok.Data;;

@Data //Lombock pour éviter getters et setters
@Entity
@Table(name = "firestations")
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Column(name="le vrai nom de ma colonne dans la BDD") si besoin
    private String address;

    private int station;
}
