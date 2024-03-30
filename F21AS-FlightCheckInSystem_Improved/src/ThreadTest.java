import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadTest {
	
    static CheckInDesk deske1 = null;
    static CheckInDesk deske2 = null;
    static CheckInDesk deskb = null;
    
    
    public static void main(String[] args) throws IOException {

        List<Passenger> passengerList;
        List<Flight> flightList;
        Queue<Passenger> eClass = new LinkedList<>();
        Queue<Passenger> bClass = new LinkedList<>();
        Queue<Passenger> eSecurityCheck = new LinkedList<>();
        Queue<Passenger> bSecurityCheck = new LinkedList<>();
        ReadFiles fcs = new ReadFiles();
        

        

        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv"); 
        passengerList = fcs.getPassengerList();
        flightList = fcs.getFlightList();

        // Separate passengers by class type
        CheckInDesk.separatePassengersByClassType(passengerList, eClass, bClass);
        System.out.println("Class Type 0 Queue:"+eClass.size());
        System.out.println("Class Type 1 Queue:"+bClass.size());
//
//
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create check-in desks
        deske1 = new CheckInDesk(eClass,flightList);
        deske2 = new CheckInDesk(eClass,flightList);
        deskb = new CheckInDesk(bClass,flightList);

        executor.execute(deske1);
        executor.execute(deske2);
        executor.execute(deskb);
        


//        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 灏嗕箻瀹㈠垎绫诲拰鍒濆鍖栨绁ㄦ煖鍙扮殑浠诲姟鎻愪氦鍒扮嚎绋嬫睜涓�
//        Runnable initializeDesksTask = () -> {
//            Queue<Passenger> eClass = new LinkedList<>();
//            Queue<Passenger> bClass = new LinkedList<>();
//            // Separate passengers by class type
//            CheckInDesk.separatePassengersByClassType(passengerList, eClass, bClass);
//            System.out.println("Class Type 0 Queue:" + eClass.size());
//            System.out.println("Class Type 1 Queue:" + bClass.size());
//
//            // 鍒涘缓妫�绁ㄦ煖鍙颁换鍔″苟鎻愪氦鍒扮嚎绋嬫睜涓�
//            executor.execute(new CheckInDesk(eClass, flightList));
//            executor.execute(new CheckInDesk(eClass, flightList));
//            executor.execute(new CheckInDesk(bClass, flightList));
//        };
//        executor.execute(initializeDesksTask);
        // Create security desks
//        executor.execute(new );

        // Shutdown the executor
//        executor1.shutdown();
//        executor2.shutdown();
        executor.shutdown();
    }
}
