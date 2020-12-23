package org.ddmed.planer;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;

public class LocationFhirHelper {

    private static final String DSTU2_URL = BuildConfig.API_URL;

    private IGenericClient client;
    private FhirContext ctx;

    public LocationFhirHelper() {
        ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setSocketTimeout(200000000);
        client = ctx.newRestfulGenericClient(DSTU2_URL);
    }

    public List<Location> getLocation() {
        // Invoke the client
        Bundle bundle = client.search()
                .forResource(Location.class)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(ctx, bundle, Location.class);
    }

    public IGenericClient getClient() {
        return client;
    }
}
