
package lk.ijse.fleet;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        FleetManager fleetManager = new FleetManager();
        UserManager userManager = new UserManager();

        userManager.registerUser("admin", "admin123", "Administrator");

        System.out.println("==================================");
        System.out.println(" FLEET RENTAL MANAGEMENT SYSTEM");
        System.out.println("==================================");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        SystemUser loggedInUser = userManager.login(username, password);

        if (loggedInUser == null) {
            System.out.println("Invalid username or password.");
            scanner.close();
            return;
        }

        System.out.println("\nWelcome, " + loggedInUser.getFullName());


        fleetManager.setRentalEventListener(agreement -> {
            System.out.println("Rental created: "
                    + agreement.getRentalId());
        });


        boolean running = true;

        while (running) {
            System.out.println("\n1. Add car");
            System.out.println("2. Add individual customer");
            System.out.println("3. View vehicles");
            System.out.println("4. Rent vehicle");
            System.out.println("5. Return vehicle");
            System.out.println("6. View rentals");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addCar(scanner, fleetManager);
                        break;
                    case "2":
                        addCustomer(scanner, fleetManager);
                        break;
                    case "3":
                        viewVehicles(fleetManager);
                        break;
                    case "4":
                        rentVehicle(scanner, fleetManager);
                        break;
                    case "5":
                        returnVehicle(scanner, fleetManager);
                        break;
                    case "6":
                        viewRentals(fleetManager);
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Program closed.");
    }

    private static void addCar(Scanner scanner,
                               FleetManager manager) {

        System.out.print("Registration number: ");
        String registration = scanner.nextLine();

        System.out.print("Brand: ");
        String brand = scanner.nextLine();

        System.out.print("Model: ");
        String model = scanner.nextLine();

        System.out.print("Daily rate: ");
        double rate = Double.parseDouble(scanner.nextLine());

        System.out.println("Fuel types: PETROL, DIESEL, ELECTRIC, HYBRID");
        System.out.print("Fuel type: ");
        FuelType fuel = FuelType.valueOf(
                scanner.nextLine().trim().toUpperCase());

        manager.addVehicle(
                new Car(registration, brand, model, rate, fuel));

        System.out.println("Car added successfully.");
    }

    private static void addCustomer(Scanner scanner,
                                    FleetManager manager) {

        System.out.print("Customer name: ");
        String name = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        manager.addCustomer(new IndividualCustomer(name, phone, 0));

        System.out.println("Customer added successfully.");
    }

    private static void viewVehicles(FleetManager manager) {
        if (manager.getVehicles().isEmpty()) {
            System.out.println("No vehicles registered.");
            return;
        }

        for (Vehicle vehicle : manager.getVehicles()) {
            System.out.println(vehicle);
        }
    }

    private static void rentVehicle(Scanner scanner,
                                    FleetManager manager)
            throws VehicleNotAvailableException {

        System.out.print("Customer ID: ");
        String customerId = scanner.nextLine();

        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Customer customer = null;
        Vehicle vehicle = null;

        for (Customer c : manager.getCustomers()) {
            if (c.getCustomerId().equalsIgnoreCase(customerId)) {
                customer = c;
                break;
            }
        }

        for (Vehicle v : manager.getVehicles()) {
            if (v.getVehicleId().equalsIgnoreCase(vehicleId)) {
                vehicle = v;
                break;
            }
        }

        if (customer == null || vehicle == null) {
            System.out.println("Customer or vehicle not found.");
            return;
        }

        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());

        System.out.print("End date (YYYY-MM-DD): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());

        RentalAgreement agreement = manager.rentVehicle(
                customer, vehicle, start, end);

        System.out.printf("Total rental cost: Rs. %.2f%n",
                agreement.getTotalCost());
    }

    private static void returnVehicle(Scanner scanner,
                                      FleetManager manager) {

        System.out.print("Rental ID: ");
        String rentalId = scanner.nextLine();

        for (RentalAgreement rental : manager.getRentals()) {
            if (rental.getRentalId().equalsIgnoreCase(rentalId)) {
                manager.returnVehicle(rental);
                System.out.println("Vehicle returned successfully.");
                return;
            }
        }

        System.out.println("Rental not found.");
    }

    private static void viewRentals(FleetManager manager) {
        if (manager.getRentals().isEmpty()) {
            System.out.println("No rentals found.");
            return;
        }

        for (RentalAgreement rental : manager.getRentals()) {
            System.out.println(rental.getSummary());
        }
    }
}
