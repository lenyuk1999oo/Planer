package org.ddmed.planer.services;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Location;

import java.util.List;

import ca.uhn.fhir.util.BundleUtil;

public class LocationService {
    public static List<Location> getLocations() {
        Bundle bundle = MyFhirContext.getClient().search()
                .forResource(Location.class)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(MyFhirContext.getContext(), bundle, Location.class);
    }
}
