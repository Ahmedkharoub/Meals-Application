package com.mahdy.mealsapplication.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtil {
    public static String getWeekDayName(int dayIndex) {
        // Create a Calendar instance and set the day of the week
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayIndex);

        // Create a SimpleDateFormat instance to format the day of the week
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());

        // Format the day of the week and return the result
        return sdf.format(calendar.getTime());
    }
}
