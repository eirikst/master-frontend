package com.andreasogeirik.master_frontend.listener;

import java.util.Calendar;

/**
 * Created by Andreas on 24.02.2016.
 */
public interface OnDateSetListener {
    void onDateSelected(Calendar calendar, Boolean isStartDate);
}
