# Smart Ride Fare Calculator

A Java desktop application that demonstrates Object-Oriented Programming (OOP) concepts through a ride booking and fare calculation system.

## Features

- Create ride bookings
- Calculate fares for Bike, Mini, Sedan, and Premium rides
- View booking history
- Search bookings by ID
- Cancel bookings
- Save and load bookings using a local text file
- Graphical user interface built with Java Swing

## OOP Concepts

- **Encapsulation:** Keeps booking and vehicle data inside classes.
- **Inheritance:** Ride-specific classes extend the common `RideVehicle` class.
- **Abstraction:** Defines common vehicle behavior using an abstract class.
- **Interface:** `FareCalculable` defines the fare calculation contract.
- **Method Overriding:** Each ride type provides its own fare calculation.
- **Runtime Polymorphism:** Fare calculation is invoked through a common vehicle reference.

## Project Structure

- `Booking.java` — booking information and receipt
- `BookingManager.java` — manages bookings and file storage
- `FareCalculable.java` — fare calculation interface
- `RideVehicle.java` — abstract parent class
- `BikeRide.java`, `MiniRide.java`, `SedanRide.java`, `PremiumRide.java` — ride types
- `RideCalculatorGUI.java` — graphical interface
- `Main.java` — console entry point
- `bookings.txt` — sample booking records

## Requirements

- Java JDK installed
- Terminal or a Java IDE

## Run the Application

Open a terminal in the project root and run:

    cd src
    javac *.java
    java RideCalculatorGUI

## Data Storage

Bookings are stored locally in `bookings.txt`. Run the application from the `src` directory so the file is found in the expected location.

## Main Learning Objective

The main focus is runtime polymorphism through method overriding, supported by other object-oriented programming concepts.
