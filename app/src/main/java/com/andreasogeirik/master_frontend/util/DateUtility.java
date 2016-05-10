package com.andreasogeirik.master_frontend.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.Years;

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

    public static String formatTimeDiff(Date date) {

        DateTime d1 = new DateTime(date.getTime());
        DateTime d2 = DateTime.now();

        int yearsDiff = Years.yearsBetween(d1, d2).getYears();

        if(yearsDiff < 1) {

            int daysDiff = Days.daysBetween(d1, d2).getDays();

            if (daysDiff < 1) {
                int hoursDiff = Hours.hoursBetween(d1, d2).getHours();

                if (hoursDiff < 1) {
                    int minutesDiff = Minutes.minutesBetween(d1, d2).getMinutes();

                    if (minutesDiff < 1) {
                        int secondsDiff = Seconds.secondsBetween(d1, d2).getSeconds();

                        if (secondsDiff == 1) {
                            return "1 sekund siden";
                        } else if (secondsDiff >= 0) {
                            return secondsDiff + " sekunder siden";
                        } else {
                            return "Akkurat";

                        }
                    } else if (minutesDiff == 1) {
                        return minutesDiff + " minutt siden";
                    } else {
                        return minutesDiff + " minutter siden";
                    }
                } else {
                    if (hoursDiff == 1) {
                        return hoursDiff + " time siden";
                    } else {
                        return hoursDiff + " timer siden";
                    }
                }
            } else {
                return daysDiff + " dager siden";
            }
        }
        else {
            return yearsDiff + " år siden";
        }
    }
}