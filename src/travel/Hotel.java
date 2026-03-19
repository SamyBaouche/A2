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
    public Hotel() throws  InvalidAccommodationDataException {
        super();
        this.starRating = 1;
    }

    /**
     * Parameterized constructor.
     * Initializes hotel name, location, price per night, and star rating.
     */
    public Hotel(String name, String location, double pricePerNight, int starRating) throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        this.starRating = starRating;
    }

    /**
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Hotel(String accommodationId, String name, String location, double pricePerNight, int stars) throws InvalidAccommodationDataException {
        super(accommodationId, name, location, pricePerNight);
        this.starRating = stars;
    }

    /**
     * Copy constructor.
     * Creates a new Hotel object based on another Hotel.
     */
    public Hotel(Hotel other) throws InvalidAccommodationDataException {
        super(other);
        this.starRating = other.getStarRating();
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) throws InvalidAccommodationDataException{
        // Rule: Stars 1-5
        if (starRating < 1 || starRating > 5) {
            throw new InvalidAccommodationDataException("Star rating must be between 1 and 5.");
        }
        this.starRating = starRating;
    }

    /**
     * Returns a formatted string representation of the Hotel.
     */
    @Override
    public String toString() {
        return "Hotel [" + super.toString() +"]";
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
