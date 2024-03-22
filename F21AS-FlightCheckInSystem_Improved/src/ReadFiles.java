import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFiles {
	//constructor to initialize List of passengers' name and flight code
	public ReadFiles() {
		passengerList = new ArrayList<>();
		flightList = new ArrayList<>();
	}
	
	// declare instance variable as static to make it accessible across all instances of the class
    static List<Passenger> passengerList;
    static List<Flight> flightList;


    public void readPassengers(String csvFilePath) throws IOException {
//    	List<Passenger> passenegerName = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String line = reader.readLine(); // Assume the first line contains headers and skip it
        while ((line = reader.readLine())!= null) {
            String[] data = line.split(",");
            passengerList.add(new Passenger(data));
        }
        reader.close();
    }
    
    public void readFlights(String csvFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String line = reader.readLine(); // Assume the first line contains headers and skip it
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            flightList.add(new Flight(data));
        }
        reader.close();
    }
    
    public List<Passenger> getPassengerList() {
        return passengerList;
    }
    
    public List<Flight> getFlightList() {
        return flightList;
    }
     public Flight getFlight(String code) {
        for(Flight f: flightList) {
            if(Objects.equals(f.flightCode, code))
                return f;
        }
        return null;
    }
   
}
