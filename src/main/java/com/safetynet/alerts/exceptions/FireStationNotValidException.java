package com.safetynet.alerts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ajouter configuration pour afficher l'erreur dans le terminal
public class FireStationNotValidException extends RuntimeException {
    public FireStationNotValidException(String s) {
        super(s);
    }
}
