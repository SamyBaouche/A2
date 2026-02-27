package travel;

import exceptions.InvalidTransportDataException;

import java.util.Objects;

/**
 * The Bus class represents bus transportation.
 * It extends the Transportation class and adds
 * bus-specific attributes such as bus company
 * and number of stops.
 */
public class Bus extends Transportation {

    private String busCompany;
    private int numberOfStops;

    private final static double DAILY_PASS_PRICE = 5;
    private final static double DISCOUNT = 0.75;

    /**
     * Default constructor.
     * Initializes parent attributes and sets
     * bus-specific fields to default values.
     */
    public Bus() {
        super();
        this.busCompany = "";
        this.numberOfStops = 0;
    }

    /**
     * Parameterized constructor.
     *
     * @param companyName   Name of the transportation company
     * @param departureCity Departure city
     * @param arrivalCity   Arrival city
     * @param numberOfStops Number of stops during the trip
     */
    public Bus(String companyName, String departureCity,
               String arrivalCity, int numberOfStops) throws InvalidTransportDataException {

        super(companyName, departureCity, arrivalCity);
        setNumberOfStops(numberOfStops);
    }

    /**
     * Copy constructor.
     *
     * @param other Bus object to copy
     */
    public Bus(Bus other) {
        super(other);
        this.busCompany = other.busCompany;
        this.numberOfStops = other.numberOfStops;
    }

    // Getters and Setters

    public String getBusCompany() {
        return busCompany;
    }

    public void setBusCompany(String busCompany) {
        this.busCompany = busCompany;
    }

    public int getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(int numberOfStops) throws InvalidTransportDataException {
        // Business rule: Bus requires >= 1 stop
        if (numberOfStops < 1) {
            throw new InvalidTransportDataException("A Bus requires at least 1 stop.");
        }
        this.numberOfStops = numberOfStops;
    }

    /**
     * Returns a string representation of the Bus object.
     *
     * @return formatted string describing the bus
     */
    @Override
    public String toString() {
        return "Bus [" + super.toString() +
                ", Operator: " + busCompany +
                ", Stops: " + numberOfStops + "]";
    }

    /**
     * Compares this Bus object with another object.
     * Two buses are equal if:
     * - They are of the same class
     * - Parent attributes are equal
     * - busCompany and numberOfStops are equal
     *
     * @param o object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bus other = (Bus) o;
        return numberOfStops == other.numberOfStops && Objects.equals(busCompany, other.busCompany);
    }

    @Override
    public double calculateCost(int numberOfDays) {
        double cost = 0;

        if (numberOfDays > 30) {
            cost = numberOfDays * DAILY_PASS_PRICE * DISCOUNT;
        } else {
            cost = numberOfDays * DAILY_PASS_PRICE;
        }

        return cost;
    }
}
