package com.safetynet.alerts;

import com.safetynet.alerts.controller.EmergencyInfoController;
import com.safetynet.alerts.model.EmergencyInfo;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.EmergencyInfoService;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.LoadDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

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
    FireStationService fireStationService;

    @BeforeEach
    public void setup() {
        List<EmergencyInfo> thereIsAnEmergencyInfo = new ArrayList<>();
        thereIsAnEmergencyInfo.add(new EmergencyInfo());

        when(emergencyInfoService.getEmergencyInfoByStation(1)).thenReturn(thereIsAnEmergencyInfo);
        when(emergencyInfoService.getEmergencyInfoByAddress("address")).thenReturn(thereIsAnEmergencyInfo);
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


        when(emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(1)).thenReturn(peopleCoveredByFirestationService);
        when(emergencyInfoService.numberOfChildrenCoveredByFirestation(1)).thenReturn(1);
        when(emergencyInfoService.numberOfAdultCoveredByFirestation(1)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n°1:'][1].lastName").value("b"))
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n°1:'][0]").value(emergencyInfoOfAChild))
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n°1:'][1]").value(emergencyInfoOfAnAdult))
                .andExpect(jsonPath("$.['Adultes présents: ']").value(1))
                .andExpect(jsonPath("$.['Enfants présents: ']").value(1));

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

        List<EmergencyInfo> emergencyInfoOfChildrenAtThisAddress = new ArrayList<>();
        emergencyInfoOfChildrenAtThisAddress.add(emergencyInfoOfPersonA);
        emergencyInfoOfChildrenAtThisAddress.add(emergencyInfoOfPersonB);

        List<String> adultAtThisAddress = new ArrayList<>();
        adultAtThisAddress.add("adultC adultC");
        adultAtThisAddress.add("adultD adultD");


        when(emergencyInfoService.findEmergencyinfoOfCHildrenByAddress("address")).thenReturn(emergencyInfoOfChildrenAtThisAddress);
        when(emergencyInfoService.findAdultByAddress("address")).thenReturn(adultAtThisAddress);

        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", "address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Enfants présents au address:'][0]").value(emergencyInfoOfPersonA))
                .andExpect(jsonPath("$.['Enfants présents au address:'][1]").value(emergencyInfoOfPersonB))
                .andExpect(jsonPath("$.['Adultes présents à cette adresse:']").value(adultAtThisAddress));
    }

   @Test
    public void findChildrenByAddress_returnCode404_whenAnUnknownAddressIsEnter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", "10 rue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Le 10 rue ne correspond à aucune adresse enregistrée."));
    }
    @Test
    public void findPhoneNumbersServedByFirestation_ReturnExpectedResult_WhenEndpointIsCalled() throws Exception {
        EmergencyInfo phoneNumberA = new EmergencyInfo();
        phoneNumberA.setPhone("a");

        EmergencyInfo phoneNumberB = new EmergencyInfo();
        phoneNumberB.setPhone("b");

        List<EmergencyInfo> phoneNumbersCoveredByFirestationService = new ArrayList<>();
        phoneNumbersCoveredByFirestationService.add(phoneNumberA);
        phoneNumbersCoveredByFirestationService.add(phoneNumberB);

        when(emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(1)).thenReturn(phoneNumbersCoveredByFirestationService);

        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert").param("firestation", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value(phoneNumberA))
                .andExpect(jsonPath("$.[1]").value(phoneNumberB));
    }

    @Test
    public void  findPhoneNumbersServedByFirestation_returnCode404_whenAnUnknowNumberOfFireStationIsEnter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert").param("firestation", "9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Il n'existe aucun centre de secours n° 9."));
    }

    @Test
    public void findEmergencyInformationByAddress_ReturnExpectedResult_WhenEndpointIsCalled() throws Exception {
        EmergencyInfo emergencyInfoOfPersonA = new EmergencyInfo();
        emergencyInfoOfPersonA.setFirstName("a");
        emergencyInfoOfPersonA.setLastName("a");
        emergencyInfoOfPersonA.setPhone("a");
        emergencyInfoOfPersonA.setAge(35);
        emergencyInfoOfPersonA.setMedications(new ArrayList<String>());
        emergencyInfoOfPersonA.setAllergies(new ArrayList<String>());

        EmergencyInfo emergencyInfoOfPersonB = new EmergencyInfo();
        emergencyInfoOfPersonB.setFirstName("b");
        emergencyInfoOfPersonB.setLastName("b");
        emergencyInfoOfPersonB.setAge(12);
        emergencyInfoOfPersonB.setPhone("b");
        emergencyInfoOfPersonB.setMedications(new ArrayList<String>());
        emergencyInfoOfPersonB.setAllergies(new ArrayList<String>());

        List<EmergencyInfo> emergencyInfoOfpeopleAtThisAdress = new ArrayList<>();
        emergencyInfoOfpeopleAtThisAdress.add(emergencyInfoOfPersonA);
        emergencyInfoOfpeopleAtThisAdress.add(emergencyInfoOfPersonB);

        FireStation fireStation = new FireStation();
        fireStation.setAddress("address");
        fireStation.setStation(1);

        when(fireStationService.getFireStation("address")).thenReturn(Optional.of(fireStation));
        when(emergencyInfoService.findEmergencyInfoOfPeopleByAddress("address")).thenReturn(emergencyInfoOfpeopleAtThisAdress);

        mockMvc.perform(MockMvcRequestBuilders.get("/fire").param("address", "address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['N° du centre de secours couvrant le address']").value(1))
                .andExpect(jsonPath("$.['Personnes présente au address:'][0]").value(emergencyInfoOfPersonA))
                .andExpect(jsonPath("$.['Personnes présente au address:'][1]").value(emergencyInfoOfPersonB));
    }

    @Test
    public void findEmergencyInformationByAddress_returnCode404_whenAnUnknownAddressIsEnter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fire").param("address", "Unknown address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Le Unknown address ne correspond à aucune adresse enregistrée."));
    }


}
