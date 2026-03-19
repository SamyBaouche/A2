package persistence;

import client.Client;
import exceptions.InvalidTripDataException;
import travel.Accommodation;
import travel.Transportation;
import travel.Trip;

import java.io.*;
import java.util.Scanner;

public class TripFileManager {
    /**
     * Saves the trips array to a CSV file.
     * Format: TripID;ClientID;AccommodationID;TransportationID;Destination;DurationDays;BasePrice
     */
    public static void saveTrips(Trip[] trips, int tripCount, String filePath) throws IOException {

        // 1. Create file and folders
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        // 2. Open file for writing
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        // 3. Loop through exactly the valid number of trips
        for (int i = 0; i < tripCount; i++) {
            Trip t = trips[i];

            // 4. Get the Client ID
            String clientId = t.getClientAssociated().getClientId();

            // 5. Get Accommodation ID
            String accId = "";
            if (t.getAccommodation() != null) {
                accId = t.getAccommodation().getAccommodationId();
            }

            // 6. Get Transportation ID
            String transId = "";
            if (t.getTransportation() != null) {
                transId = t.getTransportation().getTransportId();
            }

            // 7. Build the CSV line separated by semicolons
            String line = t.getTripId() + ";" +
                    clientId + ";" +
                    accId + ";" +
                    transId + ";" +
                    t.getDestination() + ";" +
                    t.getDurationInDays() + ";" +
                    t.getBasePrice();

            // 8. Write the line to the file
            pw.println(line);
        }

        // 9. Save and close
        pw.close();
    }

    public static int loadTrips(Trip[] trips, String filePath, Client[] clients, Accommodation[] acc, Transportation[] trans) throws IOException {
        Scanner inFile = null;
        boolean fileFound = true;

        try {
            inFile = new Scanner(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            ErrorLogger.log("Couldn't find the file " + filePath);
            fileFound = false;
        }

        if(fileFound) {

            int compteur = 0;

            while (inFile.hasNextLine()) {
                String line = inFile.nextLine();

                if (line.isEmpty())
                    continue;

                String[] data = line.split(";");

                if(data.length != 7) {
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


                Trip trip;
                Client client = null;
                Accommodation accommodation = null;
                Transportation transportation = null;

                for (int i = 0; i < clients.length; i++) {
                    if (clients[i] != null && clients[i].getClientId().equals(clientId)) {
                        client = clients[i];
                        break;
                    }
                }

                if (client == null) {
                    ErrorLogger.log("Client doesn't exist at line: " + line);
                    continue;
                }

                for (int i = 0; i < acc.length; i++) {
                    if (acc[i] != null && acc[i].getAccommodationId().equals(accId)) {
                        accommodation = acc[i];
                        break;
                    }
                }

                //Need to check if an accomodation is really needed
                //if (accommodation == null) {
                    //ErrorLogger.log("Accomodation doesn't exist at line: " + line);
                   // continue;
                //}

                for (int i = 0; i < trans.length; i++) {
                    if (trans[i] != null && trans[i].getTransportId().equals(transId)) {
                        transportation = trans[i];
                        break;
                    }
                }

                //Need to check if a transportation is really needed
                //if (transportation == null) {
                    //ErrorLogger.log("Transportation doesn't exist at line: " + line);
                    //continue;
                //}

                try {
                    trip = new Trip(tripId, destination, duration, price, client, accommodation, transportation);
                    trips[compteur] = trip;
                    compteur++;

                    Trip.updateIdCounter(tripId);
                } catch (InvalidTripDataException e) {
                    ErrorLogger.log("Trip " + tripId + "failed: " + e.getMessage());
                }
            }

            inFile.close();
        }
    }
}
