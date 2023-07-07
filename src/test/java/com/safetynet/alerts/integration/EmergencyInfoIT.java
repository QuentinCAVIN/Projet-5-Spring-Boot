package com.safetynet.alerts.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmergencyInfoIT {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testfindpeoplebystation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/firestation").param("stationNumber", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testFindChildByStation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert").param("address", "1509 Culver St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

