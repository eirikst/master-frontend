package com.andreasogeirik.master_frontend.application.event.create;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
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

public class CreateEventActivity extends AppCompatActivity implements CreateEventView, OnDateSetListener, OnTimeSetListener {

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

    // Date/time
    // Validation
    @Bind(R.id.create_event_start_date_error)
    TextView startDateError;
    @Bind(R.id.create_event_start_time_error)
    TextView startTimeError;
    @Bind(R.id.create_event_end_date_error)
    TextView endDateError;
    @Bind(R.id.create_event_end_time_error)
    TextView endTimeError;

    // Buttons
    @Bind(R.id.create_event_start_date_button)
    Button startDateButton;
    @Bind(R.id.create_event_start_time_button)
    Button startTimeButton;
    @Bind(R.id.create_event_end_date_button)
    Button endDateButton;
    @Bind(R.id.create_event_end_time_button)
    Button endTimeButton;

    // Checkbox
    @Bind(R.id.create_event_checkbox)
    CheckBox endTimeCheckbox;

    // Image
    @Bind(R.id.create_event_image_error)
    TextView imageError;
    @Bind(R.id.create_event_image_select_button)
    Button selectImageButton;
    @Bind(R.id.create_event_image_view)
    ImageView imageVIew;

    // Submit
    @Bind(R.id.create_event_error)
    TextView createEventError;
    @Bind(R.id.create_event_submit_button)
    Button createEventButton;

    CreateEventPresenter presenter;

    private Calendar startDate;
    private Calendar endDate;

    private Pair<Integer, Integer> startTimePair;
    private Pair<Integer, Integer> endTimePair;
    private ProgressBarManager progressBarManager;

    private int PICK_IMAGE_REQUEST = 1;
    private String encodedImage;
    private String imagePath;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);
        ButterKnife.bind(this);

        presenter = new CreateEventPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, createEventFormView, progressView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.imageError.setVisibility(View.GONE);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImage = data.getData();
                try {
                    presenter.encodeImage(getContentResolver().openInputStream(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    setImageError("Kunne ikke finne det valgte bildet");
                }
            } else {
                setImageError("Kunne ikke finne det valgte bildet");
            }
        }
    }

    @OnClick(R.id.create_event_submit_button)
    public void submit() {
        clearValidationMessages();
        View current = getCurrentFocus();
        if (current != null) current.clearFocus();
        String name = nameView.getText().toString();
        String location = locationView.getText().toString();
        String description = descriptionView.getText().toString();
        presenter.create(new Event(name, location, description, this.startDate, this.endDate, this.startTimePair, this.endTimePair, ""), this.encodedImage);
    }

    @OnClick(R.id.create_event_image_select_button)
    public void selectImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @OnCheckedChanged(R.id.create_event_checkbox)
    public void endTimeChecked(boolean checked) {
        if (checked) {
            endDateButton.setVisibility(View.VISIBLE);
            endTimeButton.setVisibility(View.VISIBLE);
        } else {
            endDateButton.setVisibility(View.GONE);
            endTimeButton.setVisibility(View.GONE);
            this.endDate = null;
            this.endTimePair = null;
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
        Intent i = new Intent(this, MainPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void createEventFailed(String error) {
        createEventError.setText(error);
        createEventError.setVisibility(View.VISIBLE);
        createEventError.requestFocus();
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
    public void setStartTimeError(String error) {
        startTimeError.setText(error);
        startTimeError.setVisibility(View.VISIBLE);
        startTimeError.requestFocus();
    }

    @Override
    public void setEndDateError(String error) {
        endDateError.setText(error);
        endDateError.setVisibility(View.VISIBLE);
        endDateError.requestFocus();
    }

    @Override
    public void setEndTimeError(String error) {
        endTimeError.setText(error);
        endTimeError.setVisibility(View.VISIBLE);
        endTimeError.requestFocus();
    }

    @Override
    public void setImageError(String error) {
        imageError.setText(error);
        imageError.setVisibility(View.VISIBLE);
        imageError.requestFocus();
        imageVIew.setImageDrawable(null);
    }

    @Override
    public void setImage(Bitmap bitmap, String encodedImage) {
        this.imageVIew.setImageBitmap(bitmap);
        this.encodedImage = encodedImage;
    }

    private void setDate(boolean startDate) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        Calendar date;
        if (startDate) {
            date = this.startDate;
            bundle.putString("date", "start");
        } else {
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

    private void clearValidationMessages() {
        this.startDateError.setVisibility(View.GONE);
        this.startTimeError.setVisibility(View.GONE);
        this.endDateError.setVisibility(View.GONE);
        this.endTimeError.setVisibility(View.GONE);
        this.imageError.setVisibility(View.GONE);
        this.createEventError.setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(Calendar eventDate, Boolean isStartDate) {
        int day = eventDate.get(Calendar.DAY_OF_MONTH);
        int month = eventDate.get(Calendar.MONTH) + 1;
        int year = eventDate.get(Calendar.YEAR);

        if (isStartDate) {
            this.startDateButton.setText("Dato: " + day + "." + month + "." + year);
            this.startDate = eventDate;
            this.startDateError.setVisibility(View.GONE);
        } else {
            this.endDateButton.setText("Dato (slutt): " + day + "." + month + "." + year);
            this.endDate = eventDate;
            this.endDateError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTimeSet(Pair<Integer, Integer> hourMinutePair, Boolean isStartTime) {

        if (isStartTime) {
            if (hourMinutePair.second < 10) {
                this.startTimeButton.setText("Tidspunkt: " + hourMinutePair.first + ":0" + hourMinutePair.second);
            } else {
                this.startTimeButton.setText("Tidspunkt: " + hourMinutePair.first + ":" + hourMinutePair.second);
            }
            this.startTimePair = hourMinutePair;
        } else {
            if (hourMinutePair.second < 10) {
                this.endTimeButton.setText("Tidspunkt (slutt): " + hourMinutePair.first + ":0" + hourMinutePair.second);
            } else {
                this.endTimeButton.setText("Tidspunkt (slutt): " + hourMinutePair.first + ":" + hourMinutePair.second);
            }
            this.endTimePair = hourMinutePair;

        }
        this.startTimeError.setVisibility(View.GONE);
        this.endTimeError.setVisibility(View.GONE);
        this.endDateError.setVisibility(View.GONE);
    }
}
