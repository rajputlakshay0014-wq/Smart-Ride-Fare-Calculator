public class PremiumRide extends RideVehicle {

    public PremiumRide() {
        super("Premium", 25.0);
    }

    @Override
    public double calculateFare(double distanceKm) {
        return distanceKm * getRatePerKm();
    }
}