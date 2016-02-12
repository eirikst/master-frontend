package com.andreasogeirik.master_frontend.application.event.create;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateEventActivity extends AppCompatActivity implements CreateEventView {

    @Bind(R.id.create_event_progress)
    View progressView;
    @Bind(R.id.create_event_form)
    View createEventFormView;

    @Bind(R.id.create_event_name)
    EditText nameView;
    @Bind(R.id.create_event_location)
    EditText locationView;
    @Bind(R.id.create_event_description)
    EditText descriptionView;
    @Bind(R.id.event_start_time_text)
    TextView startTimeText;
    @Bind(R.id.event_end_time_text)
    TextView endTimeText;
    @Bind(R.id.event_date_text)
    TextView dateText;
    @Bind(R.id.create_event_start_time)
    Button startTime;
    @Bind(R.id.create_event_end_time)
    Button endTime;
    @Bind(R.id.create_event_date_button)
    Button date;
    @Bind(R.id.create_event_image_select_button)
    Button selectImage;
    @Bind(R.id.create_event_button)
    Button createEvent;

    CreateEventPresenter presenter;

    private Calendar eventDate;
    private Pair<String, String> startTimePair;
    private Pair<String, String> endTimePair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);
        ButterKnife.bind(this);

        presenter = new CreateEventPresenterImpl(this);
    }

    @OnClick(R.id.create_event_button)
    public void submit() {
        String name = nameView.getText().toString();
        String location = locationView.getText().toString();
        String description = descriptionView.getText().toString();
        Date timeCreated = new Date();

        String eventDate = dateText.getText().toString();
        String[] dateParts = eventDate.split(".");
        String startTime = startTimeText.getText().toString();
        String[] startTimeParts = startTime.split(":");
        String endTime = endTimeText.getText().toString();
        String[] endTimeParts = endTime.split(":");

    }

    @OnClick(R.id.create_event_start_time)
    public void startTime() {

        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("time", "start");
        if (startTimePair != null) {
            bundle.putString("hour", startTimePair.first);
            bundle.putString("minute", startTimePair.second);

        }
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @OnClick(R.id.create_event_end_time)
    public void endTime() {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("time", "end");
        if (endTimePair != null) {
            bundle.putString("hour", endTimePair.first);
            bundle.putString("minute", endTimePair.second);

        }
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @OnClick(R.id.create_event_date_button)
    public void date() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        if (eventDate != null) {
            bundle = new Bundle();
            bundle.putInt("day", eventDate.get(Calendar.DAY_OF_MONTH));
            bundle.putInt("month", eventDate.get(Calendar.MONTH));
            bundle.putInt("year", eventDate.get(Calendar.YEAR));
        }
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void navigateToEventView() {

    }

    @Override
    public void createEventFailed(String errorMessage) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    public void setDate(Calendar eventDate) {

        int day = eventDate.get(Calendar.DAY_OF_MONTH);
        int month = eventDate.get(Calendar.MONTH) + 1;
        int year = eventDate.get(Calendar.YEAR);

        this.dateText.setText("Dato: " + day + "." + month + "." + year);
        this.eventDate = eventDate;
    }

    public void setStartTimePair(Pair<String, String> startTimePair) {
        this.startTimeText.setText("Start tidspunkt: " + startTimePair.first + ":" + startTimePair.second);
        this.startTimePair = startTimePair;
    }

    public void setEndTimePair(Pair<String, String> endTimePair) {
        this.endTimeText.setText("Slutt tidspunkt: " + endTimePair.first + ":" + endTimePair.second);
        this.endTimePair = endTimePair;
    }
}
