package com.andreasogeirik.master_frontend.application.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.view.CustomTextView;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.text_size_small)
    RadioButton smallTextRadio;

    @Bind(R.id.text_size_medium)
    RadioButton mediumTextRadio;

    @Bind(R.id.text_size_large)
    RadioButton largeTextRadio;

    @Bind(R.id.submit_text_size)
    TextView submitTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ButterKnife.bind(this);



        setupToolbar();

        setupView();
    }


    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void setupView() {
        //set checked radio button
        switch(Constants.USER_SET_SIZE){
            case Constants.SMALL:
                smallTextRadio.setChecked(true);
                break;
            case Constants.LARGE:
                largeTextRadio.setChecked(true);
                break;
            case Constants.MEDIUM:
            default:
                mediumTextRadio.setChecked(true);
                break;
        }


        //on changed check
        smallTextRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) Constants.USER_SET_SIZE = Constants.SMALL;
                updateView();
                UserPreferencesManager.getInstance().saveTextSize(Constants.USER_SET_SIZE);
            }
        });
        mediumTextRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) Constants.USER_SET_SIZE = Constants.MEDIUM;
                updateView();
                UserPreferencesManager.getInstance().saveTextSize(Constants.USER_SET_SIZE);
            }
        });
        largeTextRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) Constants.USER_SET_SIZE = Constants.LARGE;
                updateView();
                UserPreferencesManager.getInstance().saveTextSize(Constants.USER_SET_SIZE);
            }
        });
    }

    //update after change in text size
    void updateView() {
        submitTextView.setTextSize(0, 0);
    }
}
