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

    @BeforeEach
    public void setup() {
        List<EmergencyInfo> thereIsAnEmergencyInfo = new ArrayList<>();
        thereIsAnEmergencyInfo.add(new EmergencyInfo());

        when(emergencyInfoService.getEmergencyInfoByStation(1)).thenReturn(thereIsAnEmergencyInfo);
        when(emergencyInfoService.getEmergencyInfoByAddress("a")).thenReturn(thereIsAnEmergencyInfo);
    }

    @Test
    public void findPersonsCoveredByFireStation_ReturnExpectedResult_WhenEndpointIsCalled() throws Exception {
        EmergencyInfo emergencyInfoOfAChild = new EmergencyInfo();
        emergencyInfoOfAChild.setFirstName("a");
        emergencyInfoOfAChild.setLastName("a");
        emergencyInfoOfAChild.setAddress("a");
        emergencyInfoOfAChild.setPhone("a");

        EmergencyInfo emergencyInfoOfAnAdult = new EmergencyInfo();
        emergencyInfoOfAnAdult.setFirstName("b");
        emergencyInfoOfAnAdult.setLastName("b");
        emergencyInfoOfAnAdult.setAddress("b");
        emergencyInfoOfAnAdult.setPhone("b");

        List<EmergencyInfo> peopleCoveredByFirestationService = new ArrayList<>();
        peopleCoveredByFirestationService.add(emergencyInfoOfAChild);
        peopleCoveredByFirestationService.add(emergencyInfoOfAnAdult);

        Map<String, Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("Personnes couvertes par le centre de secours n° 1:", peopleCoveredByFirestationService);
        endpointExpected.put("Adultes présents:", 1);
        endpointExpected.put("Enfants présents:", 1);

        when(emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(1)).thenReturn(endpointExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber", "1")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber", "9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Il n'existe aucun centre de secours n° 9."));
    }

    @Test
    public void findChildrenByAddress_ReturnExpectedResult_WhenEndpointIsCalled() throws Exception {
        EmergencyInfo emergencyInfoOfPersonA = new EmergencyInfo();
        emergencyInfoOfPersonA.setFirstName("a");
        emergencyInfoOfPersonA.setLastName("a");
        emergencyInfoOfPersonA.setAge(10);

        EmergencyInfo emergencyInfoOfPersonB = new EmergencyInfo();
        emergencyInfoOfPersonB.setFirstName("b");
        emergencyInfoOfPersonB.setLastName("b");
        emergencyInfoOfPersonB.setAge(12);

        List<EmergencyInfo> peopleCoveredByFirestationService = new ArrayList<>();
        peopleCoveredByFirestationService.add(emergencyInfoOfPersonA);
        peopleCoveredByFirestationService.add(emergencyInfoOfPersonB);

        Map<String, Object> endpointExpected = new LinkedHashMap<>();
        endpointExpected.put("Enfants présents au a", peopleCoveredByFirestationService);
        endpointExpected.put("Adultes présents à cette adresse:", "1");

        when(emergencyInfoService.findEmergencyinfoOfCHildrenByAddress("a")).thenReturn(endpointExpected);

        System.out.println(endpointExpected);
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", "a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Enfants présents au a'][0]").value(emergencyInfoOfPersonA))
                .andExpect(jsonPath("$.['Enfants présents au a'][1]").value(emergencyInfoOfPersonB))
                .andExpect(jsonPath("$.['Adultes présents à cette adresse:']").value("1"));

    }

    @Test
    public void findChildrenByAddress_returnCode404_whenAnUnknownAddressIsEnter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", "10 rue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Le 10 rue ne correspond à aucune adresse enregistrée."));
    }
}
