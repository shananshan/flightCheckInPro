import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main{
    static WaitingPassQueue waitingPass = null;
	static CheckInDesk checkInDesk1 = null, checkInDesk2 = null, checkInDesk3 = null;
	static Secutiryqueue BSq = null, ESq = null ;
    static Queue<Passenger> eClass = new LinkedList<>(), bClass = new LinkedList<>();
	static FlightStats stats ;
    static Map<String, FlightStats> Map ;
    String eWaitingInfo, bWaitingInfo;
    static FlightCheckIn flightInfo;
    static FlightCheckIn FlightHold = new FlightCheckIn();
    static Queue<Passenger> economySecurityCheck= new LinkedList<>(), businessSecurityCheck= new LinkedList<>();
    static ConcurrentLinkedQueue<Passenger> businessSecurityQueue = new ConcurrentLinkedQueue<>(), economySecurityQueue = new ConcurrentLinkedQueue<>();


    public static void main(String[] args) throws IOException{
        // we can generate a log by calling a method

        List<Passenger> passengerList;
        List<Flight> flightList;

        LocalTime flightTime;

        ReadFiles fcs = new ReadFiles();
        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        passengerList = fcs.getPassengerList();
        flightList = fcs.getFlightList();

        CheckInDesk.preCheck(passengerList);

        waitingPass = new WaitingPassQueue(passengerList,eClass,bClass);
        checkInDesk1 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk2 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk3 = new CheckInDesk(0,bClass,eClass,flightList);
        economySecurityCheck = CheckInDesk.getEconomyDesk();
        businessSecurityCheck = CheckInDesk.getBusinessDesk();
        eClass = WaitingPassQueue.getWEQueue();
        bClass = WaitingPassQueue.getWBQueue();
        BSq = new Secutiryqueue(businessSecurityCheck, businessSecurityQueue, "Business");
        ESq = new Secutiryqueue(economySecurityCheck, economySecurityQueue, "Economy");


        ExecutorService executor = Executors.newFixedThreadPool(6);
        // create waiting queues
        executor.execute(waitingPass);
        // Create check-in desks
        executor.execute(checkInDesk1 );
        executor.execute(checkInDesk2 );
        executor.execute(checkInDesk3 );

        View checkInView = new View(checkInDesk1,checkInDesk2,checkInDesk3 ,BSq, ESq);
        ControllerDesk checkInController = new ControllerDesk(checkInDesk1, checkInDesk2, checkInDesk3, BSq, ESq, checkInView);
        // security process
        executor.execute(BSq);
        executor.execute(ESq);

        executor.shutdown();
    }
}
