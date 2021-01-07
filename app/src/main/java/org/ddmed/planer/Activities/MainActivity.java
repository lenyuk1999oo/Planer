package org.ddmed.planer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.ddmed.planer.Event;
import org.ddmed.planer.Popup;
import org.ddmed.planer.R;
import org.ddmed.planer.services.LocationService;
import org.ddmed.planer.services.ReferenceService;
import org.ddmed.planer.services.ServiceRequestService;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.ServiceRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.core.content.ContextCompat;

import android.util.Log;
import android.widget.ImageView;

import com.framgia.library.calendardayview.CalendarDayView;
import com.framgia.library.calendardayview.EventView;
import com.framgia.library.calendardayview.PopupView;
import com.framgia.library.calendardayview.data.IEvent;
import com.framgia.library.calendardayview.data.IPopup;
import com.framgia.library.calendardayview.decoration.CdvDecorationDefault;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Spinner spinner;
    private Button btnOk;
    private Button btnSelect;
    private EditText eText;
    private DatePickerDialog picker;
    private Button btnOpenPopup;

    private ArrayList<Location> locations;

    String[] DayOfWeek = {"Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"};

    CalendarDayView dayView;

    ArrayList<IEvent> events;
    ArrayList<IPopup> popups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spn_new_SR_patient);
        eText = (EditText) findViewById(R.id.edt_day);
        eText.setInputType(InputType.TYPE_NULL);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        locations = new ArrayList<>();
        eText.setText(day + "/" + (month + 1) + "/" + year);

        initLocations();
        initServiceRequest();
        addOnClickListener();
        /*calenderTest();*/

    }

   /* public void calenderTest() {
        dayView = (CalendarDayView) findViewById(R.id.dayView);
        dayView.setLimitTime(9, 22);

        ((CdvDecorationDefault) (dayView.getDecoration())).setOnEventClickListener(
                new EventView.OnEventClickListener() {
                    @Override
                    public void onEventClick(EventView view, IEvent data) {
                        Log.e("TAG", "onEventClick:" + data.getName());
                    }

                    @Override
                    public void onEventViewClick(View view, EventView eventView, IEvent data) {
                        Log.e("TAG", "onEventViewClick:" + data.getName());
                        if (data instanceof Event) {
                            // change event (ex: set event color)
                            dayView.setEvents(events);
                        }
                    }
                });

        ((CdvDecorationDefault) (dayView.getDecoration())).setOnPopupClickListener(
                new PopupView.OnEventPopupClickListener() {
                    @Override
                    public void onPopupClick(PopupView view, IPopup data) {
                        Log.e("TAG", "onPopupClick:" + data.getTitle());
                    }

                    @Override
                    public void onQuoteClick(PopupView view, IPopup data) {
                        Log.e("TAG", "onQuoteClick:" + data.getTitle());
                    }

                    @Override
                    public void onLoadData(PopupView view, ImageView start, ImageView end,
                                           IPopup data) {
                        start.setImageResource(R.drawable.logo_angelholm);
                    }
                });

        events = new ArrayList<>();

        {
            int eventColor = ContextCompat.getColor(this, R.color.eventColor);
            Calendar timeStart = Calendar.getInstance();
            timeStart.set(Calendar.HOUR_OF_DAY, 11);
            timeStart.set(Calendar.MINUTE, 0);
            Calendar timeEnd = (Calendar) timeStart.clone();
            timeEnd.set(Calendar.HOUR_OF_DAY, 15);
            timeEnd.set(Calendar.MINUTE, 30);
            Event event = new Event(1, timeStart, timeEnd, "Event", "Hockaido", eventColor);

            events.add(event);
        }

        {
            int eventColor = ContextCompat.getColor(this, R.color.eventColor1);
            Calendar timeStart = Calendar.getInstance();
            timeStart.set(Calendar.HOUR_OF_DAY, 18);
            timeStart.set(Calendar.MINUTE, 0);
            Calendar timeEnd = (Calendar) timeStart.clone();
            timeEnd.set(Calendar.HOUR_OF_DAY, 20);
            timeEnd.set(Calendar.MINUTE, 30);
            Event event = new Event(1, timeStart, timeEnd, "Another event", "Hockaido", eventColor);

            events.add(event);
        }

        popups = new ArrayList<>();

        {
            Calendar timeStart = Calendar.getInstance();
            timeStart.set(Calendar.HOUR_OF_DAY, 12);
            timeStart.set(Calendar.MINUTE, 0);
            Calendar timeEnd = (Calendar) timeStart.clone();
            timeEnd.set(Calendar.HOUR_OF_DAY, 13);
            timeEnd.set(Calendar.MINUTE, 30);

            Popup popup = new Popup();
            popup.setStartTime(timeStart);
            popup.setEndTime(timeEnd);
            popup.setImageStart("http://sample.com/image.png");
            popup.setTitle("event 1 with title");
            popup.setDescription("Yuong alsdf");
            popups.add(popup);
        }

        {
            Calendar timeStart = Calendar.getInstance();
            timeStart.set(Calendar.HOUR_OF_DAY, 20);
            timeStart.set(Calendar.MINUTE, 0);
            Calendar timeEnd = (Calendar) timeStart.clone();
            timeEnd.set(Calendar.HOUR_OF_DAY, 22);
            timeEnd.set(Calendar.MINUTE, 0);

            Popup popup = new Popup();
            popup.setStartTime(timeStart);
            popup.setEndTime(timeEnd);
            popup.setImageStart("http://sample.com/image.png");
            popup.setTitle("event 2 with title");
            popup.setDescription("Yuong alsdf");
            popups.add(popup);
        }

        dayView.setEvents(events);
        dayView.setPopups(popups);
    }*/

    private void addOnClickListener() {
        spinner = (Spinner) findViewById(R.id.spn_new_SR_patient);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initServiceRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnOk = (Button) findViewById(R.id.button);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocations();
            }
        });

        btnSelect = (Button) findViewById(R.id.select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textView.setText("select :" + spinner.getSelectedItem().toString() + " " + eText.getText());
            }
        });

        eText = (EditText) findViewById(R.id.edt_day);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = Integer.parseInt(eText.getText().toString().split("/")[0]);
                int month = Integer.parseInt(eText.getText().toString().split("/")[1]) - 1;
                int year = Integer.parseInt(eText.getText().toString().split("/")[2]);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                initServiceRequest();
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        btnOpenPopup = (Button) findViewById(R.id.button2);
        btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewServiceRequestActivity.class);
                Location location = locations.get(spinner.getSelectedItemPosition());
                intent.putExtra("location", location);
                int day = Integer.parseInt(eText.getText().toString().split("/")[0]);
                int month = Integer.parseInt(eText.getText().toString().split("/")[1]) - 1;
                int year = Integer.parseInt(eText.getText().toString().split("/")[2]);
                Date date = new Date(year, month, day);

                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

    private void initLocations() {
        final String[] s = {""};
        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                locations = (ArrayList<Location>) LocationService.getLocations();
                for (Location location : locations)
                    s[0] += location.getName() + ";";

                handler.post(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, s[0].split(";"));
                        spinner.setAdapter(adapter);
                    }
                });
            }
        });

        thread.start();
    }

    private void initServiceRequest() {
        final String[] s = {""};
        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                ArrayList<ServiceRequest> serviceRequests = (ArrayList<ServiceRequest>) ServiceRequestService.getServiceRequest();
                int day = Integer.parseInt(eText.getText().toString().split("/")[0]);
                int month = Integer.parseInt(eText.getText().toString().split("/")[1]) - 1;
                int year = Integer.parseInt(eText.getText().toString().split("/")[2]);
                System.out.println(year);
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getOccurrencePeriod().getStart().getYear() == year - 1900
                            && serviceRequest.getOccurrencePeriod().getStart().getMonth() == month
                            && serviceRequest.getOccurrencePeriod().getStart().getDate() == day)
                        if (!locations.isEmpty() && spinner.getSelectedItemPosition() >= 0) {
                            System.out.println("shit");
                            if (serviceRequest.getLocationReferenceFirstRep().getId().equals(ReferenceService.getReference(locations.get(spinner.getSelectedItemPosition())).getId()))
                                s[0] += NewServiceRequestActivity.periodCheck(serviceRequest.getOccurrencePeriod());
                        } else
                            s[0] += NewServiceRequestActivity.periodCheck(serviceRequest.getOccurrencePeriod());

                }
                System.out.println("some - " + serviceRequests.size() + " - " + s[0]);
                handler.post(new Runnable() {
                    public void run() {
                        TextView textView = (TextView) findViewById(R.id.textView);
                        textView.setText(s[0]);
                    }
                });
            }
        });

        thread.start();
    }
}