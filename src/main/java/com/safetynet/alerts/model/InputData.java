package com.safetynet.alerts.model;

import lombok.Data;
import java.util.List;
@Data
public class InputData {

    private List<FireStation> firestations;

    private List<Person> persons;

    private List<MedicalRecord> medicalrecords;
}
