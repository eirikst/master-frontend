package com.andreasogeirik.master_frontend.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class DateUtility {
    private static DateFormat fullFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    //dd.HH.yyyy HH:mm
    public static String formatFull(Date date) {
        return fullFormat.format(date);
    }

    //dd.HH.yyyy
    public static String format(Date date) {
        return format.format(date);
    }

    // HH:mm
    public static String formatTime(Date date){
        return timeFormat.format(date);
    }
}