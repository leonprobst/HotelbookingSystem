package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * GUI-Anwendung für ein Hotel-Buchungssystem.
 */
public class HotelBookingSystem extends JFrame {

    // GUI-Komponenten
    private DefaultListModel<Booking> bookingListModel;
    private JList<Booking> bookingList;
    private JTextField nameField;
    private JComboBox<String> roomComboBox;
    private JTextField dateFromField;
    private JTextField dateToField;
    private JTextField personsField;
    private JButton addButton;
    private JButton cancelButton;
    private JButton editButton;
    private JButton adminLoginButton;
    private JButton adminLogoutButton;
    private JButton showAvailabilityButton;
    private DateSystem dateSystem;

    // Zustände und Daten für das Hotel-Buchungssystem
    private boolean isAdmin = false;
    private ArrayList<Booking> allBookings = new ArrayList<>();
    private ArrayList<Room> roomAvailability = new ArrayList<>();
    private JPanel buttonPanel;
    private int singlePrice = 150;
    private int doublePrice = 250;
    private int suitePrice = 400;

    /**
     * Konstruktor für das Hotel-Buchungssystem GUI.
     * Initialisiert die GUI-Komponenten und die Startwerte für das System.
     */
    public HotelBookingSystem() {
        // Initialisierung des Hauptfensters
        setTitle("Hotel Booking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialisierung des Datesystems
        dateSystem = new DateSystem();

        // Initialisierung der Zimmerverfügbarkeit
        roomAvailability.add(new Room("Single (1 Person) (" + singlePrice + "€)", 10));
        roomAvailability.add(new Room("Double (2 Persons) ("+ doublePrice + "€)", 6));
        roomAvailability.add(new Room("Suite (4 Persons) ("+ suitePrice +"€)", 4));

        // Initialisierung der Buchungsliste
        bookingListModel = new DefaultListModel<>();
        bookingList = new JList<>(bookingListModel);
        bookingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookingList);

        // Listeners für Auswahl in der Buchungsliste
        bookingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Booking selectedBooking = bookingList.getSelectedValue();
                    if (selectedBooking != null) {
                        nameField.setText(selectedBooking.name);
                        roomComboBox.setSelectedItem(selectedBooking.roomType);
                        dateFromField.setText(selectedBooking.dateFrom);
                        dateToField.setText(selectedBooking.dateTo);
                        personsField.setText(String.valueOf(selectedBooking.persons));
                    }
                }
            }
        });

        // MouseListener für Klicks in der Buchungsliste
        bookingList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = bookingList.locationToIndex(e.getPoint());
                if (index == -1 || !bookingList.getCellBounds(index, index).contains(e.getPoint())) {
                    bookingList.clearSelection();
                    clearFields();
                }
            }
        });

        // Panel für Eingabefelder
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        // Initialisierung der GUI-Komponenten für Eingabe
        JLabel nameLabel = new JLabel("Firstname Lastname:");
        nameField = new JTextField();
        JLabel roomLabel = new JLabel("Room Type:");
        String[] roomTypes = {"Single (1 Person) (" + singlePrice + "€)", "Double (2 Persons) (" + doublePrice + "€)", "Suite (4 Persons) (" + suitePrice + "€)"};
        roomComboBox = new JComboBox<>(roomTypes);
        JLabel dateFromLabel = new JLabel("Date From (dd-mm-yyyy):");
        dateFromField = new JTextField();
        JLabel dateToLabel = new JLabel("Date To (dd-mm-yyyy):");
        dateToField = new JTextField();
        JLabel personsLabel = new JLabel("Number of Persons:");
        personsField = new JTextField();

        // Hinzufügen der GUI-Komponenten zum Eingabe-Panel
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(roomLabel);
        inputPanel.add(roomComboBox);
        inputPanel.add(dateFromLabel);
        inputPanel.add(dateFromField);
        inputPanel.add(dateToLabel);
        inputPanel.add(dateToField);
        inputPanel.add(personsLabel);
        inputPanel.add(personsField);

        // Initialisierung der Buttons und deren ActionListener
        addButton = new JButton("Add Booking");
        cancelButton = new JButton("Cancel Booking");
        editButton = new JButton("Edit Booking");
        adminLoginButton = new JButton("Admin Login");
        adminLogoutButton = new JButton("Admin Logout");
        showAvailabilityButton = new JButton("Show Availability");
        adminLogoutButton.setVisible(false);

        // ActionListener für die Buttons
        addButton.addActionListener(e -> addBooking());
        cancelButton.addActionListener(e -> cancelBooking());
        editButton.addActionListener(e -> editBooking());
        adminLoginButton.addActionListener(e -> adminLogin());
        adminLogoutButton.addActionListener(e -> adminLogout());
        showAvailabilityButton.addActionListener(e -> showAvailability());

        // Panel für die Buttons
        buttonPanel = new JPanel(new GridLayout(0, 6)); // 0 Zeilen, 6 Spalten
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(editButton);
        buttonPanel.add(showAvailabilityButton);
        buttonPanel.add(adminLoginButton);
        buttonPanel.add(adminLogoutButton);

        // Hinzufügen der GUI-Komponenten zum Hauptfenster
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);
    }

    /**
     * Leert die Eingabefelder.
     */
    private void clearFields() {
        nameField.setText("");
        roomComboBox.setSelectedIndex(0);
        dateFromField.setText("");
        dateToField.setText("");
        personsField.setText("");
    }

    /**
     * Validiert die Eingabefelder für eine neue Buchung.
     * @return true, wenn die Eingaben gültig sind, sonst false.
     */
    private boolean validateFields() {
        // Validierungslogik für Name, Datum, Personenanzahl, usw.
        String datePattern = "\\d{2}-\\d{2}-\\d{4}";
        if (nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name and last name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String[] names = nameField.getText().split("\\s+");
        if (names.length != 2) {
            JOptionPane.showMessageDialog(this, "Please enter first name and last name separated by space", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // String firstName = names[0];
        // String lastName = names[1];

        if (dateFromField.getText().isEmpty() || dateToField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all date fields", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Pattern.matches(datePattern, dateFromField.getText()) || !Pattern.matches(datePattern, dateToField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter dates in the format dd-mm-yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Neue Validierung: Überprüfen, ob die eingegebenen Datumswerte gültig sind
        if (!Booking.isValidDate(dateFromField.getText()) || !Booking.isValidDate(dateToField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String[] dateFromParts = dateFromField.getText().split("-");
        String[] dateToParts = dateToField.getText().split("-");

        int dayFrom = Integer.parseInt(dateFromParts[0]);
        int monthFrom = Integer.parseInt(dateFromParts[1]);
        int yearFrom = Integer.parseInt(dateFromParts[2]);

        int dayTo = Integer.parseInt(dateToParts[0]);
        int monthTo = Integer.parseInt(dateToParts[1]);
        int yearTo = Integer.parseInt(dateToParts[2]);

        // Überprüfung, ob das Datum in der Zukunft liegt
        if (!dateSystem.isFutureDate(dayFrom, monthFrom, yearFrom) || !dateSystem.isFutureDate(dayTo, monthTo, yearTo)) {
            JOptionPane.showMessageDialog(this, "Please enter future dates", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Überprüfung, ob dateTo mindestens 1 Tag nach dateFrom liegt
        if (!dateSystem.isDateAfter(dayFrom, monthFrom, yearFrom, dayTo, monthTo, yearTo)) {
            JOptionPane.showMessageDialog(this, "dateTo must be at least 1 day after dateFrom", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int persons;
        try {
            persons = Integer.parseInt(personsField.getText()); // Validierung der Personenanzahl
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for persons", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (persons <= 0) {
            JOptionPane.showMessageDialog(this, "Number of persons must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String roomType = (String) roomComboBox.getSelectedItem();
        if (!isValidRoomCapacity(roomType, persons)) {
            JOptionPane.showMessageDialog(this, "The number of persons exceeds the capacity of the selected room type", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        return true;
    }

    /**
     * Prüft, ob eine gegebene Zimmerkapazität für die Anzahl von Personen ausreicht.
     * @param roomType Der Typ des Zimmers.
     * @param persons Die Anzahl der Personen.
     * @return true, wenn die Zimmerkapazität ausreicht, sonst false.
     */
    private boolean isValidRoomCapacity(String roomType, int persons) {
        // Prüfung der Kapazität für verschiedene Zimmerarten
        int capacity;

        if (roomType.equals("Single (1 Person) (" + singlePrice + "€)")) {
            capacity = 1;
        } else if (roomType.equals("Double (2 Persons) (" + doublePrice + "€)")) {
            capacity = 2;
        } else if (roomType.equals("Suite (4 Persons) (" + suitePrice + "€)")) {
            capacity = 4;
        } else {
            return false;
        }
        return persons <= capacity;
    }

    /**
     * Berechnet die Anzahl der Nächte zwischen zwei Datumsangaben.
     * @param dateFrom Das Startdatum.
     * @param dateTo Das Enddatum.
     * @return Die Anzahl der Nächte zwischen den beiden Datumsangaben.
     */
    public int getNumberOfNights(String dateFrom, String dateTo) {
        // Berechnung der Anzahl der Nächte zwischen zwei Datumsangaben
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
     * Berechnet den Gesamtpreis für eine Buchung basierend auf Zimmertyp und Anzahl der Nächte.
     * @param roomType Der Typ des Zimmers.
     * @param nights Die Anzahl der Nächte.
     * @return Der Gesamtpreis für die Buchung.
     */
    private int calculatePrice(String roomType, int nights) {
        // Berechnung des Gesamtpreises für eine Buchung
        int price = 0;

        for (int i = 0; i < nights; i++) {
            if (roomType.equals("Single (1 Person) (" + singlePrice + "€)")) {
                price += singlePrice;
            } else if (roomType.equals("Double (2 Persons) (" + doublePrice + "€)")) {
                price += doublePrice;
            } else if (roomType.equals("Suite (4 Persons) (" + suitePrice + "€)")) {
                price += suitePrice;
            } else {
                return -1;
            }
        }
        return price;
    }

    /**
     * Fügt eine neue Buchung hinzu.
     */
    private void addBooking() {
        // Logik zum Hinzufügen einer neuen Buchung
        if (!validateFields()) return;

        String[] names = nameField.getText().split("\\s+");
        String firstName = names[0];
        String lastName = names[1];

        String roomType = (String) roomComboBox.getSelectedItem();
        String dateFrom = dateFromField.getText();
        String dateTo = dateToField.getText();
        int index = isAdmin ? 1 : 0;
        int persons = Integer.parseInt(personsField.getText()); // Erhalten der Personenanzahl

        int nights = getNumberOfNights(dateFrom, dateTo);
        int price = calculatePrice(roomType, nights);

        int availableRooms = 0;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date fromDate = sdf.parse(dateFrom);
            Date toDate = sdf.parse(dateTo);
            availableRooms = getAvailableRooms(roomType, fromDate, toDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter dates in the format dd-mm-yyyy", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if(availableRooms > 0) {
            Booking newBooking = new Booking(firstName + " " + lastName, roomType, dateFrom, dateTo, persons, price, index);
            allBookings.add(newBooking);
            bookingListModel.addElement(newBooking);
            clearFields();
            filterBookings(index);
        } else {
            JOptionPane.showMessageDialog(this, "No rooms of this type available anymore.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Storniert eine bestehende Buchung.
     */
    private void cancelBooking() {
        // Logik zum Stornieren einer Buchung
        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex != -1) {
            Booking selectedBooking = bookingList.getSelectedValue();
            allBookings.remove(selectedBooking);
            bookingListModel.remove(selectedIndex);
            clearFields();
        }
    }

    /**
     * Bearbeitet eine bestehende Buchung.
     */
    private void editBooking() {
        // Logik zum Bearbeiten einer Buchung
        if (!validateFields()) return;

        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex != -1) {
            Booking selectedBooking = bookingList.getSelectedValue();
            String[] names = nameField.getText().split("\\s+");
            String firstName = names[0];
            String lastName = names[1];

            selectedBooking.name = firstName + " " + lastName;
            selectedBooking.roomType = (String) roomComboBox.getSelectedItem();
            selectedBooking.dateFrom = dateFromField.getText();
            selectedBooking.dateTo = dateToField.getText();
            selectedBooking.index = isAdmin ? 1 : 0;

            bookingList.repaint();
            clearFields();
        }
    }

    /**
     * Filtert die Buchungsliste nach einem gegebenen index.
     * @param index Der index, nach dem gefiltert werden soll.
     */
    private void filterBookings(int index) {
        bookingListModel.clear();
        for (Booking booking : allBookings) {
            if (isAdmin || booking.index == index) {
                bookingListModel.addElement(booking);
            }
        }
    }

    /**
     * Zeigt die Verfügbarkeit der Zimmer an.
     */
    private void showAvailability() {
        // Logik zum Anzeigen der Zimmerverfügbarkeit
        String roomType = (String) roomComboBox.getSelectedItem();
        String dateFrom = dateFromField.getText();
        String dateTo = dateToField.getText();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date fromDate = sdf.parse(dateFrom);
            Date toDate = sdf.parse(dateTo);

            int availableRooms = getAvailableRooms(roomType, fromDate, toDate);
            if (availableRooms == -1) {
                JOptionPane.showMessageDialog(this, "Invalid date range or room type", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(availableRooms == 0) {
                JOptionPane.showMessageDialog(this, "No rooms of this type available anymore.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Available " + roomType + " rooms: " + availableRooms, "Availability", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter dates in the format dd-mm-yyyy", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gibt eine Liste der verfügbaren Zimmer zurück, basierend auf der aktuellen Buchungssituation.
     * @return Eine Liste der verfügbaren Zimmer.
     */
    private int getAvailableRooms(String roomType, Date fromDate, Date toDate) {
        for (Room room : roomAvailability) {
            if (room.type.equals(roomType)) {
                int available = room.quantity;
                for (Booking booking : allBookings) {
                    if (booking.roomType.equals(roomType)) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date bookingFrom = sdf.parse(booking.dateFrom);
                            Date bookingTo = sdf.parse(booking.dateTo);

                            if (fromDate.before(bookingTo) && toDate.after(bookingFrom)) {
                                available--;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return available;
            }
        }
        return -1; // Room type not found
    }

    /**
     * Wechselt den Admin-Modus ein.
     */
    private void adminLogin() {
        // Logik zum Wechseln in den Admin-Modus
        String password = JOptionPane.showInputDialog("Enter Admin Password:");
        if (password != null && password.equals("admin")) {
            isAdmin = true;
            toggleAdminView();
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect password!");
        }
    }

    /**
     * Beendet den Admin-Modus.
     */
    private void adminLogout() {
        // Logik zum Beenden des Admin-Modus
        isAdmin = false;
        toggleAdminView();
    }

    /**
     * Schaltet zwischen Admin- und Benutzeransicht um.
     * Aktualisiert die Sichtbarkeit der Admin-spezifischen UI-Elemente.
     */
    private void toggleAdminView() {
        if (isAdmin) {
            bookingListModel.clear();
            setTitle("Hotel Booking System - Admin");
            for (Booking booking : allBookings) {
                bookingListModel.addElement(booking);
            }
            adminLoginButton.setVisible(false);
            adminLogoutButton.setVisible(true);
        } else {
            setTitle("Hotel Booking System");
            adminLoginButton.setVisible(true);
            adminLogoutButton.setVisible(false);
            filterBookings(0); //0 is user
        }
    }

    /**
     * Hauptmethode, die das Hotel-Buchungssystem startet.
     * @param args Die Kommandozeilenargumente (werden nicht verwendet).
     */
    public static void main(String[] args) {
        // Startet das Hotel-Buchungssystem
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HotelBookingSystem().setVisible(true);
            }
        });
    }
}
