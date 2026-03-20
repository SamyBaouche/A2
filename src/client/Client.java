package client;

import exceptions.InvalidClientDataException;

import java.util.Objects;

import service.SmartTravelService;
import travel.Trip;

/**
 * The Client class represents a customer in the travel system.
 * Each client has a unique ID, a first name, a last name, and an email.
 */
public class Client {

    private String clientId;
    private String firstName;
    private String lastName;
    private String email;

    private double totalSpent; // New attribute for A2 to track total spending

    private static int idCounter = 1001;

    /**
     * Default constructor.
     * Creates a client with empty fields and an auto-generated ID.
     */

    public Client() {
        this.clientId = "C" + idCounter;
        idCounter++;

        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.totalSpent = 0.0;
    }

    /**
     * Parameterized constructor.
     * Creates a client with provided personal information
     * and an auto-generated ID.
     */

    public Client(String firstName,
                  String lastName, String email) throws InvalidClientDataException {

        if (firstName.isEmpty()) {
            throw new InvalidClientDataException("Your first name must be non-empty");
        } else if (firstName.length() > 50) {
            throw new InvalidClientDataException("Your first name can't longer than 50 characters");
        }

        if (lastName.isEmpty()) {
            throw new InvalidClientDataException("Your last name must be non-empty");
        } else if (lastName.length() > 50) {
            throw new InvalidClientDataException("Your last name can't be longer than 50 caracters");
        }

        if (email == null || email.length() > 100 || email.contains(" ") || !email.contains("@") || !email.contains(".")) {
            throw new InvalidClientDataException("Invalid email format. Must contain @ and ., have no spaces, and be 100 characters or less.");
        }

        this.clientId = "C" + idCounter;
        idCounter++;

        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        this.totalSpent = 0.0;
    }

    /**
     * Copy constructor.
     * Creates a new Client object based on another Client.
     * A new unique ID is generated.
     */
    public Client (Client other) {
        this.clientId = "C" + idCounter;
        idCounter++;

        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
        this.totalSpent = other.totalSpent;
    }

    public Client(String id, String firstName, String lastName, String email) throws InvalidClientDataException {

        if (firstName.isEmpty()) {
            throw new InvalidClientDataException("Your first name must be non-empty");
        } else if (firstName.length() > 50) {
            throw new InvalidClientDataException("Your first name can't longer than 50 characters");
        }

        if (lastName.isEmpty()) {
            throw new InvalidClientDataException("Your last name must be non-empty");
        } else if (lastName.length() > 50) {
            throw new InvalidClientDataException("Your last name can't be longer than 50 caracters");
        }

        if (email == null || email.length() > 100 || email.contains(" ") || !email.contains("@") || !email.contains(".")) {
            throw new InvalidClientDataException("Invalid email format. Must contain @ and ., have no spaces, and be 100 characters or less.");
        }

        this.clientId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters
    public String getClientId() {
        return clientId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setFirstName(String firstName) throws InvalidClientDataException {
        // Rule: non-empty, <= 50 chars
        if (firstName == null || firstName.trim().isEmpty() || firstName.length() > 50) {
            throw new InvalidClientDataException("First name must be non-empty and 50 characters or less.");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) throws InvalidClientDataException {
        // Rule: non-empty, <= 50 chars
        if (lastName == null || lastName.trim().isEmpty() || lastName.length() > 50) {
            throw new InvalidClientDataException("Last name must be non-empty and 50 characters or less.");
        }
        this.lastName = lastName;
    }

    public void setEmail(String email) throws InvalidClientDataException {
        // Rule: contains @ and ., no spaces, <= 100 chars
        if (email == null || email.length() > 100 || email.contains(" ") || !email.contains("@") || !email.contains(".")) {
            throw new InvalidClientDataException("Invalid email format. Must contain @ and ., have no spaces, and be 100 characters or less.");
        }
        this.email = email;
    }


    public double getTotalSpent() {
        Trip[] trips = SmartTravelService.getTrips();

        double total = 0.0;

        for (Trip trip : trips) {
            if (trip.getClientAssociated().getClientId().equals(this.clientId)) {
                total += trip.calculateTotalCost();
            }
        }

        return total;
    }

    /**
     * Returns a formatted string representation of the Client object.
     */
    @Override
    public String toString() {
        return "Client Info: " +
                "[ID: " + clientId +
                ", Name: " + firstName + " " + lastName +
                ", Email: " + email + "]";
    }

    /**
     * Overrides the equals method to compare two Client objects.
     * Two clients are considered equal if all their attributes are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client other = (Client) o;
        return Objects.equals(clientId, other.clientId) &&
                Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName) &&
                Objects.equals(email, other.email);
    }


}

