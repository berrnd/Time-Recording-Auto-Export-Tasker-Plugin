package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

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
}
