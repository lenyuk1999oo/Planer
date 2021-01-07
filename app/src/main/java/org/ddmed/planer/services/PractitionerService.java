package org.ddmed.planer.services;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Practitioner;

import java.util.List;

import ca.uhn.fhir.util.BundleUtil;

public class PractitionerService {
    public static List<Practitioner> getPractitioner() {
        Bundle bundle = MyFhirContext.getClient().search()
                .forResource(Practitioner.class)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(MyFhirContext.getContext(), bundle, Practitioner.class);
    }
}
