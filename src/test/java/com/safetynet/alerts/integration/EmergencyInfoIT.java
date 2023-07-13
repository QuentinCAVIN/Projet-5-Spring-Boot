package com.safetynet.alerts.integration;

import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.LoadDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class EmergencyInfoIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private LoadDataService loadDataService;

    @BeforeEach
    public void setup() {
        fireStationRepository.deleteAll();
        personRepository.deleteAll();
        medicalRecordRepository.deleteAll();
        loadDataService.loadData();
    }

    @Test
    public void findPersonsCoveredByFireStationWithSucces() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n°1:'][0].firstName").value("Peter"))
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n°1:'][1].lastName").value("Walker"))
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n°1:'][2].address").value("908 73rd St"))
                .andExpect(jsonPath("$.['Personnes couvertes par le centre de secours n°1:'][3].phone").value("841-874-7784"))
                .andExpect(jsonPath("$.['Adultes présents: ']").value(5))
                .andExpect(jsonPath("$.['Enfants présents: ']").value(1));
    }

    @Test
    public void findPersonsCoveredByFireStationWithUnknownArgument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber", "9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Il n'existe aucun centre de secours n° 9."));
    }

    @Test
    public void findChildrenByAddressWithSucces() throws Exception {
        String param = "1509 Culver St";

        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Enfants présents au " + param + ":'][0].firstName").value("Tenley"))
                .andExpect(jsonPath("$.['Enfants présents au " + param + ":'][1].age").value(5))
                .andExpect(jsonPath("$.['Adultes présents à cette adresse:']")
                        .value(new ArrayList<>(Arrays.asList("John Boyd", "Jacob Boyd", "Felicia Boyd"))));
    }

    @Test
    public void findChildrenByAddressWithWrongAddress() throws Exception {
        String param = "Wrong address";

        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Le " + param + " ne correspond à aucune adresse enregistrée."));
    }

    @Test
    public void findPhoneNumbersServedByFirestationWithSucces() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert").param("firestation", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$[*].phone")
                        .value(contains("841-874-6874", "841-874-9845", "841-874-8888", "841-874-9888")));
    }

    @Test
    public void findPhoneNumbersServedByFirestationWithWrongParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert").param("firestations", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.errorMessage").value("Le paramètre firestation est requis pour que la requête aboutisse."));
    }

    @Test
    public void findEmergencyInformationByAddressWithSucces() throws Exception {
        String param = "947 E. Rose Dr";

        mockMvc.perform(MockMvcRequestBuilders.get("/fire").param("address", param)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['N° du centre de secours couvrant le " + param + "']").value(1))
                .andExpect(jsonPath("$.['Personnes présente au " + param + ":'][0].firstName").value("Brian"))
                .andExpect(jsonPath("$.['Personnes présente au " + param + ":'][1].lastName").value("Stelzer"))
                .andExpect(jsonPath("$.['Personnes présente au " + param + ":'][2].age").value(9))
                .andExpect(jsonPath("$.['Personnes présente au " + param + ":'][0].allergies").value("nillacilan"))
                .andExpect(jsonPath("$.['Personnes présente au " + param + ":'][2].medications")
                        .value(contains("noxidian:100mg", "pharmacol:2500mg")));
    }

    @Test
    public void findEmergencyInformationByAddressWithAnUnknownAddress() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fire").param("address", "Unknown address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Le Unknown address ne correspond à aucune adresse enregistrée."));
    }

    @Test
    public void findEmergencyInformationByFireStationsCoverageWithSucces() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations").param("stations", "2,3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['29 15th St'][0].firstName").value("Jonanathan"))
                .andExpect(jsonPath("$.['892 Downing Ct'][0].lastName").value("Zemicks"))
                .andExpect(jsonPath("$.['892 Downing Ct'][1].phone").value("841-874-7512"))
                .andExpect(jsonPath("$.['892 Downing Ct'][2].age").value(6))
                .andExpect(jsonPath("$.['951 LoneTree Rd'][0].medications").value(contains("tradoxidine:400mg")))
                .andExpect(jsonPath("$.['1509 Culver St'][0].allergies").value(contains("nillacilan")));
    }

    @Test
    public void findEmergencyInformationByFireStationsCoverageWithUnknowNumberOfFireStation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations").param("stations", "1,9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Il n'existe aucun centre de secours n° 9."));
    }

    @Test
    public void findEmergencyInformationByPersonWithSucces() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/personInfo")
                        .param("firstName", "Tony").param("lastName", "Cooper")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value("Tony"))
                .andExpect(jsonPath("$.[0].lastName").value("Cooper"))
                .andExpect(jsonPath("$.[0].age").value("29"))
                .andExpect(jsonPath("$.[0].medications").value(contains("hydrapermazol:300mg", "dodoxadin:30mg")))
                .andExpect(jsonPath("$.[0].allergies").value("shellfish"));
    }

    @Test
    public void findEmergencyInformationByPersonWithAWrongName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/personInfo")
                        .param("firstName", "Tony").param("lastName", "Coper")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage").value("Il n'y a pas de données associé à Tony Coper."));
    }

    @Test
    public void findEmailByCityWithSucces() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "Culver")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].email").value("jaboyd@email.com"))
                .andExpect(jsonPath("$.[1].email").value("drk@email.com"));
    }

    @Test
    public void findEmailByCityWithAWrongName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "Dax").contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de donnée associée à Dax."));
    }
}


