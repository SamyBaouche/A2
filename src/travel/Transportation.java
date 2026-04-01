package travel;

import java.util.Objects;
import exceptions.InvalidTransportDataException;
import interfaces.CsvPersistable;
import interfaces.Identifiable;


/**
 * Abstract class representing a general transportation.
 * This class serves as a base for specific types of transportation
 * (e.g., Bus, Train, Flight) and contains common attributes and methods.
 */

public abstract class Transportation implements Identifiable, CsvPersistable, Comparable<Transportation> {

    protected String transportId;
    protected String companyName;
    protected String departureCity;
    protected String arrivalCity;
    protected double price; // Added to match the CSV data structure

    // Static counter to generate unique transport IDs
    private static int idCounter = 3001;

    /**
     * Default constructor.
     * Initializes transportId with a unique ID and sets other fields to empty strings.
     */
    public Transportation() throws InvalidTransportDataException {
        // Generate unique ID
        // Increment counter for next object
        this.transportId = "TR" + idCounter;
        idCounter++;

        this.companyName = "Unknown";
        this.departureCity = "Unknown";
        this.arrivalCity = "Unknown";
        setPrice(0.0);
    }

    /**
     * Constructor with parameters.
     * Initializes the transportation object with company name, departure city, and arrival city.
     * @param companyName Name of the transportation company
     * @param departureCity Departure city
     * @param arrivalCity Arrival city
     */
    public Transportation (String companyName,
                          String departureCity, String arrivalCity) throws InvalidTransportDataException {

        // Generate unique ID
        this.transportId = "TR" + idCounter;
        idCounter++;

        this.companyName = companyName;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
    }

    /**
     * Copy constructor.
     * Creates a new transportation object by copying the details from another object.
     * @param other Another Transportation object
     */
    public Transportation (Transportation other) throws InvalidTransportDataException {

        if (other == null) {
            throw new InvalidTransportDataException("Cannot copy a null transportation object.");
        }

        // Generate new unique ID
        this.transportId = "TR" + idCounter++;
        this.companyName = other.companyName;
        this.departureCity = other.departureCity;
        this.arrivalCity = other.arrivalCity;
        setPrice(other.price);
    }

    /**
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Transportation(String transportId, String companyName, String departureCity, String arrivalCity, double price) throws InvalidTransportDataException {
        this.transportId = transportId;

        this.companyName = companyName;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        setPrice(price);
    }

    // Getters and Setters

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidTransportDataException {
        if (price < 0) {
            throw new InvalidTransportDataException("Price cannot be negative.");
        }
        this.price = price;
    }


    /**
     * Returns a string representation of the transportation object.
     * Includes ID, company, and route information.
     */
    @Override
    public String toString() {
        return "ID: " + transportId +
                ", Company: " + companyName +
                ", Route: " + departureCity + " -> " + arrivalCity +
                ", Price: $" + price;
    }

    /**
     * Compares two Transportation objects for equality.
     * Two objects are considered equal if they have the same company, departure, and arrival cities.
     * @param o Object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transportation other = (Transportation) o;
        return  Objects.equals(companyName, other.companyName)
                && Objects.equals(departureCity, other.departureCity)
                && Objects.equals(arrivalCity, other.arrivalCity) && Double.compare(other.price, price) == 0;
    }

    /**
     * Abstract method to calculate the cost of the transportation.
     * Must be implemented by subclasses.
     * @param numberOfDays Number of days the transportation is used (if applicable)
     * @return cost as a double
     */
    public abstract double calculateCost(int numberOfDays);

    /**
     * Natural sort order: base price (descending) — premium transport first.
     */
    @Override
    public int compareTo(Transportation o) {
        if (o == null) {
            return -1;
        }
        return Double.compare(o.getPrice(), this.getPrice());
    }

    @Override
    public String getId() {
        return transportId;
    }

}
