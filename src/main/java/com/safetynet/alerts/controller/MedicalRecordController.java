package com.safetynet.alerts.controller;

/*
http://localhost:8080/medicalRecord
Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete HTTP :
● ajouter un dossier médical ;
● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
prénom et le nom de famille ne changent pas) ;
● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
identificateur unique).
 */

import com.safetynet.alerts.model.MedicalRecord;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

//TODO: implémenter les méthodes necessaires pour coller au endpoint ci-dessus.
public class MedicalRecordController {
    @Autowired
    MedicalRecord medicalRecord;
    //● ajouter un dossier médical ;
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> createMedicalRecord (){
        return null;
    }


    //● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
    //prénom et le nom de famille ne changent pas) ;
    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> changeMedicalRecord (){
        return null;
    }


    //● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
    //identificateur unique).
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> deleteMedicalRecord (){
        return null;
    }

}
