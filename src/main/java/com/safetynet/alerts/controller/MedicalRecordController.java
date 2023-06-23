package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/*
http://localhost:8080/medicalRecord
Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete HTTP :
● ajouter un dossier médical ;
● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
prénom et le nom de famille ne changent pas) ;
● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
identificateur unique).
 */
//TODO: implémenter les méthodes necessaires pour coller au endpoint ci-dessus.
@RestController
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;
    //● ajouter un dossier médical ;
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> createMedicalRecord (@Valid @RequestBody MedicalRecord medicalRecord){

        Optional<MedicalRecord> medicalRecordAlreadyPresent = medicalRecordService.getMedicalRecord(medicalRecord.getFirstName(),medicalRecord.getLastName());
        if (Optional.of(medicalRecordAlreadyPresent).orElse(null).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        medicalRecordService.saveMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
    //prénom et le nom de famille ne changent pas) ;
    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> updateMedicalRecord (){
        return null;
    }


    //● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
    //identificateur unique).
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> deleteMedicalRecord (){
        return null;
    }

}
