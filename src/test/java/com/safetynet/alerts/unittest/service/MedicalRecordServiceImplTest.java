package com.safetynet.alerts.unittest.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class MedicalRecordServiceImplTest {

    @Autowired
    private MedicalRecordServiceImpl medicalRecordService;

    @MockBean
    private static MedicalRecordRepository medicalRecordRepository;

    private MedicalRecord medicalRecordA;

    @BeforeEach
    public void setup() {
        medicalRecordA = new MedicalRecord();
        medicalRecordA.setFirstName("a");
        medicalRecordA.setLastName("a");
        medicalRecordA.setBirthdate("a");
        medicalRecordA.setMedications(Arrays.asList("medicationA", "medicationB"));
        medicalRecordA.setAllergies(Arrays.asList("allergieA", "allergieB"));
    }

    @Test
    public void getMedicalRecordTest() {
        when(medicalRecordRepository.findByFirstNameAndLastName("a", "a")).thenReturn(Optional.of(medicalRecordA));
        medicalRecordService.getMedicalRecord("a", "a");

        verify(medicalRecordRepository, Mockito.times(1)).findByFirstNameAndLastName("a", "a");
        assertThat(medicalRecordRepository.findByFirstNameAndLastName("a", "a")).isEqualTo(Optional.of(medicalRecordA));
    }

    @Test
    public void deleteMedicalRecordTest() {
        medicalRecordService.deleteMedicalRecord("a", "a");

        verify(medicalRecordRepository, Mockito.times(1)).deleteByFirstNameAndLastName("a", "a");
    }

    @Test
    public void saveMedicalRecordTest() {
        when(medicalRecordRepository.save(medicalRecordA)).thenReturn(medicalRecordA);

        medicalRecordService.saveMedicalRecord(medicalRecordA);

        verify(medicalRecordRepository, Mockito.times(1)).save(medicalRecordA);
        assertThat(medicalRecordRepository.save(medicalRecordA)).isEqualTo(medicalRecordA);
    }

    @Test
    public void getMedicalRecordTestGoesWrong() {
        when(medicalRecordRepository.findByFirstNameAndLastName("a", "a")).thenReturn(null);

        Optional<MedicalRecord> medicalRecord = medicalRecordService.getMedicalRecord("a", "a");

        assertThat(medicalRecord).isEqualTo(null);
    }
}