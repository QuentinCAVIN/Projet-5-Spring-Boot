package com.safetynet.alerts.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(FireStationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleFireStationNotFoundException(FireStationNotFoundException exception, HttpServletRequest request) {
        logger.error(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setPath(request.getRequestURI());
        // J'ai choisis Les attribut de ErrorResponse pour coller à ceux qui était généré automatiquement par Spring

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidArgument(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> { // ça regroupe toute les erreur dans une map?
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        //TODO Essayer de piger l'expression lambda ci dessus

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessages(map);
        errorResponse.setPath(request.getRequestURI());

        //TODO: implémenter de quoi stocker la map dans un String + supprimer le
        // champ message de ErrorResponse ajouté pour l'occasion

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /*
    Problème rencontré sur les throws :

    1/throw new FireStationNotFoundException ("La Caserne de pompier avec l'address " + address + " est introuvable.");

    2/ FireStationNotFoundException notFoundException = new FireStationNotFoundException("La Caserne de pompier avec l'address " + address + " est introuvable.");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException);

    L'expression 1/ ne retourne rien dans le body de la réponse http contrairement a ce qui est expliqué dans le cours.
    La 2/ renvoie toute la stack
    la 1/ et la 2/ ne renvoie rien dans le terminal


    Origine du problème:
    1/ L'annotation @ResponseStatus(HttpStatus.NOT_FOUND) intercepte l'exception ce qui explique
    pourquoi elle ne s'affiche pas dans le terminal
    2/ Il ya un parametrage par defaut de Spring qui fait que les messages d'erreur ne s'affichent pas dans
    le body pour des raisons de sécurité. (ça n'a pas toujours été le cas)

    Option simple:
    1/ Ajouter un logger.error sur FireStationNotFoundException pour contourner le probleme d'interception
    2/ Une ligne au document application.properties : loserver.error.include-message=ALWAYS modifie le parametrage

    MAIS!:
    Ce serait mieux d'utiliser une classe (CustomExceptionHandler) pour centraliser la gestion des exceptions.
    //TODO : essayer de générer les messages des @notBlank de FireStation sans passer
        par CustomException pour voir la différence. Pourquoi le resultat du body doit être paramétré manuellement dans un cas et pas dans l'autre?

        https://auth0.com/blog/get-started-with-custom-error-handling-in-spring-boot-java/
        https://medium.com/nerd-for-tech/how-to-handle-custom-exceptions-in-spring-boot-6673047b1dc7

    */
}