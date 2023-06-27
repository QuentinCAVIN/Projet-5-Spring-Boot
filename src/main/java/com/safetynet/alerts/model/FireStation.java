package com.safetynet.alerts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "firestations")
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message="Champ obligatoire")
    private String address;

    @NotNull(message="Champ obligatoire")
    @Positive( message = "La valeur doit être un entier.")
    private String station;

    //J'ai du modifier le type de "station" (autrefois Integer maintenant String) pour pouvoir utiliser l'annotation @Digits.
    //Une entrée d'une String dans mon body sur le champs "station" (normalement prévu pour recevoir un entier) va générer une erreur MethodArgumentNotValidException qui retourne correctement le message.

    //Quand j'utilise un Integer, une entrée d'une String au même endroit crée un problème de désérialisation du Json et renvoie une erreur HttpMessageNotReadableException qui ne renverra pas le message voulu.

    //TODO : Regarder du coté des validateurs personnalisés
}