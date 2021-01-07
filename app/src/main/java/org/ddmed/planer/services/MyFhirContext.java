package org.ddmed.planer.services;

import org.ddmed.planer.BuildConfig;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.context.FhirContext;

class MyFhirContext {
    private static final String DSTU2_URL = BuildConfig.API_URL;
    private static FhirContext ctx = FhirContext.forR4();
    private static IGenericClient client = ctx.newRestfulGenericClient(DSTU2_URL);

    static FhirContext getContext(){
        return ctx;
    }

    static IGenericClient getClient(){
        return client;
    }
}
