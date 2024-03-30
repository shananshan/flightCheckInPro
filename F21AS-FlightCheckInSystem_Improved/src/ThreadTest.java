import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

    static CheckInDesk deske1 = null;
    static CheckInDesk deske2 = null;
    static CheckInDesk deskb = null;


    public static void main(String[] args) throws IOException {

        List<Passenger> passengerList;
        List<Flight> flightList;
        Queue<Passenger> useless = null;
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
        deske1 = new CheckInDesk(1,bClass,eClass,flightList);
        deske2 = new CheckInDesk(1,bClass,eClass,flightList);
        deskb = new CheckInDesk(0,bClass,eClass,flightList);

        executor.execute(deske1);
        executor.execute(deske2);
        executor.execute(deskb);

        executor.shutdown();
    }
}
