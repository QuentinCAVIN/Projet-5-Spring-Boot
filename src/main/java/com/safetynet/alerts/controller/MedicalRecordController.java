package com.safetynet.alerts.controller;

import com.safetynet.alerts.exceptions.AlreadyPresentException;
import com.safetynet.alerts.exceptions.NotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RestController
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    //● ajouter un dossier médical ;
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> createMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        logger.info("une requête Http POST à été reçue à l'url /medicalRecord avec le body " + medicalRecord);

        Optional<MedicalRecord> medicalRecordAlreadyPresent = medicalRecordService.getMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());

        if (Optional.of(medicalRecordAlreadyPresent).orElse(null).isPresent()) {
            throw new AlreadyPresentException("Il y a déjà un dossier médical associé à ce nom: \""+ medicalRecordAlreadyPresent.orElse(null) +"\"");
        }

        MedicalRecord medicalRecordSaved = medicalRecordService.saveMedicalRecord(medicalRecord);
        logger.info("L'objet {} à été créé", medicalRecordSaved);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
    //prénom et le nom de famille ne changent pas) ;
    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName, @RequestBody MedicalRecord medicalRecord) {
        logger.info("une requête Http PUT à été reçue à l'url /medicalRecord avec les paramètres {} {} et le body {}.", firstName , lastName, medicalRecord);

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

            MedicalRecord medicalRecordSaved = medicalRecordService.saveMedicalRecord(currentMedicalRecord);
            logger.info("L'objet à été modifié: " +  medicalRecordSaved);
            return ResponseEntity.status(HttpStatus.OK).body(medicalRecordSaved);
        } else {
            throw new NotFoundException("Il n'y a pas de dossier médical associé à " + firstName + " " + lastName + ".");
        }
    }

    //● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
    //identificateur unique).
    @Transactional
    @DeleteMapping("/medicalRecord")
    public ResponseEntity deleteMedicalRecord(@RequestParam("firstName") final String firstName, @RequestParam("lastName") final String lastName) {
        logger.info("une requête Http DELETE à été reçue à l'url /medicalRecord avec les paramètres {} {}.", firstName,lastName);
        Optional<MedicalRecord> medicalRecordAlreadyPresent = medicalRecordService.getMedicalRecord(firstName, lastName);
        if (medicalRecordAlreadyPresent.isPresent()) {
            medicalRecordService.deleteMedicalRecord(firstName,lastName);
            logger.info("L'objet à été supprimé.");
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException ("Il n'y a pas de dossier médical associé à " + firstName + " " + lastName + ".");
        }
    }
}