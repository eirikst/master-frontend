package com.andreasogeirik.master_frontend.application.event.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.create.fragments.DatePickerFragment;
import com.andreasogeirik.master_frontend.application.event.create.fragments.TimePickerFragment;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventPresenter;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventView;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.listener.OnDateSetListener;
import com.andreasogeirik.master_frontend.listener.OnTimeSetListener;
import com.andreasogeirik.master_frontend.model.Event;

import java.io.FileNotFoundException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EditEventActivity extends AppCompatActivity implements EditEventView, OnDateSetListener, OnTimeSetListener {

    // Containers
    @Bind(R.id.progress)
    View progressView;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.image_container)
    View imageContainer;


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.home)
    Button homeBtn;

    // Input fields
    @Bind(R.id.name)
    EditText nameView;
    @Bind(R.id.location)
    EditText locationView;
    @Bind(R.id.description)
    EditText descriptionView;

    // Date/time
    // Validation
    @Bind(R.id.start_date_error)
    TextView startDateError;
    @Bind(R.id.end_date_error)
    TextView endDateError;

    // Buttons
    @Bind(R.id.start_date_button)
    Button startDateButton;
    @Bind(R.id.start_time_button)
    Button startTimeButton;
    @Bind(R.id.end_date_button)
    Button endDateButton;
    @Bind(R.id.end_time_button)
    Button endTimeButton;

    // Checkbox
    @Bind(R.id.checkbox)
    CheckBox endTimeCheckbox;

    // Image
    @Bind(R.id.image_error)
    TextView imageError;
    @Bind(R.id.image_select_button)
    Button selectImageButton;
    @Bind(R.id.image_view)
    ImageView imageVIew;

    // Submit
    @Bind(R.id.error)
    TextView eventError;
    @Bind(R.id.submit_button)
    Button submitButton;

    EditEventPresenter presenter;
    private ProgressBarManager progressBarManager;

    private static int PICK_IMAGE_REQUEST = 1;
    private static int EDIT_EVENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);
        ButterKnife.bind(this);

        this.progressBarManager = new ProgressBarManager(this, scrollView, progressView);
        setupToolbar();

        try {
            this.presenter = new EditEventPresenterImpl(this, (Event) getIntent().getSerializableExtra("event"));
            this.presenter.setEventAttributes();
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @OnClick(R.id.home)
    public void navigateToHome() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.imageError.setVisibility(View.GONE);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImage = data.getData();
                try {
                    presenter.SampleImage(getContentResolver().openInputStream(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    setImageError("Kunne ikke finne det valgte bildet");
                }
            } else {
                setImageError("Kunne ikke finne det valgte bildet");
            }
        }
    }

    @OnClick(R.id.submit_button)
    public void submit() {
        clearValidationMessages();
        View current = getCurrentFocus();
        if (current != null) current.clearFocus();
        String name = nameView.getText().toString();
        String location = locationView.getText().toString();
        String description = descriptionView.getText().toString();
        this.presenter.editEvent(name, location, description);
    }

    @OnClick(R.id.image_select_button)
    public void selectImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @OnCheckedChanged(R.id.checkbox)
    public void endTimeChecked(boolean checked) {
        if (checked) {
            endDateButton.setVisibility(View.VISIBLE);
            endTimeButton.setVisibility(View.VISIBLE);
        } else {
            endDateButton.setVisibility(View.GONE);
            endTimeButton.setVisibility(View.GONE);
            this.endDateButton.setText("DATO");
            this.endTimeButton.setText("TID");
            this.endDateError.setText("");
            this.endDateError.setVisibility(View.GONE);
            this.presenter.deleteEndTimes();
        }
    }

    @OnClick(R.id.start_time_button)
    public void startTime() {
        this.presenter.setTime(true);
    }

    @OnClick(R.id.end_time_button)
    public void endTime() {
        this.presenter.setTime(false);
    }

    @OnClick(R.id.start_date_button)
    public void startDate() {
        this.presenter.setDate(true);
    }

    @OnClick(R.id.end_date_button)
    public void endDate() {
        this.presenter.setDate(false);
    }

    @Override
    public void navigateToEventView(Event event) {
        Intent i = new Intent(this, EventActivity.class);
        i.putExtra("event", event);
        i.putExtra("requestCode", EDIT_EVENT_REQUEST);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void editEventFailed(String error) {
        eventError.setText(error);
        eventError.setVisibility(View.VISIBLE);
        eventError.requestFocus();
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
        startDateError.requestFocus();
    }

    @Override
    public void setEndDateError(String error) {
        endDateError.setText(error);
        endDateError.setVisibility(View.VISIBLE);
        endDateError.requestFocus();
    }

    @Override
    public void setImageError(String error) {
        imageError.setText(error);
        imageError.setVisibility(View.VISIBLE);
        imageError.requestFocus();
        imageVIew.setImageDrawable(null);
    }

    @Override
    public void setEventAttributes(String name, String location, String description, String startDate, String startTime) {
        this.nameView.setText(name);
        this.locationView.setText(location);
        this.descriptionView.setText(description);
        this.startDateButton.setText(startDate);
        this.startTimeButton.setText(startTime);
    }

    @Override
    public void setEndDate(String endDate, String endTime) {
        this.endTimeCheckbox.setChecked(true);
        this.endDateButton.setVisibility(View.VISIBLE);
        this.endTimeButton.setVisibility(View.VISIBLE);
        this.endDateButton.setText(endDate);
        this.endTimeButton.setText(endTime);
    }

    @Override
    public void setImage(Bitmap bitmap) {
        this.imageContainer.setVisibility(View.VISIBLE);
        this.imageVIew.setImageBitmap(bitmap);
    }

    private void clearValidationMessages() {
        this.startDateError.setVisibility(View.GONE);
        this.endDateError.setVisibility(View.GONE);
        this.imageError.setVisibility(View.GONE);
        this.eventError.setVisibility(View.GONE);
    }

    @Override
    public void onDateSelected(Calendar eventDate, Boolean isStartDate) {
        this.presenter.updateDateModel(eventDate, isStartDate);
    }

    @Override
    public void onTimeSelected(Pair<Integer, Integer> hourMinutePair, Boolean isStartTime) {
        this.presenter.updateTimeModel(hourMinutePair, isStartTime);
    }

    @Override
    public void onDateSet(Bundle bundle) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeSet(Bundle bundle) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void updateStartDateView(int day, int month, int year) {
        this.startDateButton.setText(day + "." + month + "." + year);
        this.startDateError.setVisibility(View.GONE);
    }

    @Override
    public void updateEndDateView(int day, int month, int year) {
        this.endDateButton.setText(day + "." + month + "." + year);
        this.endDateError.setVisibility(View.GONE);
    }

    @Override
    public void updateStartTimeView(int hour, int minute) {
        if (hour < 10 && minute < 10){
            this.startTimeButton.setText("0" + hour + ":0" + minute);
        }
        else if (minute < 10){
            this.startTimeButton.setText(hour + ":0" + minute);

        }
        else if (hour < 10){
            this.startTimeButton.setText("0" + hour + ":" + minute);
        }
        else{
            this.startTimeButton.setText(hour + ":" + minute);
        }
        this.endDateError.setVisibility(View.GONE);
    }

    @Override
    public void updateEndTimeView(int hour, int minute) {
        if (hour < 10 && minute < 10){
            this.endTimeButton.setText("0" + hour + ":0" + minute);
        }
        else if (minute < 10){
            this.endTimeButton.setText(hour + ":0" + minute);
        }
        else if (hour < 10){
            this.endTimeButton.setText("0" + hour + ":" + minute);
        }
        else{
            this.endTimeButton.setText(hour + ":" + minute);
        }
        this.endDateError.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
