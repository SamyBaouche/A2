package travel;

import exceptions.InvalidAccommodationDataException;


/**
 * The Hostel class represents a type of Accommodation
 * where guests share beds (dormitory style).
 * It extends the Accommodation class.
 */

public class Hostel extends Accommodation {

    private int sharedBeds;

    /**
     * Default constructor.
     * Calls the parent constructor and initializes sharedBeds to 1.
     */
    public Hostel() throws  InvalidAccommodationDataException {
        super();
        this.sharedBeds = 1;
    }

    /**
     * Parameterized constructor.
     * Initializes hostel information including shared beds.
     */
    public Hostel(String name, String location, double pricePerNight, int sharedBeds) throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        this.sharedBeds = sharedBeds;
    }

    /**
     * Copy constructor.
     * Creates a new Hostel object based on another Hostel.
     */
    public Hostel(Hostel other)  throws InvalidAccommodationDataException {
        super(other);
        this.sharedBeds = other.sharedBeds;
    }

    /**
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Hostel(String accommodationId, String name, String location, double pricePerNight, int sharedBeds) throws InvalidAccommodationDataException {
        super(accommodationId, name, location, pricePerNight);
        this.sharedBeds = sharedBeds;
    }

    // Getters and Setters

    public int getSharedBeds() {
        return sharedBeds;
    }

    public void setSharedBeds(int sharedBeds) {
        this.sharedBeds = sharedBeds;
    }

    @Override
    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        super.setPricePerNight(pricePerNight);

        if (pricePerNight >= 150.0) {
            throw new InvalidAccommodationDataException("Hostel price must be less than $150 per night.");
        }
    }

    /**
     * Returns a formatted string representation of the Hostel.
     */
    @Override
    public String toString() {
        return "Hostel [" + super.toString() +
                ", Shared Beds: " + sharedBeds + "]";
    }

    /**
     * Overrides equals method.
     * Two Hostels are equal if:
     * - Their parent attributes are equal
     * - They have the same number of shared beds
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Hostel other = (Hostel) o;
        return sharedBeds == other.sharedBeds;
    }

    /**
     * Calculates the cost of staying in the hostel.
     * The total price is divided by the number of shared beds,
     * since the room is shared among multiple guests.
     */

    @Override
    public double calculateCost(int numberOfDays) throws InvalidAccommodationDataException {
        if (numberOfDays < 1) {
            throw new InvalidAccommodationDataException("Number of nights must be at least 1");
        }
        return (pricePerNight * numberOfDays) / sharedBeds;
    }

    @Override
    public String toCsvRow() {
        // Format: Type; ID; Name; Location; PricePerNight; SharedBeds
        // We include "Hostel" at the beginning so the file reader knows what object to build
        return "HOSTEL;" + getAccommodationId() + ";" + getName() + ";" +
                getLocation() + ";" + getPricePerNight() + ";" + sharedBeds;
    }

    @Override
    public String getId() {
        // Fulfills the Identifiable interface using the parent's ID
        return getAccommodationId();
    }

    @Override
    public int compareTo(Accommodation o) {
        return Double.compare(o.getPricePerNight(), this.getPricePerNight());
    }

    /**
     * Parses one CSV row into a fully-formed Hostel object[cite: 106, 107].
     */
    public static Hostel fromCsvRow(String csvLine) throws InvalidAccommodationDataException {

        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new InvalidAccommodationDataException("CSV line is empty.");
        }

        String[] parts = csvLine.split(";");

        // Expecting 6 parts based on toCsvRow: Type, ID, Name, Location, Price, SharedBeds
        if (parts.length < 6) {
            throw new InvalidAccommodationDataException("Invalid number of fields for Hostel.");
        }

        try {
            // parts[0] is "Hostel", so we skip to parts[1] for the ID
            String id = parts[1].trim();
            String name = parts[2].trim();
            String location = parts[3].trim();
            double price = Double.parseDouble(parts[4].trim());
            int beds = Integer.parseInt(parts[5].trim());

            return new Hostel(id, name, location, price, beds);
        } catch (NumberFormatException e) {
            throw new InvalidAccommodationDataException("Error parsing numerical values for Hostel: " + e.getMessage());
        }
    }
}

