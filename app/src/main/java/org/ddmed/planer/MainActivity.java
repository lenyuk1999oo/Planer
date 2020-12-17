package org.ddmed.planer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] s = {""};
                Handler handler = new Handler();
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        PatientFhirHelper patientFhirHelper = new PatientFhirHelper();

                        for (Patient patient : patientFhirHelper.getPatients())
                            s[0] += patient.getNameFirstRep().getFamily() + " ";

                        handler.post(new Runnable() {
                            public void run() {
                                textView.setText(s[0]);
                            }
                        });
                    }
                });

                thread.start();
            }
        };
        Button btnOk = (Button) findViewById(R.id.button);
        btnOk.setOnClickListener(onClickListener);
    }
}