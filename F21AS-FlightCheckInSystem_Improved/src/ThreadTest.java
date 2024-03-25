import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {
    public static void main(String[] args) throws IOException {

        List<Passenger> passengerList;
        List<Flight> flightList;
        Queue<Passenger> classType0 = new LinkedList<>();
        Queue<Passenger> classType1 = new LinkedList<>();
//        Queue<Passenger> economySecurityCheck1 = new LinkedList<>();
//        Queue<Passenger> economySecurityCheck2 = new LinkedList<>();
//        Queue<Passenger> economySecurityCheck3 = new LinkedList<>();

        ReadFiles fcs = new ReadFiles();
        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        passengerList = fcs.getPassengerList();
        flightList = fcs.getFlightList();
        System.out.println(flightList);

        // Separate passengers by class type
        CheckInDesk.separatePassengersByClassType(passengerList, classType0, classType1);
        System.out.println("Class Type 0 Queue:"+classType0.size());
        System.out.println("Class Type 1 Queue:"+classType1.size());

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create check-in desks
        executor.execute(new CheckInDesk(classType0,flightList));
        executor.execute(new CheckInDesk(classType0,flightList));
        executor.execute(new CheckInDesk(classType1,flightList));

        // Create security desks
//        executor.execute(new );

        // Shutdown the executor
//        executor1.shutdown();
//        executor2.shutdown();
        executor.shutdown();
    }
}
