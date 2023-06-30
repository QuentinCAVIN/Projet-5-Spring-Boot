package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PersonRepository personRepository;

    private Person person;
    private String firstName = "Garrus";
    private String lastName = "Vakarian";
    private String address = "5 grande rue";
    private String city = "Palaven";
    private int zip = 40100;
    private String phone = "18-18";
    private String email = "gvakarian@normandysr2.com";

    // TODO : Ajuster les attributs et le BeforeEach de la classe, beaucoups de champs inutile

    @BeforeEach
    public void setUpPerTest(){
        person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        person.setCity(city);
        person.setZip(zip);
        person.setPhone(phone);
        person.setEmail(email);
        personRepository.deleteAll();
    }

    @Test
    public void createPersonWithSuccess() throws Exception {
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void updatePersonCreatedWithSuccess() throws Exception {
        createPersonWithSuccess();
        mockMvc.perform(put("/person")
                        .param("firstName",firstName).param("lastName",lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Citadelle\"}"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.city").value("Citadelle"));
    }

    @Test
    public void deletePersonUpdatedWithSuccess() throws Exception {
        createPersonWithSuccess();
        mockMvc.perform(delete("/person").param("firstName",firstName).param("lastName",lastName))
                .andExpect((status().isNoContent()));
    }

    @Test
    public void createPersonWithoutLastName() throws Exception {
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("{lastName=Champ obligatoire.}"));
    }

    @Test
    public void updatePersonWithAWrongFirstName() throws Exception {
        createPersonWithSuccess();
        mockMvc.perform(put("/person")
                        .param("firstName",firstName).param("lastName","Wrong lastName")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Citadelle\"}"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de données associé à " + firstName + " Wrong lastName."));
    }

    @Test
    public void deletePersonUpdatedTwice() throws Exception {
        deletePersonUpdatedWithSuccess();
        mockMvc.perform(delete("/person").param("firstName",firstName).param("lastName",lastName))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de données associé à " + firstName + " " + lastName+"."));
    }
}