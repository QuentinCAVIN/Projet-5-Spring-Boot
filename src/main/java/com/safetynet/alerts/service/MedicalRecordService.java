package com.safetynet.alerts.service;


import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface MedicalRecordService {

    public Optional<MedicalRecord> getMedicalRecord(final Long id);

    public Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName);

    public Iterable<MedicalRecord> getMedicalRecord();

    public void deleteMedicalRecord(final String firstName, final String LastName);

    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);
}