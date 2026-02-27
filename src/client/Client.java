package client;

import exceptions.InvalidClientDataException;

import java.util.Objects;

/**
 * The Client class represents a customer in the travel system.
 * Each client has a unique ID, a first name, a last name, and an email.
 */
public class Client {

    private String clientId;
    private String firstName;
    private String lastName;
    private String email;

    private double amountSpent; // New attribute for A2 to track total spending

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
    }

    /**
     * Parameterized constructor.
     * Creates a client with provided personal information
     * and an auto-generated ID.
     */

    public Client(String firstName,
                  String lastName, String email) throws InvalidClientDataException {

        this.clientId = "C" + idCounter;
        idCounter++;

        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        this.amountSpent = 0.0;
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
    public double getAmountSpent() { return amountSpent; }


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


    public void addAmountSpent(double amount) {
        if (amount > 0) {
            this.amountSpent += amount;
        }
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

