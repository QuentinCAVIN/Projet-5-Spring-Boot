package com.safetynet.alerts;

import com.safetynet.alerts.controller.EmergencyInfoController;
import com.safetynet.alerts.controller.FireStationController;
import com.safetynet.alerts.model.EmergencyInfo;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.EmergencyInfoService;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.LoadDataService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {EmergencyInfoController.class})
public class EmergencyInfoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmergencyInfoService emergencyInfoService;
    @MockBean
    private LoadDataService loadDataService;
    @MockBean
    FireStationRepository fireStationRepository;

    Map<String, Object> endpointExpected = new LinkedHashMap<>();
    List<EmergencyInfo> peopleCoveredByFirestationService = new ArrayList<>();
    EmergencyInfo emergencyInfoOfAChild = new EmergencyInfo();
    EmergencyInfo emergencyInfoOfAnAdult = new EmergencyInfo();

    @BeforeEach
    public void setup() {

        emergencyInfoOfAChild.setFirstName("a");
        emergencyInfoOfAChild.setLastName("a");
        emergencyInfoOfAChild.setAddress("a");
        emergencyInfoOfAChild.setPhone("a");


        emergencyInfoOfAnAdult.setFirstName("b");
        emergencyInfoOfAnAdult.setLastName("b");
        emergencyInfoOfAnAdult.setAddress("b");
        emergencyInfoOfAnAdult.setPhone("b");


        peopleCoveredByFirestationService.add(emergencyInfoOfAChild);
        peopleCoveredByFirestationService.add(emergencyInfoOfAnAdult);


        endpointExpected.put("Personnes couvertes par le centre de secours n° 1:",peopleCoveredByFirestationService);
        endpointExpected.put("Adultes présents:",1);
        endpointExpected.put("Enfants présents:",1);

        when(emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(1)).thenReturn(endpointExpected);
        when(fireStationRepository.findByStation(1)).thenReturn(Optional.of(new FireStation()));

    }

    @Test
    public void findPersonsCoveredByFireStation_ReturnExpectedInstruction_WhenEndpointIsCalled() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber","1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n° 1:'][1].lastName").value("b"))
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n° 1:'][0]").value(emergencyInfoOfAChild))
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n° 1:'][1]").value(emergencyInfoOfAnAdult))
                .andExpect(jsonPath("$.['Enfants présents:']").value(1))
                .andExpect(jsonPath("$.['Adultes présents:']").value(1));

    }
    @Test
    public void findPersonsCoveredByFireStation_returnCode404_whenAnUnknowNumberOfFireStationIsEnter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber","9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Il n'existe aucun centre de secours n° 9."));
    }
}
