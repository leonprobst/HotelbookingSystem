import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:hotel_booking.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase() {
        try (Connection conn = connect()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS bookings (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " name text NOT NULL,\n"
                + " roomType text NOT NULL,\n"
                + " date text NOT NULL,\n"
                + " personCount integer NOT NULL\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertBooking(Booking booking) {
        String sql = "INSERT INTO bookings(name, roomType, date, personCount) VALUES(?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, booking.getName());
            pstmt.setString(2, booking.getRoomType());
            pstmt.setString(3, booking.getDate());
            pstmt.setInt(4, booking.getPersonCount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Booking> getAllBookings() {
        String sql = "SELECT id, name, roomType, date, personCount FROM bookings";
        ArrayList<Booking> bookings = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getString("name"),
                        rs.getString("roomType"),
                        rs.getString("date"),
                        rs.getInt("personCount")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return bookings;
    }

    public static void updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET name = ?, roomType = ?, date = ?, personCount = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, booking.getName());
            pstmt.setString(2, booking.getRoomType());
            pstmt.setString(3, booking.getDate());
            pstmt.setInt(4, booking.getPersonCount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteBooking(int id) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
