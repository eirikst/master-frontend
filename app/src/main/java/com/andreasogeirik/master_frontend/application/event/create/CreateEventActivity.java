package com.andreasogeirik.master_frontend.application.event.create;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.EventActivity;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.model.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);
        ButterKnife.bind(this);

        presenter = new CreateEventPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, createEventFormView, progressView);
    }

    @OnClick(R.id.create_event_button)
    public void submit() {
        String name = nameView.getText().toString();
        String location = locationView.getText().toString();
        String description = descriptionView.getText().toString();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Date timeCreated = new Date();
        if (startTimePair == null){
            setTimeStartError("Velg et starttidspunkt");
            return;
        }
        int startHour = Integer.valueOf(startTimePair.first);
        int startMinute = Integer.valueOf(startTimePair.second);
        int endHour = 0;
        int endMinute = 0;
        if (endTimePair != null){
            endHour = Integer.valueOf(endTimePair.first);
            endMinute = Integer.valueOf(endTimePair.second);
        }

        Calendar startTime = new GregorianCalendar(year, month, day, startHour, startMinute);
        Calendar endTime = new GregorianCalendar(year, month, day, endHour, endMinute);

        presenter.create(new Event(name, location, description, timeCreated, startTime.getTime(), startTime.getTime(), ""));
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
        Intent i = new Intent(this, EventActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void createEventFailed(String errorMessage) {

    }

    @Override
    public void showProgress() {
        this.progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        this.progressBarManager.showProgress(false);
    }

    @Override
    public void setNameError(String error) {
        nameView.setError(error);
        View focusView = nameView;
        focusView.requestFocus();
    }

    @Override
    public void setLocationError(String error) {
        locationView.setError(error);
        View focusView = locationView;
        focusView.requestFocus();
    }

    @Override
    public void setDescriptionError(String error) {
        descriptionView.setError(error);
        View focusView = descriptionView;
        focusView.requestFocus();
    }

    @Override
    public void setTimeStartError(String error) {
        startTimeText.setText(error);
        startTimeText.setTextColor(Color.parseColor("#ff0033"));
        View focusView = startTimeText;
        focusView.requestFocus();
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
