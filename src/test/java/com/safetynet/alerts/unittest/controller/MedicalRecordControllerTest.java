package com.safetynet.alerts.unittest.controller;

import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;;
import com.safetynet.alerts.service.LoadDataService;
import com.safetynet.alerts.service.MedicalRecordService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    MedicalRecordService medicalRecordService;
    @MockBean
    LoadDataService loadDataService;
    private MedicalRecord medicalRecord;
    private String firstName;
    private String lastName;
    private List<String> medications;

    @BeforeEach
    public void setUpPerTest() {
        firstName = "Garrus";
        lastName = "Vakarian";

        medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(firstName);
        medicalRecord.setLastName(lastName);
        medicalRecord.setBirthdate("13/11/2251");
        medicalRecord.setMedications(Arrays.asList("Kill people"));
        medicalRecord.setAllergies(Arrays.asList("Geth", "Peace"));
    }

    @Test
    public void createMedicalRecord_returnCode201_whenMedicalRecordIsCreated() throws Exception {
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void createMedicalRecord_returnCode400_WhenLastNameIsMissing() throws Exception {
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("{lastName=Obligatoire pour créer un nouveau dossier médical.}"));
    }

    @Test
    public void createMedicalRecord_returnCode409_WhenAMedicalRecordAlreadyExist() throws Exception {
        when(medicalRecordService.getMedicalRecord(firstName, lastName)).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.errorMessage")
                        .value("Il y a déjà un dossier médical associé à ce nom: \"" + medicalRecord + "\""));
    }

    @Test
    public void updateMedicalRecord_returnCode200_whenAMedicalRecordIsModified() throws Exception {
        medications = Arrays.asList("\"Normandy\"", "\"Shepard\"");

        when(medicalRecordService.getMedicalRecord(firstName, lastName)).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordService.saveMedicalRecord((medicalRecord))).thenReturn(medicalRecord);

        mockMvc.perform(put("/medicalRecord")
                        .param("firstName", firstName).param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":" + medications + "}"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.medications").value(Matchers.contains("Normandy", "Shepard")));

        //Difficulté à trouver comment comparer le json avec ma variable de type list.
        // Matchers de Hamcrest résout le problème en partie mais pas possible d'utiliser la liste
        //https://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html
    }

    @Test
    public void updateMedicalRecord_returnCode400_whenFirstNameIsMissing() throws Exception {
        medications = Arrays.asList("\"Normandy\"", "\"Shepard\"");

        mockMvc.perform(put("/medicalRecord").param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":" + medications + "}"))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Le paramètre firstName est requis pour que la requête aboutisse."));
    }

    @Test
    public void updateMedicalRecord_returnCode404_whenAWrongLastNameIsEnter() throws Exception {
        medications = Arrays.asList("\"Normandy\"", "\"Shepard\"");

        when(medicalRecordService.getMedicalRecord(firstName, lastName)).thenReturn(Optional.of(medicalRecord));
        mockMvc.perform(put("/medicalRecord")
                        .param("firstName", firstName).param("lastName", "Wrong lastName")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":" + medications + "}"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de dossier médical associé à " + firstName + " Wrong lastName."));
    }

    @Test
    public void deleteMedicalRecord_returnCode204_whenAMedicalRecordIsDelete() throws Exception {
        when(medicalRecordService.getMedicalRecord(firstName, lastName)).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(delete("/medicalRecord").param("firstName", firstName).param("lastName", lastName))
                .andExpect((status().isNoContent()));
    }

    @Test
    public void deleteMedicalRecord_returnCode404_WhenTheMedicalRecordToDeleteIsNotFound() throws Exception {
        when(medicalRecordService.getMedicalRecord(firstName, lastName)).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(delete("/medicalRecord").param("firstName", "first name not present").param("lastName", lastName))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de dossier médical associé à first name not present " + lastName + "."));
    }
}