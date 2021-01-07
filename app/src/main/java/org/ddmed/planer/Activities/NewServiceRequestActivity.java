package org.ddmed.planer.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import org.ddmed.planer.R;
import org.ddmed.planer.services.PatientService;
import org.ddmed.planer.services.PractitionerService;
import org.ddmed.planer.services.ReferenceService;
import org.ddmed.planer.services.ServiceRequestService;
import org.ddmed.planer.services.ValueSetService;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.ValueSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewServiceRequestActivity extends AppCompatActivity {
    private TimePickerDialog picker;
    private EditText eText;
    private Spinner spinnerCode;
    private Spinner spinnerPatient;
    private Spinner spinnerPractitioner;
    private Button cancel;
    private Button save;
    private EditText duration;

    private Date date;
    private Location location;
    private ArrayList<Patient> patients;
    private ArrayList<Practitioner> practitioners;
    private List<ValueSet.ConceptSetComponent> listCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.new_service_request);
        Intent intent = getIntent();
        location = (Location) intent.getSerializableExtra("location");

        date = (Date) intent.getSerializableExtra("date");
        System.out.println("-------------------------------Location --------"
                + location.getName() + " - "
                + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getYear());

        spinnerCode = (Spinner) findViewById(R.id.spn_new_SR_code);
        spinnerPatient = (Spinner) findViewById(R.id.spn_new_SR_patient);
        spinnerPractitioner = (Spinner) findViewById(R.id.spn_new_SR_practitioner);
        duration = (EditText) findViewById(R.id.edt_new_SR_duration);

        initCodes();
        initPatients();
        initPractitioners();
        addOnClickListener();
    }

    private void addOnClickListener() {
        cancel = (Button) findViewById(R.id.btn_new_SR_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewServiceRequestActivity.this.finish();
                return;
            }
        });

        save = (Button) findViewById(R.id.btn_new_SR_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceRequest serviceRequest = new ServiceRequest();
                serviceRequest.setLocationReference(new ArrayList<>());
                serviceRequest.addLocationReference(ReferenceService.getReference(location));

                Patient patient = patients.get(spinnerPatient.getSelectedItemPosition());
                serviceRequest.setSubject(ReferenceService.getReference(patient));
                Practitioner practitioner = practitioners.get(spinnerPractitioner.getSelectedItemPosition());
                serviceRequest.setRequester(ReferenceService.getReference(practitioner));

                serviceRequest.setOccurrence(getPeriod());
                serviceRequest.setCode(getCodeableConceptElement());


                System.out.println("/--/" + periodCheck(getPeriod()) + "/--/");
                try {
                    if (ServiceRequestService.save(serviceRequest).isEmpty()) {
                        System.out.println("shit");
                    } else {
                        NewServiceRequestActivity.this.finish();
                        return;
                    }
                } catch (NullPointerException e) {
                    System.out.println("---------------------------fucking shit-------------------");
                }
                System.out.println("---------------------------SUCK---------------------------");
            }
        });

        eText = (EditText) findViewById(R.id.edt_new_SR_timeStart);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(eText.getText().toString().split(":")[0]);
                int minutes = Integer.parseInt(eText.getText().toString().split(":")[1]);
                // time picker dialog
                picker = new TimePickerDialog(NewServiceRequestActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String massage = "";
                                if (sHour < 10)
                                    massage += "0";
                                massage += sHour + ":";
                                if (sMinute < 10)
                                    massage += "0";
                                massage += sMinute;
                                eText.setText(massage);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
    }

    private Period getPeriod() {
        Period period = new Period();

        int hour = Integer.parseInt(eText.getText().toString().split(":")[0]);
        int minutes = Integer.parseInt(eText.getText().toString().split(":")[1]);
        Date dateStart = new Date(date.getYear(), date.getMonth(), date.getDate(), hour, minutes, 0);
        Date dateEnd = new Date(date.getYear(), date.getMonth(), date.getDate(), hour, minutes, 0);

        dateStart.setYear(date.getYear() - 1900);
        period.setStart(dateStart);

        dateEnd.setYear(date.getYear() - 1900);
        dateEnd.setMinutes(minutes + Integer.parseInt(duration.getText().toString()));
        period.setEnd(dateEnd);
        return period;
    }

    public static String periodCheck(Period period) {
        String string = "";
        String pattern = "MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        string += simpleDateFormat.format(period.getStart());
        string += "\n";
        string += simpleDateFormat.format(period.getEnd());

        string += "\n\n";
        return string;
    }

    private void initPatients() {
        final String[] s = {""};
        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                patients = (ArrayList<Patient>) PatientService.getPatients();
                for (Patient patient : patients)
                    s[0] += patient.getNameFirstRep().getText() + ";";

                handler.post(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewServiceRequestActivity.this, android.R.layout.simple_spinner_dropdown_item, s[0].split(";"));
                        spinnerPatient.setAdapter(adapter);
                    }
                });
            }
        });

        thread.start();
    }

    private void initPractitioners() {
        final String[] s = {""};
        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                practitioners = (ArrayList<Practitioner>) PractitionerService.getPractitioner();
                for (Practitioner practitioner : practitioners)
                    s[0] += practitioner.getNameFirstRep().getText() + ";";

                handler.post(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewServiceRequestActivity.this, android.R.layout.simple_spinner_dropdown_item, s[0].split(";"));
                        spinnerPractitioner.setAdapter(adapter);
                    }
                });
            }
        });

        thread.start();
    }

    private CodeableConcept getCodeableConceptElement() {
        ValueSet.ConceptReferenceComponent selectedElement = listCodes.get(0).getConcept().get(spinnerPractitioner.getSelectedItemPosition());
        return new CodeableConcept().setText(selectedElement.getDisplay()).addCoding(
                new Coding().setCode(selectedElement.getCode()).setDisplay(selectedElement.getDisplay()).setSystem(listCodes.get(0).getSystem()));
    }

    private void initCodes() {
        final String[] s = {""};
        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    listCodes =
                            ValueSetService.getValueSet("ang-procedure-code", "").getCompose().getInclude();
                } catch (NullPointerException nullPointerException) {
                    listCodes = new ArrayList<>();
                    listCodes.add(new ValueSet.ConceptSetComponent().addConcept(new ValueSet.ConceptReferenceComponent()));
                }
                for (ValueSet.ConceptReferenceComponent concept : listCodes.get(0).getConcept())
                    s[0] += concept.getDisplay() + ";";

                handler.post(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewServiceRequestActivity.this, android.R.layout.simple_spinner_dropdown_item, s[0].split(";"));
                        spinnerCode.setAdapter(adapter);
                    }
                });
            }
        });

        thread.start();
    }
}
