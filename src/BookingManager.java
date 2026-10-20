
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BookingManager {

    private final Booking[] bookings = new Booking[100];
    private int bookingCount = 0;

    private final Path storageFile = Paths.get("bookings.txt");

    public BookingManager() {
        loadBookings();
    }

    public boolean addBooking(Booking booking) {
        if (booking == null || bookingCount >= bookings.length) {
            return false;
        }

        bookings[bookingCount] = booking;
        bookingCount++;

        saveBookings();
        return true;
    }

    public void displayAllBookings() {
        if (bookingCount == 0) {
            System.out.println("No bookings found.");
            return;
        }

        for (int i = 0; i < bookingCount; i++) {
            bookings[i].displayReceipt();
        }
    }

    public Booking findBooking(int bookingId) {
        for (int i = 0; i < bookingCount; i++) {
            if (bookings[i].getBookingId() == bookingId) {
                return bookings[i];
            }
        }

        return null;
    }

    public boolean cancelBooking(int bookingId) {
        Booking booking = findBooking(bookingId);

        if (booking == null || booking.isCancelled()) {
            return false;
        }

        booking.cancelBooking();
        saveBookings();
        return true;
    }

    public int getBookingCount() {
        return bookingCount;
    }

    public Booking[] getAllBookings() {
        Booking[] result = new Booking[bookingCount];

        for (int i = 0; i < bookingCount; i++) {
            result[i] = bookings[i];
        }

        return result;
    }

    private void saveBookings() {
        List<String> lines = new ArrayList<>();

        for (int i = 0; i < bookingCount; i++) {
            Booking booking = bookings[i];

            String encodedName = Base64.getEncoder().encodeToString(
                    booking.getPassengerName().getBytes(StandardCharsets.UTF_8)
            );

            String rideType = booking.getRide().getVehicleName();

            String line = booking.getBookingId()
                    + "|" + encodedName
                    + "|" + rideType
                    + "|" + booking.getDistanceKm()
                    + "|" + booking.isCancelled();

            lines.add(line);
        }

        try {
            Files.write(
                    storageFile,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            System.err.println("Could not save bookings: " + e.getMessage());
        }
    }

    private void loadBookings() {
        if (!Files.exists(storageFile)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(
                    storageFile, StandardCharsets.UTF_8
            );

            for (String line : lines) {
                if (bookingCount >= bookings.length) {
                    System.err.println("Booking limit reached while loading.");
                    break;
                }

                try {
                    String[] parts = line.split("\\|", -1);

                    if (parts.length != 5) {
                        continue;
                    }

                    int id = Integer.parseInt(parts[0]);

                    String name = new String(
                            Base64.getDecoder().decode(parts[1]),
                            StandardCharsets.UTF_8
                    );

                    String rideType = parts[2];
                    double distance = Double.parseDouble(parts[3]);
                    boolean cancelled = Boolean.parseBoolean(parts[4]);

                    if (id <= 0 || name.trim().isEmpty()
                            || !Double.isFinite(distance) || distance <= 0) {
                        continue;
                    }

                    RideVehicle ride;

                    switch (rideType) {
                        case "Bike":
                            ride = new BikeRide();
                            break;
                        case "Mini":
                            ride = new MiniRide();
                            break;
                        case "Sedan":
                            ride = new SedanRide();
                            break;
                        case "Premium":
                            ride = new PremiumRide();
                            break;
                        default:
                            continue;
                    }

                    bookings[bookingCount] = new Booking(
                            id, name, ride, distance, cancelled
                    );
                    bookingCount++;

                } catch (IllegalArgumentException | NullPointerException e) {
                    System.err.println("Skipping invalid saved booking.");
                }
            }

            System.out.println("Loaded bookings: " + bookingCount);

        } catch (IOException e) {
            System.err.println("Could not load bookings: " + e.getMessage());
        }
    }
}
