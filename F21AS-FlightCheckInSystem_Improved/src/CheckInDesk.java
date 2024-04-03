import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class CheckInDesk implements Runnable {
    /*
    This class is used to simulate check-in process
    need a queue, include the waiting passengers' info: name, booking code, flight code, luggage[dimension, weight], state
    is one of the thread
     */

    // Vacancy status of check-in desks
    private static boolean desk1Vacancy = true;        //economy
    private static boolean deskBVacancy = true;        //business

    // Queues for different check-in and security check processes
    static Queue<Passenger> economyCheckIn = new LinkedList<>(), businessCheckIn= new LinkedList<>();
    static Queue<Passenger> economySecurityCheck= new LinkedList<>(), businessSecurityCheck = new LinkedList<>();
    static ReadFiles fcs = new ReadFiles();
    private static List<Flight> flightList = fcs.getFlightList();
    static FlightCheckIn FlightHold = new FlightCheckIn();
    private static List<Observer> observers = new ArrayList<>();
    static List<Flight> flightOnTime = flightList;
    static List<Flight> flightLate = null;
    static String flightLate1 = "Flight Late: ";
    private final int flag;
    private volatile Passenger currentPassenger; // It is the latest passenger information at each thread access
    Random rand = new Random();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected static void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public static void preCheck(List<Passenger> passengerList) {
        for (Passenger pass : passengerList) {
            if (pass.checkInSuccess.equals(true)) {
                FlightHold.checkInPassenger(pass.flightCode, pass.getLuggageWeight(), pass.getLuggageSize());
            }
        }

    }

    private static Flight findFlightByCode(String flightCode) {
        for (Flight flight : flightList) {
            if (flight.flightCode.equals(flightCode)) {
                return flight;
            }
        }
        return null;
    }

    /**
     * Generates check-in for economy class passengers.
     * @return Warning message if any.
     */
    public synchronized static Queue<Passenger> generateEconomyDesk(Passenger pass, List<Flight> flightList){
        // Removes a passenger from the economy check-in queue and processes their check-in.
        desk1Vacancy = false;
        Logger.getInstance().log(pass.name + " is checking in...");
        Flight matchingFlight = findFlightByCode(pass.getFlightCode());
        LocalTime time = LocalTime.now(); // Current time
        time =  time.withHour(6).withMinute(30).withSecond(20); // Set a hypothetical current time
        LocalTime flightTime = fcs.getFlightTime(flightList,pass.getFlightCode());
        String warning = null;
        // Start timer to track if desk1Vacancy changes to true within 10 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!desk1Vacancy) {
                    // If desk1Vacancy is still false after 10 seconds, set check-in success to false
                    pass.setCheckInSuccess(false);
                    desk1Vacancy = true;
                    System.out.println("Check-in success set to false due to timeout.");
                }
            }
        }, 10000); // 10 seconds

        // Check if the passenger's check-in and fee payment are not yet completed
        if (matchingFlight != null && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = matchingFlight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                Logger.getInstance().log("Checked In: " + pass.name);
                pass.setFeePaymentSuccess(true);
                pass.fee = fee;
                desk1Vacancy = true;
                economySecurityCheck.add(pass);
                FlightHold.checkInPassenger(pass.flightCode, pass.getLuggageWeight(), pass.getLuggageSize());
                Logger.getInstance().log(pass.flightCode +" has included "+ pass.name);
            } else {
                warning = giveLateCheckInError(); // Generate a warning message for late check-in
                desk1Vacancy = true;
            }
        } else {
            warning = giveRepeatCheckInError(); // Generate a warning message for repeated check-in
            desk1Vacancy = true;
        }
        notifyObservers();
        return economySecurityCheck;
    }
   
    /**
     * Generates check-in for business class passengers.
     * @return Warning message if any.
     */
    public synchronized static Queue<Passenger> generateBusinessDesk(Passenger pass, List<Flight> flightList){
        // Removes a passenger from the business check-in queue and processes their check-in.
        deskBVacancy = false;
        Logger.getInstance().log(pass.name + " is checking in...");
        Flight matchingFlight = findFlightByCode(pass.getFlightCode());
        LocalTime time = LocalTime.now(); // Current time
        time =  time.withHour(6).withMinute(30).withSecond(20);    // Set a hypothetical current time
        LocalTime flightTime = fcs.getFlightTime(flightList,pass.getFlightCode());
        String warning = null;
        // Start timer to track if desk1Vacancy changes to true within 60 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!deskBVacancy) {
                    // If desk1Vacancy is still false after 10 seconds, set check-in success to false
                    pass.setCheckInSuccess(false);
                    deskBVacancy = true;
                    System.out.println("Check-in success set to false due to timeout.");
                }
            }
        }, 10000); // 10 seconds

        // Check if the passenger's check-in and fee payment are not yet completed
        if (matchingFlight != null && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = matchingFlight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                Logger.getInstance().log("Checked In: " + pass.name);
                pass.setFeePaymentSuccess(true);
                pass.fee = fee;
                deskBVacancy = true;
                businessSecurityCheck.add(pass);
                FlightHold.checkInPassenger(pass.flightCode, pass.getLuggageWeight(), pass.getLuggageSize()); 
                Logger.getInstance().log(pass.flightCode +" has included "+ pass.name);
            } else {
                warning = giveLateCheckInError(); // Generate a warning message for late check-in
                deskBVacancy = true;
            }
        } else {
            warning = giveRepeatCheckInError(); // Generate a warning message for repeated check-in
            deskBVacancy = true;
        }
        notifyObservers();
        return businessSecurityCheck;
    }

    // Returns a warning message for late check-in.
    private static String giveLateCheckInError() {
        return "You are late!";
    }
    // Returns a warning message for repeated check-in.
    private static String giveRepeatCheckInError() {
        return "You have already checked in!";
    }

    /**
     * Checks the state of a passenger.
     * @param p The passenger to be checked.
     * @return The state of the passenger.
     */
    public static boolean checkAPassenger(Passenger p){
        // This method checks the state of a passenger and returns it.
        return p.state();
    }

    public CheckInDesk(int flag, Queue<Passenger> businessCheckIn,Queue<Passenger> economyCheckIn, List<Flight> flightList) {
        this.businessCheckIn = businessCheckIn;
        this.flightList = flightList;
        this.economyCheckIn = economyCheckIn;
        this.flag = flag;

    }

    public static Queue<Passenger> getEconomyQueue(){
        // the economy queue waiting for check-in
        return economyCheckIn;
    }

    public static Queue<Passenger> getBusinessQueue(){
        // the business priority queue waiting for check-in
        return businessCheckIn;
    }
    public static Queue<Passenger> getEconomyDesk(){
        // the economy queue waiting for security check
        return economySecurityCheck;
    }
   
    public static Queue<Passenger> getBusinessDesk(){
        // the economy queue waiting for security check
        return businessSecurityCheck;
    }
    public synchronized Passenger getCurrentPassenger() {
        return currentPassenger;
    }
    public synchronized FlightCheckIn getFighthold() {
        return FlightHold;
    }

    /*
     * Run the thread
     */
    @Override
    public void run() {
       while(true) {
           if (flag == 1 && !economyCheckIn.isEmpty()) {
               synchronized (this) {
                   currentPassenger = economyCheckIn.poll();
               }
               try {
                   generateEconomyDesk(currentPassenger, flightList);
                   TimeUnit.SECONDS.sleep(rand.nextInt(5) + 0);
                   System.out.println("Desk e is processing: " + currentPassenger);
                   System.out.println("Size of E waiting for security check is "+economySecurityCheck.size());
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }else{
               currentPassenger = null;
           }
           if (flag == 0 && !businessCheckIn.isEmpty()) {
               synchronized (this) {
                   currentPassenger = businessCheckIn.poll();
               }
               try {
                   generateBusinessDesk(currentPassenger, flightList);
                   TimeUnit.SECONDS.sleep(rand.nextInt(5) + 0);
                   System.out.println("Size of B waiting for security check is "+businessSecurityCheck.size());
                   System.out.println("Desk b is processing: " + currentPassenger);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }else{
               currentPassenger = null;
           }
           try {
               FlightHold.printFlightStats();
           } catch (IOException e) {
               e.printStackTrace();
           }
           if (businessCheckIn.isEmpty() || economyCheckIn.isEmpty()) {
               try {
                   TimeUnit.SECONDS.sleep(1);
                   System.out.println("Wait for passengers...");
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        }
    }

}
