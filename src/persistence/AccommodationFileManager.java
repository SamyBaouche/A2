package persistence;

import travel.Accommodation;
import travel.Hostel;
import travel.Hotel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AccommodationFileManager {

    /**
     * Saves the accommodations array to a CSV file.
     */
    public static void saveAccommodations(Accommodation[] accoms, int accomCount, String filePath) throws IOException {

        // 1. Create file and make sure the folder exists
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        // 2. Open the file for writing
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        // 3. Loop through exactly the valid number of accommodations
        for (int i = 0; i < accomCount; i++) {
            Accommodation a = accoms[i];

            // 4. Check if it is a Hotel or a Hostel so we can format it correctly
            if (a instanceof Hotel) {
                Hotel h = (Hotel) a;
                String line = "HOTEL;" +
                        h.getAccommodationId() + ";" +
                        h.getName() + ";" +
                        h.getLocation() + ";" +
                        h.getPricePerNight() + ";" +
                        h.getStars();
                pw.println(line);
            }
            else if (a instanceof Hostel) {
                Hostel hs = (Hostel) a;
                String line = "HOSTEL;" +
                        hs.getAccommodationId() + ";" +
                        hs.getName() + ";" +
                        hs.getLocation() + ";" +
                        hs.getPricePerNight() + ";" +
                        hs.getSharedBeds();
                pw.println(line);
            }
        }

        // 5. Save and close
        pw.close();
    }
}
