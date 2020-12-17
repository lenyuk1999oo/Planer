package org.ddmed.planer;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;

public class PatientFhirHelper {

    private static final String DSTU2_URL = BuildConfig.API_URL;

    private IGenericClient client;
    private FhirContext ctx;

    public PatientFhirHelper() {
        ctx = FhirContext.forR4();
        client = ctx.newRestfulGenericClient(DSTU2_URL);
    }

    public List<Patient> getPatients() {
        // Invoke the client
        Bundle bundle = client.search().forResource(Patient.class)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(ctx, bundle, Patient.class);
    }

    public IGenericClient getClient() {
        return client;
    }
}
