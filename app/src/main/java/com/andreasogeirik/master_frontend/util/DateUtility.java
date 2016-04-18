package com.andreasogeirik.master_frontend.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    /*
    This is shit
    todo:fjern

    public static boolean equals(Date lhs, Date rhs) {
        Calendar lhsc = new GregorianCalendar();
        lhsc.setTime(lhs);

        Calendar rhsc = new GregorianCalendar();
        lhsc.setTime(rhs);

        if (lhsc.get(Calendar.YEAR) == rhsc.get(Calendar.YEAR)
                && lhsc.get(Calendar.MONTH) == rhsc.get(Calendar.MONTH)
                && lhsc.get(Calendar.DAY_OF_MONTH) == rhsc.get(Calendar.DAY_OF_MONTH)
                && lhsc.get(Calendar.HOUR_OF_DAY) == rhsc.get(Calendar.HOUR_OF_DAY)
                && lhsc.get(Calendar.MINUTE) == rhsc.get(Calendar.MINUTE)) {
            return true;
        }
        return false;
    }

    public static boolean equals(Calendar lhsc, Calendar rhsc) {
        if (lhsc.get(Calendar.YEAR) == rhsc.get(Calendar.YEAR)
                && lhsc.get(Calendar.MONTH) == rhsc.get(Calendar.MONTH)
                && lhsc.get(Calendar.DAY_OF_MONTH) == rhsc.get(Calendar.DAY_OF_MONTH)
                && lhsc.get(Calendar.HOUR_OF_DAY) == rhsc.get(Calendar.HOUR_OF_DAY)
                && lhsc.get(Calendar.MINUTE) == rhsc.get(Calendar.MINUTE)) {
            return true;
        }
        return false;
    }*/
}