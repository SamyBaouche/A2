package interfaces;

public interface CsvPersistable {
    // (Client, Trip, Transportation, Accommodation)

    // CSV string matching A2 format. ("C1001;Sophia;Rossi;sophia@example.com")
    String toCsvRow();
}