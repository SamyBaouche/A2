package travel;

import client.Client;
import exceptions.InvalidAccommodationDataException;
import exceptions.InvalidTripDataException;
import interfaces.Billable;
import interfaces.CsvPersistable;
import interfaces.Identifiable;
import service.SmartTravelService;

/**
 * The Trip class represents a travel booking.
 * It contains information about the destination, duration,
 * pricing, associated client, accommodation, and transportation.
 */
public class Trip implements Identifiable, Billable, CsvPersistable, Comparable<Trip> {

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
        setClientAssociated(client);

        if (accommodation == null && transportation == null) {
            throw new InvalidTripDataException("A trip must have at least an accommodation or a transportation.");
        }

        this.accommodation = accommodation;
        this.transportation = transportation;
    }

    /**
     * Default constructor.
     * Creates an empty trip with default values.
     */
    public Trip() throws InvalidTripDataException {
        this.tripID = "T" + tripIdCounter++;
        this.destination = "Unknown";
        setBasePrice(100);
        setDurationInDays(1); // Rule: 1-20 days
    }

    /**
     * Copy constructor.
     * Creates a new Trip based on another Trip object.
     * A new unique ID is generated.
     */

    public Trip(Trip trip) throws InvalidTripDataException {
        if (trip == null) {
            throw new InvalidTripDataException("Cannot copy a null trip.");
        }
        this.tripID = "T" + tripIdCounter++;
        this.destination = trip.getDestination();

        setBasePrice(trip.getBasePrice());
        setDurationInDays(trip.getDurationInDays());
        setClientAssociated(trip.getClientAssociated());

        this.accommodation = trip.getAccommodation();
        this.transportation = trip.getTransportation();
    }

    /**
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Trip(String tripID, String destination, int durationInDays, double basePrice, Client client, Accommodation accommodation, Transportation transportation) throws InvalidTripDataException {
        this.tripID = tripID;
        this.destination = destination;

        setBasePrice(basePrice);
        setDurationInDays(durationInDays);
        setClientAssociated(client);

        // Rule: Accommodation/Transportation are optional, but minimum one required
        if (accommodation == null && transportation == null) {
            throw new InvalidTripDataException("A trip must have at least an accommodation or a transportation.");
        }
        this.accommodation = accommodation;
        this.transportation = transportation;
    }

    /**
     * Calculates the total cost of the trip.
     * Includes base price, accommodation cost, and transportation cost.
     */

    public double calculateTotalCost() {
        double cost = 0;
        cost += basePrice;

        if (accommodation != null) {
            try {
                cost += accommodation.calculateCost(durationInDays);
            } catch (InvalidAccommodationDataException e) {
                System.err.println(e.getMessage());
            }
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

    public int getDurationInDays() {
        return durationInDays;
    }

    @Override
    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public double getTotalCost() {
        return calculateTotalCost();
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

    // Setters with Business Rule Validation
    public void setTripId(String tripID) {
        this.tripID = tripID;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDurationInDays(int durationDays) throws InvalidTripDataException {
        // Rule: 1-20 days
        if (durationDays < 1 || durationDays > 20) {
            throw new InvalidTripDataException("Trip duration must be between 1 and 20 days.");
        }
        this.durationInDays = durationDays;
    }

    public void setBasePrice(double basePrice) throws InvalidTripDataException {
        if (basePrice < 100) {
            throw new InvalidTripDataException("Base price must be positive and over $100.");
        }

        this.basePrice = basePrice;
    }

    public void setClientAssociated(Client clientAssociated) throws InvalidTripDataException {

        if (clientAssociated == null) {
            throw new InvalidTripDataException("A trip must be associated with a valid client.");
        }

        for (Client client: SmartTravelService.getClients()) {
            if (client.getClientId().equals(clientAssociated.getClientId())) {
                this.clientAssociated = client;
                return;
            }
        }

        throw new InvalidTripDataException("Client not found");
    }

    public void setTransportation(Transportation transportation) throws InvalidTripDataException {
        if (this.accommodation == null && transportation == null) {
            throw new InvalidTripDataException("Cannot remove transportation if no accommodation exists.");
        }
        this.transportation = transportation;
    }

    public void setAccommodation(Accommodation accommodation) throws InvalidTripDataException {
        if (this.transportation == null && accommodation == null) {
            throw new InvalidTripDataException("Cannot remove accommodation if no transportation exists.");
        }
        this.accommodation = accommodation;
    }

    public static void updateIdCounter(String lastId) {
        // Strip the "T" and convert to int
        int idNum = Integer.parseInt(lastId.substring(1));

        // Update the tripIdCounter so the next generated id is above the highest from the file
        if (idNum >= tripIdCounter) {
            tripIdCounter = idNum + 1;
        }
    }

    public static Trip fromCsvRow(String csvLine) throws InvalidTripDataException {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new InvalidTripDataException("CSV line is empty.");
        }

        // Required A2 trip CSV format:
        // TripID;ClientID;AccommodationID;TransportationID;Destination;DurationDays;BasePrice
        String[] parts = csvLine.split(";");
        if (parts.length != 7) {
            throw new InvalidTripDataException("Invalid number of fields for Trip. Expected 7, got: " + parts.length);
        }

        String tripId = parts[0].trim();
        String clientId = parts[1].trim();
        String accId = parts[2].trim();
        String transId = parts[3].trim();
        String destination = parts[4].trim();

        int duration;
        try {
            duration = Integer.parseInt(parts[5].trim());
        } catch (NumberFormatException e) {
            throw new InvalidTripDataException("Invalid duration value: " + parts[5].trim());
        }

        double basePrice;
        try {
            basePrice = Double.parseDouble(parts[6].trim());
        } catch (NumberFormatException e) {
            throw new InvalidTripDataException("Invalid base price value: " + parts[6].trim());
        }

        // Lookup linked objects from the service layer (already loaded in memory)
        Client client = null;
        for (Client c : SmartTravelService.getClients()) {
            if (c != null && c.getId().equalsIgnoreCase(clientId)) {
                client = c;
                break;
            }
        }
        if (client == null) {
            throw new InvalidTripDataException("Client not found: " + clientId);
        }

        Accommodation accommodation = null;
        if (!accId.isEmpty()) {
            for (Accommodation a : SmartTravelService.getAccommodations()) {
                if (a != null && a.getId().equalsIgnoreCase(accId)) {
                    accommodation = a;
                    break;
                }
            }
        }

        Transportation transportation = null;
        if (!transId.isEmpty()) {
            for (Transportation t : SmartTravelService.getTransportations()) {
                if (t != null && t.getId().equalsIgnoreCase(transId)) {
                    transportation = t;
                    break;
                }
            }
        }

        // Enforce business rule: at least one of accommodation or transportation must exist
        if (accommodation == null && transportation == null) {
            throw new InvalidTripDataException("A trip must have at least an accommodation or a transportation.");
        }

        Trip trip = new Trip(tripId, destination, duration, basePrice, client, accommodation, transportation);
        updateIdCounter(tripId);
        return trip;
    }


    @Override
    public String toCsvRow() {
        // CSV string matching A2 trips format:
        // TripID;ClientID;AccommodationID;TransportationID;Destination;DurationDays;BasePrice
        String clientId = (clientAssociated != null) ? clientAssociated.getId() : "";
        String accId = (accommodation != null) ? accommodation.getId() : "";
        String transId = (transportation != null) ? transportation.getId() : "";

        return tripID + ";" + clientId + ";" + accId + ";" + transId + ";" +
                destination + ";" + durationInDays + ";" + basePrice;
    }

    @Override
    public String getId() {
        return this.tripID;
    }

    @Override
    public int compareTo(Trip o) {
        return Double.compare(o.getTotalCost(), this.getTotalCost());
    }
}