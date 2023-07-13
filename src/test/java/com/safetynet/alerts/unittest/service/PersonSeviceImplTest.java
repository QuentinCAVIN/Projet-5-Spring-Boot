package com.safetynet.alerts.unittest.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonSeviceImplTest {

    @Autowired
    private PersonServiceImpl personService;

    @MockBean
    private static PersonRepository personRepository;

    private Person personA;

    @BeforeEach
    public void setup() {
        personA = new Person();
        personA.setFirstName("a");
        personA.setLastName("a");
        personA.setAddress("a");
        personA.setCity("a");
        personA.setZip(1);
        personA.setPhone("a");
        personA.setEmail("a");
    }

    @Test
    public void getPersonTest() {
        when(personRepository.findByFirstNameAndLastName("a", "a")).thenReturn(Optional.of(personA));
        personService.getPerson("a", "a");

        verify(personRepository, Mockito.times(1)).findByFirstNameAndLastName("a", "a");
        assertThat(personRepository.findByFirstNameAndLastName("a", "a")).isEqualTo(Optional.of(personA));
    }

    @Test
    public void deletePersonTest() {
        personService.deletePerson("a", "a");

        verify(personRepository, Mockito.times(1)).deleteByFirstNameAndLastName("a", "a");
    }

    @Test
    public void savePersonTest() {
        when(personRepository.save(personA)).thenReturn(personA);

        personService.savePerson(personA);

        verify(personRepository, Mockito.times(1)).save(personA);
        assertThat(personRepository.save(personA)).isEqualTo(personA);
    }

    @Test
    public void getPersonTestGoesWrong() {
        when(personRepository.findByFirstNameAndLastName("a", "a")).thenReturn(null);

        Optional<Person> person = personService.getPerson("a", "a");

        assertThat(person).isEqualTo(null);
    }
}