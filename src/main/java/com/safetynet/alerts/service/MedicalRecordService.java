package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Data
@Service
public class MedicalRecordService {

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
// Les methodes ci-dessous ont été recopiées ici a titre d'exemple.

    @Autowired
    private FireStationRepository fireStationRepository;

    public Optional<FireStation> getFireStation(final Long id) {
        return fireStationRepository.findById(id);
    }

    public Iterable<FireStation> getFireStation() {
        return fireStationRepository.findAll();
    }

    public void deleteFireStation(final Long id) {
        fireStationRepository.deleteById(id);
    }

    public FireStation saveFireStation(FireStation fireStation) {
        FireStation savedFireStation = fireStationRepository.save(fireStation);
        return savedFireStation;
    }
}

