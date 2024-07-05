package org.example;

import java.util.Date;

/**
 * Klasse zur Verwaltung von Datum und Zeit.
 */
public class DateSystem {

    /**
     * Prüft, ob ein gegebenes Datum in der Zukunft liegt.
     * @param day Der Tag.
     * @param month Der Monat.
     * @param year Das Jahr.
     * @return true, wenn das Datum in der Zukunft liegt, sonst false.
     */
    protected boolean isFutureDate(int day, int month, int year) {
        // Prüfung, ob das Datum in der Zukunft liegt
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int currentYear = calendar.get(java.util.Calendar.YEAR);
        int currentMonth = calendar.get(java.util.Calendar.MONTH) + 1; // Monat ist 0-basiert
        int currentDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);

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

    /**
     * Prüft, ob das Enddatum nach dem Startdatum liegt.
     * @param dayFrom Der Tag des Startdatums.
     * @param monthFrom Der Monat des Startdatums.
     * @param yearFrom Das Jahr des Startdatums.
     * @param dayTo Der Tag des Enddatums.
     * @param monthTo Der Monat des Enddatums.
     * @param yearTo Das Jahr des Enddatums.
     * @return true, wenn das Enddatum nach dem Startdatum liegt, sonst false.
     */
    protected boolean isDateAfter(int dayFrom, int monthFrom, int yearFrom, int dayTo, int monthTo, int yearTo) {
        // Prüfung, ob das Enddatum nach dem Startdatum liegt
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(yearFrom, monthFrom - 1, dayFrom); // Monat ist 0-basiert
        Date dateFrom = calendar.getTime();

        calendar.set(yearTo, monthTo - 1, dayTo);
        Date dateTo = calendar.getTime();

        return dateTo.after(dateFrom);
    }
}
