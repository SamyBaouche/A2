package service;

import client.Client;
import exceptions.InvalidClientDataException;
import travel.*;

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
        System.out.print("Enter client First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter client Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("Enter client email: ");
        String email = sc.nextLine();

        Client client = new Client(firstName, lastName, email);

        Client[] clientProcess = new Client[clients.length + 1];

        if (clients.length > 0) {
            for (int i = 0; i < clients.length; i++) {
                clientProcess[i] = clients[i];
            }
        }

        clientProcess[clientProcess.length - 1] = client;
        clients = clientProcess;
    }

    /**
     * editClient(): Allows user to select a client and update their information.
     */

    public static void editClient() throws InvalidClientDataException {
        if (clients.length == 0) {
            System.out.println("There is no client to edit.");
        } else {
            int choice = choiceCheckClient("edit", true);

            if (choice != clients.length) {

                System.out.print("Enter new client First Name > ");
                String firstName = sc.nextLine();
                clients[choice].setFirstName(firstName);

                System.out.print("Enter new client Last Name > ");
                String lastName = sc.nextLine();
                clients[choice].setLastName(lastName);

                System.out.print("Enter new client Email > ");
                String email = sc.nextLine();
                clients[choice].setEmail(email);
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
            int choice = choiceCheckClient("delete", true);

            if (choice != clients.length) {

                Client[] smallerClient;

                if ((clients.length - 1) == 0) {
                    smallerClient = new Client[0];
                } else {
                    smallerClient = new Client[clients.length - 1];

                    int compteur = 0;

                    for (int i = 0; i < clients.length; i++) {
                        if (i != choice) {
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
    public static void createTrip() throws InvalidClientDataException {

        Accommodation accomodation = null;
        Transportation transportation = null;

        System.out.print("Enter your destination: ");
        String destination = sc.nextLine();

        System.out.print("Enter duration days: ");
        int daysDuration = sc.nextInt();

        System.out.print("Enter base price: ");
        double price = sc.nextDouble();

        Client client = new Client();

        if (clients.length != 0) {
            int choice = choiceCheckClient("associate to the trip", false);

            client = clients[choice];
        } else {
            System.out.println("No client to associate please create one and add it to the trip later");
        }

        if (accommodations.length != 0) {
            int choice = choiceCheckAccommodation("associate to the trip", false);

            accomodation = accommodations[choice];
        } else {
            System.out.println("No accommodation to associate please create one and add it to the trip later");
        }

        if (transportations.length != 0) {
            int choice = choiceCheckTransportation("associate to the trip", false);

            transportation = transportations[choice];
        } else {
            System.out.println("No transportation to associate please create one and add it to the trip later");
        }

        Trip trip = new Trip (destination, daysDuration, price, client, accomodation, transportation);

        Trip[] tripsCopy = new Trip[trips.length + 1];

        for (int i = 0; i < trips.length; i++) {
            tripsCopy[i] = trips[i];
        }

        tripsCopy[tripsCopy.length - 1] = trip;

        trips = tripsCopy;

        System.out.println("New trip created successfully");
    }

    /**
     * editTripInformation(): Allows user to modify an existing trip.
     * Updates destination, duration, price, accommodation, and transportation.
     */
    public static void editTripInformation() {
        if (trips.length != 0) {
            int choice = choiceCheckTrip("edit", true);

            if (choice != trips.length) {

                System.out.print("Enter the new destination > ");
                String destination = sc.nextLine();

                System.out.print("Enter the new duration in days > ");
                int duration = sc.nextInt();

                System.out.print("Enter the new price > ");
                double price = sc.nextDouble();

                int choiceAccommodation = 0;
                int choiceTransportation = 0;

                if (accommodations.length != 0) {
                    choiceAccommodation = choiceCheckAccommodation("associate to the trip", false);
                } else {
                    System.out.println("No accommodation registered add one and try again");
                }

                if (transportations.length != 0) {
                    choiceTransportation = choiceCheckTransportation("associate to the trip", false);
                } else {
                    System.out.println("No transportation registered add one and try again");
                }

                trips[choice].setDestination(destination);
                trips[choice].setDurationInDays(duration);
                trips[choice].setBasePrice(price);
                if (choiceAccommodation != accommodations.length && choiceTransportation != transportations.length) {
                    trips[choice].setAccommodation(accommodations[choiceAccommodation]);
                    trips[choice].setTransportation(transportations[choiceTransportation]);
                }

                System.out.println("Trip edited successfully");
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
            int choice = choiceCheckTrip("cancel", true);

            Trip[] tripCopy = new Trip[trips.length - 1];

            if (choice != trips.length) {

                if (tripCopy.length != 0) {
                    int tripCopyCompteur = 0;
                    for (int i = 0; i < trips.length; i++) {
                        if (i != choice) {
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
            int choice = choiceCheckClient("see the trips of", true);

            if (choice != trips.length) {
                for (Trip trip : trips) {
                    if (trip.getClientAssociated() == clients[choice]) {
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
            type = sc.nextInt();
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
                System.out.print("Enter Luggage Allowance (kg): ");
                double luggage = sc.nextDouble();
                Transportation[] transportationCopy = new Transportation[transportations.length + 1];
                for (int i = 0; i < transportations.length; i++) {
                    transportationCopy[i] = transportations[i];
                }
                transportationCopy[transportationCopy.length - 1] = new Flight(company, departure, arrival, luggage);
                transportations = transportationCopy;
                System.out.println("Transportation added successfully.");
            }
            case 2 -> {
                System.out.print("Enter Train Type: ");
                String trainType = sc.nextLine();
                System.out.print("Enter Seat Class (Economy/First Class): ");
                String seatClass = sc.nextLine();
                Transportation[] transportationCopy = new Transportation[transportations.length + 1];
                for (int i = 0; i < transportations.length; i++) {
                    transportationCopy[i] = transportations[i];
                }
                transportationCopy[transportationCopy.length - 1] = new Train(company,departure,arrival,trainType,seatClass);
                transportations = transportationCopy;
                System.out.println("Transportation added successfully.");
            }
            case 3 -> {
                System.out.print("Enter Number of Stops: ");
                int stops = sc.nextInt();
                Transportation[] transportationCopy = new Transportation[transportations.length + 1];
                for (int i = 0; i < transportations.length; i++) {
                    transportationCopy[i] = transportations[i];
                }
                transportationCopy[transportationCopy.length - 1] = new Bus(company, departure, arrival, stops);
                transportations = transportationCopy;
                System.out.println("Transportation added successfully.");
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

        int choice = choiceCheckTransportation("remove", true);

        if (choice != transportations.length) {

            Transportation[] copyTransportations = new Transportation[transportations.length - 1];

            int compteur = 0;
            for (int i = 0; i < transportations.length; i++) {
                if (i != choice) {
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

            type = sc.nextInt();

            sc.nextLine();
        } while (type < 1 || type > 2);

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Location: ");
        String location = sc.nextLine();

        System.out.print("Price/night: ");
        double price = sc.nextDouble();

        switch (type) {
            case 1 -> {
                System.out.print("Enter Star Rating: ");
                int stars = sc.nextInt();
                Accommodation[] copyAccommodations = new Accommodation[accommodations.length + 1];
                for (int i = 0; i < accommodations.length; i++) {
                    copyAccommodations[i] = accommodations[i];
                }
                copyAccommodations[copyAccommodations.length - 1] = new Hotel(name, location, price, stars);
                accommodations = copyAccommodations;
                System.out.println("Accommodation added successfully.");
            } case 2 -> {
                System.out.print("Enter Number of Shared Beds: ");
                int beds = sc.nextInt();
                Accommodation[] copyAccommodations = new Accommodation[accommodations.length + 1];
                for (int i = 0; i < accommodations.length; i++) {
                    copyAccommodations[i] = accommodations[i];
                }
                copyAccommodations[copyAccommodations.length - 1] = new Hostel(name, location, price, beds);
                accommodations = copyAccommodations;
                System.out.println("Accommodation added successfully.");
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

        int choice = choiceCheckAccommodation("remove", true);

        if (choice != accommodations.length) {

            Accommodation[] copyAccommodations = new Accommodation[accommodations.length - 1];

            int compteur = 0;

            for (int i = 0; i < accommodations.length; i++) {
                if (i != choice) {
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
}
