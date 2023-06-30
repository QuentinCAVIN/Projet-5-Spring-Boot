package com.safetynet.alerts.exceptions;


// ajouter configuration pour afficher l'erreur dans le terminal

public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }
}



