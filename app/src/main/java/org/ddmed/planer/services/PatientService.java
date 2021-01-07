package org.ddmed.planer.services;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import ca.uhn.fhir.util.BundleUtil;

public class PatientService {
    public static List<Patient> getPatients() {
        Bundle bundle = MyFhirContext.getClient().search()
                .forResource(Patient.class)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(MyFhirContext.getContext(), bundle, Patient.class);
    }
}
