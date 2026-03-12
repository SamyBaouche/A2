package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorLogger {

    // Log file path specified in the assignment
    private static final String LOG_FILE_PATH = "output/logs/errors.txt";

    /**
     * Appends an error message to the log file.
     * @param message The error message to log.
     */
    public static void log(String message) {
        // Ensure the directories exist before writing
        File logFile = new File(LOG_FILE_PATH);
        logFile.getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.println(message);
        } catch (IOException e) {
            System.out.println("Critical failure: Unable to write to error log. " + e.getMessage());
        }
    }
}

