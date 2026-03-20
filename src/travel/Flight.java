package travel;

import exceptions.InvalidTransportDataException;

import java.util.Objects;

/**
 * The Flight class represents an air transportation.
 * It extends the Transportation class and adds flight-specific attributes
 * such as airline name and luggage allowance.
 */

public class Flight extends Transportation {

    // Maximum luggage weight allowed (in kg)
    private double luggageAllowance;

    private final static double BASE_FLIGHT_PRICE = 400;

    /**
     * Default constructor.
     * Initializes empty transportation data and sets luggage allowance to 0.
     */
    public Flight() throws InvalidTransportDataException {
        super();
        setLuggageAllowance(0.0);
    }

    /**
     * Parameterized constructor.
     *
     * @param companyName      Name of the airline company
     * @param departureCity    City of departure
     * @param arrivalCity      City of arrival
     * @param luggageAllowance Allowed luggage weight (kg)
     */
    public Flight(String companyName, String departureCity,
                  String arrivalCity, double luggageAllowance) throws InvalidTransportDataException {

        super(companyName, departureCity, arrivalCity);
        setLuggageAllowance(luggageAllowance);
    }

    /**
     * Copy constructor.
     *
     * @param other Flight object to copy
     */
    public Flight(Flight other) throws InvalidTransportDataException {
        super(other);
        setLuggageAllowance(other.luggageAllowance);
    }

    /**
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Flight(String transportId, String companyName, String departureCity, String arrivalCity, double price, double luggageAllowance) throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, price);
        setLuggageAllowance(luggageAllowance);
    }

    public double getLuggageAllowance() {
        return luggageAllowance;
    }

    public void setLuggageAllowance(double luggageAllowance) throws InvalidTransportDataException {
        if (luggageAllowance < 0) {
            throw new InvalidTransportDataException("Luggage allowance cannot be negative.");
        }
        this.luggageAllowance = luggageAllowance;
    }

    public double getBasePrice() {
        return BASE_FLIGHT_PRICE;
    }

    /**
     * Returns a string representation of the Flight object.
     *
     * @return formatted string describing the flight
     */
    @Override
    public String toString() {
        return "Flight [" + super.toString() +
                ", Luggage: " + luggageAllowance + "kg]";
    }

    /**
     * Compares this Flight object to another object for equality.
     * Two flights are equal if:
     * - They are of the same class
     * - Their parent attributes are equal
     * - Their airline name and luggage allowance are equal
     *
     * @param o object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        if (!super.equals(o)) return false;

        Flight other = (Flight) o;
        return Double.compare(luggageAllowance, other.luggageAllowance) == 0;
    }


    @Override
    public double calculateCost(int numberOfDays) {
        //Implemented luggageAllowance into the cost calcul
        return BASE_FLIGHT_PRICE + (luggageAllowance * 1);
    }
}
