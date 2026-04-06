package driver;

import client.Client;
import exceptions.*;
import persistence.ErrorLogger;
import travel.*;

import service.SmartTravelService;
import visualization.DashboardGenerator;

import java.io.IOException;
import java.util.Scanner;

//-----------------------------------------------------
// Assignment 2
// Question: 1 (only one)
// Written by: Mohammed El Ouaabani 40338377, Samy Baouche 40336149
//-----------------------------------------------------

/**This code is a smart travel tool that helps the user manage clients, trips, transportations and accommodations
 * in a simple way with a user friendly UI
 * The user can also troubleshoot the driver and all the importants methods by choosing the predefined scenario options
 */

/**
 * SmartTravelDriver
 *
 * Main driver class for Smart Travel Planner application.
 * Handles user interaction via menu or predefined scenario.
 */
public class SmartTravelDriver {

    static Scanner sc = new Scanner(System.in);

    /**
     * SmartTravelDriver
     *
     * Main driver class for Smart Travel Planner application.
     * Handles user interaction via menu or predefined scenario.
     */
    public static void main(String[] args) throws InvalidAccommodationDataException, InvalidTripDataException, InvalidTransportDataException, InvalidClientDataException {

        System.out.println();
        System.out.println("Welcome to SmartTravelPlanner by: \n" +
                "   Mohammed El Ouaabani and Samy Baouche \n");

        int optionChoice;

        do {
            System.out.print("What would you like to access: \n" +
                    "   1- Menu-driven interface \n" +
                    "   2- Predefined testing scenario \n" +
                    "Enter your choice > ");

            optionChoice = SmartTravelService.valideIntegerInput();
        } while (optionChoice < 1 || optionChoice > 2);

        switch (optionChoice) {

            //Menu driven interface
            case 1 -> {

                int choiceMenu;

                do {
                    choiceMenu = mainMenu();

                    switch (choiceMenu) {
                        case 1 -> {
                            clientManagement();
                        }

                        case 2 -> {
                            tripManagement();
                        }

                        case 3 -> {
                            transportationManagement();
                        }
                        case 4 -> {
                            accommodationManagement();
                        }

                        case 5 -> {
                            SmartTravelService.additionalOperations();
                        }

                        case 6 -> {
                            SmartTravelService.listAllTrips();
                        }

                        case 7 -> {
                            SmartTravelService.advancedAnalyticsMenu();
                        }

                        case 8 -> {
                            SmartTravelService.loadAllData();
                        }

                        case 9 -> {
                            SmartTravelService.saveAllData();
                        }

                        case 10 -> {
                            predefinedScenario();
                        }

                        case 11 -> {
                            try {
                                visualization.DashboardGenerator.generateDashboard();
                            } catch (IOException e) {
                                ErrorLogger.log(e.getMessage());
                            }
                        }

                        case 0 -> {
                            System.out.println("Thank you for using our Smart Travel Planner");
                        }
                    }
                } while (choiceMenu != 0);

            }

            //Predefined testing scenario
            case 2 -> {
                predefinedScenario();
            }
        }

    }

    /**
     * Prints the main menu and returns the user's choice.
     */
    public static int mainMenu() {

        int choice;

        do {
            System.out.println();
            System.out.println("-- Choose one of the operations below -- ");
            System.out.println();
            System.out.println("1. Client Management");
            System.out.println("2. Trip Management");
            System.out.println("3. Transportation Management");
            System.out.println("4. Accomodation Management");
            System.out.println("5. Additional Operations");
            System.out.println("6. List All Data Summary");
            System.out.println("7. Advanced Analytics");
            System.out.println("8. Load All Data");
            System.out.println("9. Save All Data");
            System.out.println("10. Run Predefined Scenario");
            System.out.println("11. Generate Dashboard");
            System.out.println("0. Exit");
            System.out.print("> ");

            choice = SmartTravelService.valideIntegerInput();
        } while (choice < 0 || choice > 11);

        return choice;
    }

    /**
     * clientManagement(): Menu specifically for managing clients.
     * Options:
     * 1 - Add a client
     * 2 - Edit a client
     * 3 - Delete a client
     * 4 - Display all clients
     * 0 - Return to main menu
     */

    public static void clientManagement() {
        int choice;
        do {
            System.out.println("-- Client Management --\n");
            System.out.println("1. Add a client\n" +
                    "2. Edit a client\n" +
                    "3. Delete a client\n" +
                    "4. List all clients\n" +
                    "0. Exit to main menu");
            System.out.print("> ");

            choice = SmartTravelService.valideIntegerInput();
        } while (choice < 0 || choice > 4);

        switch (choice) {
            case 1 -> {
                SmartTravelService.addClient();
            }

            case 2 -> {
                SmartTravelService.editClient();
            }

            case 3-> {
                SmartTravelService.deleteClient();
            }

            case 4 -> {
                SmartTravelService.displayClients();
            }

            case 0 -> {}
        }
    }

    /**
     * tripManagement(): Displays the trip management menu.
     * Options allow creating, editing, canceling, listing trips, or returning to main menu.
     */
    public static void tripManagement() {
        int choice;
        do {
            System.out.println("-- Trip Management --\n");
            System.out.println("1. Create Trip\n" +
                    "2. Edit Trip\n" +
                    "3. Cancel Trip\n" +
                    "4. List all Trips\n" +
                    "5. List Trips for Client\n" +
                    "0. Exit to main menu");
            System.out.print("> ");

            choice = SmartTravelService.valideIntegerInput();
        } while (choice < 0 || choice > 5);

        switch (choice) {
            case 1 -> {
                SmartTravelService.createTrip();
            }

            case 2 -> {
                SmartTravelService.editTripInformation();
            }

            case 3-> {
                SmartTravelService.cancelTrip();
            }

            case 4 -> {
                SmartTravelService.listAllTrips();
            }

            case 5 -> {
                SmartTravelService.listAllTripsByClient();
            }

            case 0 -> {}

        }


    }

    /**
     * transportationManagement(): Displays transportation menu.
     * Allows adding, removing, listing transportation options or returning to main menu.
     */
    public static void transportationManagement() {

        int choice;
        do {
            System.out.println("-- Transportation Management --\n");
            System.out.println("1. Add a transportation option\n" +
                    "2. Remove a transportation option\n" +
                    "3. List transportation options by type (Flight, Train, Bus) \n" +
                    "0. Exit to main menu");
            System.out.print("> ");

            choice = SmartTravelService.valideIntegerInput();
        } while (choice < 0 || choice > 3);

        switch (choice) {
            case 1 -> {
                SmartTravelService.addTransportation();
            }

            case 2 -> {
                SmartTravelService.removeTransportation();
            }

            case 3-> {
                SmartTravelService.listTransportationOptions();
            }

            case 0 -> {}
        }
    }

    /**
     * accommodationManagement(): Displays accommodation menu.
     * Allows adding, removing, listing accommodations or returning to main menu.
     */
    public static void accommodationManagement() {
        int choice;
        do {
            System.out.println("-- Accommodation Management --\n");
            System.out.println("1. Add an accommodation\n" +
                    "2. Remove an accommodation\n" +
                    "3. List accommodations by type (Hotel, Hostel)\n" +
                    "0. Exit to main menu");
            System.out.print("> ");

            choice = SmartTravelService.valideIntegerInput();
        } while (choice < 0 || choice > 3);

        switch (choice) {
            case 1 -> {
                SmartTravelService.addAccommodation();
            }

            case 2 -> {
                SmartTravelService.removeAccommodation();
            }

            case 3-> {
                SmartTravelService.listAccommodationByType();
            }

            case 0 -> {}
        }
    }

    /**
     * Runs the predefined scenario that builds sample data and shows the A2/A3 features.
     */
    public static void predefinedScenario() throws InvalidTransportDataException, InvalidAccommodationDataException, InvalidClientDataException, InvalidTripDataException {
        System.out.println("\n      1. Creating... \n" +
                "- 3 Clients \n" +
                "- 3 Trips \n" +
                "- 2 Objects of each Transportation Type \n" +
                "- 2 Objects of each Accomodation Type");

        //3 Clients
        Client client1 = null;
        Client client2 = null;
        Client client3 = null;

        try {
            client1 = new Client("Jacob", "Delaire", "jacob.delaire@gmail.com");
            client2 = new Client("Nathan", "Blo", "nathan.blo@gmailcom");
            client3 = new Client("Mohammed", "Baouche", "mohammed.baouche@gmail.com");
        } catch (InvalidClientDataException | DuplicateEmailException e) {
            System.out.println("Error: " + e.getMessage());
        }

        //Clients Array predefined
        SmartTravelService.addClientPredefined(client1);
        SmartTravelService.addClientPredefined(client2);
        SmartTravelService.addClientPredefined(client3);

        //2 Flights
        Flight flight1 = new Flight("Air Transat", "Montreal", "Fort Laudertale", 22);
        Flight flight2 = new Flight("Air Canada", "Montreal", "Barcelona", 22);

        //2 Buses
        Bus bus1 = new Bus("Chartrand", "Fort Laudertale", "Miami", 30);
        Bus bus2 = new Bus("Irizar", "Barcelona", "Marbella", 12);

        //2 Trains
        Train train1 = new Train("Trans-Canada", "Montreal", "Vancouver", "TGV", "First Class");
        Train train2 = new Train("Trans-Canada", "Montreal", "Vancouver", "TGV", "First Class");

        //Transportation Array predefined
        SmartTravelService.addTransportationPredefined(flight1);
        SmartTravelService.addTransportationPredefined(flight2);
        SmartTravelService.addTransportationPredefined(bus1);
        SmartTravelService.addTransportationPredefined(bus2);
        SmartTravelService.addTransportationPredefined(train1);
        SmartTravelService.addTransportationPredefined(train2);

        //2 Hotels
        Hotel hotel1 = new Hotel("Marriott", "Barcelona", 100, 4);
        Hotel hotel2 = new Hotel("Hilton", "Miami", 65, 3);

        //2 Hostels
        Hostel hostel1 = new Hostel("SleepInPeace", "Vancouver", 90, 3);
        Hostel hostel2 = new Hostel("SleepInPeace", "Vancouver", 90, 3);

        //Accommodation Array predefined
        SmartTravelService.addAccommodationPredefined(hotel1);
        SmartTravelService.addAccommodationPredefined(hotel2);
        SmartTravelService.addAccommodationPredefined(hostel1);
        SmartTravelService.addAccommodationPredefined(hostel2);

        //3 Trips
        Trip trip1 = new Trip("Vancouver", 14, 600, client3, hostel1, train1);
        Trip trip2 = new Trip("Barcelona", 10, 400, client3, hotel2, bus1);
        Trip trip3 = new Trip("Barcelona", 20, 900, client1, hotel1, flight2);

        //Trips Array predefined
        SmartTravelService.addTripPredefined(trip1);
        SmartTravelService.addTripPredefined(trip2);
        SmartTravelService.addTripPredefined(trip3);

        System.out.println("\n      2. Display all created objects\n");

        System.out.println("Clients:");
        for (Client client: SmartTravelService.getClients()) {
            System.out.println("    " + client);
        }

        System.out.println("\nTrips:");
        for (Trip trip: SmartTravelService.getTrips()) {
            System.out.println("    " + trip);
        }

        System.out.println("\nTransportation Options:");
        for (Transportation transportation: SmartTravelService.getTransportations()) {
            System.out.println("    " + transportation);
        }

        System.out.println("\nAccommodations");
        for (Accommodation accommodation: SmartTravelService.getAccommodations()) {
            System.out.println("    " + accommodation);
        }

        System.out.println("\n      3. Test of the equals method:\n");
        System.out.println("-- Equals of a Flight and a Train");
        System.out.println(flight1.equals(train1));
        System.out.println("-- Equals of two Hotels (Different attributes)");
        System.out.println(hotel1.equals(hotel2));
        System.out.println("-- Equals of two Trains (Identical attributes)");
        System.out.println(train1.equals(train2));

        System.out.println("\n      4. Cost of the Trips\n");
        for (Trip trip: SmartTravelService.getTrips()) {
            System.out.println(trip.getTripId() + ": " + trip.calculateTotalCost() + "$");
        }

        System.out.println("\n      5. Most expensive Trip\n" );
        SmartTravelService.mostExpensiveTrip(SmartTravelService.getTrips());

        System.out.println("\n      6. Deep copie of the Transportation Array\n");

        System.out.println("Making the deep copy...\n");
        Transportation[] transportationsCopy = SmartTravelService.copyTransportationArray(SmartTravelService.getTransportations());

        System.out.println("-- Displaying both Arrays --\n");
        System.out.println("Original");
        for (Transportation transportation: SmartTravelService.getTransportations()) {
            System.out.println("    " + transportation);
        }

        System.out.println("\nCopy");
        for (Transportation transportation: transportationsCopy) {
            System.out.println("    " + transportation);
        }

        System.out.println("\nModify the copy Array...\n");
        transportationsCopy[0].setCompanyName("Unknown Company");

        System.out.println("-- Display both Arrays AFTER MODIFICATION --\n");

        System.out.println("Original");
        for (Transportation transportation: SmartTravelService.getTransportations()) {
            System.out.println("    " + transportation);
        }

        System.out.println("\nCopy");
        for (Transportation transportation: transportationsCopy) {
            System.out.println("    " + transportation);
        }

        //Exceptions test
        System.out.println("\n      7. Exceptions test\n");

        System.out.println("-- Creating a client with an invalid First Name --");
        try {
            Client invalidName = new Client("jbqibubauibfuibwquicbauiBCUIBSADUIbcuiabCUIABUICBAUIUBUIbiuovipvuvpuvuvpivivipvipvipvivivivuvuiv", "Doe", "Doe@gmail.com");
        } catch (InvalidClientDataException | DuplicateEmailException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Try finding for a Client ID that doesn't exist --");
        try {
            // Attempting to find a Client ID that was never added to the Service array
            SmartTravelService.findClientById("fake id");
        } catch (EntityNotFoundException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Creating a client with invalid email --");
        try {
            Client invalidEmailClient = new Client("J", "D", "jd.com");
            SmartTravelService.addClientPredefined(invalidEmailClient);
        } catch (InvalidClientDataException | DuplicateEmailException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Creating a client with duplicate email --");
        try {
            Client dupeClient = new Client("Jacob", "Two", "jacob.delaire@gmail.com");
            SmartTravelService.addClientPredefined(dupeClient);
        } catch (DuplicateEmailException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Creating a trip with negative price --");
        try {
            Trip invalidTrip = new Trip("Montreal", 5, -100, client1, hotel1, flight1);
        } catch (InvalidTripDataException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Creating a bus with zero stops --");
        try {
            Bus invalidBus = new Bus("Test-Bus", "Montreal", "Ottawa", 0);
        } catch (InvalidTransportDataException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Flight with negative baggage allowance --");
        try {
            Flight invalidFlight = new Flight("Air Canada", "Montreal", "New York", -5);
        } catch (InvalidTransportDataException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Creating a hostel that's too expensive --");
        try {
            Hostel invalidHostel = new Hostel("Test Hostel", "Paris", 200.00, 5);
        } catch (InvalidAccommodationDataException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Creating a hotel with zero stars --");
        try {
            Hotel invalidHotel = new Hotel("Test Hotel", "Paris", 50.00, 0);
        } catch (InvalidAccommodationDataException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Creating a hotel with negative pricePerNight --");
        try {
            Hotel invalidHotel2 = new Hotel("Test Hotel", "Paris", -50.00, 3);
        } catch (InvalidAccommodationDataException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        System.out.println("\n-- Calculating cost of  a hotel with 0 days --");
        try {
            Hotel hotelTest = new Hotel("Test Hotel", "Paris", 50.00, 3);
            hotelTest.calculateCost(0);
        } catch (InvalidAccommodationDataException e) {
            ErrorLogger.log("Error: " + e.getMessage());
        }

        //Save All Data
        System.out.println("\n      8. Saving all data to files\n");
        SmartTravelService.saveAllData();

        //Clear all data from memory for test
        SmartTravelService.clearAllData();

        //Display all data after clearing to confirm it's empty
        System.out.println("\n      9. Displaying all data after clearing \n");
        SmartTravelService.displayClients();
        SmartTravelService.listAllTrips();
        SmartTravelService.listTransportationOptions();
        SmartTravelService.listAccommodationByType();

        //Load All Data
        System.out.println("\n      10. Loading all data from files\n");
        SmartTravelService.loadAllData();

        //Display all data after loading to confirm it's back
        System.out.println("\n      11. Displaying all data after loading \n");
        System.out.println("Clients:");
        SmartTravelService.displayClients();
        System.out.println("\nTrips:");
        SmartTravelService.listAllTrips();
        System.out.println("\nTransportation Options:");
        SmartTravelService.listTransportationOptions();
        System.out.println("\nAccommodations");
        SmartTravelService.listAccommodationByType();

        //Generate Dashboard
        System.out.println("\n      12. Generating Dashboard\n");
        try {
            DashboardGenerator.generateDashboard();
        } catch (IOException e) {
            ErrorLogger.log(e.getMessage());
        }
    }
}

