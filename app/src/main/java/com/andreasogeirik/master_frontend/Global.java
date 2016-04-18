package com.andreasogeirik.master_frontend;

import android.app.Application;

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

        //TODO: MUSTDO: init this
        //setup handler for uncaught exceptions
        /*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                String stacktrace = "";

                for(StackTraceElement a: e.getStackTrace()) {
                    stacktrace.concat("\n" + a);
                }

                System.err.println("Uncaught exception." + stacktrace);
                e.printStackTrace();
            }
        });*/
    }
}