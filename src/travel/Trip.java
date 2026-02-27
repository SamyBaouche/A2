package travel;

import client.Client;
import exceptions.InvalidTripDataException;

/**
 * The Trip class represents a travel booking.
 * It contains information about the destination, duration,
 * pricing, associated client, accommodation, and transportation.
 */
public class Trip {

    private String tripID;
    private String destination;
    private int durationInDays;
    private double basePrice;
    private Client clientAssociated;

    private Accommodation accommodation;
    private Transportation transportation;

    private static int tripIdCounter = 2001;

    /**
     * Main constructor.
     * Creates a fully initialized Trip object.
     */
    public Trip(String destination, int durationInDays, double basePrice, Client client, Accommodation accommodation, Transportation transportation)
            throws InvalidTripDataException {
        this.tripID = "T" + tripIdCounter++;
        this.destination = destination;

        setBasePrice(basePrice);
        setDurationInDays(durationInDays);

        this.clientAssociated = client;
        this.accommodation = accommodation;
        this.transportation = transportation;
    }

    /**
     * Default constructor.
     * Creates an empty trip with default values.
     */
    public Trip () {
        this.tripID = "T" + tripIdCounter++;
        this.destination = "";
        this.durationInDays = 0;
        this.basePrice = 0;
        this.accommodation = null;
        this.transportation = null;
    }

    /**
     * Copy constructor.
     * Creates a new Trip based on another Trip object.
     * A new unique ID is generated.
     */

    public Trip(Trip trip) {
        this.tripID = "T" + tripIdCounter++;
        this.destination = trip.getDestination();
        this.durationInDays = trip.getDurationInDays();
        this.basePrice = trip.getBasePrice();
        this.clientAssociated = trip.getClientAssociated();
    }

    /**
     * Calculates the total cost of the trip.
     * Includes base price, accommodation cost, and transportation cost.
     */

    public double calculateTotalCost() {
        double cost = 0;
        cost += basePrice;

        if (accommodation != null) {
            cost += accommodation.calculateCost(durationInDays);
        }

        if (transportation != null) {
            cost += transportation.calculateCost(durationInDays);
        }

        return cost;
    }

    /**
     * Returns a formatted string representation of the Trip object.
     */

    @Override
    public String toString() {

        String tripText = "Trip [" +
                "ID='" + tripID + '\'' +
                ", Destination: '" + destination +
                ", DurationInDays: " + durationInDays +
                ", BasePrice: " + basePrice + "$" +
                ", ClientAssociated: " + clientAssociated.getClientId();

        // Add transportation info if not null
        if (transportation != null)
            tripText += ", Transportation: " + transportation.getTransportId();
        // Add transportation info if not null
        if (accommodation != null)
            tripText += ", Accommodation: " + accommodation.getAccommodationId();

        tripText += ']';

        return tripText;
    }

    // Getters and Setters

    public String getTripId() {
        return tripID;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public int getDurationInDays() {
        return durationInDays;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public Client getClientAssociated() {
        return clientAssociated;
    }
    public Transportation getTransportation() {
        return transportation;
    }
    public Accommodation getAccommodation() {
        return accommodation;
    }



    public void setDurationInDays(int durationDays) throws InvalidTripDataException {
        // Rule: 1-20 days
        if (durationDays < 1 || durationDays > 20) {
            throw new InvalidTripDataException("Trip duration must be between 1 and 20 days.");
        }
        this.durationInDays = durationDays;
    }

    public void setBasePrice(double basePrice) throws InvalidTripDataException {
        // Rule: >= $100.00
        if (basePrice < 100.00) {
            throw new InvalidTripDataException("Base price must be at least $100.00.");
        }
        this.basePrice = basePrice;
    }

    public void setClientAssociated(Client clientAssociated) {
        this.clientAssociated = clientAssociated;
    }

    public void setTripId(String tripID) {
        this.tripID = tripID;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }


}
