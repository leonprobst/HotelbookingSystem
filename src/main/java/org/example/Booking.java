package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
    String name;
    String roomType;
    String dateFrom;
    String dateTo;
    int persons;
    int price;
    int index;

    public Booking(String name, String roomType, String dateFrom, String dateTo, int persons, int price, int index) {
        this.name = name;
        this.roomType = roomType;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.persons = persons;
        this.index = index;
        this.price = price;
    }

    @Override
    public String toString() {
        return "[Name: " + name + "] [Roomtype: " + roomType + "] [Date: " + dateFrom + " to " + dateTo + "] [Persons: " + persons + "] [Price: " + price + "] [Index: " + index + "] (" + getNumberOfNights() + " nights)";
    }

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

    // Neue Methode zur Validierung der Datumseingabe
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
