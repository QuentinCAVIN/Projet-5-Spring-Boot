package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<MedicalRecord> createMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {

        Optional<MedicalRecord> medicalRecordAlreadyPresent = medicalRecordService.getMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (Optional.of(medicalRecordAlreadyPresent).orElse(null).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        medicalRecordService.saveMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
    //prénom et le nom de famille ne changent pas) ;
    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName, @RequestBody MedicalRecord medicalRecord) {
        Optional<MedicalRecord> medicalRecordAlreadyPresent = medicalRecordService.getMedicalRecord(firstName, lastName);
        if (medicalRecordAlreadyPresent.isPresent()) {

            MedicalRecord currentMedicalRecord = medicalRecordAlreadyPresent.get();
            String birthdate = medicalRecord.getBirthdate();
            List<String> medications = medicalRecord.getMedications();
            List<String> allergies = medicalRecord.getAllergies();
            if (birthdate != null) {
                currentMedicalRecord.setBirthdate(birthdate);
            }
            if (medications != null) {
                currentMedicalRecord.setMedications(medications);
            }
            if (allergies != null) {
                currentMedicalRecord.setAllergies(allergies);
            }
            // TODO voir si il serais mieux d'utiliser un for each pour vérifier que les attributs sont nul
            // https://stackoverflow.com/questions/1038308/how-to-get-the-list-of-all-attributes-of-a-java-object-using-beanutils-introspec

            medicalRecordService.saveMedicalRecord(currentMedicalRecord);
            return ResponseEntity.status(HttpStatus.OK).body(currentMedicalRecord);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            // TODO : implémenter la gestion d'exception
            //throw new  FireStationNotFoundException("L'adresse \"" + address + "\" ne correspond à aucun centre de secours.");
        }
    }

    //● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
    //identificateur unique).
    @Transactional
    @DeleteMapping("/medicalRecord")
    public ResponseEntity deleteMedicalRecord(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName) {
        Optional<MedicalRecord> medicalRecordAlreadyPresent = medicalRecordService.getMedicalRecord(firstName, lastName);
        if (medicalRecordAlreadyPresent.isPresent()) {
            medicalRecordService.deleteMedicalRecord(firstName,lastName);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //TODO: Temporaire en attendant d'implémenter la gestion de l'exception
            /*throw new FireStationNotFoundException ("L'adresse \"" + address + "\" ne correspond à aucun centre de secours.");*/
        }
    }
}