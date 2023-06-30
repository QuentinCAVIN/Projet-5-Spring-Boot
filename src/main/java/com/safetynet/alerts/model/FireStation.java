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

    @NotNull(message="Champ obligatoire")
    private String address;

    @NotNull(message="Champ obligatoire")
    //@Positive supprimé vu que l'erreur NotReadable est throw avant d'accéder à l'annotation Jakarta
    private Integer station;

    //J'ai du modifier le type de "station" (autrefois Integer maintenant String) pour pouvoir utiliser l'annotation @Positive.
    //Une entrée d'une String dans mon body sur le champs "station" (normalement prévu pour recevoir un entier) va générer une erreur MethodArgumentNotValidException qui retourne correctement le message.

    //Quand j'utilise un Integer, une entrée d'une String au même endroit crée un problème de désérialisation du Json et renvoie une erreur HttpMessageNotReadableException qui ne renverra pas le message voulu.
    //Je pense que le probléme viens du fait que le body {"address":"10 rue", "station":cinq} n'est pas reconnu comme un objet Java quand la variable station est de type Integer, mon programme n'accéde jamais aux annotations Jakarta
}