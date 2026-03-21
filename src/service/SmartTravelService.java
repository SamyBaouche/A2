package service;

import client.Client;
import exceptions.*;
import persistence.AccommodationFileManager;
import persistence.ClientFileManager;
import persistence.TransportFileManager;
import persistence.TripFileManager;
import travel.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

//-----------------------------------------------------
// Assignment 2
// Question: 1 (only one)
// Written by: Mohammed El Ouaabani 40338377, Samy Baouche 40336149
//-----------------------------------------------------

public class SmartTravelService {

    static Scanner sc = new Scanner(System.in);

    private static Client[] clients = new Client[0];
    private static Trip[] trips = new Trip[0];
    private static Accommodation[] accommodations = new Accommodation[0];
    private static Transportation[] transportations = new Transportation[0];

    /**
     * Loads all data from CSV files into the application.
     */
    public static void loadAllData(){
        try {
            clients = ClientFileManager.loadClients("output/data/clients.csv");
            accommodations = AccommodationFileManager.loadAccommodations("output/data/accommodations.csv");
            transportations = TransportFileManager.loadTransportations("output/data/transports.csv");
            trips = TripFileManager.loadTrips("output/data/trips.csv",clients,accommodations,transportations);

            System.out.println("All data loaded !");
        } catch (IOException e) {
            System.out.println("Something went wrong when loading all data !");
        }
    }

    /**
     * Saves all current data to CSV files.
     */
    public static void saveAllData(){
        try {
            ClientFileManager.saveClients(clients,"output/data/clients.csv");
            AccommodationFileManager.saveAccommodations(accommodations,"output/data/accommodations.csv");
            TransportFileManager.saveTransportations(transportations,"output/data/transports.csv");
            TripFileManager.saveTrips(trips,"output/data/trips.csv");

            System.out.println("All data saved !");

        } catch (IOException e) {
            System.out.println("Something went wrong when saving all data !");
        }
    }

    /**
     * Clears all data from memory.
     */
    public static void clearAllData() {
        clients = new Client[0];
        trips = new Trip[0];
        accommodations = new Accommodation[0];
        transportations = new Transportation[0];
    }

    /**
     * addClient(): Prompts user for client information and adds it to the client array.
     */

    public static void addClient() {

        try {
            System.out.print("Enter client First Name: ");
            String firstName = sc.nextLine();
            System.out.print("Enter client Last Name: ");
            String lastName = sc.nextLine();
            System.out.print("Enter client email: ");
            String email = sc.nextLine();

            for (Client client: clients) {
                if (email.equals(client.getEmail())) {
                    throw new DuplicateEmailException("This email is already associated to a client");
                }
            }

            Client client = new Client(firstName, lastName, email);


            Client[] clientProcess = new Client[clients.length + 1];

            if (clients.length > 0) {
                for (int i = 0; i < clients.length; i++) {
                    clientProcess[i] = clients[i];
                }
            }

            clientProcess[clientProcess.length - 1] = client;
            clients = clientProcess;
        } catch (InvalidClientDataException | DuplicateEmailException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * editClient(): Allows user to select a client and update their information.
     */

    public static void editClient() {
        if (clients.length == 0) {
            System.out.println("There is no client to edit.");
        } else {
            String id;

            try {
                id = choiceCheckClient("edit", true);
            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }

            if (!id.equals("0")) {

                int choice = 0;

                for (int i = 0; i < clients.length; i++) {
                    if (clients[i].getClientId().equals(id))
                        choice = i;
                }

                try {
                    System.out.print("Enter new client First Name > ");
                    String firstName = sc.nextLine();
                    clients[choice].setFirstName(firstName);

                    System.out.print("Enter new client Last Name > ");
                    String lastName = sc.nextLine();
                    clients[choice].setLastName(lastName);

                    System.out.print("Enter new client Email > ");
                    String email = sc.nextLine();
                    try {
                        clients[choice].setEmail(email);
                    } catch (DuplicateEmailException e) {
                        System.err.println(e.getMessage());
                    }
                } catch (InvalidClientDataException e) {
                    System.err.println(e.getMessage());
                }
            }

        }
    }

    /**
     * deleteClient(): Removes a client selected by the user from the array.
     * Rebuilds the array with one fewer element.
     */

    public static void deleteClient() {
        if (clients.length == 0) {
            System.out.println("There is no client to delete.");
        } else {
            String id;
            try {
                id = choiceCheckClient("delete", true);
            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }

            if (!id.equals("0")) {

                Client[] smallerClient;

                if ((clients.length - 1) == 0) {
                    smallerClient = new Client[0];
                } else {
                    smallerClient = new Client[clients.length - 1];

                    int compteur = 0;

                    for (int i = 0; i < clients.length; i++) {
                        if (!id.equalsIgnoreCase(clients[i].getClientId())) {
                            smallerClient[compteur++] = clients[i];
                        }
                    }
                }

                clients = smallerClient;
            }
        }
    }
    /**
     * displayClients(): Prints all clients with their index, ID, and full name.
     * Used in menus to let the user select a client by index.
     */

    public static void displayClients() {
        if (clients.length != 0) {
            for (int i = 0; i < clients.length; i++) {
                System.out.println(i + ". " + clients[i].getClientId() + ", " + clients[i].getFirstName() + " " + clients[i].getLastName());
            }
        } else {
            System.out.println("There is no clients to display.");
        }
    }

    /**
     * createTrip(): Guides the user to create a new Trip.
     * Prompts for destination, duration, base price, and optionally associates
     * a client, accommodation, and transportation.
     * Adds the trip to the trips array.
     */
    public static void createTrip() {

        Accommodation accomodation = null;
        Transportation transportation = null;

        System.out.print("Enter your destination: ");
        String destination = sc.nextLine();

        System.out.print("Enter duration days: ");
        int daysDuration = valideIntegerInput();

        System.out.print("Enter base price: ");
        double price = valideDoubleInput();

        Client client = null;

        if (clients.length != 0) {
            String id = "";

            try {
                id = choiceCheckClient("associate to the trip", true);

                if (id.equals("0"))
                    return;

            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
                System.out.println("A trip must have a client associated try again.");
            }

            int choice = -1;

            for  (int i = 0; i < clients.length; i++) {
                if (clients[i].getClientId().equalsIgnoreCase(id)) {
                    choice = i;
                }
            }

            if (choice != -1)
                client = clients[choice];

        } else {
            System.out.println("No client to associate please create one before creating a trip");
            return;
        }

        if (accommodations.length != 0) {
            String id = "";
            try {
                id = choiceCheckAccommodation("associate to the trip", false);
            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
            }

            int choice = -1;

            for  (int i = 0; i < accommodations.length; i++) {
                if (accommodations[i].getAccommodationId().equalsIgnoreCase(id)) {
                    choice = i;
                }
            }

            if (choice != -1)
                accomodation = accommodations[choice];

        } else {
            System.err.println("No accommodation to associate please create one and add it to the trip later");
        }

        if (transportations.length != 0) {
            String id = "";
            try {
                id = choiceCheckTransportation("associate to the trip", false);
            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
            }

            int index = -1;

            for  (int i = 0; i < transportations.length; i++) {
                if (id.equalsIgnoreCase(transportations[i].getTransportId())) {
                    index = i;
                }
            }

            if (index != -1)
                transportation = transportations[index];
        } else {
            System.err.println("No transportation to associate please create one and add it to the trip later");
        }

        if (accomodation == null && transportation == null) {
            System.err.println("A trip must have at least an Accommodation OR a Transportation");
        } else {

            try {
                Trip trip = new Trip(destination, daysDuration, price, client, accomodation, transportation);

                Trip[] tripsCopy = new Trip[trips.length + 1];

                for (int i = 0; i < trips.length; i++) {
                    tripsCopy[i] = trips[i];
                }

                tripsCopy[tripsCopy.length - 1] = trip;

                trips = tripsCopy;

                System.out.println("New trip created successfully");

            } catch (InvalidTripDataException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * editTripInformation(): Allows user to modify an existing trip.
     * Updates destination, duration, price, accommodation, and transportation.
     */
    public static void editTripInformation() {
        if (trips.length != 0) {
            String choice;

            try {
                choice = choiceCheckTrip("edit", true);
            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }

            int choiceTrip = -1;

            for (int i = 0; i < trips.length; i++) {
                if (trips[i].getTripId().equalsIgnoreCase(choice))
                    choiceTrip = i;
            }

            if (!choice.equalsIgnoreCase("0")) {

                System.out.print("Enter the new destination > ");
                String destination = sc.nextLine();

                System.out.print("Enter the new duration in days > ");
                int duration = valideIntegerInput();

                System.out.print("Enter the new price > ");
                double price = valideDoubleInput();

                int choiceAccommodation = -1;
                int choiceTransportation = -1;

                String accId = "";
                String transId = "";

                if (accommodations.length != 0) {
                    try {
                        accId = choiceCheckAccommodation("associate to the trip", false);
                    } catch (EntityNotFoundException e) {
                        System.err.println(e.getMessage());
                    }

                    for (int i = 0; i < accommodations.length; i++) {
                        if (accId.equalsIgnoreCase(accommodations[i].getAccommodationId())) {
                            choiceAccommodation = i;
                        }
                    }
                } else {
                    System.out.println("No accommodation registered add one and try again");
                }

                if (transportations.length != 0) {
                    try {
                        transId = choiceCheckTransportation("associate to the trip", false);
                    } catch (EntityNotFoundException e) {
                        System.err.println(e.getMessage());
                    }

                    for (int i = 0; i < transportations.length; i++) {
                        if (transId.equalsIgnoreCase(transportations[i].getTransportId())) {
                            choiceTransportation = i;
                        }
                    }
                } else {
                    System.out.println("No transportation registered add one and try again");
                }

                if (choiceAccommodation == -1 && choiceTransportation == -1) {
                    System.err.println("A trip must have at least an accommodation or a transportation.");
                    return;
                }

                try {
                    trips[choiceTrip].setDestination(destination);
                    trips[choiceTrip].setDurationInDays(duration);
                    trips[choiceTrip].setBasePrice(price);
                    if (choiceAccommodation != -1) {
                        trips[choiceTrip].setAccommodation(accommodations[choiceAccommodation]);
                    }
                    if (choiceTransportation != -1) {
                        trips[choiceTrip].setTransportation(transportations[choiceTransportation]);
                    }

                    System.out.println("Trip edited successfully");
                } catch (InvalidTripDataException e) {
                    System.err.println(e.getMessage());
                }
            }

        } else {
            System.out.println("No trips to edit");
        }
    }

    /**
     * cancelTrip(): Deletes a selected trip.
     * Rebuilds trips array without the removed trip.
     */
    public static void cancelTrip() {
        if (trips.length != 0) {
            String choice = "";

            try {
                choice = choiceCheckTrip("cancel", true);
            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }

            int index = 0;

            for (int i = 0; i < trips.length; i++) {
                if (choice.equalsIgnoreCase(trips[i].getTripId())) {
                    index = i;
                }
            }

            if (!choice.equalsIgnoreCase("0")) {

                Trip[] tripCopy = new Trip[trips.length - 1];

                if (tripCopy.length != 0) {
                    int tripCopyCompteur = 0;
                    for (int i = 0; i < trips.length; i++) {
                        if (i != index) {
                            tripCopy[tripCopyCompteur] = trips[i];
                            tripCopyCompteur++;
                        }
                    }

                    trips = tripCopy;
                } else {
                    trips = new Trip[0];
                }
            }
        } else {
            System.out.println("No trip to cancel");
        }
    }

    /**
     * listAllTrips(): Prints all trips in the system.
     */
    public static void listAllTrips() {
        if (trips.length != 0) {
            for (int i = 0; i < trips.length; i++) {
                System.out.println(i + ". " + trips[i]);
            }
        } else {
            System.out.println("No trips to list");
        }
    }

    /**
     * listAllTripsByClient(): Lists all trips associated with a chosen client.
     */
    public static void listAllTripsByClient() {
        if (trips.length != 0) {
            String choice = "";

            try {
                choice = choiceCheckClient("see the trips of", true);
            } catch (EntityNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }

            int index = 0;

            for (int i = 0; i < clients.length; i++) {
                if (choice.equalsIgnoreCase(clients[i].getClientId())) {
                    index = i;
                }
            }

            if (!choice.equalsIgnoreCase("0")) {
                for (Trip trip : trips) {
                    if (trip.getClientAssociated() == clients[index]) {
                        System.out.println(trip);
                    }
                }
            }

        } else {
            System.out.println("No trips to list");
        }
    }

    /**
     * addTransportation(): Adds a new transportation (Flight, Train, Bus)
     * Prompts for type-specific information and adds it to transportations array.
     */
    public static void addTransportation() {

        int type;

        do {
            System.out.println("\nSelect Transportation Type:");
            System.out.println("1- Flight");
            System.out.println("2- Train");
            System.out.println("3- Bus");
            System.out.print("> ");
            type = valideIntegerInput();
            sc.nextLine();
        } while (type < 1 || type > 3);

        System.out.print("Enter Company Name: ");
        String company = sc.nextLine();
        System.out.print("Enter Departure City: ");
        String departure = sc.nextLine();
        System.out.print("Enter Arrival City: ");
        String arrival = sc.nextLine();


        switch (type) {
            case 1 -> {
                try {
                    System.out.print("Enter Luggage Allowance (kg): ");
                    double luggage = valideDoubleInput();
                    Transportation[] transportationCopy = new Transportation[transportations.length + 1];
                    for (int i = 0; i < transportations.length; i++) {
                        transportationCopy[i] = transportations[i];
                    }
                    transportationCopy[transportationCopy.length - 1] = new Flight(company, departure, arrival, luggage);
                    transportations = transportationCopy;
                    System.out.println("Transportation added successfully.");
                } catch (InvalidTransportDataException | NumberFormatException e) {
                    System.err.println(e.getMessage());
                }
            }
            case 2 -> {
                System.out.print("Enter Train Type: ");
                String trainType = sc.nextLine();
                System.out.print("Enter Seat Class (Economy/Business): ");
                String seatClass = sc.nextLine();
                Transportation[] transportationCopy = new Transportation[transportations.length + 1];
                for (int i = 0; i < transportations.length; i++) {
                    transportationCopy[i] = transportations[i];
                }
                try {
                    transportationCopy[transportationCopy.length - 1] = new Train(company, departure, arrival, trainType, seatClass);
                    transportations = transportationCopy;
                    System.out.println("Transportation added successfully.");
                } catch (InvalidTransportDataException e) {
                    System.err.println(e.getMessage());
                }
            }
            case 3 -> {
                System.out.print("Enter Number of Stops: ");
                int stops = valideIntegerInput();
                Transportation[] transportationCopy = new Transportation[transportations.length + 1];
                for (int i = 0; i < transportations.length; i++) {
                    transportationCopy[i] = transportations[i];
                }
                try {
                    transportationCopy[transportationCopy.length - 1] = new Bus(company, departure, arrival, stops);
                    transportations = transportationCopy;
                    System.out.println("Transportation added successfully.");
                } catch (InvalidTransportDataException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * listTransportationOptions(): Lists all registered transportation, grouped by type.
     */
    public static void listTransportationOptions() {

        if (transportations.length == 0) {
            System.out.println("No transportation options recorded.");
            return;
        }

        System.out.println("\n--- Registered Flights ---");
        for (Transportation t : transportations) {
            if (t instanceof Flight) System.out.println(t);
        }

        System.out.println("\n--- Registered Trains ---");
        for (Transportation t : transportations) {
            if (t instanceof Train) System.out.println(t);
        }

        System.out.println("\n--- Registered Buses ---");
        for (Transportation t : transportations) {
            if (t instanceof Bus) System.out.println(t);
        }
    }

    /**
     * removeTransportation(): Removes a selected transportation option from the array.
     */
    public static void removeTransportation() {

        if (transportations.length == 0) {
            System.out.println("There are no transportation options to remove.");
            return;
        }

        String id;

        try {
            id = choiceCheckTransportation("remove", true);
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        if (!id.equals("0")) {

            Transportation[] copyTransportations = new Transportation[transportations.length - 1];

            int compteur = 0;
            for (int i = 0; i < transportations.length; i++) {
                if (!id.equals(transportations[i].getTransportId())) {
                    copyTransportations[compteur++] = transportations[i];
                }
            }

            transportations = copyTransportations;

            System.out.println("Transportation removed successfully.");
        }
    }

    /**
     * addAccommodation(): Adds a Hotel or Hostel.
     * Prompts for type-specific info and appends to accommodations array.
     */
    public static void addAccommodation() {

        int type;

        do {
            System.out.println("Choose an accommodation type:");
            System.out.println("1. Hotel\n2. Hostel");

            type = valideIntegerInput();

            sc.nextLine();
        } while (type < 1 || type > 2);

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Location: ");
        String location = sc.nextLine();

        System.out.print("Price/night: ");
        double price = valideDoubleInput();

        switch (type) {
            case 1 -> {
                System.out.print("Enter Star Rating: ");
                int stars = valideIntegerInput();
                Accommodation[] copyAccommodations = new Accommodation[accommodations.length + 1];
                for (int i = 0; i < accommodations.length; i++) {
                    copyAccommodations[i] = accommodations[i];
                }
                try {
                    copyAccommodations[copyAccommodations.length - 1] = new Hotel(name, location, price, stars);
                    accommodations = copyAccommodations;
                    System.out.println("Accommodation added successfully.");
                } catch (InvalidAccommodationDataException e) {
                    System.err.println(e.getMessage());
                }
            } case 2 -> {
                System.out.print("Enter Star Rating: ");
                int stars = valideIntegerInput();
                System.out.print("Enter Number of Shared Beds: ");
                int beds = valideIntegerInput();
                Accommodation[] copyAccommodations = new Accommodation[accommodations.length + 1];
                for (int i = 0; i < accommodations.length; i++) {
                    copyAccommodations[i] = accommodations[i];
                }
                try {
                    copyAccommodations[copyAccommodations.length - 1] = new Hostel(name, location, stars, beds);
                    accommodations = copyAccommodations;
                    System.out.println("Accommodation added successfully.");
                } catch (InvalidAccommodationDataException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * removeAccommodation(): Deletes selected accommodation from the array.
     */
    public static void removeAccommodation() {

        if (accommodations.length == 0) {
            System.out.println("There are no accommodation options to remove.");
            return;
        }

        String id;

        try {
            id = choiceCheckAccommodation("remove", true);
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        if (!id.equals("0")) {

            Accommodation[] copyAccommodations = new Accommodation[accommodations.length - 1];

            int compteur = 0;

            for (int i = 0; i < accommodations.length; i++) {
                if (!accommodations[i].getAccommodationId().equalsIgnoreCase(id)) {
                    copyAccommodations[compteur++] = accommodations[i];
                }
            }

            accommodations = copyAccommodations;

            System.out.println("Accommodation removed successfully.");
        }

    }

    /**
     * listAccommodationByType(): Lists all accommodations grouped by type (Hotel/Hostel)
     */
    public static void listAccommodationByType() {

        if (accommodations.length != 0) {
            System.out.println("--- Hotels ---");
            for (Accommodation a : accommodations) {
                if (a instanceof Hotel)
                    System.out.println(a);
            }

            System.out.println("--- Hostels ---");
            for (Accommodation a : accommodations) {
                if (a instanceof Hostel) System.out.println(a);
            }
        } else {
            System.out.println("There are no accommodations to list.");
        }
    }

    /**
     * Helper method to prompt user to select a client by ID.
     * @param action The action being performed (for display).
     * @param exit Whether the user can choose to exit (enter "0").
     * @return The selected client ID, or "0" if exited.
     * @throws EntityNotFoundException if the ID is not found.
     */
    public static String choiceCheckClient(String action, boolean exit) throws EntityNotFoundException {
        String id;

        System.out.println("\nWhich client do you want to " + action + "?");

        if (exit) {
            System.out.print("Enter the id OR 0 to exit> ");
            id = sc.nextLine();
            if (id.equals("0"))
                return id;

        } else {
            System.out.print("Enter the id > ");
            id = sc.nextLine();
        }

        for (Client client: clients) {
            if (client.getClientId().equalsIgnoreCase(id)) {
                return id;
            }
        }

        throw new EntityNotFoundException("No Client matches this Id");
    }

    /**
     * Helper method to prompt user to select a trip by ID.
     * @param action The action being performed.
     * @param exit Whether the user can choose to exit.
     * @return The selected trip ID.
     * @throws EntityNotFoundException if the ID is not found.
     */
    public static String choiceCheckTrip(String action, boolean exit) throws EntityNotFoundException {
        String id;

        System.out.println("\nWhich trip do you want to " + action + "?");

        if (exit) {
            System.out.print("Enter the id OR 0 to exit> ");
            id = sc.nextLine();
            if (id.equals("0"))
                return id;

        } else {
            System.out.print("Enter the id > ");
            id = sc.nextLine();
        }

        for (Trip trip: trips) {
            if (trip.getTripId().equalsIgnoreCase(id)) {
                return id;
            }
        }

        throw new EntityNotFoundException("No Trip matches this Id");
    }

    /**
     * Helper method to prompt user to select a transportation by ID.
     * @param action The action being performed.
     * @param exit Whether the user can choose to exit.
     * @return The selected transportation ID.
     * @throws EntityNotFoundException if the ID is not found.
     */
    public static String choiceCheckTransportation(String action, boolean exit) throws EntityNotFoundException {
        String id;

        System.out.println("\nWhich transportation do you want to " + action + "?");

        if (exit) {
            System.out.print("Enter the id OR 0 to exit> ");
            id = sc.nextLine();
            if (id.equals("0"))
                return id;

        } else {
            System.out.print("Enter the id > ");
            id = sc.nextLine();
        }

        for (Transportation trans: transportations) {
            if (trans.getTransportId().equalsIgnoreCase(id)) {
                return id;
            }
        }

        throw new EntityNotFoundException("No Transportation matches this Id");
    }

    /**
     * Helper method to prompt user to select an accommodation by ID.
     * @param action The action being performed.
     * @param exit Whether the user can choose to exit.
     * @return The selected accommodation ID.
     * @throws EntityNotFoundException if the ID is not found.
     */
    public static String choiceCheckAccommodation(String action, boolean exit) throws EntityNotFoundException {
        String id;

        System.out.println("\nWhich accommodation do you want to " + action + "?");

        if (exit) {
            System.out.print("Enter the id OR 0 to exit> ");
            id = sc.nextLine();
            if (id.equals("0"))
                return id;

        } else {
            System.out.print("Enter the id > ");
            id = sc.nextLine();
        }

        for (Accommodation acc: accommodations) {
            if (acc.getAccommodationId().equalsIgnoreCase(id)) {
                return id;
            }
        }

        throw new EntityNotFoundException("No Accommodation matches this Id");
    }

    /**
     * additionalOperations(): Menu for extra operations outside the main CRUD menus.
     * Options:
     * 1 - Display the most expensive trip
     * 2 - Calculate and display total cost of a specific trip
     * 3 - Create a deep copy of the transportation array
     * 4 - Create a deep copy of the accommodation array
     */

    public static void additionalOperations() throws InvalidAccommodationDataException, InvalidTransportDataException, InvalidTripDataException {

        int choice;

        do {
            System.out.println("-- Additional Operations --\n");
            System.out.println("1. Display the most expensive trip\n" +
                    "2. Calculate and display the total cost of a trip\n" +
                    "3. Create a deep copy of the transportation array\n" +
                    "4. Create a deep copy of the accomodation array");
            System.out.print("> ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    mostExpensiveTrip(trips);
                } case 2 -> {
                    totalCostOfATrip(trips);
                } case 3 -> {

                    if (transportations.length != 0) {
                        Transportation[] copyTransportations = copyTransportationArray(transportations);

                        System.out.println("Here is the deep copy of the transportation array");
                        for (Transportation transportation : copyTransportations) {
                            System.out.println(transportation);
                        }
                    } else {
                        System.out.println("There are no transportations to copy");
                    }

                } case 4 -> {

                    if (accommodations.length != 0) {
                        Accommodation[] copyAccommodations = copyAccommodationArray(accommodations);

                        System.out.println("Here is the deep copy of the accomodation array");
                        for (Accommodation accommodation : copyAccommodations) {
                            System.out.println(accommodation);
                        }
                    } else {
                        System.out.println("There are no accomodations to copy");
                    }

                }
            }

        } while (choice < 0 || choice > 4);
    }

    /**
     * totalCostOfATrip(Trip[] trips):
     * Allows the user to select a trip and calculates its total cost.
     * The cost is calculated using the Trip object's calculateTotalCost() method.
     * If no trips exist, informs the user.
     */
    public static void totalCostOfATrip(Trip[] trips) {
        if (trips.length == 0) {
            System.out.println("There is no trip stored.");
        } else {
            int choice;
            do {
                System.out.println("Which trip do you want to calculate the cost of? :");
                for (int i = 0; i < trips.length; i++) {
                    System.out.println((i + 1) + ". " + trips[i].getTripId());
                }
                choice = sc.nextInt();
            } while (choice < 1 || choice > trips.length);

            System.out.println("Total cost of trip [" + trips[choice - 1].getTripId() + "]" + trips[choice -1].calculateTotalCost());
        }
    }

    // --- Additional Operations ---
    /**
     * Finds and displays the most expensive trip in the provided array.
     * @param trips The array of trips to check.
     */
    public static void mostExpensiveTrip(Trip[] trips) {

        if (trips != null && trips.length != 0) {
            double topPrice = 0;

            Trip mostExpensiveTrip = null;

            for (Trip trip : trips) {
                double costTrip = trip.calculateTotalCost();
                if (costTrip > topPrice) {
                    topPrice = costTrip;
                    mostExpensiveTrip = trip;
                }
            }

            if (mostExpensiveTrip != null) {
                System.out.println("The most expensive trip is: " + mostExpensiveTrip.getTripId());
            }
        } else {
            System.out.println("There are no trips to compare");
        }
    }

    /**
     * Creates a deep copy of a transportation array.
     */
    public static Transportation[] copyTransportationArray(Transportation[] original) throws InvalidTransportDataException {

        Transportation[] copy = new Transportation[original.length];

        for (int i = 0; i < original.length; i++) {
            if (original[i] instanceof Flight) {
                copy[i] = new Flight((Flight) original[i]);
            } else if (original[i] instanceof Train) {
                copy[i] = new Train((Train) original[i]);
            } else if (original[i] instanceof Bus) {
                copy[i] = new Bus((Bus) original[i]);
            }
        }

        return copy;

    }

    /**
     * Creates a deep copy of an accommodation array.
     */
    public static Accommodation[] copyAccommodationArray(Accommodation[] original) throws InvalidAccommodationDataException {

        Accommodation[] copy = new Accommodation[original.length];

        for (int i = 0; i < original.length; i++) {
            if (original[i] instanceof Hotel) {
                copy[i] = new Hotel((Hotel) original[i]);
            } else if (original[i] instanceof Hostel) {
                copy[i] = new Hostel((Hostel) original[i]);
            }
        }

        return copy;

    }

    /**
     * Validates and returns an integer input from the user.
     * @return A valid integer.
     */
    public static int valideIntegerInput() {
        while (true) {
            try {
                String s = sc.nextLine();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please type a valid number");
                sc.nextLine();
            }
        }
    }

    /**
     * Validates and returns a double input from the user.
     * @return A valid double.
     */
    public static double valideDoubleInput() {
        while (true) {
            try {
                String s = sc.nextLine();
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Please type a valid number");
                sc.nextLine();
            }
        }
    }

    /**
     * Adds a predefined client to the list (for testing/setup).
     * @param client The client to add.
     */
    public static void addClientPredefined(Client client) {
        Client[] copyClients = new Client[clients.length + 1];
        for (int i = 0; i < clients.length; i++) {
            copyClients[i] = clients[i];
        }
        copyClients[copyClients.length - 1] = client;
        clients = copyClients;
    }

    /**
     * Adds a predefined trip to the list (for testing/setup).
     * @param trip The trip to add.
     */
    public static void addTripPredefined(Trip trip) {
        Trip[] copyTrips = new Trip[trips.length + 1];
        for (int i = 0; i < trips.length; i++) {
            copyTrips[i] = trips[i];
        }
        copyTrips[copyTrips.length - 1] = trip;
        trips = copyTrips;
    }

    /**
     * Adds a predefined accommodation to the list (for testing/setup).
     * @param accommodation The accommodation to add.
     */
    public static void addAccommodationPredefined(Accommodation accommodation) {
        Accommodation[] copyAccommodations = new Accommodation[accommodations.length + 1];
        for (int i = 0; i < accommodations.length; i++) {
            copyAccommodations[i] = accommodations[i];
        }
        copyAccommodations[copyAccommodations.length - 1] = accommodation;
        accommodations = copyAccommodations;
    }

    /**
     * Adds a predefined transportation to the list (for testing/setup).
     * @param transportation The transportation to add.
     */
    public static void addTransportationPredefined(Transportation transportation) {
        Transportation[] copyTransportations = new Transportation[transportations.length + 1];
        for (int i = 0; i < transportations.length; i++) {
            copyTransportations[i] = transportations[i];
        }
        copyTransportations[copyTransportations.length - 1] = transportation;
        transportations = copyTransportations;
    }

    /**
     * Finds a client by ID and prints their details.
     * @param clientId The ID to search for.
     * @throws EntityNotFoundException if the client is not found.
     */
    public static void findClientById(String clientId) throws EntityNotFoundException {
        for (Client client : clients) {
            if (client.getClientId().equalsIgnoreCase(clientId)) {
                System.out.println(client);
                return;
            }
        }
        throw new EntityNotFoundException("Client not found");
    }

    /**
     * Gets the list of all clients.
     * @return Array of clients.
     */
    public static Client[] getClients() {
        return clients;
    }

    /**
     * Gets the list of all trips.
     * @return Array of trips.
     */
    public static Trip[] getTrips() {
        return trips;
    }

    /**
     * Gets the list of all accommodations.
     * @return Array of accommodations.
     */
    public static Accommodation[] getAccommodations() {
        return accommodations;
    }

    /**
     * Gets the list of all transportations.
     * @return Array of transportations.
     */
    public static Transportation[] getTransportations() {
        return transportations;
    }
}
