package travel;

import java.util.Objects;
import exceptions.InvalidTransportDataException;


/**
 * The Train class represents a train transportation.
 * It extends the Transportation class and adds train-specific
 * attributes such as train type and seat class.
 */
public class Train extends Transportation {

    private String trainType;
    private String seatClass;

    private final static double BUSINESS_PRICE = 250;
    private final static double ECONOMY_PRICE = 75;

    /**
     * Default constructor.
     * Initializes parent attributes and sets train-specific fields to empty.
     */
    public Train() throws InvalidTransportDataException {
        super();
        this.trainType = "Basic";
        this.seatClass = "Economy";
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
                 String arrivalCity, String trainType, String seatClass) throws InvalidTransportDataException {

        super(companyName, departureCity, arrivalCity);
        this.trainType = trainType;
        this.seatClass = seatClass;

    }

    /**
     * Parameterized constructor for loading data from files (CSV) WITHOUT seat class.
     * Matches the CSV format: TRAIN; TR3002;Shinkansen;Tokyo; Kyoto;250.00;HighSpeed
     */
    public Train(String transportId, String companyName, String departureCity, String arrivalCity, double price, String trainType, String seatClass) throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, price);
        this.trainType = trainType;
        this.seatClass = seatClass;
    }

    /**
     * Copy constructor.
     *
     * @param other Train object to copy
     */
    public Train(Train other) throws InvalidTransportDataException {

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
                ", Type: " + trainType + "]";
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
        return Objects.equals(trainType, other.trainType) ;
    }

    /**
     * Calculates the cost of the train ticket based on seat class.
     * - Economy → ECONOMY_PRICE
     * - Business → BUSINESS_PRICE
     * @param numberOfDays not used (required by abstract method)
     * @return calculated ticket price
     */
    @Override
    public double calculateCost(int numberOfDays) {
        double price = 0;
        if (seatClass.equalsIgnoreCase("Economy")) {
            price = ECONOMY_PRICE;
        } else if (seatClass.equalsIgnoreCase("Business")) {
            price = BUSINESS_PRICE;
        }

        return price;
    }
    @Override
    public String toCsvRow() {
        // Format: Type; ID; Company; Departure; Arrival; Price; TrainType; SeatClass
        return "TRAIN;" + getTransportId() + ";" + getCompanyName() + ";" +
                getDepartureCity() + ";" + getArrivalCity() + ";" + getPrice() + ";" +
                trainType + ";" + seatClass;
    }

    @Override
    public String getId() {
        // Fulfills Identifiable
        return getTransportId();
    }


    public static Train fromCsvRow(String csvLine) throws InvalidTransportDataException {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new InvalidTransportDataException("CSV line is empty.");
        }

        String[] parts = csvLine.split(";");

        if (parts.length < 8) {
            throw new InvalidTransportDataException("Invalid number of fields for Train.");
        }

        try {
            // parts[0] is "Train", so we start at index 1
            String id = parts[1].trim();
            String company = parts[2].trim();
            String departure = parts[3].trim();
            String arrival = parts[4].trim();
            double price = Double.parseDouble(parts[5].trim());
            String type = parts[6].trim();
            String seat = parts[7].trim();

            return new Train(id, company, departure, arrival, price, type, seat);
        } catch (NumberFormatException e) {
            throw new InvalidTransportDataException("Error parsing numerical values for Train: " + e.getMessage());
        }
    }
}
