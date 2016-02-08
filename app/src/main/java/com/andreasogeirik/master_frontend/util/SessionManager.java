package com.andreasogeirik.master_frontend.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.andreasogeirik.master_frontend.MainActivity;
import com.andreasogeirik.master_frontend.auth.logout.LogoutTask;
import com.andreasogeirik.master_frontend.event.EventActivity;

/**
 * Created by Andreas on 05.02.2016.
 */
public class SessionManager {

    public static String getCookie(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences("session", activity.MODE_PRIVATE);
        String cookie = preferences.getString("cookie", null);
        return cookie;
    }

    public static void saveCookie(Activity activity, String cookie){
        SharedPreferences preferences = activity.getSharedPreferences("session", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cookie", cookie);
        editor.commit();
    }

    public static void deleteCookie(Activity activity){
        SharedPreferences preferences = activity.getSharedPreferences("session", activity.MODE_PRIVATE);
        preferences.edit().remove("cookie").commit();
    }

    public static void signOut(Context context){
        SharedPreferences preferences = context.getSharedPreferences("session", context.MODE_PRIVATE);
        String cookie = preferences.getString("cookie", null);
        new LogoutTask(Constants.BACKEND_URL, cookie).execute();
        preferences.edit().remove("cookie").commit();
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
}
