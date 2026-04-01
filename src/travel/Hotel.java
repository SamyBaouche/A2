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
        setStarRating(1);
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
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Hotel(String accommodationId, String name, String location, double pricePerNight, int stars) throws InvalidAccommodationDataException {
        super(accommodationId, name, location, pricePerNight);

        setStarRating(stars);
    }

    /**
     * Copy constructor.
     * Creates a new Hotel object based on another Hotel.
     */
    public Hotel(Hotel other) throws InvalidAccommodationDataException {
        super(other);

        setStarRating(other.starRating);
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) throws InvalidAccommodationDataException{
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
        if (numberOfDays < 1) {
            throw new InvalidAccommodationDataException("Number of nights must be at least 1.");
        }
        return getPricePerNight() * numberOfDays;
    }

    @Override
    public String toCsvRow() {
        // Format: Type; ID; Name; Location; PricePerNight; StarRating
        return "HOTEL;" + getAccommodationId() + ";" + getName() + ";" +
                getLocation() + ";" + getPricePerNight() + ";" + starRating;
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


    public static Hotel fromCsvRow(String csvLine) throws InvalidAccommodationDataException {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new InvalidAccommodationDataException("CSV line is empty.");
        }

        String[] parts = csvLine.split(";");

        // Expecting 6 parts based on toCsvRow: Type, ID, Name, Location, Price, StarRating
        if (parts.length < 6) {
            throw new InvalidAccommodationDataException("Invalid number of fields for Hotel.");
        }

        try {
            // parts[0] is "Hotel", so we skip to parts[1] for the ID
            String id = parts[1].trim();
            String name = parts[2].trim();
            String location = parts[3].trim();
            double price = Double.parseDouble(parts[4].trim());
            int stars = Integer.parseInt(parts[5].trim());

            return new Hotel(id, name, location, price, stars);
        } catch (NumberFormatException e) {
            throw new InvalidAccommodationDataException("Error parsing numerical values for Hotel: " + e.getMessage());
        }
    }
}
