package com.andreasogeirik.master_frontend.application.event.edit;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.create.fragments.ActivityTypeFragment;
import com.andreasogeirik.master_frontend.application.event.create.fragments.DatePickerFragment;
import com.andreasogeirik.master_frontend.application.event.create.fragments.TimePickerFragment;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventPresenter;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventView;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.view.CustomScrollView;
import com.andreasogeirik.master_frontend.layout.view.CustomSlider;
import com.andreasogeirik.master_frontend.listener.OnActivityTypeSet;
import com.andreasogeirik.master_frontend.listener.OnDateSetListener;
import com.andreasogeirik.master_frontend.listener.OnTimeSetListener;
import com.andreasogeirik.master_frontend.model.ActivityType;
import com.andreasogeirik.master_frontend.model.Event;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTouch;

public class EditEventActivity extends AppCompatActivity implements EditEventView, OnDateSetListener,
        OnTimeSetListener, CustomSlider.OnValueChangedListener, CustomSlider.OnTouchListener, OnActivityTypeSet {

    // Toolbar
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_header)
    TextView toolbarHeader;
    @Bind(R.id.home)
    Button homeBtn;

    @Bind(R.id.progress)
    View progress;
    @Bind(R.id.scroll_view)
    CustomScrollView scrollView;
    @Bind(R.id.slider)
    CustomSlider slider;
    // Input fields
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.location)
    EditText location;
    @Bind(R.id.description)
    EditText description;
    @Bind(R.id.difficulty)
    TextView difficulty;

    @Bind(R.id.end_date_panel)
    View endDatePanel;

    // Date/time
    // Validation
    @Bind(R.id.start_date_error)
    TextView startDateError;
    @Bind(R.id.end_date_error)
    TextView endDateError;

    // Buttons
    @Bind(R.id.start_date_button)
    EditText startDateBtn;
    @Bind(R.id.start_time_button)
    EditText startTimeBtn;
    @Bind(R.id.end_date_button)
    EditText endDateBtn;
    @Bind(R.id.end_time_button)
    EditText endTimeBtn;
    @Bind(R.id.type)
    EditText typeBtn;

    // Checkbox
    @Bind(R.id.checkbox)
    CheckBox checkbox;

    @Bind(R.id.type_symbol)
    ImageView activityTypeSymbol;

    @Bind(R.id.image_view)
    ImageView imageView;

    // Submit
    @Bind(R.id.error)
    TextView error;
    @Bind(R.id.submit_button)
    AppCompatButton submitBtn;

    EditEventPresenter presenter;
    private ProgressBarManager progressBarManager;

    private static int PICK_IMAGE_REQUEST = 1;
    private static int EDIT_EVENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_event_activity);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.progressBarManager = new ProgressBarManager(this, scrollView, progress);
        setupToolbar();
        this.slider.setOnValueChangedListener(this);
        this.slider.setOnTouchListener(this);
        try {
            this.presenter = new EditEventPresenterImpl(this, (Event) getIntent().getSerializableExtra("event"));
            this.presenter.setEventAttributes();
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }
        this.submitBtn.setText("Endre aktivitet");
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.toolbarHeader.setText("ENDRE AKTIVITET");
    }

    @OnTouch(R.id.name)
    public boolean selectImage(MotionEvent event){
        final int DRAWABLE_RIGHT = 2;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (name.getRight() - name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, PICK_IMAGE_REQUEST);

                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @OnClick(R.id.type)
    public void selectType(){
        showActivityTypeCenter();
    }

    private void showActivityTypeCenter() {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().
                beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("typeDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = ActivityTypeFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putInt("checkedId", this.presenter.getActivityTypeId());
        newFragment.setArguments(bundle);
        newFragment.show(ft, "typeDialog");
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImage = data.getData();
                try {
                    presenter.SampleImage(getContentResolver().openInputStream(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    imageError("Kunne ikke finne det valgte bildet");
                }
            } else {
                imageError("Kunne ikke finne det valgte bildet");
            }
        }
    }

    @OnClick(R.id.submit_button)
    public void submit() {
        clearValidationMessages();
        View current = getCurrentFocus();
        if (current != null) current.clearFocus();
        String name = this.name.getText().toString();
        String location = this.location.getText().toString();
        String description = this.description.getText().toString();
        int difficulty = this.slider.getValue();
        this.presenter.editEvent(name, location, description, difficulty);
    }

    @OnCheckedChanged(R.id.checkbox)
    public void endTimeChecked(boolean checked) {
        if (checked) {
            this.endDatePanel.setVisibility(View.VISIBLE);
        } else {
            this.endDatePanel.setVisibility(View.GONE);
            this.endDateBtn.setText("Dato");
            this.endTimeBtn.setText("Tid");
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
    public void displayError(String error) {
        this.error.setText(error);
        this.error.setVisibility(View.VISIBLE);
        this.error.requestFocus();
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
        this.name.setError(error);
        View focusView = this.name;
        focusView.requestFocus();
    }

    @Override
    public void setLocationError(String error) {
        this.location.setError(error);
        View focusView = this.location;
        focusView.requestFocus();
    }

    @Override
    public void setDescriptionError(String error) {
        this.description.setError(error);
        View focusView = this.description;
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
    public void imageError(String error) {
        Toast.makeText(EditEventActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEventAttributes(String name, String location, String description,
                                   String startDate, String startTime, int difficulty, ActivityType activityType) {
        this.name.setText(name);
        this.location.setText(location);
        this.description.setText(description);
        this.startDateBtn.setText(startDate);
        this.startTimeBtn.setText(startTime);
        int clientDiff = difficulty - 1;
        this.slider.setValue(clientDiff);
        setSliderStyle(clientDiff);
        Resources resources = getResources();



        switch (activityType) {
            case WALK:
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.walk));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_walk_red_400_24dp));
                break;
            case RUN:
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.run));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_run_orange_300_24dp));
                break;
            case BIKE:
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.bike));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_bike_blue_24dp));
                break;
            case SKI:
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.ski));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ski_510));
                break;
            case SWIM:
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.swim));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_swim_226));
                break;
            case GROUP_WORKOUT:
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.group_workout));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_gruppetrening_24dp));
                break;
            default:
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.other));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_question_24dp));
                break;
        }
    }

    @Override
    public void setEndDate(String endDate, String endTime) {
        this.checkbox.setChecked(true);
        this.endDatePanel.setVisibility(View.VISIBLE);
        this.endDateBtn.setText(endDate);
        this.endTimeBtn.setText(endTime);
    }

    @Override
    public void setImage(String imageUri) {
        this.imageView.setVisibility(View.VISIBLE);
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .error(R.drawable.default_event)
                    .into(imageView);
        }
        else {
            Picasso.with(this)
                    .load(R.drawable.default_event)
                    .into(imageView);
        }

    }

    private void clearValidationMessages() {
        this.startDateError.setVisibility(View.GONE);
        this.endDateError.setVisibility(View.GONE);
        this.error.setVisibility(View.GONE);
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
        this.startDateBtn.setText(day + "." + month + "." + year);
        this.startDateError.setVisibility(View.GONE);
    }

    @Override
    public void updateEndDateView(int day, int month, int year) {
        this.endDateBtn.setText(day + "." + month + "." + year);
        this.endDateError.setVisibility(View.GONE);
    }

    @Override
    public void updateStartTimeView(int hour, int minute) {
        if (hour < 10 && minute < 10) {
            this.startTimeBtn.setText("0" + hour + ":0" + minute);
        } else if (minute < 10) {
            this.startTimeBtn.setText(hour + ":0" + minute);

        } else if (hour < 10) {
            this.startTimeBtn.setText("0" + hour + ":" + minute);
        } else {
            this.startTimeBtn.setText(hour + ":" + minute);
        }
        this.endDateError.setVisibility(View.GONE);
    }

    @Override
    public void updateEndTimeView(int hour, int minute) {
        if (hour < 10 && minute < 10) {
            this.endTimeBtn.setText("0" + hour + ":0" + minute);
        } else if (minute < 10) {
            this.endTimeBtn.setText(hour + ":0" + minute);
        } else if (hour < 10) {
            this.endTimeBtn.setText("0" + hour + ":" + minute);
        } else {
            this.endTimeBtn.setText(hour + ":" + minute);
        }
        this.endDateError.setVisibility(View.GONE);
    }

    @Override
    public void updateImage(Bitmap image) {
        this.imageView.setVisibility(View.VISIBLE);
        this.imageView.setImageBitmap(image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onValueChanged(int i) {
        setSliderStyle(i);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.scrollView.setScrollingEnabled(false);
                break;
            case MotionEvent.ACTION_UP:
                this.scrollView.setScrollingEnabled(true);
                break;
        }
        return false;
    }

    private void setSliderStyle(int difficulty){
        switch (difficulty) {
            case 0:
                this.slider.setBackgroundColor(getResources().getColor(R.color.app_blue));
                this.difficulty.setText(getResources().getText(R.string.event_create_difficulty_easy));
                this.difficulty.setTextColor(getResources().getColor(R.color.app_blue));
                break;
            case 1:
                this.slider.setBackgroundColor(getResources().getColor(R.color.app_green));
                this.difficulty.setText(getResources().getText(R.string.event_create_difficulty_medium));
                this.difficulty.setTextColor(getResources().getColor(R.color.app_green));
                break;
            case 2:
                this.slider.setBackgroundColor(getResources().getColor(R.color.app_red));
                this.difficulty.setText(getResources().getText(R.string.event_create_difficulty_hard));
                this.difficulty.setTextColor(getResources().getColor(R.color.app_red));
                break;
        }
    }

    @Override
    public void onActivityTypeSet(int checkedId) {
        Resources resources = getResources();
        switch (checkedId) {
            case R.id.walk:
                this.presenter.updateActivityTypeModel(ActivityType.WALK.getId());
                this.typeBtn.setText(resources.getString(R.string.event_activity_type_label) + getResources().getString(R.string.walk));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_walk_red_400_24dp));
                break;
            case R.id.run:
                this.presenter.updateActivityTypeModel(ActivityType.RUN.getId());
                this.typeBtn.setText(getString(R.string.event_activity_type_label) + resources.getString(R.string.run));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_run_orange_300_24dp));
                break;
            case R.id.bike:
                this.presenter.updateActivityTypeModel(ActivityType.BIKE.getId());
                this.typeBtn.setText(getString(R.string.event_activity_type_label) + resources.getString(R.string.bike));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_directions_bike_blue_24dp));
                break;
            case R.id.swim:
                this.presenter.updateActivityTypeModel(ActivityType.SWIM.getId());
                this.typeBtn.setText(getString(R.string.event_activity_type_label) + resources.getString(R.string.swim));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_swim_226));
                break;
            case R.id.ski:
                this.presenter.updateActivityTypeModel(ActivityType.SKI.getId());
                this.typeBtn.setText(getString(R.string.event_activity_type_label) + resources.getString(R.string.ski));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ski_510));
                break;
            case R.id.group_workout:
                this.presenter.updateActivityTypeModel(ActivityType.GROUP_WORKOUT.getId());
                this.typeBtn.setText(getString(R.string.event_activity_type_label) + resources.getString(R.string.group_workout));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_gruppetrening_24dp));
                break;
            case R.id.other:
                this.presenter.updateActivityTypeModel(ActivityType.OTHER.getId());
                this.typeBtn.setText(getString(R.string.event_activity_type_label) + resources.getString(R.string.other));
                this.activityTypeSymbol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_question_24dp));
                break;
        }
    }
}
