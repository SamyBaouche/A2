package travel;

import java.util.Objects;


/**
 * Abstract class representing a general transportation.
 * This class serves as a base for specific types of transportation
 * (e.g., Bus, Train, Flight) and contains common attributes and methods.
 */

public abstract class  Transportation {

    // Unique identifier for each transportation object
    private String transportId;

    // Name of the company providing the transportation
    private String companyName;

    // City from which the transportation departs
    private String departureCity;

    // City where the transportation arrives
    private String arrivalCity;

    // Static counter to generate unique transport IDs
    private static int idCounter = 3001;

    /**
     * Default constructor.
     * Initializes transportId with a unique ID and sets other fields to empty strings.
     */
    public Transportation() {
        // Generate unique ID
        // Increment counter for next object
        this.transportId = "TR" + idCounter;
        idCounter++;

        this.companyName = "";
        this.departureCity = "";
        this.arrivalCity = "";
    }

    /**
     * Constructor with parameters.
     * Initializes the transportation object with company name, departure city, and arrival city.
     * @param companyName Name of the transportation company
     * @param departureCity Departure city
     * @param arrivalCity Arrival city
     */
    public Transportation (String companyName,
                          String departureCity, String arrivalCity) {

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
    public Transportation (Transportation other) {

        // Generate new unique ID
        this.transportId = "TR" + idCounter++;
        this.companyName = other.companyName;
        this.departureCity = other.departureCity;
        this.arrivalCity = other.arrivalCity;
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


    /**
     * Returns a string representation of the transportation object.
     * Includes ID, company, and route information.
     */
    @Override
    public String toString() {
        return "ID: " + transportId +
                ", Company: " + companyName +
                ", Route: " + departureCity + " -> " + arrivalCity;
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
                && Objects.equals(arrivalCity, other.arrivalCity);
    }

    /**
     * Abstract method to calculate the cost of the transportation.
     * Must be implemented by subclasses.
     * @param numberOfDays Number of days the transportation is used (if applicable)
     * @return cost as a double
     */
    public abstract double calculateCost(int numberOfDays);

}
