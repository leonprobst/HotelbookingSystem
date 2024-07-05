package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateSystem {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public static boolean isValidDate(String dateStr) {
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isFutureDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Monat ist 0-basiert
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (year > currentYear) {
            return true;
        } else if (year == currentYear) {
            if (month > currentMonth) {
                return true;
            } else if (month == currentMonth) {
                return day >= currentDay;
            }
        }
        return false;
    }

    public static boolean isDateAfter(int dayFrom, int monthFrom, int yearFrom, int dayTo, int monthTo, int yearTo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearFrom, monthFrom - 1, dayFrom); // Monat ist 0-basiert
        Date dateFrom = calendar.getTime();

        calendar.set(yearTo, monthTo - 1, dayTo);
        Date dateTo = calendar.getTime();

        return dateTo.after(dateFrom);
    }

    public static int getNumberOfNights(String dateFrom, String dateTo) {
        try {
            Date fromDate = sdf.parse(dateFrom);
            Date toDate = sdf.parse(dateTo);
            long diff = toDate.getTime() - fromDate.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24)); // Convert milliseconds to days
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
