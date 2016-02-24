package com.andreasogeirik.master_frontend.application.general.interactors;

import android.app.Activity;
import android.content.Intent;

import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

/**
 * Created by eirikstadheim on 24/02/16.
 *
 * General presenter class that handles initialization of UserPreferencesManager singleton. Should
 * be used by presenter that needs access to shared preferences or for logged in activities that
 * needs to check if the user singleton is set.
 */
public abstract class GeneralPresenter {
    private Activity activity;

    public GeneralPresenter(Activity activity) {
        this.activity = activity;

        //setup shared preferences
        UserPreferencesManager.getInstance().initialize(activity);
    }

    public Activity getActivity() {
        return activity;
    }

    /*
     * Checks that user singleton is available. If not, redirect to main view to load user
     */
    public void userAvailable() {
        if(CurrentUser.getInstance().getUser() != null) {
            return;
        }
        //user singleton is not there, redirect to main activity, where user is loaded
        Intent intent = new Intent(activity, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
