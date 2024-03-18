import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    // Attributes to store flight details.
    String flightNum; // Unique flight number.
    int numOfPassengers; // Current number of passengers on the flight.
    static int maximumPassengers; // Maximum number of passengers 
    double maximumBaggageWeight; // Maximum total weight of baggage 
    double maxbaggageVolume; // Maximum volume of baggage allowed per passenger.
    double extraVolumeFee; // Fee for each unit volume of baggage exceeding the maximum allowed.
    double extraWeightFee; // Fee for each unit weight of baggage exceeding the maximum allowed.

    static double maxFlightVolume; // Maximum total volume of baggage allowed on the flight.
    static double maxFlightWeight; // Maximum total weight of baggage allowed on the flight.
    
	String flightCode;
	String destination;
	String carrier;

    static LocalTime flightTime = LocalTime.now();


    // List to store passengers on the flight.
    List<Passenger> passengerList;
    
    public Flight(String[] data) {
		// TODO Auto-generated constructor stub
        passengerList = new ArrayList<>();
        flightCode = data[0];
        destination = data[2];
        carrier = data[3];
	}

	public static float calulatefee(float dimension, float weight) {
        // calculate the extra fee...
		
        return 0;
    }
	
	public static float calulateload(float dimension, float weight) {
        // calculate the load percentage...
        return 0;
    }

	public static LocalTime getFlightTime() {
		// TODO Auto-generated method stub
        flightTime = flightTime.withHour(5).withMinute(30).withSecond(20); // Set a hypothetical current time

        return flightTime;
	}
}
