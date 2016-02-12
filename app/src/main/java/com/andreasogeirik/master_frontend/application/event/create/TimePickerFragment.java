package com.andreasogeirik.master_frontend.application.event.create;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Pair;
import android.widget.TimePicker;


import java.util.Calendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hour;
        int minute;

        Bundle arguments = getArguments();

        if (arguments.get("hour") != null) {
            hour = Integer.parseInt((String) arguments.get("hour"));
            minute = Integer.parseInt((String) arguments.get("minute"));
        } else {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
        return new TimePickerDialog(getActivity(), TimePickerDialog.THEME_HOLO_DARK, this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        CreateEventActivity activity = (CreateEventActivity) getActivity();
        Bundle arguments = getArguments();
        if (arguments.get("time").equals("start")) {
            activity.setStartTimePair(new Pair<>(Integer.toString(hourOfDay), Integer.toString(minute)));
        } else if (arguments.get("time").equals("end")) {
            activity.setEndTimePair(new Pair<>(Integer.toString(hourOfDay), Integer.toString(minute)));
        }
    }
}
