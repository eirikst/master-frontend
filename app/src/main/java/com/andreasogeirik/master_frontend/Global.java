package com.andreasogeirik.master_frontend;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;
import com.squareup.picasso.Picasso;

public class Global extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UserPreferencesManager.getInstance().initialize(this);

        //init picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder
                .build();
        //built.setIndicatorsEnabled(true);
        //built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        //setup handler for uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                String stacktrace = "";

                for(StackTraceElement a: e.getStackTrace()) {
                    stacktrace.concat("\n" + a);
                }

                System.err.println("Uncaught exception." + stacktrace);
                e.printStackTrace();

                //restart();
            }
        });
    }

    //TODO:starter mainactivity to ganger, er vel ikke så viktig...
    private void restart() {
        Intent mStartActivity = new Intent(getApplicationContext(), MainPageActivity.class);
        mStartActivity.putExtra("msg", "En feil skjedde. Vi beklager.");
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService
                (Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}