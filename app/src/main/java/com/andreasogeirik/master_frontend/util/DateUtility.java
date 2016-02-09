package com.andreasogeirik.master_frontend.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public class DateUtility {
    public static DateUtility dateUtil;
    private SimpleDateFormat dateFormat;

    private DateUtility() {
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public static DateUtility getDateUtility() {
        if(dateUtil == null) {
            dateUtil = new DateUtility();
        }
        return dateUtil;
    }

    public String format(Date date) {
        return dateFormat.format(date);
    }
}
