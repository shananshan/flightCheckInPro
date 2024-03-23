import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    // Attributes to store flight details.
//    String flightNum; // Unique flight number.
    int numOfPassengers; // Current number of passengers on the flight.
    int maximumPassengers; // Maximum number of passengers 
    double maximumBaggageWeight; // Maximum total weight of baggage 
    double maxbaggageVolume; // Maximum volume of baggage allowed per passenger.
    double extraVolumeFee; // Fee for each unit volume of baggage exceeding the maximum allowed.
    double extraWeightFee; // Fee for each unit weight of baggage exceeding the maximum allowed.

    double maxFlightVolume; // Maximum total volume of baggage allowed on the flight.
    double maxFlightWeight; // Maximum total weight of baggage allowed on the flight.
    
	String flightCode;
	String destination;
	String carrier;
	String TakeoffTime;

//   static LocalTime flightTime = LocalTime.now();
//    ReadFiles fcs = new ReadFiles();


    // List to store passengers on the flight.
    List<Passenger> passengerList;

    
    Flight(String []data) {
        passengerList = new ArrayList<>();
        flightCode = data[0];
        destination= data[2];
        carrier = data[3];

        maximumPassengers = Integer.parseInt(data[4]);
        maximumBaggageWeight = Double.parseDouble(data[6]);
        maxbaggageVolume = Double.parseDouble(data[7]);

        extraVolumeFee = Double.parseDouble(data[10]);
        extraWeightFee = Double.parseDouble(data[8]);

        maxFlightWeight = Double.parseDouble(data[11]);
        maxFlightVolume = Double.parseDouble(data[12]);
        TakeoffTime = data[13];
        
    }

	public static float calulatefee(float dimension, float weight) {
        // calculate the extra fee...
		
        return 0;
    }
	
	public static float calulateload(float dimension, float weight) {
        // calculate the load percentage...
        return 0;
    }
	
	 public double getmaximumBaggageWeight() {
		 return maxFlightWeight;
		 
	    }
	 
	 public double getmaxbaggageVolumet() {
		  return maxFlightVolume;
	    }
	 public String gettakeoftime() {
		  return TakeoffTime;
	    }
	
	
	 public String toString() {
	        return String.format(flightCode + " , " + destination + " , " + carrier + " , " + maximumPassengers + " , " + maximumBaggageWeight + " , " + maxbaggageVolume+" ,"
	        		+ +extraVolumeFee+","+ extraWeightFee+" , "+maxFlightWeight+" , " +maxFlightVolume+" , "+TakeoffTime);
	    }
}
