import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main{
	static CheckInDesk checkInDesk1 = null;
	static CheckInDesk checkInDesk2 = null;
	static CheckInDesk checkInDesk3 = null;
	
	
    public static void main(String[] args) throws IOException{
        // we can generate a log by calling a method
       
        List<Passenger> passengerList;
        List<Flight> flightList;
//        Queue<Passenger> classType0 = new LinkedList<>();
//        Queue<Passenger> classType1 = new LinkedList<>();
        Queue<Passenger> eClass = new LinkedList<>();
        Queue<Passenger> bClass = new LinkedList<>();
        LocalTime flightTime;

        ReadFiles fcs = new ReadFiles();
        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        passengerList = fcs.getPassengerList();
        flightList = fcs.getFlightList();
        
//        for(Flight f: flightList){
//            
////        	System.out.println(f);
////        	System.out.println(f.flightCode);
////        	Flight f1 = fcs.getFlight(flightList,f.flightCode);
////        	System.out.println(f1);
//            	flightTime = fcs.getFlightTime(flightList, f.flightCode);
////        	flightTime = fcs.getFlightTime(flightList f.getflightcode());
//            
//          // 打印以验证
//	            	System.out.println("Flight time: " + flightTime);
//	     }
        
//        CheckInDesk.separatePassengersByClassType(passengerList, classType0, classType1);
        CheckInDesk.separatePassengersByClassType(passengerList, eClass, bClass);
        checkInDesk1 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk2 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk3 = new CheckInDesk(0,bClass,eClass,flightList);
        
       
//        System.out.println("1"+checkInDesk1);
//        System.out.println("2"+checkInDesk1);
//        System.out.println("3"+checkInDesk1);
        
        
        
        View checkInView = new View(checkInDesk1,checkInDesk2,checkInDesk3);
        Controller checkInController = new Controller(checkInDesk1, checkInDesk2, checkInDesk3, checkInView);
//        Controller checkInController2 = new Controller(checkInDesk2, checkInView2);
//        Controller checkInController3 = new Controller(checkInDesk3, checkInView3);
        

       
//        // Separate passengers by class type
//        CheckInDesk.separatePassengersByClassType(passengerList, classType0, classType1);
//        System.out.println("Class Type 0 Queue:"+classType0.size());
//        System.out.println("Class Type 1 Queue:"+classType1.size());
        System.out.println("Class Type 0 Queue:"+eClass.size());
        System.out.println("Class Type 1 Queue:"+bClass.size());

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create check-in desks
        executor.execute(checkInDesk1 );
        executor.execute(checkInDesk2 );
        executor.execute(checkInDesk3 );
     
        executor.shutdown();
    

    }
}
