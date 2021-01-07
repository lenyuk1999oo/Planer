package org.ddmed.planer.services;

import ca.uhn.fhir.rest.gclient.StringClientParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ValueSet;


public class ValueSetService {

    public static ValueSet getValueSet(String name, String language) {

        Bundle result = null;
        if (!language.isEmpty()) {
            result = MyFhirContext.getClient()
                    .search()
                    .forResource(ValueSet.class)
                    .returnBundle(Bundle.class)
                    .where(ValueSet.NAME.matchesExactly().value(name))
                    .and(new StringClientParam(ValueSet.SP_RES_LANGUAGE).matches().value(language))
                    .execute();
        } else {
            result = MyFhirContext.getClient()
                    .search()
                    .forResource(ValueSet.class)
                    .returnBundle(Bundle.class)
                    .where(ValueSet.NAME.matchesExactly().value(name))
                    .execute();
        }
        if (result.getEntry().size() < 1) {
            return null;
        }

        return (ValueSet) result.getEntry().get(0).getResource();
    }
}
