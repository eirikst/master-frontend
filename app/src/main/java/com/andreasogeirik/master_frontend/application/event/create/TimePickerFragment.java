package com.andreasogeirik.master_frontend.application.event.create;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Pair;
import android.widget.TimePicker;

import com.andreasogeirik.master_frontend.listener.OnTimeSetListener;

import java.util.Calendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    OnTimeSetListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (OnTimeSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTimeSetListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hour;
        int minute;

        Bundle arguments = getArguments();


        if (arguments.get("hour") != null) {
            hour = arguments.getInt("hour");
            minute = arguments.getInt("minute");
        } else {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY) + 1;
            minute = 0;
        }

        return new TimePickerDialog(getActivity(), TimePickerDialog.THEME_HOLO_DARK, this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        boolean isStartTime = true;
        Bundle arguments = getArguments();
        if (arguments.get("time").equals("end")) {
            isStartTime = false;
        }
        callback.onTimeSet(new Pair<>(hourOfDay, minute), isStartTime);
    }
}
