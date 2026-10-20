public class BikeRide extends RideVehicle {

    public BikeRide() {
        super("Bike", 8.0);
    }

    @Override
    public double calculateFare(double distanceKm) {
        return distanceKm * getRatePerKm();
    }
}