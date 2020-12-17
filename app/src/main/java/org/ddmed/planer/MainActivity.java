package org.ddmed.planer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.hl7.fhir.r4.model.Patient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("fuck");
        PatientFhirHelper patientFhirHelper = new PatientFhirHelper();
        String s = "";
        for (Patient patient : patientFhirHelper.getPatients())
            s += patient.getNameFirstRep().getFamily() + " ";

        System.out.println("fuuuuuuuuuuuuuuuuuuck - "+s);
    }
}