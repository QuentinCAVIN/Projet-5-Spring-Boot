package com.safetynet.alerts.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetFirestation() throws Exception {
        mockMvc.perform(get("/firestations")).andExpect((status().isOk()));
        // la methode perform de mockMvc execute une requête get sur l'URL firestations
    }

    @Test
    public void testPostFirestation() throws Exception {
        mockMvc.perform(post("/firestation")).andExpect((status().isOk()));
    }
}