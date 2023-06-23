package com.safetynet.alerts;

import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.service.LoadDataService;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    MedicalRecordService medicalRecordService;
    @MockBean
    LoadDataService loadDataService; // même problème que précédemment


    @Test
    public void createMedicalRecord_returnCode201_whenMedicalRecordIsCreated() throws Exception{
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"Garrus\", \"lastName\": \"VAKARIAN\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void createMedicalRecord_returnCode400_whenMedicalRecordIsCreated() throws Exception{
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content("{\"lastName\": \"VAKARIAN\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void createMedicalRecord_returnCode409_() throws Exception{
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Garrus");
        medicalRecord.setLastName("VAKARIAN");
        when(medicalRecordService.getMedicalRecord("Garrus","VAKARIAN")).thenReturn(Optional.of(medicalRecord));
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"Garrus\", \"lastName\": \"VAKARIAN\"}"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }


}
