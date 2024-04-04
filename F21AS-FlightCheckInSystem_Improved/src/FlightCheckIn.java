import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class FlightStats {
    int passengerCount; // The count of passengers checked.
    double totalWeight; // Total weight of luggage 
    double totalVolume; // Total volume of luggage 
    double weighthold; // Percentage of total luggage weight.
    double sizehold; // Percentage of total luggage volume.
    double passengerhold; // Percentage of total passenger count relative to flight's max passenger capacity.
    ReadFiles fcs = new ReadFiles(); // Helper class to read flight and passenger data from files.
    
    // Constructor initializes all fields to their default values.
    public FlightStats() {
        this.passengerCount = 0;
        this.totalWeight = 0.0;
        this.totalVolume = 0.0;
        this.weighthold = 0.0;
        this.sizehold = 0.0;
        this.passengerhold = 0.0;
    }

    // Adds a passenger's luggage weight and volume to the totals.
    public void addPassenger(double weight, double volume) {
        this.passengerCount++;
        this.totalWeight += weight;
        this.totalVolume += volume;
    }

    // Returns the current passenger count.
    public int getPassengerCount() {
        return passengerCount;
    }

    // Returns the total weight of checked luggage.
    public double getTotalWeight() {
        return totalWeight;
    }

    // Returns the total volume of checked luggage.
    public double getTotalVolume() {
        return totalVolume;
    }
    
    // Calculates and returns the weight hold percentage.
    public double getWeightHold(String flightNumber) {
        Flight f = fcs.getFlight(fcs.getFlightList(),flightNumber);
        if (f != null) {
            weighthold = (totalWeight / f.getmaximumBaggageWeight()) * 100;
        }
        return weighthold;
    }
    
    // Calculates and returns the size hold percentage.
    public double getSizeHold(String flightNumber) {
        Flight f = fcs.getFlight(fcs.getFlightList(),flightNumber);
        if(f!=null){
            sizehold = (totalVolume / f.getmaxbaggageVolumet()) * 100;
        }
        return sizehold;
    }
    
    // Calculates and returns the passenger hold percentage.
    public double getPassengerHold(String flightNumber) {
        Flight f = fcs.getFlight(fcs.getFlightList(),flightNumber);
        if(f!=null) {
        	passengerhold = (passengerCount / f.getmaxpassenger()) * 100;
        }
        return passengerhold;
    }
    
    // Determines and returns the larger of size hold or weight hold percentages.
    public double getMaxLuggageHold(String flightNumber) {
        Flight f = fcs.getFlight(fcs.getFlightList(),flightNumber);
//        if(f!=null) {
        	
//        }
        return Math.max(getSizeHold(flightNumber), getWeightHold(flightNumber));
    }
}

public class FlightCheckIn {
    private Map<String, FlightStats> flightMap; // Maps flight numbers to their respective FlightStats.
    ReadFiles fcs = new ReadFiles(); // Helper class to read flight and passenger data from files.
    
    // Constructor initializes the flightMap.
    public FlightCheckIn() {
        this.flightMap = new HashMap<>();
    }

    // Adds a passenger's luggage to the specified flight's stats.
    public void checkInPassenger(String flightNumber, double weight, double volume) {
        FlightStats stats = flightMap.getOrDefault(flightNumber, new FlightStats());
        stats.addPassenger(weight, volume);
        flightMap.put(flightNumber, stats);
    }

    // Retrieves FlightStats for a specific flight number.
    public static FlightStats getFlightStats(Map<String, FlightStats> map, String flightNumber) {
        return map.get(flightNumber);
    }

    // Returns the entire map of flight numbers to their stats.
    public Map<String, FlightStats> getFlightMap() {
        return flightMap;
    }
    
    // Prints statistics for all flights.
    public void printFlightStats() throws IOException {
        fcs.readFlights("Flight Detail.csv");
        for (Map.Entry<String, FlightStats> entry : flightMap.entrySet()) {
            if (entry !=null) {
                String flightNumber = entry.getKey();
                FlightStats stats = entry.getValue();
                System.out.println("Flight Number: " + flightNumber);
                System.out.println("Passenger Count: " + stats.getPassengerCount());
//              System.out.println("Weight Hold : " + stats.getWeightHold(flightNumber) + "%");
//              System.out.println("Volume Hold : " + stats.getSizeHold(flightNumber) + "%");
                System.out.println("Maximum Luggage Hold : " + stats.getMaxLuggageHold(flightNumber) + "%");
                System.out.println("Passenger Hold : " + stats.getPassengerHold(flightNumber) + "%");
            }
        }
    }
}
