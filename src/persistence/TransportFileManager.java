package persistence;

import travel.Trip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TransportFileManager {

    /**
     * Saves the trips array to a CSV file.
     * Format: TripID;ClientID;AccommodationID;TransportationID;Destination;DurationDays;BasePrice
     */
    public static void saveTrips(Trip[] trips, int tripCount, String filePath) throws IOException { //

        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Make sure folder exists

        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        for (int i = 0; i < tripCount; i++) {
            Trip t = trips[i];

            // 1. Get the Client ID
            String clientId = t.getClientAssociated().getClientId();

            // 2. Get Acc ID
            String accId = "NONE";
            if (t.getAccommodation() != null) {
                accId = t.getAccommodation().getAccommodationId();
            }

            // 3. Get Trans ID
            String transId = "NONE";
            if (t.getTransportation() != null) {
                transId = t.getTransportation().getTransportId();
            }

            // 4. Build the CSV line separated by semicolons
            String line = t.getTripId() + ";" +
                    clientId + ";" +
                    accId + ";" +
                    transId + ";" +
                    t.getDestination() + ";" +
                    t.getDurationInDays() + ";" +
                    t.getBasePrice();

            pw.println(line);
        }

        pw.close(); // Save and close
    }
}
