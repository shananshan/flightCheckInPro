import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public static Flight getFlight(List<Flight> flightList,String code) {
        Flight f1 = null ;
        for(Flight f: flightList) {
            if(Objects.equals(f.flightCode, code))
                f1 = f;
        }
        return f1;
    }

    public static LocalTime getFlightTime(List<Flight> flightList,String flightcode) {
        // TODO Auto-generated method stub
        Flight f = getFlight(flightList,flightcode);
//		System.out.println(f.flightCode);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime takeOffTime = LocalTime.parse(f.TakeoffTime, formatter);
        // 现在可以从takeOffTime获取小时、分钟和秒了
        int h = takeOffTime.getHour();
        int m = takeOffTime.getMinute();
        int s = takeOffTime.getSecond();
//	    flightTime = LocalTime.of(h, m, s);
        flightTime = flightTime.withHour(h).withMinute(m).withSecond(s);
//        flightTime = flightTime.withHour(23).withMinute(30).withSecond(20); // Set a hypothetical current time
        return flightTime;
    }
    
   
}
