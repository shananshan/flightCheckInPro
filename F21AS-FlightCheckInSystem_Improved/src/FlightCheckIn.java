import java.io.IOException;
import java.util.HashMap;
	import java.util.Map;
import java.util.Objects;

	
	class FlightStats {
	    int passengerCount;
	    double totalWeight;
	    double totalVolume;
	    double weighthold;
	    double sizehold;
	    double maxBagVolume;
	    double maxBagWeight;
	    ReadFiles fcs = new ReadFiles();
	    
	   

	    // 构造函数
	    public FlightStats() {
	        this.passengerCount = 0;
	        this.totalWeight = 0.0;
	        this.totalVolume = 0.0;
	        this. weighthold = 0.0;
		    this.sizehold = 0.0;
	    }

	    // 添加乘客时更新统计信息
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
	    	sizehold  = (totalVolume / f.getmaximumBaggageWeight()) * 100;
	        return sizehold;
	    }
	    
	}

	
	public class FlightCheckIn {
	    private Map<String, FlightStats> flightMap;
	    ReadFiles fcs = new ReadFiles();
	    
	  

	    // 构造函数
	    public FlightCheckIn() {
	        this.flightMap = new HashMap<>();
	    }

	    // 处理乘客checkin的方法
	    public void checkInPassenger(String flightNumber, double weight, double volume) {
	        FlightStats stats = flightMap.getOrDefault(flightNumber, new FlightStats());
	        stats.addPassenger(weight, volume);
	        flightMap.put(flightNumber, stats);
	    }

	    // 打印所有航班的统计信息
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
	        }
	    }
	    
//	    // 主函数，用于示例
//	    public static void main(String[] args) {
//	        FlightCheckIn system = new FlightCheckIn();
//		
////
////	        // 打印统计信息
//	        system.printFlightStats();
//	    }
	}
