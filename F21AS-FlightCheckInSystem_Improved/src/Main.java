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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

public class Main{
    static WaitingPassQueue waitingPass = null;
	static CheckInDesk checkInDesk1 = null, checkInDesk2 = null, checkInDesk3 = null;
	static Securityqueue BSq = null, ESq = null ;
    static Queue<Passenger> eClass = new LinkedList<>(), bClass = new LinkedList<>();
	static FlightStats stats ;
    static Map<String, FlightStats> Map ;
    String eWaitingInfo, bWaitingInfo;
    static FlightCheckIn flightInfo;
    static FlightCheckIn FlightHold = new FlightCheckIn();
    static Queue<Passenger> economySecurityCheck= new LinkedList<>(), businessSecurityCheck= new LinkedList<>();
    static Queue<Passenger> businessSecurityQueue = new LinkedList<>(), economySecurityQueue = new LinkedList<>();

    public static void scheduleShutdown(ExecutorService executor, View view, long delay, TimeUnit timeUnit) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // 安排在一段时间后执行的任务
        scheduler.schedule(() -> {
            // 关闭GUI窗口
            if (view != null) {
                SwingUtilities.invokeLater(() -> {
                    view.dispose(); // 关闭窗口
                    System.out.println("GUI closed");
                });
            }

            // 关闭正在执行的服务
            executor.shutdownNow();
            System.exit(0);
           
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate in the specified time.");
                    List<Runnable> droppedTasks = executor.shutdownNow();
                    System.err.println("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
                }
            } catch (InterruptedException e) {
                System.err.println("Termination interrupted");
                executor.shutdownNow();
            }
            // 关闭调度器本身
            scheduler.shutdownNow();
            System.out.println("Program terminated");
        }, delay, timeUnit);
    }

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
        Logger.getInstance().log("All passengers who successfully checked in early have been credited to the corresponding flight.");

        waitingPass = new WaitingPassQueue(passengerList,eClass,bClass);
        checkInDesk1 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk2 = new CheckInDesk(1,bClass,eClass,flightList);
        checkInDesk3 = new CheckInDesk(0,bClass,eClass,flightList);
        economySecurityCheck = CheckInDesk.getEconomyDesk();
        businessSecurityCheck = CheckInDesk.getBusinessDesk();
        eClass = WaitingPassQueue.getWEQueue();
        bClass = WaitingPassQueue.getWBQueue();
        BSq = new Securityqueue(businessSecurityCheck, businessSecurityQueue, "Business");
        ESq = new Securityqueue(economySecurityCheck, economySecurityQueue, "Economy");


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
        
        scheduleShutdown(executor, checkInView,60, TimeUnit.SECONDS);

//        executor.shutdown();
    }
}
