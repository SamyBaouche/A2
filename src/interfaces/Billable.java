package interfaces;

public interface Billable { //(Only Trip implements (use existing pricing logic))

        double getBasePrice();
        double getTotalCost();   // Includes accommodations + transportation
}