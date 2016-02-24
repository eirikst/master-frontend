package com.andreasogeirik.master_frontend.application.general.interactors;

import android.app.Activity;
import android.content.Intent;

import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;

/**
 * Created by eirikstadheim on 24/02/16.
 */
public class GeneralPresenter {
    private Activity activity;

    public GeneralPresenter(Activity activity) {
        this.activity = activity;
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
        Intent intent = new Intent(activity, EventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
