package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
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
    private String firstName;
    private String lastName;

    @BeforeEach
    public void setUpPerTest() {
        firstName = "Garrus";
        lastName = "Vakarian";

        person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress("5 grande rue");
        person.setCity("Palaven");
        person.setZip(40100);
        person.setPhone("18-18");
        person.setEmail("gvakarian@normandysr2.com");

        personRepository.deleteAll();
        personRepository.loadData();
    }




    @Test
    public void createPersonWithSuccess() throws Exception {
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isCreated());
        assertThat(personRepository.findByFirstNameAndLastName(firstName,lastName).get().getFirstName()).isEqualTo(firstName);
        assertThat(personRepository.findByFirstNameAndLastName(firstName,lastName).get().getLastName()).isEqualTo(lastName);
    }

    @Test
    public void updatePersonCreatedWithSuccess() throws Exception {
        createPersonWithSuccess();

        mockMvc.perform(put("/person")
                        .param("firstName", firstName).param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Citadelle\"}"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.city").value("Citadelle"));
    }

    @Test
    public void deletePersonUpdatedWithSuccess() throws Exception {
        createPersonWithSuccess();

        mockMvc.perform(delete("/person").param("firstName", firstName).param("lastName", lastName))
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
                        .param("firstName", firstName).param("lastName", "Wrong lastName")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Citadelle\"}"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de données associé à " + firstName + " Wrong lastName."));
    }

    @Test
    public void deletePersonUpdatedTwice() throws Exception {
        deletePersonUpdatedWithSuccess();

        mockMvc.perform(delete("/person").param("firstName", firstName).param("lastName", lastName))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de données associé à " + firstName + " " + lastName + "."));
    }
}