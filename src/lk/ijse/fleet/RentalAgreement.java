
package lk.ijse.fleet;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalAgreement implements Reportable {

    private static int nextId = 1001;

    private final String rentalId;
    private final Customer customer;
    private final Vehicle vehicle;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int rentalDays;
    private final double totalCost;
    private boolean returned;

    public RentalAgreement(Customer customer, Vehicle vehicle,
                           LocalDate startDate, LocalDate endDate) {

        if (customer == null || vehicle == null
                || startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Customer, vehicle and dates are required.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }

        this.rentalId = "REN-" + nextId++;
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;

        this.rentalDays = Math.max(
                1, (int) ChronoUnit.DAYS.between(startDate, endDate));

        double baseCost = vehicle.calculateRentalCost(rentalDays);
        this.totalCost = customer.applyDiscount(baseCost);

        this.returned = false;
    }

    public String getRentalId() {
        return rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public boolean isReturned() {
        return returned;
    }

    public void markReturned() {
        returned = true;
    }

    @Override
    public String getReportCategory() {
        return "RENTAL";
    }

    @Override
    public String getSummary() {
        return rentalId + " - " + customer.getFullName()
                + " - " + vehicle.getRegistrationNumber()
                + " - Total: " + totalCost;
    }
}
