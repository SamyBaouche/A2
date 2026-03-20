package persistence;

import client.Client;
import exceptions.DuplicateEmailException;
import exceptions.InvalidClientDataException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientFileManager {

    /**
     * Saves the clients array to a CSV file exactly as required.
     */
    public static void saveClients(Client[] clients, String filePath) throws IOException {

        // 1. Create a File object
        File file = new File(filePath);

        // 2. Make sure the folders exist (like output/data/) so it doesn't crash
        file.getParentFile().mkdirs();

        // 3. Open the file for writing
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        // 4. Loop exactly from 0 to clientCount
        for (Client c : clients) {

            // 5. Create a string with the exact CSV format using semicolons
            String line = c.getClientId() + ";" +
                    c.getFirstName() + ";" +
                    c.getLastName() + ";" +
                    c.getEmail();

            // 6. Write the line to the file
            pw.println(line);
        }

        // 7. Always close the file
        pw.close();
    }

    public static Client[] loadClients(String filePath) throws IOException {

        Client[] clients = new Client[0];
        File file = new File(filePath);

        if (!file.exists()) {
            ErrorLogger.log("Client file does not exist: " + filePath);
            return new Client[0];
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

                if (parts.length != 4) {
                    ErrorLogger.log("Invalid line format (skipping): " + line);
                    continue;
                }

                String id = parts[0].trim();
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                String email = parts[3].trim();

                try {
                    if (clients.length != 0) {
                        for (int i = 0; i < clients.length; i++) {
                            if (clients[i] != null && clients[i].getEmail().equalsIgnoreCase(email)) {
                                throw new DuplicateEmailException("Email already exists: " + email);
                            }
                        }
                    }

                    Client newClient = new Client(id, firstName, lastName, email);

                    Client[] temp = new Client[clients.length + 1];

                    for (int i = 0 ; i < clients.length; i++) {
                        temp[i] = clients[i];
                    }

                    temp[temp.length - 1] = newClient;

                    clients = temp;

                } catch (DuplicateEmailException e) {
                    ErrorLogger.log("Duplicate email error: " + e.getMessage() + " in line: " + line);
                } catch (InvalidClientDataException e) {
                    ErrorLogger.log("Validation error: " + e.getMessage() + " in line: " + line);
                }
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        return clients;

    }
}
