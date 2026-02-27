package travel;

import exceptions.InvalidAccommodationDataException;

import java.util.Objects;

/**
 * The Hotel class represents a type of Accommodation
 * with a star rating.
 * It extends the Accommodation class.
 */
public class Hotel extends Accommodation {

    private int starRating;

    /**
     * Default constructor.
     * Calls parent constructor and initializes starRating to 0.
     */
    public Hotel() {
        super();
        this.starRating = 0;
    }

    /**
     * Parameterized constructor.
     * Initializes hotel name, location, price per night, and star rating.
     */
    public Hotel(String name, String location, double pricePerNight, int starRating) throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        setStarRating(starRating);
    }

    /**
     * Copy constructor.
     * Creates a new Hotel object based on another Hotel.
     */
    public Hotel(Hotel other) {
        super(other);
        this.starRating = other.starRating;
    }

    // Getters and Setters

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int stars) throws InvalidAccommodationDataException {
        // Rule: Stars 1-5
        if (stars < 1 || stars > 5) {
            throw new InvalidAccommodationDataException("Star rating must be between 1 and 5.");
        }
        this.starRating = stars;
    }

    /**
     * Returns a formatted string representation of the Hotel.
     */
    @Override
    public String toString() {
        return "Hotel [" + super.toString() +
                ", Stars: " + starRating + "]";
    }

    /**
     * Overrides equals method.
     * Two Hotels are equal if:
     * - Their parent attributes are equal
     * - They have the same star rating
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Hotel other = (Hotel) o;
        return starRating == other.starRating;
    }

    /**
     * Calculates the cost of staying in the hotel.
     * Total cost = price per night * number of days.
     */
    public double calculateCost(int numberOfDays) throws InvalidAccommodationDataException {
        // Rule: Number of nights >= 1 [cite: 50]
        if (numberOfDays < 1) {
            throw new InvalidAccommodationDataException("Number of nights must be at least 1.");
        }
        return getPricePerNight() * numberOfDays;
    }
}
