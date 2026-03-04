package travel;

import exceptions.InvalidAccommodationDataException;

import java.util.Objects;

public abstract class Accommodation {

    protected String accommodationId;
    protected String name;
    protected String location;
    protected double pricePerNight;
    protected int stars;

    private static int idCounter = 4001;

    public Accommodation()  throws InvalidAccommodationDataException {
        this.accommodationId = "A" + idCounter;
        idCounter++;

        this.name = "Unknown";
        this.location = "Unknown";
        setPricePerNight(1.0);
        setStars(1);
    }

    public Accommodation(String name, String location, double pricePerNight, int stars) throws InvalidAccommodationDataException{

        this.accommodationId = "A" + idCounter;
        idCounter++;

        this.name = name;
        this.location = location;
        setPricePerNight(pricePerNight);
        setStars(stars);
    }

    public Accommodation(Accommodation other) throws InvalidAccommodationDataException {
        if (other == null) {
            throw new InvalidAccommodationDataException("Can't copy a null accommodation");
        }
        this.accommodationId = "A" + idCounter;
        idCounter++;

        this.name = other.name;
        this.location = other.location;
        setPricePerNight(other.pricePerNight);
        setStars(other.stars);
    }

    /**
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Accommodation(String accommodationId, String name, String location, double pricePerNight, int stars) throws InvalidAccommodationDataException {
        this.accommodationId = accommodationId;

        this.name = name;
        this.location = location;
        setPricePerNight(pricePerNight);
        setStars(stars);
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(String accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getStars() {
        return stars;
    }

    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        // Rule: Price/night > $0
        if (pricePerNight <= 0) {
            throw new InvalidAccommodationDataException("Price per night must be greater than $0.");
        }
        this.pricePerNight = pricePerNight;
    }

    public void setStars(int stars) throws InvalidAccommodationDataException {
        // Rule: Stars 1-5
        if (stars < 1 || stars > 5) {
            throw new InvalidAccommodationDataException("Star rating must be between 1 and 5.");
        }
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "ID: " + accommodationId +
                ", Name: " + name +
                ", Location: " + location +
                ", Rate: $" + pricePerNight + "/night" +
                ", Stars: " + stars;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Accommodation other = (Accommodation) o;
        return Double.compare(other.pricePerNight, pricePerNight) == 0 &&
                stars == other.stars &&
                Objects.equals(name, other.name) &&
                Objects.equals(location, other.location);
    }

    /**
     * Calculates the total cost for the accommodation.
     * Enforces the rule that the number of nights must be >= 1.
     */
    public double calculateCost(int numberOfDays) throws InvalidAccommodationDataException {
        if (numberOfDays < 1) {
            throw new InvalidAccommodationDataException("Number of nights must be at least 1.");
        }
        return pricePerNight * numberOfDays;
    }


}
