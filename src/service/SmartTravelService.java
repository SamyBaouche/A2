package service;

import client.Client;
import exceptions.*;
import travel.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SmartTravelService {

    static Scanner sc = new Scanner(System.in);

    private static Client[] clients;
    private static Trip[] trips;
    private static Accommodation[] accommodations;
    private static Transportation[] transportations;

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
            String id = choiceCheckClient("edit", true);

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
                    clients[choice].setEmail(email);
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
            String id = choiceCheckClient("delete", true);

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

        Client client = new Client();

        if (clients.length != 0) {
            String id = choiceCheckClient("associate to the trip", false);
            int choice = 0;

            for  (int i = 0; i < clients.length; i++) {
                if (clients[i].getClientId().equalsIgnoreCase(id)) {
                    choice = i;
                }
            }

            client = clients[choice];

        } else {
            System.out.println("No client to associate please create one and add it to the trip later");
        }

        if (accommodations.length != 0) {
            String id = choiceCheckAccommodation("associate to the trip", false);
            int choice = 0;

            for  (int i = 0; i < accommodations.length; i++) {
                if (accommodations[i].getAccommodationId().equalsIgnoreCase(id)) {
                    choice = i;
                }
            }

            accomodation = accommodations[choice];

        } else {
            System.out.println("No accommodation to associate please create one and add it to the trip later");
        }

        if (transportations.length != 0) {
            String choice = choiceCheckTransportation("associate to the trip", false);
            int index = 0;

            for  (int i = 0; i < transportations.length; i++) {
                if (choice.equalsIgnoreCase(transportations[i].getTransportId())) {
                    index = i;
                }
            }

            transportation = transportations[index];
        } else {
            System.out.println("No transportation to associate please create one and add it to the trip later");
        }

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

    /**
     * editTripInformation(): Allows user to modify an existing trip.
     * Updates destination, duration, price, accommodation, and transportation.
     */
    public static void editTripInformation() {
        if (trips.length != 0) {
            String choice = choiceCheckTrip("edit", true);

            int choiceTrip = 0;

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

                int choiceAccommodation = 0;
                int choiceTransportation = 0;

                String accId;
                String transId;

                if (accommodations.length != 0) {
                    accId = choiceCheckAccommodation("associate to the trip", false);

                    for (int i = 0; i < accommodations.length; i++) {
                        if (accId.equalsIgnoreCase(accommodations[i].getAccommodationId())) {
                            choiceAccommodation = i;
                        }
                    }
                } else {
                    System.out.println("No accommodation registered add one and try again");
                }

                if (transportations.length != 0) {
                    transId = choiceCheckTransportation("associate to the trip", false);

                    for (int i = 0; i < transportations.length; i++) {
                        if (transId.equalsIgnoreCase(transportations[i].getTransportId())) {
                            choiceTransportation = i;
                        }
                    }
                } else {
                    System.out.println("No transportation registered add one and try again");
                }

                try {
                    trips[choiceTrip].setDestination(destination);
                    trips[choiceTrip].setDurationInDays(duration);
                    trips[choiceTrip].setBasePrice(price);
                    if (choiceAccommodation != accommodations.length && choiceTransportation != transportations.length) {
                        trips[choiceTrip].setAccommodation(accommodations[choiceAccommodation]);
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
            String choice = choiceCheckTrip("cancel", true);
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
            String choice = choiceCheckClient("see the trips of", true);
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

        String id = choiceCheckTransportation("remove", true);

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

        String id = choiceCheckAccommodation("remove", true);

        if (Integer.parseInt(id) != 0) {

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

    public static String choiceCheckClient(String action, boolean exit) {
        String id;
        boolean found = false;

        do {
            System.out.println("\nWhich client do you want to " + action + "?");
            if (exit) {
                System.out.print("Enter the id OR 0 to exit> ");
                id = sc.nextLine();
                if (id.equals("0"))
                    break;
            } else {
                System.out.print("Enter the id > ");
                id = sc.nextLine();
            }

            for (Client client: clients) {
                if (client.getClientId().equalsIgnoreCase(id)) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        return id;
    }

    public static String choiceCheckTrip(String action, boolean exit) {
        String id;
        boolean found = false;

        do {
            System.out.println("\nWhich trip do you want to " + action + "?");
            if (exit) {
                System.out.print("Enter the id OR 0 to exit> ");
                id = sc.nextLine();
                if (id.equals("0"))
                    break;
            } else {
                System.out.print("Enter the id > ");
                id = sc.nextLine();
            }

            for (Trip trip: trips) {
                if (trip.getTripId().equalsIgnoreCase(id)) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        return id;
    }

    public static String choiceCheckTransportation(String action, boolean exit) {
        String id;
        boolean found = false;

        do {
            System.out.println("\nWhich transportation do you want to " + action + "?");
            if (exit) {
                System.out.print("Enter the id OR 0 to exit> ");
                id = sc.nextLine();
                if (id.equals("0"))
                    break;
            } else {
                System.out.print("Enter the id > ");
                id = sc.nextLine();
            }

            for (Transportation trans: transportations) {
                if (trans.getTransportId().equalsIgnoreCase(id)) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        return id;
    }

    public static String choiceCheckAccommodation(String action, boolean exit) {
        String id;
        boolean found = false;

        do {
            System.out.println("\nWhich accommodation do you want to " + action + "?");
            if (exit) {
                System.out.print("Enter the id OR 0 to exit> ");
                id = sc.nextLine();
                if (id.equals("0"))
                    break;
            } else {
                System.out.print("Enter the id > ");
                id = sc.nextLine();
            }

            for (Accommodation acc: accommodations) {
                if (acc.getAccommodationId().equalsIgnoreCase(id)) {
                        found = true;
                        break;
                }
            }
        } while (!found);

        return id;
    }

    public static int valideIntegerInput() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please type a valid number");
            }
        }
    }

    public static double valideDoubleInput() {
        while (true) {
            try {
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Please type a valid number");
            }
        }
    }
}
