package com.safetynet.alerts;

//givenStateUnderTest_whenMethodAction_thenExpectedBehavior
//ou
//add_returnsTheSum_ofTwoPositiveIntegers()

import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.LoadDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    //Je ne comprends pas pourquoi le Mock est obligatoire ici, vu que FireStationController
    //n'utilise pas LoadataService.

    //TODO : finaliser les test pour qu'ils répondent aux exigences définis.

    @Test
    public void createFireStation_returnFireStationCreated_whenEndpointIsCall() throws Exception {
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\":\"10 rue de la gare\", \"station\":\"9\" }"))
                .andExpect((status().isOk()));
    }

    @Test
    public void updateFireStation_returnFireStationUpdated_whenEndpointIsCall() throws Exception {
        when(fireStationService.getFireStation(any())).thenReturn(Optional.of(new FireStation())); // Pas besoin de definir le contenu de FireStation? JPA?
        mockMvc.perform(put("/firestation/{address}", "10 rue de la paix")
                .contentType(MediaType.APPLICATION_JSON).content("{  \"station\":\"9\" }"))
                .andExpect((status().isOk()));
    }

    @Test
    public void deleteFireStations_sendsRequestSuccessfully_whenEndpointIsCall() throws Exception {
        mockMvc.perform(delete("/firestation/1")).andExpect((status().isOk()));
    }

    @Test
    public void getFireStations_sendsRequestSuccessfully_whenEndpointIsCall() throws Exception {
        mockMvc.perform(get("/firestations")).andExpect((status().isOk()));
        // la methode perform de mockMvc execute une requete get sur l'URL /firestations
    }

    //@Test
    //public void create//add_returnsTheSum_ofTwoPositiveIntegers()

    //exemple test mockmvc: https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
    // video : https://www.youtube.com/watch?v=Aasp0mWT3Ac
}
