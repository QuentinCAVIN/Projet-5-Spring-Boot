package com.safetynet.alerts;

import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.LoadDataService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
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
    //Je ne comprends pas pourquoi le Mock est obligatoire ici, vu que FireStationController
    //n'utilise pas LoadataService.
    //TODO: Trouver un moyen de corriger ça.
    String addressTest = "10 rue de la gare";

    @Test
    public void createFireStation_returnCode201_whenFireStationIsCreated() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation(9);
        fireStation.setAddress(addressTest);// a factoriser dans un BeforeEach
        when(fireStationService.saveFireStation(any())).thenReturn(fireStation);
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\":\" "+ addressTest+"\",\"station\":\"9\" }"))
                .andExpect((status().isCreated()));
        //TODO Voir pour perfectionner le test avec un getFirestation
    }

    @Test
    public void createFireStation_returnCode400_whenAFirestationAttributIsNull() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress(addressTest);// a factoriser dans un BeforeEach
        when(fireStationService.saveFireStation(any())).thenReturn(fireStation);
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"station\":\"9\" }"))
                .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.errorMessage").value("{address=Champ obligatoire}"));
    }

    @Test
    public void createFirestation_returnCode409_whenTheFireStationAddedIsAlreadyCreated() throws Exception{
        FireStation fireStation = new FireStation();
        fireStation.setAddress(addressTest);
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStation));
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"address\":\"10 rue de la gare\", \"station\":\"9\" }"))
                .andExpect((status().isConflict()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Ce centre de secours à déja été créé: \""+ fireStation+"\""));
    }
    @Test
    public void updateFireStation_returnCode200_whenAFireStationIsModified() throws Exception {
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
    }

    @Test
    public void updateFireStation_returnCode400_whenAWrongStationArgumentIsEnter() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation(9);
        fireStation.setAddress(addressTest);
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStation));
        mockMvc.perform(put("/firestation").param("address",addressTest)
                        // param pour répondre à @RequestParam("address")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"Wrong station argument\" }"))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.errorMessage")
                        .value(Matchers.containsString("\"Wrong station argument\": not a valid `java.lang.Integer` value")));
    }


    @Test
    public void updateFireStation_returnCode404_whenAWrongAddressIsEnter() throws Exception {
        mockMvc.perform(put("/firestation").param("address","Wrong address")
                        // param pour répondre à @RequestParam("address")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"6\" }"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("L'adresse \"Wrong address\" ne correspond à aucun centre de secours.")); //TODO modifier le message d'erreur
    }

    @Test
    public void deleteFireStation_returnCode204_whenAFireStationIsDelete() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation(9);
        fireStation.setAddress(addressTest);
        when(fireStationService.getFireStation(addressTest)).thenReturn(Optional.of(fireStation));
        mockMvc.perform(delete("/firestation").param("address",addressTest))
                .andExpect((status().isNoContent()));
    }

    @Test
    public void deleteFireStation_returnCode404_WhenTheFirestationtoDeleteIsNotFound() throws Exception {
        mockMvc.perform(delete("/firestation")
                .param("address","Address not present")
                .contentType(MediaType.APPLICATION_JSON).content("{\"station\":\"6\" }"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("L'adresse \"Address not present\" ne correspond à aucun centre de secours."));
    }

   /*certains tests unitaires sont dépendants des messages d'erreurs d'autres classes (pas bien?).
     Corriger une faute d'ortographe dans un des messages plante mes tests. PageObject?*/
    //TODO: Voir si on gére tout les messages d'erreurs dans des variable String qu'on viendrai récupérer.

    //@Test
    //public void create//add_returnsTheSum_ofTwoPositiveIntegers()

    //exemple test mockmvc: https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
    // video : https://www.youtube.com/watch?v=Aasp0mWT3Ac
}
