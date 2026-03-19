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
     * Calls the parent constructor and initializes sharedBeds to 0.
     */
    public Hostel(String sleepInPeace, String vancouver, int i, int pricePerNight, int sharedBeds) throws  InvalidAccommodationDataException {
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
        super.setPricePerNight(pricePerNight); // Enforces the > $0 rule from parent [cite: 49]

        // Rule: Hostel < $150/night [cite: 52]
        if (pricePerNight >= 150.0) {
            throw new InvalidAccommodationDataException("Hostel price must be strictly less than $150 per night.");
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
        // Rule: Number of nights >= 1 [cite: 50]
        if (numberOfDays < 1) {
            throw new InvalidAccommodationDataException("Number of nights must be at least 1");
        }
        return (pricePerNight * numberOfDays) / sharedBeds;
    }
}

