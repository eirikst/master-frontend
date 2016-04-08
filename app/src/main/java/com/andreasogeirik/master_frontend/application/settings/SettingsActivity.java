package com.andreasogeirik.master_frontend.application.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
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

    private int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ButterKnife.bind(this);


        textSize = Constants.USER_SET_SIZE;

        setupToolbar();

        setupTextSizePanel();
    }


    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void setupTextSizePanel() {
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

    @Override
    public void onBackPressed() {
        if(Constants.USER_SET_SIZE != textSize) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set dialog message
            alertDialogBuilder
                    .setMessage("Du har endret skriftstørrelse. Denne operasjonen krever at applikasjonen starter på nytt. Starte på nytt?")
                    .setCancelable(false)
                    .setPositiveButton("Nei", new DialogInterface.OnClickListener() {//this is really negative, wanted to change sides
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                            Constants.USER_SET_SIZE = textSize;
                            setupTextSizePanel();
                        }
                    })
                    .setNegativeButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {//this is really positive, wanted to change sides
                            // if this button is clicked, close
                            // current activity
                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

        else {
            super.onBackPressed();
        }
    }
}
