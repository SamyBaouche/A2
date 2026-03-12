package persistence;

import exceptions.InvalidTransportDataException;
import travel.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TransportFileManager {

    public static void saveTransportations(Transportation[] transports, int count, String filePath) throws IOException {

        File file = new File(filePath);

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        for (int i = 0; i < count; i++) {
            Transportation transport = transports[i];

            if (transport != null) {
                String line = "";

                String baseData = transport.getTransportId() + ";" +
                        transport.getCompanyName() + ";" +
                        transport.getDepartureCity() + ";" +
                        transport.getArrivalCity() + ";" +
                        transport.getPrice();


                if (transport instanceof Flight) {
                    Flight flight = (Flight) transport;
                    line = "FLIGHT;" + baseData + ";" + flight.getLuggageAllowance();
                } else if (transport instanceof Train) {
                    Train train = (Train) transport;
                    line = "TRAIN;" + baseData + ";" + train.getTrainType();
                } else if (transport instanceof Bus) {
                    Bus bus = (Bus) transport;
                    line = "BUS;" + baseData + ";" + bus.getNumberOfStops();
                }

                pw.println(line);
            }
        }

        pw.close();
    }

    public static int loadTransportations(Transportation[] transports, String filePath) throws IOException {


        int count = 0;
        File file = new File(filePath);

        if (!file.exists()) {
            ErrorLogger.log(filePath + " does not exist");
            return 0;
        }

        Scanner sc = null;

        try {
            sc = new Scanner(file);
            while (sc.hasNextLine() && count < transports.length) {
                String line = sc.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");

                if (parts.length != 7) {
                    ErrorLogger.log("Invalid line format (skipping): " + line);
                    continue;
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
                    continue;
                }

                try {
                    Transportation newTransport = null;

                    // Polymorphic loading based on the type prefix [cite: 89]
                    if (type.equals("FLIGHT")) {
                        double luggage = Double.parseDouble(parts[6].trim());
                        newTransport = new Flight(id, company, origin, destination, price, luggage);
                    } else if (type.equals("TRAIN")) {
                        String trainType = parts[6].trim();
                        newTransport = new Train(id, company, origin, destination, price, trainType);
                    } else if (type.equals("BUS")) {
                        int stops = Integer.parseInt(parts[6].trim());
                        newTransport = new Bus(id, company, origin, destination, price, stops);
                    } else {
                        ErrorLogger.log("Unknown transport type '" + type + "' in line: " + line);
                        continue;
                    }

                    transports[count] = newTransport;
                    count++;

                } catch (InvalidTransportDataException e) {
                    ErrorLogger.log("Validation error: " + e.getMessage() + " in line: " + line);
                } catch (NumberFormatException e) {
                    ErrorLogger.log("Number format error for specific transport attribute in line: " + line);
                }
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        return count;
    }
}
