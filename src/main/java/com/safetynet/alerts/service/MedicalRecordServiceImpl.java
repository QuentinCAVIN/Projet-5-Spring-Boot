package com.safetynet.alerts.service;

        import com.safetynet.alerts.model.FireStation;
        import com.safetynet.alerts.model.MedicalRecord;
        import com.safetynet.alerts.repository.FireStationRepository;
        import com.safetynet.alerts.repository.MedicalRecordRepository;
        import lombok.Data;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.Optional;


@Data
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {
        /*
        http://localhost:8080/medicalRecord
        Cet endpoint permettra d’effectuer les actions suivantes via Post/Put/Delete HTTP :
        ● ajouter un dossier médical ;
        ● mettre à jour un dossier médical existant (comme évoqué précédemment, supposer que le
        prénom et le nom de famille ne changent pas) ;
        ● supprimer un dossier médical (utilisez une combinaison de prénom et de nom comme
        identificateur unique).
         */
// TODO: implémenter les méthodes necessaires pour coller au endpoint ci-dessus.
//  Les methodes ci-dessous ont été recopiées ici a titre d'exemple.

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public Optional<MedicalRecord> getMedicalRecord(final Long id) {
        return medicalRecordRepository.findById(id);
    }
    public Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName) {
        return medicalRecordRepository.findByFirstNameAndLastName(firstName,lastName);
    }
    public Iterable<MedicalRecord> getMedicalRecord() {
        return medicalRecordRepository.findAll();
    }

    public void deleteMedicalRecord(final String firstName, final String LastName) {
        medicalRecordRepository.deleteByFirstNameAndLastName(firstName,LastName);
    }

    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
        return savedMedicalRecord;
    }
}
