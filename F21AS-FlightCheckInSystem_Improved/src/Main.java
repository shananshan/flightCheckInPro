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
        Queue<Passenger> eClass = new LinkedList<>();
        Queue<Passenger> bClass = new LinkedList<>();
        LocalTime flightTime;

        ReadFiles fcs = new ReadFiles();
        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        passengerList = fcs.getPassengerList();
        flightList = fcs.getFlightList();

        CheckInDesk.separatePassengersByClassType(passengerList, eClass, bClass);
        checkInDesk1 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk2 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk3 = new CheckInDesk(0,bClass,eClass,flightList);

        View checkInView = new View(checkInDesk1,checkInDesk2,checkInDesk3);
        Controller checkInController = new Controller(checkInDesk1, checkInDesk2, checkInDesk3, checkInView);

//        // Separate passengers by class type
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
