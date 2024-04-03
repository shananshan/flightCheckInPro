import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReadFiles {
    //constructor to initialize List of passengers' name and flight code
    public ReadFiles() {
        passengerList = new ArrayList<>();
        flightList = new ArrayList<>();
    }

    // declare instance variable as static to make it accessible across all instances of the class
    static List<Passenger> passengerList;
    static List<Flight> flightList;
    static LocalTime flightTime = LocalTime.now();

    //Reads passenger data from a CSV file and populates the passengerList
//    public void readPassengers(String csvFilePath) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
//        String line = reader.readLine(); // Assume the first line contains headers and skip it
//        while ((line = reader.readLine()) != null) {
//            String[] data = line.split(",");
//            Passenger p = new Passenger(data);
//            passengerList.add(p);
//        }
//        reader.close();
//    }
//
//    //Reads flight data from a CSV file and populates the flightList.
//    public void readFlights(String csvFilePath) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
//        String line = reader.readLine(); // Assume the first line contains headers and skip it
//        while ((line = reader.readLine()) != null) {
//            String[] data = line.split(",");
//            flightList.add(new Flight(data));
//        }
//        reader.close();
//    }
    
     public void readPassengers(String resourcePath) throws IOException {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = reader.readLine(); // Skip the first line with headers
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            Passenger p = new Passenger(data); // Assume you have a suitable constructor
            passengerList.add(p);
        }
    }

     public void readFlights(String resourcePath) throws IOException {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = reader.readLine(); // Skip the first line with headers
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            flightList.add(new Flight(data)); // Assume you have a suitable constructor
        }
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public List<Flight> getFlightList() {
        return flightList;
    }

    //Finds and returns a Flight object from the flightList based on a flight code.
    public static Flight getFlight(List<Flight> flightList, String code) {
        Flight f1 = null;
        for (Flight f : flightList) {
            if (Objects.equals(f.flightCode, code))
                f1 = f;
        }
        return f1;
    }

    //Parses and returns the take-off time of a flight as a LocalTime object.
    public static LocalTime getFlightTime(List<Flight> flightList, String flightcode) {
        Flight f = getFlight(flightList, flightcode);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime takeOffTime = LocalTime.parse(f.TakeoffTime, formatter);
        // Get hours, minutes and seconds from takeOffTime
        int h = takeOffTime.getHour();
        int m = takeOffTime.getMinute();
        int s = takeOffTime.getSecond();
        flightTime = flightTime.withHour(h).withMinute(m).withSecond(s);
        return flightTime;
    }
}
