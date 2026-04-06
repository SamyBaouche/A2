package persistence;

import client.Client;
import exceptions.DuplicateEmailException;
import exceptions.InvalidAccommodationDataException;
import exceptions.InvalidClientDataException;
import exceptions.InvalidTransportDataException;
import exceptions.InvalidTripDataException;
import interfaces.CsvPersistable;
import service.SmartTravelService;
import travel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GenericFileManager {

    public static <T extends CsvPersistable> void save(List<T> items, String filePath) throws IOException {
        Class<? extends CsvPersistable> clazz = resolveClassForSave(items);

        File file = new File(filePath);
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        try {
            if (clazz != null) {
                for (T item : items) {
                    writeByType(item, clazz, pw);
                }
            }
        } finally {
            pw.close();
        }
    }

    public static <T extends CsvPersistable> List<T> load(String filePath, Class<T> clazz) throws IOException {
        if (clazz == Trip.class) {
            List<T> loadedTrips = new ArrayList<>();
            for (Trip trip : loadTrips(filePath)) {
                loadedTrips.add(clazz.cast(trip));
            }
            return loadedTrips;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            logMissingFile(filePath, clazz);
            return new ArrayList<>();
        }

        Scanner sc = null;
        List<T> loaded = new ArrayList<>();

        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    continue;
                }

                T parsed = parseByType(line, clazz, loaded);
                if (parsed != null) {
                    loaded.add(parsed);
                }
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        return loaded;
    }

    private static void writeByType(CsvPersistable item, Class<? extends CsvPersistable> clazz, PrintWriter pw) {
        if (clazz == Client.class) {
            Client c = (Client) item;
            String line = c.getClientId() + ";" +
                    c.getFirstName() + ";" +
                    c.getLastName() + ";" +
                    c.getEmail();
            pw.println(line);
            return;
        }

        if (clazz == Accommodation.class) {
            Accommodation accommodation = (Accommodation) item;
            if (accommodation instanceof Hotel hotel) {
                String line = "HOTEL;" +
                        hotel.getAccommodationId() + ";" +
                        hotel.getName() + ";" +
                        hotel.getLocation() + ";" +
                        hotel.getPricePerNight() + ";" +
                        hotel.getStarRating();
                pw.println(line);
            } else if (accommodation instanceof Hostel hostel) {
                String line = "HOSTEL;" +
                        hostel.getAccommodationId() + ";" +
                        hostel.getName() + ";" +
                        hostel.getLocation() + ";" +
                        hostel.getPricePerNight() + ";" +
                        hostel.getSharedBeds();
                pw.println(line);
            }
            return;
        }

        if (clazz == Transportation.class) {
            Transportation transport = (Transportation) item;
            if (transport != null) {
                String line = "";
                String baseData = transport.getTransportId() + ";" +
                        transport.getCompanyName() + ";" +
                        transport.getDepartureCity() + ";" +
                        transport.getArrivalCity() + ";" +
                        transport.getPrice();

                if (transport instanceof Flight flight) {
                    line = "FLIGHT;" + baseData + ";" + flight.getLuggageAllowance();
                } else if (transport instanceof Train train) {
                    line = "TRAIN;" + baseData + ";" + train.getTrainType() + ";" + train.getSeatClass();
                } else if (transport instanceof Bus bus) {
                    line = "BUS;" + baseData + ";" + bus.getNumberOfStops();
                }

                pw.println(line);
            }
            return;
        }

        if (clazz == Trip.class) {
            Trip t = (Trip) item;
            String clientId = t.getClientAssociated().getClientId();
            String accId = "";
            if (t.getAccommodation() != null) {
                accId = t.getAccommodation().getAccommodationId();
            }

            String transId = "";
            if (t.getTransportation() != null) {
                transId = t.getTransportation().getTransportId();
            }

            String line = t.getTripId() + ";" +
                    clientId + ";" +
                    accId + ";" +
                    transId + ";" +
                    t.getDestination() + ";" +
                    t.getDurationInDays() + ";" +
                    t.getBasePrice();
            pw.println(line);
            return;
        }

        throw new IllegalArgumentException("Unsupported CSV class: " + clazz.getName());
    }

    private static <T extends CsvPersistable> T parseByType(String line, Class<T> clazz, List<T> alreadyLoaded) {
        if (clazz == Client.class) {
            String[] parts = line.split(";");
            if (parts.length != 4) {
                ErrorLogger.log("Invalid line format (skipping): " + line);
                return null;
            }

            String id = parts[0].trim();
            String firstName = parts[1].trim();
            String lastName = parts[2].trim();
            String email = parts[3].trim();

            try {
                for (T loadedClient : alreadyLoaded) {
                    if (((Client) loadedClient).getEmail().equalsIgnoreCase(email)) {
                        throw new DuplicateEmailException("Email already exists: " + email);
                    }
                }

                return clazz.cast(new Client(id, firstName, lastName, email));
            } catch (DuplicateEmailException e) {
                ErrorLogger.log("Duplicate email error: " + e.getMessage() + " in line: " + line);
                return null;
            } catch (InvalidClientDataException e) {
                ErrorLogger.log("Validation error: " + e.getMessage() + " in line: " + line);
                return null;
            }
        }

        if (clazz == Accommodation.class) {
            String[] parts = line.split(";");
            if (parts.length != 6) {
                ErrorLogger.log("Invalid accommodation line format (skipping): " + line);
                return null;
            }

            String type = parts[0].trim();
            String accommodationId = parts[1].trim();
            String name = parts[2].trim();
            String location = parts[3].trim();

            double pricePerNight;
            try {
                pricePerNight = Double.parseDouble(parts[4].trim());
            } catch (NumberFormatException e) {
                ErrorLogger.log("Invalid price format: " + line);
                return null;
            }

            try {
                if (type.equals("HOTEL")) {
                    int stars = Integer.parseInt(parts[5].trim());
                    return clazz.cast(new Hotel(accommodationId, name, location, pricePerNight, stars));
                }

                if (type.equals("HOSTEL")) {
                    int sharedBeds = Integer.parseInt(parts[5].trim());
                    return clazz.cast(new Hostel(accommodationId, name, location, pricePerNight, sharedBeds));
                }

                ErrorLogger.log("Unknown accommodation type '" + type + "' in line: " + line);
                return null;
            } catch (InvalidAccommodationDataException e) {
                ErrorLogger.log("Validation error: " + e.getMessage() + " in line: " + line);
                return null;
            } catch (NumberFormatException e) {
                ErrorLogger.log("Number format error for specific accommodation attribute in line: " + line);
                return null;
            }
        }

        if (clazz == Transportation.class) {
            String[] parts = line.split(";");
            if (parts.length != 7 && parts.length != 8) {
                ErrorLogger.log("Invalid line format (skipping): " + line);
                return null;
            }

            String type = parts[0].trim();
            String id = parts[1].trim();
            String company = parts[2].trim();
            String origin = parts[3].trim();
            String destination = parts[4].trim();

            double price;
            try {
                price = Double.parseDouble(parts[5].trim());
            } catch (NumberFormatException e) {
                ErrorLogger.log("Invalid price format: " + line);
                return null;
            }

            try {
                if (type.equals("FLIGHT")) {
                    double luggage = Double.parseDouble(parts[6].trim());
                    return clazz.cast(new Flight(id, company, origin, destination, price, luggage));
                }

                if (type.equals("TRAIN")) {
                    String trainType = parts[6].trim();
                    String seatClass = parts.length == 8 ? parts[7].trim() : "Economy";
                    return clazz.cast(new Train(id, company, origin, destination, price, trainType, seatClass));
                }

                if (type.equals("BUS")) {
                    int stops = Integer.parseInt(parts[6].trim());
                    return clazz.cast(new Bus(id, company, origin, destination, price, stops));
                }

                ErrorLogger.log("Unknown transport type '" + type + "' in line: " + line);
                return null;
            } catch (InvalidTransportDataException e) {
                ErrorLogger.log("Validation error: " + e.getMessage() + " in line: " + line);
                return null;
            } catch (NumberFormatException e) {
                ErrorLogger.log("Number format error for specific transport attribute in line: " + line);
                return null;
            }
        }

        throw new IllegalArgumentException("Unsupported CSV class: " + clazz.getName());
    }

    private static List<Trip> loadTrips(String filePath) {
        Scanner inFile = null;
        boolean fileFound = true;
        List<Trip> trips = new ArrayList<>();

        try {
            inFile = new Scanner(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            ErrorLogger.log("Couldn't find the file " + filePath);
            fileFound = false;
        }

        if (fileFound) {
            while (inFile.hasNextLine()) {
                String line = inFile.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.split(";");
                if (data.length != 7) {
                    ErrorLogger.log("Data size invalid at line: " + line);
                    continue;
                }

                String tripId = data[0];
                String clientId = data[1];
                String accId = data[2];
                String transId = data[3];
                String destination = data[4];

                int duration;
                try {
                    duration = Integer.parseInt(data[5]);
                } catch (NumberFormatException e) {
                    continue;
                }

                double price;
                try {
                    price = Double.parseDouble(data[6]);
                } catch (NumberFormatException e) {
                    continue;
                }

                Client client = null;
                for (Client current : SmartTravelService.getClients()) {
                    if (current != null && current.getClientId().equals(clientId)) {
                        client = current;
                        break;
                    }
                }

                if (client == null) {
                    ErrorLogger.log("Client doesn't exist at line: " + line);
                    continue;
                }

                Accommodation accommodation = null;
                for (Accommodation current : SmartTravelService.getAccommodations()) {
                    if (current != null && current.getAccommodationId().equals(accId)) {
                        accommodation = current;
                        break;
                    }
                }
                if (accommodation == null) {
                    ErrorLogger.log("Accomodation doesn't exist at line: " + line);
                }

                Transportation transportation = null;
                for (Transportation current : SmartTravelService.getTransportations()) {
                    if (current != null && current.getTransportId().equals(transId)) {
                        transportation = current;
                        break;
                    }
                }
                if (transportation == null) {
                    ErrorLogger.log("Transportation doesn't exist at line: " + line);
                }

                if (accommodation == null && transportation == null) {
                    ErrorLogger.log("Trip " + tripId + " failed: No accommodation or transportation found.");
                    continue;
                }

                try {
                    Trip trip = new Trip(tripId, destination, duration, price, client, accommodation, transportation);
                    trips.add(trip);
                    Trip.updateIdCounter(tripId);
                } catch (InvalidTripDataException e) {
                    ErrorLogger.log("Trip " + tripId + "failed: " + e.getMessage());
                }
            }

            inFile.close();
        }

        return trips;
    }

    private static <T extends CsvPersistable> void logMissingFile(String filePath, Class<T> clazz) {
        if (clazz == Client.class) {
            ErrorLogger.log("Client file does not exist: " + filePath);
        } else if (clazz == Accommodation.class) {
            ErrorLogger.log("Accommodations file does not exist: " + filePath);
        } else if (clazz == Transportation.class) {
            ErrorLogger.log(filePath + " does not exist");
        } else if (clazz == Trip.class) {
            ErrorLogger.log("Couldn't find the file " + filePath);
        }
    }

    private static Class<? extends CsvPersistable> resolveClassForSave(List<? extends CsvPersistable> items) {
        if (items == null) {
            return null;
        }

        CsvPersistable firstNonNull = null;
        for (CsvPersistable item : items) {
            if (item != null) {
                firstNonNull = item;
                break;
            }
        }

        if (firstNonNull == null) {
            return null;
        }

        if (firstNonNull instanceof Client) {
            return Client.class;
        }
        if (firstNonNull instanceof Accommodation) {
            return Accommodation.class;
        }
        if (firstNonNull instanceof Transportation) {
            return Transportation.class;
        }
        if (firstNonNull instanceof Trip) {
            return Trip.class;
        }

        throw new IllegalArgumentException("Unsupported CSV class: " + firstNonNull.getClass().getName());
    }

}