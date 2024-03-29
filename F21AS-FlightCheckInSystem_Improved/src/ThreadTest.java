import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadTest {
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
        executor.execute(new CheckInDesk(eClass,flightList));
        executor.execute(new CheckInDesk(eClass,flightList));
        executor.execute(new CheckInDesk(bClass,flightList));

//        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 将乘客分类和初始化检票柜台的任务提交到线程池中
//        Runnable initializeDesksTask = () -> {
//            Queue<Passenger> eClass = new LinkedList<>();
//            Queue<Passenger> bClass = new LinkedList<>();
//            // Separate passengers by class type
//            CheckInDesk.separatePassengersByClassType(passengerList, eClass, bClass);
//            System.out.println("Class Type 0 Queue:" + eClass.size());
//            System.out.println("Class Type 1 Queue:" + bClass.size());
//
//            // 创建检票柜台任务并提交到线程池中
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
