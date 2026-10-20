
public class Booking {

    private static int nextBookingId = 1001;

    private final int bookingId;
    private final String passengerName;
    private final RideVehicle ride;
    private final double distanceKm;
    private final double fare;
    private boolean cancelled;

    public Booking(String passengerName, RideVehicle ride, double distanceKm) {
        this(nextBookingId++, passengerName, ride, distanceKm, false);
    }

    public Booking(
            int bookingId,
            String passengerName,
            RideVehicle ride,
            double distanceKm,
            boolean cancelled
    ) {
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.ride = ride;
        this.distanceKm = distanceKm;
        this.fare = ride.calculateFare(distanceKm);
        this.cancelled = cancelled;

        if (bookingId >= nextBookingId) {
            nextBookingId = bookingId + 1;
        }
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public RideVehicle getRide() {
        return ride;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getFare() {
        return fare;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancelBooking() {
        cancelled = true;
    }

    public void displayReceipt() {
        System.out.println("\n========== BOOKING RECEIPT ==========");
        System.out.println("Booking ID : " + bookingId);
        System.out.println("Passenger  : " + passengerName);
        System.out.println("Ride Type  : " + ride.getVehicleName());
        System.out.println("Distance   : " + distanceKm + " km");
        System.out.printf("Rate       : Rs. %.2f/km%n", ride.getRatePerKm());
        System.out.printf("Total Fare : Rs. %.2f%n", fare);
        System.out.println("Status     : "
                + (cancelled ? "Cancelled" : "Confirmed"));
        System.out.println("=====================================");
    }
}
