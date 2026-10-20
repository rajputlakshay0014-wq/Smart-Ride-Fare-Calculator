import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookingManager manager = new BookingManager();

        boolean running = true;

        while (running) {
            System.out.println("\n==================================");
            System.out.println("      SMART RIDE FARE CALCULATOR");
            System.out.println("==================================");
            System.out.println("1. Create Booking");
            System.out.println("2. View All Bookings");
            System.out.println("3. Search Booking");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Booking Count");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number from 1 to 6.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createBooking(scanner, manager);
                    break;

                case 2:
                    manager.displayAllBookings();
                    break;

                case 3:
                    searchBooking(scanner, manager);
                    break;

                case 4:
                    cancelBooking(scanner, manager);
                    break;

                case 5:
                    System.out.println(
                            "Total bookings: " + manager.getBookingCount());
                    break;

                case 6:
                    running = false;
                    System.out.println("Thank you for using Smart Ride!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    private static void createBooking(
            Scanner scanner, BookingManager manager) {

        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Passenger name cannot be empty.");
            return;
        }

        System.out.println("\n1. Bike     - Rs. 8/km");
        System.out.println("2. Mini     - Rs. 12/km");
        System.out.println("3. Sedan    - Rs. 18/km");
        System.out.println("4. Premium  - Rs. 25/km");
        System.out.print("Choose ride (1-4): ");

        if (!scanner.hasNextInt()) {
            System.out.println("Ride choice must be a number.");
            scanner.nextLine();
            return;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        RideVehicle ride;

        switch (choice) {
            case 1:
                ride = new BikeRide();
                break;
            case 2:
                ride = new MiniRide();
                break;
            case 3:
                ride = new SedanRide();
                break;
            case 4:
                ride = new PremiumRide();
                break;
            default:
                System.out.println("Invalid ride selection.");
                return;
        }

        System.out.print("Enter distance in kilometers: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Distance must be a number.");
            scanner.nextLine();
            return;
        }

        double distance = scanner.nextDouble();
        scanner.nextLine();

        if (!Double.isFinite(distance) || distance <= 0) {
            System.out.println("Distance must be greater than zero.");
            return;
        }

        Booking booking = new Booking(name, ride, distance);

        if (manager.addBooking(booking)) {
            System.out.println("Booking created successfully!");
            booking.displayReceipt();
        } else {
            System.out.println("Booking limit reached.");
        }
    }

    private static void searchBooking(
            Scanner scanner, BookingManager manager) {

        System.out.print("Enter booking ID: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Booking ID must be a number.");
            scanner.nextLine();
            return;
        }

        int id = scanner.nextInt();
        scanner.nextLine();

        Booking booking = manager.findBooking(id);

        if (booking == null) {
            System.out.println("Booking not found.");
        } else {
            booking.displayReceipt();
        }
    }

    private static void cancelBooking(
            Scanner scanner, BookingManager manager) {

        System.out.print("Enter booking ID to cancel: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Booking ID must be a number.");
            scanner.nextLine();
            return;
        }

        int id = scanner.nextInt();
        scanner.nextLine();

        if (manager.cancelBooking(id)) {
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("Booking not found or already cancelled.");
        }
    }
}