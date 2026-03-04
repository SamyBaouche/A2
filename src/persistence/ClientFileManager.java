package persistence;

import client.Client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientFileManager {

    /**
     * Saves the clients array to a CSV file exactly as required.
     */
    public static void saveClients(Client[] clients, int clientCount, String filePath) throws IOException {

        // 1. Create a File object
        File file = new File(filePath);

        // 2. Make sure the folders exist (like output/data/) so it doesn't crash
        file.getParentFile().mkdirs();

        // 3. Open the file for writing
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        // 4. Loop exactly from 0 to clientCount
        for (int i = 0; i < clientCount; i++) {

            // Get the current client
            Client c = clients[i];

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
}
