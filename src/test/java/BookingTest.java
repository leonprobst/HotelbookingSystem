import org.example.Booking;
import org.junit.jupiter.api.Test;
import java.text.ParseException;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    public void testGetNumberOfNights() throws ParseException {
        Booking booking = new Booking("John Doe", "Single", "01-09-2024", "05-09-2024", 1, 600, 1);
        assertEquals(4, booking.getNumberOfNights());
    }

    @Test
    public void testIsValidDateValid() {
        assertTrue(Booking.isValidDate("01-09-2024"));
    }

    @Test
    public void testIsValidDateInvalid() {
        assertFalse(Booking.isValidDate("32-09-2024"));
    }

    // Weitere Tests hier hinzufügen
}