package com.safetynet.alerts.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
// ajouter configuration pour afficher l'erreur dans le terminal
public class FireStationNotFoundException extends RuntimeException {
    public FireStationNotFoundException(String s) {
        super(s);
    }
}



