package com.andreasogeirik.master_frontend.application.event.create.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.andreasogeirik.master_frontend.listener.OnDateSetListener;
import com.andreasogeirik.master_frontend.listener.OnTimeSetListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 11.02.2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnDateSetListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (OnDateSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDateSetListener");
        }
    }

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_DARK, this, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 1000);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        boolean isStartDate = true;
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(year, month, day);
        Bundle arguments = getArguments();
        if (arguments.get("date").equals("end")){
            isStartDate = false;
        }
        callback.onDateSet(gregorianCalendar, isStartDate);
    }
}
