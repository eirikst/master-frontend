package com.andreasogeirik.master_frontend.application.event.create;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.model.Event;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateEventActivity extends AppCompatActivity implements CreateEventView {

    // Containers
    @Bind(R.id.create_event_progress)
    View progressView;
    @Bind(R.id.create_event_form)
    View createEventFormView;

    // Input fields
    @Bind(R.id.create_event_name)
    EditText nameView;
    @Bind(R.id.create_event_location)
    EditText locationView;
    @Bind(R.id.create_event_description)
    EditText descriptionView;

    // Dates
    @Bind(R.id.create_event_date_label)
    TextView dateLabel;
    @Bind(R.id.create_event_date_error)
    TextView dateError;
    // Start time
    @Bind(R.id.create_event_start_time_label)
    TextView startTimeLabel;
    @Bind(R.id.create_event_start_time_error)
    TextView startTimeError;
    // End time
    @Bind(R.id.create_event_end_time_label)
    TextView endTimeLabel;

    // Buttons
    @Bind(R.id.create_event_date_button)
    Button dateButton;
    @Bind(R.id.create_event_start_time_button)
    Button startTimeButton;
    @Bind(R.id.create_event_end_time_button)
    Button endTimeButton;

    // Images
    @Bind(R.id.create_event_image_select_button)
    Button selectImage;

    // Submit
    @Bind(R.id.create_event_submit_button)
    Button createEvent;

    CreateEventPresenter presenter;

    private Calendar eventDate;
    private Pair<Integer, Integer> startTimePair;
    private Pair<Integer, Integer> endTimePair;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);
        ButterKnife.bind(this);

        presenter = new CreateEventPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, createEventFormView, progressView);
    }

    @OnClick(R.id.create_event_submit_button)
    public void submit() {
        View current = getCurrentFocus();
        if (current != null) current.clearFocus();
        String name = nameView.getText().toString();
        String location = locationView.getText().toString();
        String description = descriptionView.getText().toString();
        presenter.create(new Event(name, location, description, this.eventDate, this.startTimePair, this.endTimePair, ""));
    }

    @OnClick(R.id.create_event_start_time_button)
    public void startTime() {

        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("time", "start");
        if (startTimePair != null) {
            bundle.putInt("hour", startTimePair.first);
            bundle.putInt("minute", startTimePair.second);

        }
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @OnClick(R.id.create_event_end_time_button)
    public void endTime() {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("time", "end");
        if (endTimePair != null) {
            bundle.putInt("hour", endTimePair.first);
            bundle.putInt("minute", endTimePair.second);

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
    public void setDateError(String error) {
        dateError.setText(error);
        dateError.setVisibility(View.VISIBLE);
        dateLabel.requestFocus();
    }

    @Override
    public void setTimeStartError(String error) {
        startTimeError.setText(error);
        startTimeError.setVisibility(View.VISIBLE);
        startTimeLabel.requestFocus();
    }

    public void setDate(Calendar eventDate) {

        int day = eventDate.get(Calendar.DAY_OF_MONTH);
        int month = eventDate.get(Calendar.MONTH) + 1;
        int year = eventDate.get(Calendar.YEAR);

        this.dateLabel.setText("Dato: " + day + "." + month + "." + year);
        this.eventDate = eventDate;
        this.dateError.setVisibility(View.GONE);
    }

    public void setStartTimePair(Pair<Integer, Integer> startTimePair) {
        this.startTimeLabel.setText("Start tidspunkt: " + startTimePair.first + ":" + startTimePair.second);
        this.startTimePair = startTimePair;
        this.startTimeError.setVisibility(View.GONE);
    }

    public void setEndTimePair(Pair<Integer, Integer> endTimePair) {
        this.endTimeLabel.setText("Slutt tidspunkt: " + endTimePair.first + ":" + endTimePair.second);
        this.endTimePair = endTimePair;
    }
}
