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

public class Main {
    // Define static instances for the main components of the check-in and security system.
    static WaitingPassQueue waitingPass = null;
    static CheckInDesk checkInDesk1 = null, checkInDesk2 = null, checkInDesk3 = null;
    static Securityqueue BSq = null, ESq = null;
    static Queue<Passenger> eClass = new LinkedList<>(), bClass = new LinkedList<>();
    static FlightStats stats;
    static Map<String, FlightStats> Map;
    String eWaitingInfo, bWaitingInfo;
    static FlightCheckIn flightInfo;
    static FlightCheckIn FlightHold = new FlightCheckIn();
    static Queue<Passenger> economySecurityCheck = new LinkedList<>(), businessSecurityCheck = new LinkedList<>();
    static Queue<Passenger> businessSecurityQueue = new LinkedList<>(), economySecurityQueue = new LinkedList<>();

    //Schedules the shutdown of the system after a specified delay.
    public static void scheduleShutdown(ExecutorService executor, View view, long delay, TimeUnit timeUnit) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // Tasks scheduled for implementation over time
        scheduler.schedule(() -> {
            // Close the GUI window
            if (view != null) {
                SwingUtilities.invokeLater(() -> {
                    view.dispose(); // Close window
                    System.out.println("GUI closed");
                });
            }
            // Shutting down running services
            executor.shutdownNow();
            System.exit(0);
            try {
                // Wait for the executor to terminate and log if it doesn't within the specified time.
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate in the specified time.");
                    List<Runnable> droppedTasks = executor.shutdownNow();
                    System.err.println("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
                }
            } catch (InterruptedException e) {
                System.err.println("Termination interrupted");
                executor.shutdownNow();
            }
            // Shutting down the scheduler itself
            scheduler.shutdownNow();
            System.out.println("Program terminated");
        }, delay, timeUnit);
    }

    public static void main(String[] args) throws IOException {
        // Initial setup: reading passenger and flight data from CSV files.
        List<Passenger> passengerList;
        List<Flight> flightList;
        LocalTime flightTime;
        ReadFiles fcs = new ReadFiles();
        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        passengerList = fcs.getPassengerList();
        flightList = fcs.getFlightList();
        CheckInDesk.preCheck(passengerList);
        // Generate a log by calling a method
        Logger.getInstance().log("All passengers who successfully checked in early have been credited to the corresponding flight.");
        // Initialize main components and queues.
        waitingPass = new WaitingPassQueue(passengerList, eClass, bClass);
        checkInDesk1 = new CheckInDesk(1, bClass, eClass, flightList);
        checkInDesk2 = new CheckInDesk(1, bClass, eClass, flightList);
        checkInDesk3 = new CheckInDesk(0, bClass, eClass, flightList);
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
        executor.execute(checkInDesk1);
        executor.execute(checkInDesk2);
        executor.execute(checkInDesk3);
        View checkInView = new View(checkInDesk1, checkInDesk2, checkInDesk3, BSq, ESq);
        ControllerDesk checkInController = new ControllerDesk(checkInDesk1, checkInDesk2, checkInDesk3, BSq, ESq, checkInView);
        // security process
        executor.execute(BSq);
        executor.execute(ESq);
        // use line 98 or line 99 to shutdown threads
        scheduleShutdown(executor, checkInView, 60, TimeUnit.SECONDS);
        executor.shutdownNow(); 
    }
}
