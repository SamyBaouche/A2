package travel;

import java.util.Objects;

/**
 * The Train class represents a train transportation.
 * It extends the Transportation class and adds train-specific
 * attributes such as train type and seat class.
 */
public class Train extends Transportation {

    private String trainType;
    private String seatClass;

    private final static double FIRST_CLASS_PRICE = 250;
    private final static double ECONOMY_PRICE = 75;

    /**
     * Default constructor.
     * Initializes parent attributes and sets train-specific fields to empty.
     */
    public Train() {
        super();
        this.seatClass = "";
        this.trainType = "";
    }
    /**
     * Parameterized constructor.
     *
     * @param companyName   Name of the train company
     * @param departureCity Departure city
     * @param arrivalCity   Arrival city
     * @param trainType     Type of train
     * @param seatClass     Seat category (Economy / First Class)
     */
    public Train(String companyName, String departureCity,
                 String arrivalCity, String trainType, String seatClass) {

        super(companyName, departureCity, arrivalCity);
        this.trainType = trainType;
        this.seatClass = seatClass;
    }
    /**
     * Copy constructor.
     *
     * @param other Train object to copy
     */
    public Train(Train other) {

        super(other);
        this.trainType = other.trainType;
        this.seatClass = other.seatClass;
    }

    //Getters and Setters

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    /**
     * Returns a string representation of the Train object.
     *
     * @return formatted string describing the train
     */
    @Override
    public String toString() {
        return "Train [" + super.toString() +
                ", Type: " + trainType +
                ", Class: " + seatClass + "]";
    }

    /**
     * Compares this Train object with another object.
     * Two trains are equal if:
     * - They are the same class
     * - Parent attributes are equal
     * - trainType and seatClass are equal
     *
     * @param o object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Train other = (Train) o;
        return Objects.equals(trainType, other.trainType) && Objects.equals(seatClass, other.seatClass);
    }

    /**
     * Calculates the cost of the train ticket based on seat class.
     * - Economy → ECONOMY_PRICE
     * - First Class → FIRST_CLASS_PRICE
     *
     * @param numberOfDays not used (required by abstract method)
     * @return calculated ticket price
     */
    @Override
    public double calculateCost(int numberOfDays) {

        double cost = 0;

        if (seatClass.equalsIgnoreCase("Economy")) {
            cost = ECONOMY_PRICE;
        } else if (seatClass.equalsIgnoreCase("First Class")) {
            cost = FIRST_CLASS_PRICE;
        }

        return cost;
    }
}
