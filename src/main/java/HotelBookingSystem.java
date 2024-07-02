import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelBookingSystem extends JFrame {

    private ArrayList<Booking> bookings;
    private ArrayList<Room> rooms;
    private DefaultListModel<String> listModel;
    private JList<String> bookingList;
    private JTextField nameField, dateField;
    private JComboBox<String> roomTypeBox;
    private JSpinner personCountSpinner;
    private JPanel customerPanel, adminPanel;
    private boolean isAdmin = false;

    public HotelBookingSystem() {
        bookings = new ArrayList<>();
        rooms = new ArrayList<>();
        initializeRooms();

        setTitle("Hotel Booking Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        // Create customer UI
        createCustomerUI();

        // Create admin UI
        createAdminUI();

        // Add panels to CardLayout
        add(customerPanel, "Customer");
        add(adminPanel, "Admin");


        // Load bookings initially
        loadBookings();

        // Make customerPanel visible
        CardLayout layout = (CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), "Customer");

    }

    private void initializeBookingList(Container container) {
        listModel = new DefaultListModel<>();
        bookingList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(bookingList);

        container.add(scrollPane, BorderLayout.CENTER); // Add scrollPane to the given container

        // Load bookings into the list initially
        loadBookings();
    }

    private void createCustomerUI() {
        customerPanel = new JPanel(new BorderLayout());

        // Initialize booking list and add it to customerPanel
        initializeBookingList(customerPanel);

        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        JLabel nameLabel = new JLabel("Firstname Lastname:");
        JLabel roomTypeLabel = new JLabel("Room Type:");
        JLabel dateLabel = new JLabel("Date (dd-mm-yyyy):");
        JLabel personCountLabel = new JLabel("Person Count:");

        nameField = new JTextField();
        roomTypeBox = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        dateField = new JTextField();
        // Erzeuge den personCountSpinner mit einem Anfangswert von 1 und einer Schrittweite von 1
        personCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, getRoomCapacity((String) roomTypeBox.getSelectedItem()), 1));

        // Füge einen ActionListener zur roomTypeBox hinzu, um das Maximum des Spinners dynamisch zu aktualisieren
        roomTypeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRoomType = (String) roomTypeBox.getSelectedItem();
                int maxCapacity = getRoomCapacity(selectedRoomType);
                SpinnerNumberModel model = (SpinnerNumberModel) personCountSpinner.getModel();

                // Setze das Maximum des SpinnerNumberModel auf die Kapazität des ausgewählten Zimmers
                model.setMaximum(maxCapacity);
                model.setValue(1);
            }
        });


        JButton addButton = new JButton("Add Booking");
        JButton availableRoomsButton = new JButton("Show Available Rooms");
        JButton adminButton = new JButton("Admin Login");
        JButton editBookingButton = new JButton("Edit Booking");
        JButton cancelBookingButton = new JButton("Cancel Booking");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(roomTypeLabel);
        formPanel.add(roomTypeBox);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(personCountLabel);
        formPanel.add(personCountSpinner);
        formPanel.add(addButton);
        formPanel.add(availableRoomsButton);
        formPanel.add(adminButton);
        formPanel.add(editBookingButton);
        formPanel.add(cancelBookingButton);

        customerPanel.add(formPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBooking();
            }
        });

        availableRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAvailableRooms();
            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminLogin();
            }
        });

        editBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBooking();
            }
        });

        cancelBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelBooking();
            }
        });

        // Load bookings initially
        loadBookings();
    }

    private void createAdminUI() {
        adminPanel = new JPanel(new BorderLayout());

        // Initialize booking list for admin panel
        initializeBookingList(adminPanel);


        JPanel formPanel = new JPanel(new GridLayout(4, 2));


        JButton availableRoomsButton = new JButton("Show Available Rooms");
        JButton editBookingButton = new JButton("Edit Booking");
        JButton cancelBookingButton = new JButton("Cancel Booking");
        JButton logoutButton = new JButton("Logout");

        formPanel.add(availableRoomsButton);
        formPanel.add(editBookingButton);
        formPanel.add(cancelBookingButton);
        formPanel.add(logoutButton);



        availableRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAvailableRooms();
            }
        });

        editBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBooking();
            }
        });

        cancelBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelBooking();
            }
        });



        adminPanel.add(formPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutAdmin();
            }
        });
    }

    private void initializeRooms() {
        rooms.add(new Room("Single", 1, 50, 10));
        rooms.add(new Room("Double", 2, 75, 6));
        rooms.add(new Room("Suite", 4, 150, 3));
        rooms.add(new Room("Vincent Raphael Theodor Weingardt Room", 1, 10000, 1));
    }

    private boolean validateDate(String dateString) {
        // Define the regex pattern for dd-mm-yyyy format
        String pattern = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$";

        // Create a Pattern object
        Pattern compiledPattern = Pattern.compile(pattern);

        // Create matcher object
        Matcher matcher = compiledPattern.matcher(dateString);

        // Return if the date string matches the pattern
        return matcher.matches();
    }

    private boolean validateName(String name) {
        // Define the regex pattern for "Firstname Lastname" format
        String pattern = "^[A-Za-z]+\\s[A-Za-z]+$";

        // Create a Pattern object
        Pattern compiledPattern = Pattern.compile(pattern);

        // Create matcher object
        Matcher matcher = compiledPattern.matcher(name);

        // Return if the name string matches the pattern
        return matcher.matches();
    }

    private boolean isDateInFuture(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date inputDate = dateFormat.parse(dateString);
            Date currentDate = new Date();
            return inputDate.compareTo(currentDate) >= 0; // Returns true if inputDate is today or in the future
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getRoomCapacity(String roomType) {
        for (Room room : rooms) {
            if (room.getType().equals(roomType)) {
                return room.getCapacity();
            }
        }
        return 0; // Return value if room is not found
    }

    private int getRoomNumber(String roomType) {
        for (Room room : rooms) {
            if (room.getType().equals(roomType)) {
                return room.getNumber();
            }
        }
        return 0;
    }


    private void addBooking() {
        String name = nameField.getText();
        String roomType = (String) roomTypeBox.getSelectedItem();
        String date = dateField.getText();
        int personCount = (Integer) personCountSpinner.getValue();

        if (name.isEmpty() || roomType.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate name format
        if (!validateName(name)) {
            JOptionPane.showMessageDialog(this, "Please enter the name in 'Firstname Lastname' format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate date format
        if (!validateDate(date)) {
            JOptionPane.showMessageDialog(this, "Please enter the date in dd-mm-yyyy format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if date is in the future
        if (!isDateInFuture(date)) {
            JOptionPane.showMessageDialog(this, "The date cannot be in the past", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if person count exceeds room capacity
        int roomCapacity = getRoomCapacity(roomType);
        if (personCount > roomCapacity) {
            JOptionPane.showMessageDialog(this, "The number of persons exceeds the room capacity of " + roomCapacity, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int number = getRoomNumber(roomType);
        if (number <= 0) {
            JOptionPane.showMessageDialog(this, "The roomtype is not available", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = new Booking(name, roomType, date, personCount);
        bookings.add(booking);
        listModel.addElement(booking.toString());


        nameField.setText("");
        dateField.setText("");

        // Update the booking list
        loadBookings();
    }

    /*private void showAvailableRooms() {
        String roomType = (String) roomTypeBox.getSelectedItem();
        int personCount = (Integer) personCountSpinner.getValue();

        StringBuilder availableRooms = new StringBuilder("Available Rooms:\n");

        for (Room room : rooms) {
            if (room.getType().equals(roomType) && room.getCapacity() >= personCount) {
                availableRooms.append(room).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, availableRooms.toString(), "Available Rooms", JOptionPane.INFORMATION_MESSAGE);
    }*/

    private void showAvailableRooms() {

        StringBuilder availableRooms = new StringBuilder("Available Rooms:\n");

        // Check availability for each room
        for (Room room : rooms) {
            availableRooms.append(room).append("\n");
        }

        JOptionPane.showMessageDialog(this, availableRooms.toString(), "Available Rooms", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAdminLogin() {
        String password = JOptionPane.showInputDialog(this, "Enter Admin Password:");

        if (password.equals("admin")) {
            isAdmin = true;
            CardLayout cl = (CardLayout) (getContentPane().getLayout());
            cl.show(getContentPane(), "Admin");
            loadBookings();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBookings() {
        listModel.clear();
        for (Booking booking : bookings) {
            listModel.addElement(booking.toString());
        }
    }

    private void logoutAdmin() {
        isAdmin = false;
        CardLayout cl = (CardLayout) (getContentPane().getLayout());
        cl.show(getContentPane(), "Customer");
        loadBookings();
    }

    private void editBooking() {
        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to edit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking selectedBooking = bookings.get(selectedIndex);

        JTextField nameField = new JTextField(selectedBooking.getName());
        JTextField dateField = new JTextField(selectedBooking.getDate());
        JComboBox<String> roomTypeBox = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        roomTypeBox.setSelectedItem(selectedBooking.getRoomType());
        JSpinner personCountSpinner = new JSpinner(new SpinnerNumberModel(selectedBooking.getPersonCount(), 1, 10, 1));

        JPanel editPanel = new JPanel(new GridLayout(4, 2));
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Date (dd-mm-yyyy):"));
        editPanel.add(dateField);
        editPanel.add(new JLabel("Room Type:"));
        editPanel.add(roomTypeBox);
        editPanel.add(new JLabel("Person Count:"));
        editPanel.add(personCountSpinner);

        int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Booking", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            selectedBooking.setName(nameField.getText());
            selectedBooking.setDate(dateField.getText());
            selectedBooking.setRoomType((String) roomTypeBox.getSelectedItem());
            selectedBooking.setPersonCount((Integer) personCountSpinner.getValue());

            listModel.set(selectedIndex, selectedBooking.toString());
        }
    }

    private void cancelBooking() {
        int selectedIndex = bookingList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        bookings.remove(selectedIndex);
        listModel.remove(selectedIndex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HotelBookingSystem().setVisible(true);
            }
        });
    }
}

