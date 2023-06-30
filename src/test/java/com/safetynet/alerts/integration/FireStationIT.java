package com.safetynet.alerts.integration;

import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FireStationService fireStationService;
    @Autowired
    private FireStationRepository fireStationRepository;
    String addressTest = "10 rue de la gare";

    @BeforeEach
    private void setUpPerTest() throws Exception {
        fireStationRepository.deleteAll();
    }

    @Test
    public void createFireStationWithSucces() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"" + addressTest + "\",\"station\":\"9\"}"))
                .andExpect((status().isCreated()));
    }
    @Test
    public void updateFireStationCreatedWithSucces() throws Exception {
        createFireStationWithSucces();
        mockMvc.perform(put("/firestation").param("address", addressTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\":\"" + addressTest + "\",\"station\":\"6\"}"))
                // TODO: virer le champ address quand @Valid retiré
                .andExpect((status().isOk())).andExpect(jsonPath("$.station").value("6"));
    }
    @Test
    public void deleteFireStationUpdatedWithSucces() throws Exception {
        updateFireStationCreatedWithSucces();
        mockMvc.perform(delete("/firestation").param("address", addressTest))
                .andExpect((status().isNoContent()));
    }

    @Test
    public void createFireStationWithoutStation() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"" + addressTest + "\"}"))
                .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.errorMessage").value("{station=Champ obligatoire}"));
    }

    @Test
    public void updateFireStationWIthAWrongStationArgument() throws Exception {
        createFireStationWithSucces();
        mockMvc.perform(put("/firestation").param("address",addressTest)
                        // param pour répondre à @RequestParam("address")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"Wrong station argument\" }"))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("{address=Champ obligatoire, station=Le numéro du centre de secours doit être un entier positif}"));
        //TODO: le test ne passera plus si je bascule "station" sur un String. Adapter à ce moment la le test
    }

    @Test
    public void deleteFireStationUpdatedTwice() throws Exception {
        deleteFireStationUpdatedWithSucces();
        mockMvc.perform(delete("/firestation").param("address", addressTest)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"6\" }"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("L'adresse \"" + addressTest + "\" ne correspond à aucun centre de secours."));
    }
}