package com.andreasogeirik.master_frontend.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class DateUtility {
    public static DateUtility instance;
    private SimpleDateFormat dateFormat;

    private DateUtility() {
        dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    }

    public static DateUtility getInstance() {
        if(instance == null) {
            instance = new DateUtility();
        }
        return instance;
    }

    public String format(Date date) {
        return dateFormat.format(date);
    }
}