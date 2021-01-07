package org.ddmed.planer.services;

import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;

public class ReferenceService {
    public static Reference getReference(DomainResource domainResource) {
        if(domainResource == null)
            return new Reference();
        Reference reference = new Reference(domainResource);

        reference.setType(domainResource.fhirType());
        reference.setId(domainResource.getIdElement().getIdPart());
        reference.setDisplay(domainResource.fhirType() + "/" + reference.getId());
        reference.setReference(domainResource.fhirType() + "/" + domainResource.getIdElement().getIdPart());

        return reference;
    }

}
