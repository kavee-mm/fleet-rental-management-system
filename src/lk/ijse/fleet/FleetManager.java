
package lk.ijse.fleet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FleetManager {

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<RentalAgreement> rentals = new ArrayList<>();
    private RentalEventListener rentalEventListener;

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            vehicles.add(vehicle);
        }
    }

    public void addCustomer(Customer customer) {
        if (customer != null) {
            customers.add(customer);
        }
    }

    public void setRentalEventListener(
            RentalEventListener listener) {
        this.rentalEventListener = listener;
    }

    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public List<RentalAgreement> getRentals() {
        return new ArrayList<>(rentals);
    }

    public RentalAgreement rentVehicle(
            Customer customer, Vehicle vehicle,
            LocalDate startDate, LocalDate endDate)
            throws VehicleNotAvailableException {

        if (vehicle == null || !vehicle.isAvailable()) {
            throw new VehicleNotAvailableException(
                    "Vehicle is not available for rental.");
        }

        RentalAgreement agreement = new RentalAgreement(
                customer, vehicle, startDate, endDate);

        vehicle.setStatus(RentalStatus.RENTED);
        rentals.add(agreement);


        if (rentalEventListener != null) {
            rentalEventListener.onRentalCreated(agreement);
        }


        return agreement;
    }



    public void returnVehicle(RentalAgreement agreement) {
        if (agreement == null || agreement.isReturned()) {
            return;
        }

        agreement.markReturned();

        Vehicle vehicle = agreement.getVehicle();

        // Check whether another active rental exists for this vehicle.
        boolean hasActiveRental = false;

        for (RentalAgreement rental : rentals) {
            if (rental != agreement
                    && !rental.isReturned()
                    && rental.getVehicle() == vehicle) {
                hasActiveRental = true;
                break;
            }
        }

        // Make the vehicle available only if no active rental exists.
        if (!hasActiveRental) {
            vehicle.setStatus(RentalStatus.AVAILABLE);
        }
    }


}
