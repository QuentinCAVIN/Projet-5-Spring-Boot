package com.safetynet.alerts;

import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.service.LoadDataService;
import com.safetynet.alerts.service.MedicalRecordService;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.contains;
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
    LoadDataService loadDataService; // même problème que précédemment
    private MedicalRecord medicalRecord;
    private String firstName = "Garrus";
    private String lastName = "Vakarian";
    private String birthdate = ("13/11/2251");
    private List<String> medications = Arrays.asList("Kill people");
    private List<String> allergies= Arrays.asList("Geth","Peace");

    @BeforeEach
    public void setUpPerTest(){
        medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(firstName);
        medicalRecord.setLastName(lastName);
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);
    }
    @Test
    public void createMedicalRecord_returnCode201_whenMedicalRecordIsCreated() throws Exception{
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void createMedicalRecord_returnCode400_WhenLastNameIsMissing() throws Exception{
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"" + firstName + "\"}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void createMedicalRecord_returnCode409_WhenAMedicalRecordAlreadyExist() throws Exception{

        when(medicalRecordService.getMedicalRecord(firstName,lastName)).thenReturn(Optional.of(medicalRecord));
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void updateMedicalRecord_returnCode200_whenAMedicalRecordIsModified() throws Exception {
        medications = Arrays.asList("\"Normandy\"","\"Shepard\"");
        when(medicalRecordService.getMedicalRecord(firstName,lastName)).thenReturn(Optional.of(medicalRecord));
        mockMvc.perform(put("/medicalRecord")
                        .param("firstName",firstName).param("lastName",lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":"+ medications +"}"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.medications").value(Matchers.contains("Normandy","Shepard")));

        //Difficulté a trouver comment comparer le json avec ma variable de type list.
        // Matchers de Hamcrest résout le probléme en partie mais pas possible d'utiliser la liste
        //https://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html
    }
    @Test
    public void updateMedicalRecord_returnCode400_whenFirstNameIsMissing() throws Exception {
        medications = Arrays.asList("\"Normandy\"","\"Shepard\"");
        mockMvc.perform(put("/medicalRecord").param("firstName",firstName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":"+ medications +"}"))
                .andExpect((status().isBadRequest()));
        //TODO Vérifier message d'exception
                /*.andExpect(jsonPath("$.errorMessage")
                        .value("{address=Champ obligatoire, station=Le numéro du centre de secours doit être un entier positif}"));*/
    }

    @Test
    public void updateMedicalRecord_returnCode404_whenAWrongLastNameIsEnter() throws Exception {
        medications = Arrays.asList("\"Normandy\"","\"Shepard\"");
        when(medicalRecordService.getMedicalRecord(firstName,lastName)).thenReturn(Optional.of(medicalRecord));
        mockMvc.perform(put("/medicalRecord")
                        .param("firstName",firstName).param("lastName","Wrong lastName")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"medications\":"+ medications +"}"))
                .andExpect((status().isNotFound()));
        //TODO Vérifier message d'exception

    }
    @Test
    public void deleteMedicalRecord_returnCode204_whenAMedicalRecordIsDelete() throws Exception {
        when(medicalRecordService.getMedicalRecord(firstName,lastName)).thenReturn(Optional.of(medicalRecord));
        mockMvc.perform(delete("/medicalRecord").param("firstName",firstName).param("lastName",lastName))
                .andExpect((status().isNoContent()));
    }
    @Test
    public void deleteFireStation_returnCode404_WhenTheFirestationtoDeleteIsNotFound() throws Exception {
        when(medicalRecordService.getMedicalRecord(firstName,lastName)).thenReturn(Optional.of(medicalRecord));
        mockMvc.perform(delete("/medicalRecord").param("firstName","first name not present ").param("lastName",lastName))
                .andExpect((status().isNotFound()));
        //TODO Vérifier message d'exception

        /*  .andExpect(jsonPath("$.errorMessage").value("L'adresse \"Address not present\" ne correspond à aucun centre de secours."));*/
    }




}