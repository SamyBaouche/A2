package persistence;

import exceptions.InvalidAccommodationDataException;
import travel.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class AccommodationFileManager {

    /**
     * Saves the accommodations array to a CSV file.
     */
    public static void saveAccommodations(Accommodation[] accoms, String filePath) throws IOException {

        // 1. Create file and make sure the folder exists
        File file = new File(filePath);
        //file.getParentFile().mkdirs();

        // 2. Open the file for writing
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        // 3. Loop through exactly the valid number of accommodations
        for (int i = 0; i < accoms.length; i++) {
            Accommodation accommodation = accoms[i];

            // 4. Check if it is a Hotel or a Hostel so we can format it correctly
            if (accommodation instanceof Hotel) {
                Hotel hotel = (Hotel) accommodation;
                String line = "HOTEL;" +
                        hotel.getAccommodationId() + ";" +
                        hotel.getName() + ";" +
                        hotel.getLocation() + ";" +
                        hotel.getPricePerNight() + ";" +
                        hotel.getStarRating();
                pw.println(line);
            }
            else if (accommodation instanceof Hostel) {
                Hostel hostel = (Hostel) accommodation;
                String line = "HOSTEL;" +
                        hostel.getAccommodationId() + ";" +
                        hostel.getName() + ";" +
                        hostel.getLocation() + ";" +
                        hostel.getPricePerNight() + ";" +
                        hostel.getSharedBeds();
                pw.println(line);
            }
        }

        // 5. Save and close
        pw.close();
    }

    public static Accommodation[] loadAccommodations(String filePath) throws IOException {

        Accommodation[] accoms = new Accommodation[0];
        File file = new File(filePath);

        if (!file.exists()) {
            ErrorLogger.log("Accommodations file does not exist: " + filePath);
            return new Accommodation[0];
        }

        Scanner sc = null;



        try {
            sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");

                if (parts.length != 6) {
                    ErrorLogger.log("Invalid accommodation line format (skipping): " + line); //
                    continue;
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
                    continue;
                }

                try {
                    Accommodation newAccommodation;

                    if (type.equals("HOTEL")) {
                        int stars = Integer.parseInt(parts[5].trim());
                        newAccommodation = new Hotel(accommodationId, name, location, pricePerNight, stars);
                    } else if (type.equals("HOSTEL")) {
                        int sharedBeds = Integer.parseInt(parts[5].trim());
                        newAccommodation = new Hostel(accommodationId, name, location, pricePerNight, sharedBeds);
                    } else {
                        ErrorLogger.log("Unknown accommodation type '" + type + "' in line: " + line);
                        continue;
                    }

                    if (accoms.length == 0) {
                        Accommodation[] temp = new Accommodation[1];
                        temp[0] = newAccommodation;
                        accoms = temp;
                    } else {
                        Accommodation[] temp = new Accommodation[accoms.length + 1];
                        for (int i = 0; i < accoms.length; i++) {
                            temp[i] = accoms[i];
                        }

                        temp[temp.length - 1] = newAccommodation;
                        accoms = temp;
                    }
                } catch (InvalidAccommodationDataException e) { //
                    ErrorLogger.log("Validation error: " + e.getMessage() + " in line: " + line); //
                } catch (NumberFormatException e) {
                    ErrorLogger.log("Number format error for specific accommodation attribute in line: " + line); //
                }
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
        return accoms;
    }


}
