package com.andreasogeirik.master_frontend.application.event.create;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 11.02.2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year;
        int month;
        int day;

        Bundle arguments = getArguments();

        if (arguments.get("year") != null) {
            year = (Integer) arguments.get("year");
            month = (Integer) arguments.get("month");
            day = (Integer) arguments.get("day");
        } else {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_DARK, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        CreateEventActivity activity = (CreateEventActivity) getActivity();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(year, month, day);
        activity.setDate(gregorianCalendar);
    }
}
