package org.branlewalk.ui;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeekDays {

    private Calendar calendar;

    public WeekDays(Calendar calendar) {
        this.calendar = calendar;
    }

    public List<Integer> getCurrent() {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -dayOfWeek + 1);
        int sundayMonth = calendar.get(Calendar.MONTH) + 1;
        int sundayDate = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, 6);
        int saturdayMonth = calendar.get(Calendar.MONTH) + 1;
        ArrayList<Integer> days = new ArrayList<>();

        if (sundayMonth == saturdayMonth) {
            for (int i = 0; i < 7; i++) {
                days.add(sundayDate + i);
            }
            return days;
        }
        int lastDayOfPreviousMonth = getLastDayOfMonth(calendar, sundayMonth);
        for (int i = sundayDate; i <= lastDayOfPreviousMonth; i++) {
            days.add(i);
        }
        int previousDays = days.size();
        for (int i = 1; i <= 7 - previousDays; i++) {
            days.add(i);
        }
        return days;
    }

    private int getLastDayOfMonth(Calendar calendar, int month) {
        return YearMonth.of(calendar.get(Calendar.YEAR), month).lengthOfMonth();
    }

    public String getLabel() {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -dayOfWeek + 1);
        int sundayMonth = calendar.get(Calendar.MONTH) + 1;
        int sundayDate = calendar.get(Calendar.DAY_OF_MONTH);
        String sundayLabel = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.US) + " " + sundayDate;
        calendar.add(Calendar.DATE, 6);
        int saturdayMonth = calendar.get(Calendar.MONTH) + 1;
        if (sundayMonth == saturdayMonth) {
            return sundayLabel + " - " + (sundayDate + 6);
        }
        int saturdayDate = 7 - (getLastDayOfMonth(calendar, sundayMonth) - sundayDate + 1);
        String saturdayLabel = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.US) + " " + saturdayDate;

        return sundayLabel + " - " + saturdayLabel;
    }
}
