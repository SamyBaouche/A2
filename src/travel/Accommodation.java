package travel;

import exceptions.InvalidAccommodationDataException;

import java.util.Objects;

public abstract class Accommodation {

    protected String accommodationId;
    protected String name;
    protected String location;
    protected double pricePerNight;

    private static int idCounter = 4001;

    public Accommodation()  throws InvalidAccommodationDataException {
        this.accommodationId = "A" + idCounter;
        idCounter++;

        this.name = "Unknown";
        this.location = "Unknown";
        setPricePerNight(1.0);
    }

    public Accommodation(String name, String location, double pricePerNight) throws InvalidAccommodationDataException{

        this.accommodationId = "A" + idCounter;
        idCounter++;

        this.name = name;
        this.location = location;
        setPricePerNight(pricePerNight);
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
    }

    /**
     * Parameterized constructor for loading data from files (CSV).
     * Takes an existing ID instead of auto-generating one.
     */
    public Accommodation(String accommodationId, String name, String location, double pricePerNight) throws InvalidAccommodationDataException {
        this.accommodationId = accommodationId;

        this.name = name;
        this.location = location;
        setPricePerNight(pricePerNight);
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

    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        if (pricePerNight <= 0) {
            throw new InvalidAccommodationDataException("Price per night must be greater than $0.");
        }
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String toString() {
        return "ID: " + accommodationId +
                ", Name: " + name +
                ", Location: " + location +
                ", Rate: $" + pricePerNight + "/night";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Accommodation other = (Accommodation) o;
        return Double.compare(other.pricePerNight, pricePerNight) == 0 &&
                Objects.equals(name, other.name) &&
                Objects.equals(location, other.location);
    }

    /**
     * Calculates the total cost for the accommodation.
     * Enforces the rule that the number of nights must be >= 1.
     */
    public abstract double calculateCost(int numberOfDays) throws InvalidAccommodationDataException;


}
