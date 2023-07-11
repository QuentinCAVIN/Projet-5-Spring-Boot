package com.safetynet.alerts.exceptions;


import jakarta.servlet.http.HttpServletRequest;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LogManager.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        String errorMessage = exception.getMessage() ;
        logger.error(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setErrorMessage(exception.getMessage());
        errorResponse.setPath(request.getRequestURI());
        // J'ai choisis Les attribut de ErrorResponse pour coller à ceux qui était généré automatiquement par Spring. A confirmer

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String errorMessage = "Une erreur est survenue: " + exception ;
        logger.error(errorMessage);

        Map<String, String> map = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        //TODO Détailler l'expression lambda ci-dessus ( met tout les erreurs dans une map "address = erreur associée")

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setErrorMessage(map.toString());
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AlreadyPresentException.class)
    public ResponseEntity<ErrorResponse> handleFireStationAlreadyPresentException(AlreadyPresentException exception, HttpServletRequest request) {

        logger.error(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setErrorMessage(exception.getMessage());
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleFireStationAlreadyPresentException(MissingServletRequestParameterException exception, HttpServletRequest request) {
        String errorMessage = "Le paramètre " + exception.getParameterName() + " est requis pour que la requête aboutisse.";
        logger.error(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        /*Capturer l'exception MissingServletRequestParameterException c'est dangeureux?
        Dans le sens ou, je l'ai personalisé pour mon cas présent mais si l'exception surgit à un autre endroit inattendu, la personalistation sera alors fausse*/
        //Utiliser eventuellement le même procédé avec NotReadableExceptionMachinChose pour le problème dans FireStation
   }
    //Va gérer l'exception mais le message seras moins personalisé vu que la méthode va capturer tout les NotReadable
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        String errorMessage = "Une erreur est survenue: " + exception ;
        logger.error(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGlobalError(Exception exception, HttpServletRequest request) {

        String errorMessage = "Une erreur est survenue: " + exception ;
        logger.error(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
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
    //TODO : essayer de générer les messages des @NotBlank de FireStation sans passer
        par CustomException pour voir la différence. Pourquoi le resultat du body doit être paramétré manuellement dans le cas d'utilisation de CustomExceptionHandler et pas dans l'utilisation direct de FiresationNotFoundException?

        https://auth0.com/blog/get-started-with-custom-error-handling-in-spring-boot-java/
        https://medium.com/nerd-for-tech/how-to-handle-custom-exceptions-in-spring-boot-6673047b1dc7

    */
}