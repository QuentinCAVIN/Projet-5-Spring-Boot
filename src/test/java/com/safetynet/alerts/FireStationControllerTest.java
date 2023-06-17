package com.safetynet.alerts;

//givenStateUnderTest_whenMethodAction_thenExpectedBehavior
//ou
//add_returnsTheSum_ofTwoPositiveIntegers()

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.LoadDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.IterableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Iterator;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
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
    //Je ne comprends pas pourquoi le Mock est obligatoire ici, vu que FireStationController
    //n'utilise pas LoadataService.
    String addressTest = "10 rue de la gare";


    //TODO : finaliser les test pour qu'ils répondent aux exigences définis.

    @Test

    public void createFireStation_returnCode201_whenEndpointIsCall() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation(9);
        fireStation.setAddress(addressTest);// a factoriser dans un BeforeEach
        when(fireStationService.saveFireStation(any())).thenReturn(fireStation);
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\":\"10 rue de la gare\", \"station\":\"9\" }"))
                .andExpect((status().isCreated()));
        ;
    }

    @Test
    public void createFireStation_returnCode400_whenFirestationAttributIsNull() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress(addressTest);// a factoriser dans un BeforeEach
        when(fireStationService.saveFireStation(any())).thenReturn(fireStation);
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"station\":\"9\" }"))
                .andExpect((status().isBadRequest()));
    }

    @Test
    public void updateFireStation_returnFireStationUpdated_whenEndpointIsCall() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation(9);
        fireStation.setAddress(addressTest);
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStation));// c'est un Wrap?
        // ci-dessus a mettre dans un BeforeEach
        mockMvc.perform(put("/firestation").param("address", addressTest)
                        // param pour répondre à @RequestParam("address")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"6\" }"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$").value(fireStation));
        //Body = {"id":0,"address":"8 rue de la paix","station":6}
        //pourquoi le 8 rue de la paix est mis à jour? la requete porte sur le 10
        //

        //.andExpect(jsonPath("address", is("10 rue de la paix")));//Stop ici essayer de comprendre pourquoi ça marche pas (erreur 400)
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

    //Voir si utile (pas sûr)
    public static String asJsonString(final Object obj) {
        //Methode utilisé pour convertir (= serialiser) des objets java en fichier json.
        //On utilise la classe ObjectMapper de Jackson pour ça.
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //@Test
    //public void create//add_returnsTheSum_ofTwoPositiveIntegers()

    //exemple test mockmvc: https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
    // video : https://www.youtube.com/watch?v=Aasp0mWT3Ac
}
