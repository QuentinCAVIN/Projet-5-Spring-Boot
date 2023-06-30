package com.safetynet.alerts;

import com.safetynet.alerts.controller.MedicalRecordController;
import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.LoadDataService;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.alerts.service.PersonService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    PersonService personService;
    @MockBean
    LoadDataService loadDataService; // même problème que précédemment
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
    }

    @Test
    public void createPerson_returnCode201_whenPersonIsCreated() throws Exception{
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void createPerson_returnCode400_WhenLastNameIsMissing() throws Exception{
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("{lastName=Champ obligatoire.}"));
    }

    @Test
    public void createPerson_returnCode409_WhenAPersonAlreadyExist() throws Exception{

        when(personService.getPerson(firstName,lastName)).thenReturn(Optional.of(person));
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\"}"))
                .andExpect(status().isConflict()) .andExpect(jsonPath("$.errorMessage")
                        .value(firstName + " " + lastName +" est déjà enregistré: \""+ person +"\""));;
    }

    @Test
    public void updatePerson_returnCode200_whenAPersonIsModified() throws Exception {

        when(personService.getPerson(firstName,lastName)).thenReturn(Optional.of(person));
        mockMvc.perform(put("/person")
                        .param("firstName",firstName).param("lastName",lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Citadelle\"}"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.city").value("Citadelle"));
    }

    @Test
    public void updatePerson_returnCode400_whenFirstNameIsMissing() throws Exception {

        mockMvc.perform(put("/person").param("lastName",lastName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Citadelle\"}"))
                .andExpect((status().isBadRequest()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Le paramètre firstName est requis pour que la requête aboutisse."));
    }

    @Test
    public void updatePerson_returnCode404_whenAWrongLastNameIsEnter() throws Exception {

        when(personService.getPerson(firstName,lastName)).thenReturn(Optional.of(person));
        mockMvc.perform(put("/person")
                        .param("firstName",firstName).param("lastName","Wrong lastName")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"city\":\"Citadelle\"}"))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de données associé à " + firstName + " Wrong lastName."));
    }

    @Test
    public void deletePerson_returnCode204_whenAPersonIsDelete() throws Exception {
        when(personService.getPerson(firstName,lastName)).thenReturn(Optional.of(person));
        mockMvc.perform(delete("/person").param("firstName",firstName).param("lastName",lastName))
                .andExpect((status().isNoContent()));
    }

    @Test
    public void deletePerson_returnCode404_WhenThePersontoDeleteIsNotFound() throws Exception {
        when(personService.getPerson(firstName,lastName)).thenReturn(Optional.of(person));
        mockMvc.perform(delete("/person").param("firstName","first name not present").param("lastName",lastName))
                .andExpect((status().isNotFound()))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Il n'y a pas de données associé à first name not present "+ lastName+"."));
    }
}
