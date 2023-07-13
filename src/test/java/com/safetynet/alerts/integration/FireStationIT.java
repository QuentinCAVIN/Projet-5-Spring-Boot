package com.safetynet.alerts.integration;

import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.FireStationService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;
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
    private String addressTest = "10 rue de la gare";

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
        assertThat(fireStationRepository.findByAddress(addressTest).get().getAddress()).isEqualTo(addressTest);
        assertThat(fireStationRepository.findByAddress(addressTest).get().getStation()).isEqualTo(9);
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

        mockMvc.perform(put("/firestation").param("address", addressTest)
                        // param pour répondre à @RequestParam("address")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"Wrong station argument\" }"))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.errorMessage")
                        .value(Matchers.containsString("\"Wrong station argument\": not a valid `java.lang.Integer` value")));
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