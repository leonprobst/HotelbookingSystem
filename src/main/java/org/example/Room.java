package org.example;

/**
 * Repräsentiert einen Zimmertyp und dessen Verfügbarkeitsmenge.
 */
public class Room {

    String type;
    int quantity;

    /**
     * Konstruktor für ein neues Room-Objekt mit dem gegebenen Typ und der Anzahl.
     *
     * @param type     Der Typ des Zimmers.
     * @param quantity Die Anzahl der verfügbaren Zimmer.
     */
    Room(String type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }
}
