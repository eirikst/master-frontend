package com.andreasogeirik.master_frontend.layout;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.auth.entrance.EntranceActivity;
import com.andreasogeirik.master_frontend.application.event.create.CreateEventActivity;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.search.UserSearchActivity;
import com.andreasogeirik.master_frontend.application.settings.SettingsActivity;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.util.LogoutHandler;

/**
 * Created by Andreas on 29.02.2016.
 */
public class Toolbar {

    public static boolean onOptionsItemSelected(MenuItem item, Context context){
        Intent i;
        switch (item.getItemId()) {
            case R.id.sign_out:
                LogoutHandler.getInstance().logOut();
                i = new Intent(context, EntranceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
                return true;
            case R.id.create_event:
                i = new Intent(context, CreateEventActivity.class);
                context.startActivity(i);
                return true;
            case R.id.settings:
                i = new Intent(context, SettingsActivity.class);
                context.startActivity(i);
                return true;
            case R.id.my_profile:
                i = new Intent(context, ProfileActivity.class);
                i.putExtra("user", CurrentUser.getInstance().getUser());
                context.startActivity(i);
                return true;
            case R.id.search_user:
                i = new Intent(context, UserSearchActivity.class);
                context.startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return false;

        }
    }

}
