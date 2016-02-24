package com.andreasogeirik.master_frontend.application.general.interactors;

import android.app.Activity;
import android.content.Intent;

import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.general.interactors.interfaces.ToolbarPresenter;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

/**
 * Created by eirikstadheim on 24/02/16.
 *
 * General presenter class that handles initialization of UserPreferencesManager singleton. Should
 * be used by presenter that needs access to shared preferences or for logged in activities that
 * needs to check if the user singleton is set.
 */
public class ToolbarPresenterImpl implements ToolbarPresenter {
    private Activity activity;

    public ToolbarPresenterImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void home() {
        Intent intent = new Intent(activity, EventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
