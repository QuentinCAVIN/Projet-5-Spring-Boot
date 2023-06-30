package com.safetynet.alerts.exceptions;

// ajouter configuration pour afficher l'erreur dans le terminal
public class NotValidException extends RuntimeException {
    public NotValidException(String s) {
        super(s);
    }
}
