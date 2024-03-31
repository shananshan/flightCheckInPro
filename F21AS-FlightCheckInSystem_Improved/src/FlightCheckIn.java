import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observer;

	
	class FlightStats {
	    int passengerCount;
	    double totalWeight;
	    double totalVolume;
	    double weighthold;
	    double sizehold;
	    double passengerhold;
	    double maxBagVolume;
	    double maxBagWeight;
	    ReadFiles fcs = new ReadFiles();
	    
	   
	    // 鏋勯�犲嚱鏁�
	    public FlightStats() {
	        this.passengerCount = 0;
	        this.totalWeight = 0.0;
	        this.totalVolume = 0.0;
	        this. weighthold = 0.0;
		    this.sizehold = 0.0;
		    this.passengerhold = 0.0;
	    }

	    // 娣诲姞涔樺鏃舵洿鏂扮粺璁′俊鎭�
	    public void addPassenger(double weight, double volume) {
	        this.passengerCount++;
	        this.totalWeight += weight;
	        this.totalVolume += volume;
	    }

	    public int getPassengerCount() {
	        return passengerCount;
	    }

	    public double getTotalWeight() {
	        return totalWeight;
	    }

	    public double getTotalVolume() {
	        return totalVolume;
	    }
	    
	    public double getWeightHold(String flightNumber) {
	    	Flight f  = fcs.getFlight(fcs.getFlightList(),flightNumber);
	    	weighthold  = (totalWeight / f.getmaximumBaggageWeight() ) * 100;
	        return weighthold;
	    }
	    
	    public double getSizeHold(String flightNumber) {
	    	Flight f  = fcs.getFlight(fcs.getFlightList(),flightNumber);
//	    	System.out.print(f);
	    	sizehold  = (totalVolume / f.getmaxbaggageVolumet()) * 100;
	        return sizehold;
	    }
	    
		public double getPassengerHold(String flightNumber) {
			Flight f =  fcs.getFlight(fcs.getFlightList(),flightNumber);
			passengerhold = (passengerCount / f.getmaxpassenger()) * 100;
			return passengerhold;
		}
		
		public double getMaxLuggageHold(String flightNumber) {
			Flight f =  fcs.getFlight(fcs.getFlightList(),flightNumber);
//			System.out.println("size:"+ getSizeHold(flightNumber) );
//			System.out.println("weight:"+ getWeightHold(flightNumber) );
			if(getSizeHold(flightNumber) > getWeightHold(flightNumber)) {
				return getSizeHold(flightNumber);
			}else {
				return getWeightHold(flightNumber);
			}
		}
	    
	}

	
	
	public class FlightCheckIn {
	    private Map<String, FlightStats> flightMap;
	    ReadFiles fcs = new ReadFiles();
	    
	    // 鏋勯�犲嚱鏁�
	    public FlightCheckIn() {
	        this.flightMap = new HashMap<>();
	    }

	    // 澶勭悊涔樺checkin鐨勬柟娉�
	    public void checkInPassenger(String flightNumber, double weight, double volume) {
	        FlightStats stats = flightMap.getOrDefault(flightNumber, new FlightStats());
	        stats.addPassenger(weight, volume);
	        flightMap.put(flightNumber, stats);
	        
	    }
	    
	    public Map<String, FlightStats> getFlightMap() {
	        return flightMap;
	    }
	    // 鎵撳嵃鎵�鏈夎埅鐝殑缁熻淇℃伅
	    public void printFlightStats() throws IOException {
	    	fcs.readFlights("Flight Detail.csv");
	        for(Map.Entry<String, FlightStats> entry : flightMap.entrySet()) {
	            String flightNumber = entry.getKey();
	            FlightStats stats = entry.getValue();
	            System.out.println("Flight Number: " + flightNumber);
	            System.out.println("Passenger Count: " + stats.getPassengerCount());
//	            System.out.println("Total Weight: " + stats.getTotalWeight());
//	            System.out.println("Total Volume: " + stats.getTotalVolume());
	            System.out.println("Weight Hold : " + stats.getWeightHold(flightNumber)+"%");
	            System.out.println("Volume Hold : " + stats.getSizeHold(flightNumber)+"%");
	            System.out.println("Maximum Luggage Hold : " + stats.getMaxLuggageHold(flightNumber)+"%");
	            System.out.println("Passenger Hold : " + stats.getPassengerHold(flightNumber)+"%");
	            
	        
	        }
			
	    }
	}
