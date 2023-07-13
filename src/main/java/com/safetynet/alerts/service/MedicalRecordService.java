package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import java.util.Optional;

public interface MedicalRecordService {


    public Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName);

    public void deleteMedicalRecord(final String firstName, final String LastName);

    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);
}