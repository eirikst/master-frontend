package com.andreasogeirik.master_frontend.application.event.create;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.model.Event;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
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
    TextView startDateLabel;
    @Bind(R.id.create_event_date_error)
    TextView startDateError;
    // Start time
    @Bind(R.id.create_event_start_time_label)
    TextView startTimeLabel;
    @Bind(R.id.create_event_start_time_error)
    TextView startTimeError;
    // End time
    @Bind(R.id.create_event_checkbox)
    CheckBox endTimeCheckbox;
    @Bind(R.id.create_event_end_date_button)
    Button endDateButton;
    @Bind(R.id.create_event_end_date_label)
    TextView endDateLabel;
    @Bind(R.id.create_event_end_time_label)
    TextView endTimeLabel;


    // Buttons
    @Bind(R.id.create_event_start_date_button)
    Button startDateButton;
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

    private Calendar startDate;
    private Calendar endDate;

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
        presenter.create(new Event(name, location, description, this.startDate, this.endDate, this.startTimePair, this.endTimePair, ""));
    }

    @OnCheckedChanged(R.id.create_event_checkbox)
    public void endTimeChecked(boolean checked){
        if (checked){
            endDateLabel.setVisibility(View.VISIBLE);
            endDateButton.setVisibility(View.VISIBLE);
            endTimeLabel.setVisibility(View.VISIBLE);
            endTimeButton.setVisibility(View.VISIBLE);
        }
        else{
            endDateLabel.setVisibility(View.GONE);
            endTimeLabel.setVisibility(View.GONE);
            endDateButton.setVisibility(View.GONE);
            endTimeButton.setVisibility(View.GONE);
        }
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

    @OnClick(R.id.create_event_start_date_button)
    public void startDate() {
        setDate(true);
    }

    @OnClick(R.id.create_event_end_date_button)
    public void endDate() {
        setDate(false);
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
    public void setStartDateError(String error) {
        startDateError.setText(error);
        startDateError.setVisibility(View.VISIBLE);
        startDateLabel.requestFocus();
    }

    @Override
    public void setTimeStartError(String error) {
        startTimeError.setText(error);
        startTimeError.setVisibility(View.VISIBLE);
        startTimeLabel.requestFocus();
    }

    @Override
    public void setEndDateError(String error) {
        // TODO: Update this shit
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        toast.show();
    }

    public void setDate(Calendar eventDate, boolean startDate) {

        int day = eventDate.get(Calendar.DAY_OF_MONTH);
        int month = eventDate.get(Calendar.MONTH) + 1;
        int year = eventDate.get(Calendar.YEAR);

        if (startDate){
            this.startDateLabel.setText("Dato start: " + day + "." + month + "." + year);
            this.startDate = eventDate;
            this.startDateError.setVisibility(View.GONE);
        }
        else{
            this.endDateLabel.setText("Dato slutt: " + day + "." + month + "." + year);
            this.endDate = eventDate;
        }
    }

    public void setStartTimePair(Pair<Integer, Integer> startTimePair) {
        this.startTimeLabel.setText("Tidspunkt start: " + startTimePair.first + ":" + startTimePair.second);
        this.startTimePair = startTimePair;
        this.startTimeError.setVisibility(View.GONE);
    }

    public void setEndTimePair(Pair<Integer, Integer> endTimePair) {
        this.endTimeLabel.setText("Tidspunkt slutt: " + endTimePair.first + ":" + endTimePair.second);
        this.endTimePair = endTimePair;
    }

    private void setDate(boolean startDate){
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        Calendar date;
        if (startDate){
            date = this.startDate;
            bundle.putString("date", "start");
        }
        else{
            date = this.endDate;
            bundle.putString("date", "end");
        }
        if (date != null) {
            bundle.putInt("day", date.get(Calendar.DAY_OF_MONTH));
            bundle.putInt("month", date.get(Calendar.MONTH));
            bundle.putInt("year", date.get(Calendar.YEAR));
        }
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
