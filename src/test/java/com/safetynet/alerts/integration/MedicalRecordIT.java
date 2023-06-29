package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    private MedicalRecord medicalRecord;
    private String firstName = "Garrus";
    private String lastName = "Vakarian";
    private String birthdate = ("13/11/2251");
    private List<String> medications = Arrays.asList("Kill people");
    private List<String> allergies= Arrays.asList("Geth","Peace");

    // TODO : Ajuster les attributs et le BeforeEach de la classe, beaucoups de champs inutile
    @BeforeEach
    private void setUpPerTest() throws Exception {
        medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(firstName);
        medicalRecord.setLastName(lastName);
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);
        medicalRecordRepository.deleteAll();
    }

    @Test
    public void createMedicalRecordWithSuccess() throws Exception {
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isCreated());
    }

   @Test
    public void updateMedicalRecordCreatedWithSuccess() throws Exception {
        createMedicalRecordWithSuccess();
       medications = Arrays.asList("\"Normandy\"","\"Shepard\"");
       mockMvc.perform(put("/medicalRecord")
                       .param("firstName",firstName).param("lastName",lastName)
                       .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":"+ medications +"}"))
               .andExpect((status().isOk()))
               .andExpect(jsonPath("$.medications").value(Matchers.contains("Normandy","Shepard")));
    }

   @Test
    public void deleteFireStationUpdatedWithSuccess() throws Exception {
        updateMedicalRecordCreatedWithSuccess();
       mockMvc.perform(delete("/medicalRecord").param("firstName",firstName).param("lastName",lastName))
               .andExpect((status().isNoContent()));
    }

    @Test
    public void createMedicalRecordWithoutLastName() throws Exception {
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("{lastName=Obligatoire pour créer un nouveau dossier médical.}"));
    }

    @Test
    public void updateMedicalRecordWithAWrongFirstName() throws Exception {
        createMedicalRecordWithSuccess();
        medications = Arrays.asList("\"Normandy\"","\"Shepard\"");
        mockMvc.perform(put("/medicalRecord")
                        .param("firstName","Wrong firstName").param("lastName",lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":"+ medications +"}"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de dossier médical associé à Wrong firstName " + lastName +"."));
    }

   @Test
    public void deleteMedicalRecordUpdatedTwice() throws Exception {
       updateMedicalRecordCreatedWithSuccess();
       mockMvc.perform(delete("/medicalRecord").param("firstName","first name not present").param("lastName",lastName))
               .andExpect((status().isNotFound()))
               .andExpect(jsonPath("$.errorMessage")
                       .value("Il n'y a pas de dossier médical associé à first name not present "+ lastName+"."));
    }
}
