public class SedanRide extends RideVehicle {

    public SedanRide() {
        super("Sedan", 18.0);
    }

    @Override
    public double calculateFare(double distanceKm) {
        return distanceKm * getRatePerKm();
    }
}