import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HotelBookingSystem extends JFrame {

    private ArrayList<Booking> bookings;
    private ArrayList<Room> rooms;
    private DefaultListModel<String> listModel;
    private JList<String> bookingList;
    private JTextField nameField, dateField;
    private JPasswordField passwordField;
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
    }

    private void createCustomerUI() {
        customerPanel = new JPanel(new BorderLayout());

        listModel = new DefaultListModel<>();
        bookingList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(bookingList);

        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        JLabel nameLabel = new JLabel("Name:");
        JLabel roomTypeLabel = new JLabel("Room Type:");
        JLabel dateLabel = new JLabel("Date (dd-mm-yyyy):");
        JLabel personCountLabel = new JLabel("Person Count:");

        nameField = new JTextField();
        roomTypeBox = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        dateField = new JTextField();
        personCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

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

        customerPanel.add(scrollPane, BorderLayout.CENTER);
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
    }

    private void createAdminUI() {
        adminPanel = new JPanel(new BorderLayout());

        listModel = new DefaultListModel<>();
        bookingList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(bookingList);

        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        JButton logoutButton = new JButton("Logout");

        formPanel.add(logoutButton);

        adminPanel.add(scrollPane, BorderLayout.CENTER);
        adminPanel.add(formPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutAdmin();
            }
        });
    }

    private void initializeRooms() {
        rooms.add(new Room("Single", 1, 50));
        rooms.add(new Room("Double", 2, 75));
        rooms.add(new Room("Suite", 4, 150));
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

        Booking booking = new Booking(name, roomType, date, personCount);
        bookings.add(booking);
        listModel.addElement(booking.toString());

        nameField.setText("");
        dateField.setText("");
    }

    private void showAvailableRooms() {
        String roomType = (String) roomTypeBox.getSelectedItem();
        int personCount = (Integer) personCountSpinner.getValue();
        String date = dateField.getText();

        StringBuilder availableRooms = new StringBuilder("Available Rooms:\n");

        for (Room room : rooms) {
            if (room.getType().equals(roomType) && room.getCapacity() >= personCount) {
                availableRooms.append(room).append("\n");
            }
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

