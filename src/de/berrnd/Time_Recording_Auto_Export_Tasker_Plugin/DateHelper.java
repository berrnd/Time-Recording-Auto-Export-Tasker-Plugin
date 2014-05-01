package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static int getDatePart(Date date, int part) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(part);
    }

    public static String toIsoDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static Date fromIsoDate(String isoDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        try {
            date = dateFormat.parse(isoDate);
        }
        catch (ParseException ex) { }

        return date;
    }

}
