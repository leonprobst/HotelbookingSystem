package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Repräsentiert eine Hotelbuchung mit Name, Zimmertyp, Datum, Personenanzahl und Preis.
 */
public class Booking {
    String name;
    String roomType;
    String dateFrom;
    String dateTo;
    int persons;
    int price;
    int index;

    /**
     * Konstruktor für ein neues Booking-Objekt mit den gegebenen Parametern.
     *
     * @param name     Der Name des Gastes.
     * @param roomType Der Typ des Zimmers.
     * @param dateFrom Das Datum des Check-ins im Format "dd-MM-yyyy".
     * @param dateTo   Das Datum des Check-outs im Format "dd-MM-yyyy".
     * @param persons  Die Anzahl der Personen.
     * @param price    Der Preis für die Buchung.
     * @param index    Der Index der Buchung (0 für Benutzer, 1 für Admin).
     */
    public Booking(String name, String roomType, String dateFrom, String dateTo, int persons, int price, int index) {
        this.name = name;
        this.roomType = roomType;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.persons = persons;
        this.index = index;
        this.price = price;
    }

    /**
     * Gibt eine Zeichenfolgenrepräsentation des Booking-Objekts zurück.
     *
     * @return Eine Zeichenfolgenrepräsentation des Booking-Objekts.
     */
    @Override
    public String toString() {
        return "[Name: " + name + "] [Roomtype: " + roomType + "] [Date: " + dateFrom + " to " + dateTo + "] [Persons: " + persons + "] [Price: " + price + "] [Index: " + index + "] (" + getNumberOfNights() + " nights)";
    }

    /**
     * Berechnet die Anzahl der Nächte zwischen dem Check-in- und Check-out-Datum.
     *
     * @return Die Anzahl der Nächte zwischen dem Check-in- und Check-out-Datum.
     */
    public int getNumberOfNights() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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

    /**
     * Validiert ein gegebenes Datum im Format "dd-MM-yyyy".
     *
     * @param dateStr Das zu validierende Datum als Zeichenfolge.
     * @return true, wenn das Datum gültig ist, ansonsten false.
     */
    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // Setze auf false, um strikte Datumsvalidierung zu erzwingen
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
