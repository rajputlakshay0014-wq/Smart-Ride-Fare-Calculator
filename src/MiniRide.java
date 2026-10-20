public class MiniRide extends RideVehicle {

    public MiniRide() {
        super("Mini", 12.0);
    }

    @Override
    public double calculateFare(double distanceKm) {
        return distanceKm * getRatePerKm();
    }
}