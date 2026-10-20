 public abstract class RideVehicle implements FareCalculable {

    private String vehicleName;
    private double ratePerKm;

    public RideVehicle(String vehicleName, double ratePerKm) {
        this.vehicleName = vehicleName;
        this.ratePerKm = ratePerKm;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public double getRatePerKm() {
        return ratePerKm;
    }

    @Override
    public abstract double calculateFare(double distanceKm);
}