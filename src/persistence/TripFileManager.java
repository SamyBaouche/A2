package persistence;

import travel.Trip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
}
