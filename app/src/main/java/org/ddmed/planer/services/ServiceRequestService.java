package org.ddmed.planer.services;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.ServiceRequest;

import java.util.List;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.BundleUtil;

public class ServiceRequestService {
    public static List<ServiceRequest> getServiceRequest() {
        Bundle bundle = MyFhirContext.getClient().search()
                .forResource(ServiceRequest.class)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(MyFhirContext.getContext(), bundle, ServiceRequest.class);
    }

    public static ServiceRequest save(ServiceRequest resource) {
        try {
            MethodOutcome outcome = MyFhirContext.getClient().create()
                    .resource(resource)
                    .prettyPrint()
                    .encodedJson()
                    .execute();

            resource.setId(outcome.getId());
            return resource;

        } catch (Exception e) {
            System.out.println("---------------------------fucking shit-------------------");
            e.printStackTrace();
            return null;
        }

    }
}
