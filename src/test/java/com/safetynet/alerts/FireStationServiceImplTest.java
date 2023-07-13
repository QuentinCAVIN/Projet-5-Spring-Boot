package com.safetynet.alerts;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.FireStationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FireStationServiceImplTest {
    @Autowired
    private FireStationServiceImpl fireStationService;

    @MockBean
    private static FireStationRepository fireStationRepository;

    private FireStation fireStationA;
    private FireStation fireStationB;
    List<FireStation> listOfFireStationExpected;

    List<String> listOfAddressesExpected;

    @BeforeEach
    public void setup() {
        fireStationA = new FireStation();
        fireStationA.setAddress("addressA");
        fireStationA.setStation(1);

        fireStationB = new FireStation();
        fireStationB.setAddress("addressB");
        fireStationB.setStation(2);

        listOfFireStationExpected = new ArrayList<>(Arrays.asList(fireStationA, fireStationB));

        listOfAddressesExpected = new ArrayList<>(Arrays.asList(fireStationA.getAddress(),fireStationB.getAddress()));
    }

    @Test
    public void getFireStationTest() {
        when(fireStationRepository.findByAddress("addressA")).thenReturn(Optional.of(fireStationA));
        fireStationService.getFireStation("addressA");

        verify(fireStationRepository, Mockito.times(1)).findByAddress("addressA");
        assertThat(fireStationRepository.findByAddress("addressA")).isEqualTo(Optional.of(fireStationA));
    }

    @Test
    public void getFireStationsTest() {
        when(fireStationRepository.findAll()).thenReturn(List.of(fireStationA, fireStationB));
        fireStationService.getFireStations();

        verify(fireStationRepository, Mockito.times(1)).findAll();
        assertThat(fireStationRepository.findAll()).isEqualTo(List.of(fireStationA, fireStationB));
    }

    @Test
    public void deleteFireStationTest() {
        fireStationService.deleteFireStation("addressA");

        verify(fireStationRepository, Mockito.times(1)).deleteByAddress("addressA");
    }

    @Test
    public void saveFireStationTest() {
        when(fireStationRepository.save(fireStationA)).thenReturn(fireStationA);

        fireStationService.saveFireStation(fireStationA);

        verify(fireStationRepository, Mockito.times(1)).save(fireStationA);
        assertThat(fireStationRepository.save(fireStationA)).isEqualTo(fireStationA);
    }


    @Test
    public void getAddressesCoveredByStationTest() {
        when(fireStationRepository.findAllByStation(1)).thenReturn(listOfFireStationExpected);

        fireStationService.getAddressesCoveredByStation(1);

        verify(fireStationRepository, Mockito.times(1)).findAllByStation(1);
        assertThat(fireStationService.getAddressesCoveredByStation(1))
                .isEqualTo(listOfAddressesExpected);
    }

    @Test
    public void getAddressesCoveredByStationTestGoesWrong() {
        when(fireStationRepository.findAllByStation(1)).thenReturn(Collections.emptyList());

        List<String> list =fireStationService.getAddressesCoveredByStation(1);

        assertThat(list).isEqualTo(Collections.emptyList());
    }


    @Test
    public void getAddressesCoveredByStationsTest() {
        when(fireStationRepository.findAllByStation(1)).thenReturn(List.of(fireStationA));
        when(fireStationRepository.findAllByStation(2)).thenReturn(List.of(fireStationB));

        fireStationService.getAddressesCoveredByStations(Arrays.asList(1,2));

        verify(fireStationRepository, Mockito.times(1)).findAllByStation(1);
        verify(fireStationRepository, Mockito.times(1)).findAllByStation(2);
        assertThat(fireStationService.getAddressesCoveredByStations(Arrays.asList(1,2)))
                .isEqualTo(listOfAddressesExpected);
    }

    @Test
    public void getAddressesCoveredByStationsTestGoesWrong() {
        when(fireStationRepository.findAllByStation(any())).thenReturn(Collections.emptyList());

        fireStationService.getAddressesCoveredByStations(Arrays.asList(1,2));

        verify(fireStationRepository, Mockito.times(1)).findAllByStation(1);
        verify(fireStationRepository, Mockito.times(1)).findAllByStation(2);
        assertThat(fireStationService.getAddressesCoveredByStations(Arrays.asList(1,2)))
                .isEqualTo(Collections.emptyList());
    }
}