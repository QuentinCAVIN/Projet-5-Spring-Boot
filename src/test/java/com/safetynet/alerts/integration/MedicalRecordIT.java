package com.safetynet.alerts.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPostMedicalRecord() throws Exception{
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content(" { \"firstName\":\"Garrus\", \"lastName\":\"VAKARIAN\" }"))
                .andExpect(status().isCreated());
    }
    @Test
    public void testPutMedicalRecord(){
        fail();
    }
    @Test
    public void testDeleteMedicalRecord(){
        fail();
    }
}
