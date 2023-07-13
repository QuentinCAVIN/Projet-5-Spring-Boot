package com.safetynet.alerts.unittest.controller;

import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.LoadDataService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FireStationController.class})
//A confirmer: on indique a Spring que l'on va tester uniquement cette classe
// Spring ne va pas charger toute l'application. Donc = Test unitaire

public class FireStationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean //remplis le même role que @Mock
    //Spring configure par default le comportement du Mock, pas besoin de le renseigner ici.
    private FireStationService fireStationService;
    @MockBean
    private LoadDataService loadDataService;
    String addressTest = "10 rue de la gare";
    FireStation fireStationExpected;

    @BeforeEach
    public void setup() {
        fireStationExpected = new FireStation();
        fireStationExpected.setStation(9);
        fireStationExpected.setAddress(addressTest);
    }

    @Test
    public void createFireStation_returnCode201_whenFireStationIsCreated() throws Exception {
        when(fireStationService.saveFireStation(any())).thenReturn(fireStationExpected);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\":\" " + addressTest + "\",\"station\":\"9\" }"))
                .andExpect((status().isCreated()));
    }

    @Test
    public void createFireStation_returnCode400_whenAFirestationAttributIsNull() throws Exception {
        when(fireStationService.saveFireStation(any())).thenReturn(fireStationExpected);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"station\":\"9\" }"))
                .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.errorMessage").value("{address=Champ obligatoire}"));
    }

    @Test
    public void createFirestation_returnCode409_whenTheFireStationAddedIsAlreadyCreated() throws Exception {
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStationExpected));

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\":\"10 rue de la gare\", \"station\":\"9\" }"))
                .andExpect((status().isConflict()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Ce centre de secours à déja été créé: \"" + fireStationExpected + "\""));
    }

    @Test
    public void updateFireStation_returnCode200_whenAFireStationIsModified() throws Exception {
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStationExpected));
        when(fireStationService.saveFireStation(fireStationExpected)).thenReturn(fireStationExpected);

        mockMvc.perform(put("/firestation").param("address", addressTest)
                        // param pour répondre à @RequestParam("address")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"6\" }"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$").value(fireStationExpected));
    }

    @Test
    public void updateFireStation_returnCode400_whenAWrongStationArgumentIsEnter() throws Exception {
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStationExpected));

        mockMvc.perform(put("/firestation").param("address", addressTest)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"Wrong station argument\" }"))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.errorMessage")
                        .value(Matchers.containsString("\"Wrong station argument\": not a valid `java.lang.Integer` value")));
    }


    @Test
    public void updateFireStation_returnCode404_whenAWrongAddressIsEnter() throws Exception {
        mockMvc.perform(put("/firestation").param("address", "Wrong address")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"6\" }"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("L'adresse \"Wrong address\" ne correspond à aucun centre de secours."));
    }

    @Test
    public void deleteFireStation_returnCode204_whenAFireStationIsDelete() throws Exception {
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStationExpected));

        mockMvc.perform(delete("/firestation").param("address", addressTest))
                .andExpect((status().isNoContent()));
    }

    @Test
    public void deleteFireStation_returnCode404_WhenTheFirestationtoDeleteIsNotFound() throws Exception {
        mockMvc.perform(delete("/firestation")
                        .param("address", "Address not present")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"6\" }"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("L'adresse \"Address not present\" ne correspond à aucun centre de secours."));
    }
    //exemple test mockmvc: https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
    // video : https://www.youtube.com/watch?v=Aasp0mWT3Ac
}
