package com.safetynet.alerts;

//givenStateUnderTest_whenMethodAction_thenExpectedBehavior
//ou
//add_returnsTheSum_ofTwoPositiveIntegers()

import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.LoadDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FireStationController.class} )
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

    @Test
    public void testGetFirestation() throws Exception {
        mockMvc.perform(get("/firestations")).andExpect((status().isOk()));
        // la methode perform de mockMvc execute une requête get sur l'URL /firestations
    }
}
