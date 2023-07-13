package com.safetynet.alerts.unittest.service;

import com.safetynet.alerts.model.EmergencyInfo;
import com.safetynet.alerts.repository.EmergencyInfoRepository;
import com.safetynet.alerts.service.EmergencyInfoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class EmergencyInfoServiceImplTest {
    @Autowired
    private EmergencyInfoService emergencyInfoService;

    @MockBean
    private static EmergencyInfoRepository emergencyInfoRepository;
    private EmergencyInfo emergencyInfoOfPersonA;
    private EmergencyInfo emergencyInfoOfPersonB;
    List<EmergencyInfo> listOfEmergencyInfoExpected;

    @BeforeEach
    public void setup() {
        emergencyInfoOfPersonA = new EmergencyInfo();
        emergencyInfoOfPersonA.setFirstName("a");
        emergencyInfoOfPersonA.setLastName("a");
        emergencyInfoOfPersonA.setAddress("a");
        emergencyInfoOfPersonA.setCity("a");
        emergencyInfoOfPersonA.setZip(1);
        emergencyInfoOfPersonA.setPhone("a");
        emergencyInfoOfPersonA.setEmail("a");
        emergencyInfoOfPersonA.setBirthdate("a");
        emergencyInfoOfPersonA.setAge(35);
        emergencyInfoOfPersonA.setMedications(new ArrayList<String>());
        emergencyInfoOfPersonA.setAllergies(new ArrayList<String>());
        emergencyInfoOfPersonA.setStation(1);

        emergencyInfoOfPersonB = new EmergencyInfo();
        emergencyInfoOfPersonB.setFirstName("b");
        emergencyInfoOfPersonB.setLastName("b");
        emergencyInfoOfPersonB.setAddress("b");
        emergencyInfoOfPersonB.setCity("b");
        emergencyInfoOfPersonB.setZip(1);
        emergencyInfoOfPersonB.setPhone("b");
        emergencyInfoOfPersonB.setEmail("b");
        emergencyInfoOfPersonB.setBirthdate("b");
        emergencyInfoOfPersonB.setAge(12);
        emergencyInfoOfPersonB.setPhone("b");
        emergencyInfoOfPersonB.setMedications(new ArrayList<String>());
        emergencyInfoOfPersonB.setAllergies(new ArrayList<String>());
        emergencyInfoOfPersonB.setStation(1);

        listOfEmergencyInfoExpected = new ArrayList<>(Arrays.asList(emergencyInfoOfPersonA, emergencyInfoOfPersonB));

        when(emergencyInfoRepository.findAllByStation(1)).thenReturn(listOfEmergencyInfoExpected);
        when(emergencyInfoRepository.findAllByAddress("address")).thenReturn(listOfEmergencyInfoExpected);
    }

    @Test
    public void findEmergencyInfoOfPeopleCoveredByFirestationTest() {
        emergencyInfoService.findEmergencyInfoOfPeopleCoveredByFirestation(1);

        verify(emergencyInfoRepository, Mockito.times(1)).findAllByStation(any());
        Assertions.assertThat(emergencyInfoRepository.findAllByStation(1)).isEqualTo(listOfEmergencyInfoExpected);
    }

    @Test
    public void findEmergencyInfoOfPeopleByAddressTest() {
        emergencyInfoService.findEmergencyInfoOfPeopleByAddress("address");

        verify(emergencyInfoRepository, Mockito.times(1)).findAllByAddress("address");
        Assertions.assertThat(emergencyInfoRepository.findAllByAddress("address")).isEqualTo(listOfEmergencyInfoExpected);
    }

    @Test
    public void findEmergencyinfoOfCHildrenByAddressTest() {
        when(emergencyInfoRepository.findEmergencyinfoOfCHildrenByAddress("address")).thenReturn(listOfEmergencyInfoExpected);

        emergencyInfoService.findEmergencyinfoOfCHildrenByAddress("address");

        verify(emergencyInfoRepository, Mockito.times(1)).findEmergencyinfoOfCHildrenByAddress("address");
        Assertions.assertThat(emergencyInfoRepository.findEmergencyinfoOfCHildrenByAddress("address")).isEqualTo(listOfEmergencyInfoExpected);
    }

    @Test
    public void findAdultByAddressTest() {
        List<String> adultListExpected = new ArrayList<>(Arrays.asList("AdultA", "AdultB"));
        when(emergencyInfoRepository.findAdultByAddress("address")).thenReturn(adultListExpected);

        emergencyInfoService.findAdultByAddress("address");

        verify(emergencyInfoRepository, Mockito.times(1)).findAdultByAddress("address");
        Assertions.assertThat(emergencyInfoRepository.findAdultByAddress("address")).isEqualTo(adultListExpected);
    }

    @Test
    public void findEmergencyInfoByFirstNameAndLastNameTest() {
        when(emergencyInfoRepository.findEmergencyInfoByFirstNameAndLastName("firstname", "lastname")).thenReturn(listOfEmergencyInfoExpected);

        emergencyInfoService.findEmergencyInfoByFirstNameAndLastName("firstname", "lastname");

        verify(emergencyInfoRepository, Mockito.times(1)).findEmergencyInfoByFirstNameAndLastName("firstname", "lastname");
        Assertions.assertThat(emergencyInfoRepository.findEmergencyInfoByFirstNameAndLastName("firstname", "lastname")).isEqualTo(listOfEmergencyInfoExpected);
    }

    @Test
    public void findEmergencyInfoByCityTest() {
        when(emergencyInfoRepository.findAllByCity("city")).thenReturn(listOfEmergencyInfoExpected);

        emergencyInfoService.findEmergencyInfoByCity("city");

        verify(emergencyInfoRepository, Mockito.times(1)).findAllByCity("city");
        Assertions.assertThat(emergencyInfoRepository.findAllByCity("city")).isEqualTo(listOfEmergencyInfoExpected);
    }

    @Test
    public void numberOfAdultCoveredByFirestationTest() {
        int numberOfAdultsExpected = 666;
        when(emergencyInfoRepository.numberOfAdultCoveredByFirestation(1)).thenReturn(numberOfAdultsExpected);

        emergencyInfoService.numberOfAdultCoveredByFirestation(1);

        verify(emergencyInfoRepository, Mockito.times(1)).numberOfAdultCoveredByFirestation(1);
        Assertions.assertThat(emergencyInfoRepository.numberOfAdultCoveredByFirestation(1)).isEqualTo(numberOfAdultsExpected);
    }

    @Test
    public void numberOfChildrenCoveredByFirestationTest() {
        int numberOfChildrenExpected = 1234;
        when(emergencyInfoRepository.numberOfChildrenCoveredByFirestation(1)).thenReturn(numberOfChildrenExpected);

        emergencyInfoService.numberOfChildrenCoveredByFirestation(1);

        verify(emergencyInfoRepository, Mockito.times(1)).numberOfChildrenCoveredByFirestation(1);
        Assertions.assertThat(emergencyInfoRepository.numberOfChildrenCoveredByFirestation(1)).isEqualTo(numberOfChildrenExpected);
    }

    @Test
    public void getAllEmergencyInfoTest() {
        when(emergencyInfoRepository.getListOfAllEmergencyInfo()).thenReturn(listOfEmergencyInfoExpected);

        emergencyInfoService.getAllEmergencyInfo();

        verify(emergencyInfoRepository, Mockito.times(1)).getListOfAllEmergencyInfo();
        Assertions.assertThat(emergencyInfoRepository.getListOfAllEmergencyInfo()).isEqualTo(listOfEmergencyInfoExpected);
    }

    @Test
    public void getEmergencyInfoByAddressTest() {
        emergencyInfoService.getEmergencyInfoByAddress("address");

        verify(emergencyInfoRepository, Mockito.times(1)).findAllByAddress("address");
        Assertions.assertThat(emergencyInfoRepository.findAllByAddress("address")).isEqualTo(listOfEmergencyInfoExpected);
    }

    @Test
    public void getEmergencyInfoByStationTest() {
        emergencyInfoService.getEmergencyInfoByStation(1);

        verify(emergencyInfoRepository, Mockito.times(1)).findAllByStation(1);
        Assertions.assertThat(emergencyInfoRepository.findAllByStation(1)).isEqualTo(listOfEmergencyInfoExpected);
    }
}