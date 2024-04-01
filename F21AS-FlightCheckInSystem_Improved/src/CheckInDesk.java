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
    static Queue<Passenger> economyCheckIn = new LinkedList<>();
    static Queue<Passenger> businessCheckIn= new LinkedList<>();
    static Queue<Passenger> economySecurityCheck= new LinkedList<>();
    static Queue<Passenger> businessSecurityCheck= new LinkedList<>();

    static Queue<Passenger> economySecurityCheck1= new LinkedList<>();
    static Queue<Passenger> economySecurityCheck2= new LinkedList<>();
    static Queue<Passenger> economySecurityCheck3= new LinkedList<>();
    static ReadFiles fcs = new ReadFiles();
//    private final Queue<Passenger> passQueue;
    private static List<Flight> flightList = fcs.getFlightList();
    static FlightCheckIn FlightHold = new FlightCheckIn();
    private static List<Observer> observers = new ArrayList<>();
    
    static List<Flight> flightOnTime = flightList;
    static List<Flight> flightLate = null;
    static String flightLate1 = "Flight Late: ";
    private final int flag;
    private volatile Passenger currentPassenger; // 当前正在处理的乘客    

    LocalTime time = LocalTime.of(6,30,20,200);
//    private List<Flight> flightList = fcs.getFlightList();
    
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
    public static void separatePassengers(List<Passenger> passengerList, Queue<Passenger> economy, Queue<Passenger> business) {
        while (economy.size()<10 || business.size()<10) {
            Random random = new Random();
            int index = random.nextInt(passengerList.size());
            if (!passengerList.isEmpty()) {
                Passenger passenger = passengerList.remove(index);
                if (passenger.checkInSuccess.equals("FALSE") && passenger.classType.equals("1")) {
                    economy.offer(passenger);
                } else if (passenger.checkInSuccess.equals("FALSE") && passenger.classType.equals("0")) {
                    business.offer(passenger);
                }
            }
        }
    }
    public static void separatePassengerByClassType(Passenger passenger, ArrayBlockingQueue<Passenger> q1, ArrayBlockingQueue<Passenger> q2) {
        if (q1.size() < q2.size()) {
            q1.offer(passenger);
        } else if (q2.size() <= q2.size()) {
            q2.offer(passenger);
        }
    }

    
    public static String checkFlightOnTime(List<Flight> flightOnTime){
        for(Flight flight: flightOnTime) {
            LocalTime time = LocalTime.now(); // Current time
            time.withHour(6).withMinute(30).withSecond(20); // Set a hypothetical current time
            LocalTime flightTime =fcs.getFlightTime(flightOnTime,flight.flightCode);
            if(flightTime.isBefore(time)) {
                flightOnTime.remove(flight);
                flightLate.add(flight);
                flightLate1 = flightLate1 + ", " + flight.flightCode;
            }

        }
        return flightLate1;
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
//        Passenger pass = economyCheckIn.poll();

        desk1Vacancy = false;
        Flight matchingFlight = findFlightByCode(pass.getFlightCode());
        LocalTime time = LocalTime.now(); // Current time
        time =  time.withHour(6).withMinute(30).withSecond(20); // Set a hypothetical current time
        LocalTime flightTime = fcs.getFlightTime(flightList,pass.getFlightCode());
        String warning = null;
        // Start timer to track if desk1Vacancy changes to true within 60 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!desk1Vacancy) {
                    // If desk1Vacancy is still false after 60 seconds, set check-in success to false
                    pass.setCheckInSuccess(false);
                    desk1Vacancy = true;
                    System.out.println("Check-in success set to false due to timeout.");
                }
            }
        }, 11000); // 60 seconds

        // Check if the passenger's check-in and fee payment are not yet completed
        if (matchingFlight != null && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = matchingFlight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                pass.setFeePaymentSuccess(true);
                pass.fee = fee;
                desk1Vacancy = true;
                economySecurityCheck.add(pass);
                FlightHold.checkInPassenger(pass.flightCode, pass.getLuggageWeight(), pass.getLuggageSize());     
                
            } else {
                warning = giveLateCheckInError(); // Generate a warning message for late check-in
                desk1Vacancy = true;
            }
        } else {
            warning = giveRepeatCheckInError(); // Generate a warning message for repeated check-in
            desk1Vacancy = true;
        }
//        System.out.println("------------------------------------------");
//        System.out.println(pass);
       
//        if(pass.getCheckInSuccess()== true) {
//        	FlightHold.checkInPassenger(pass.flightCode, pass.getLuggageWeight(), pass.getLuggageSize());     

//        	try {
//    			FlightHold.printFlightStats();
////    			System.out.println("flight yes");
//    			
//    		} catch (IOException e) {
//    			// TODO Auto-generated catch block
//    			e.printStackTrace();
////    			System.out.println("flight no");
//    		}
//        }
        notifyObservers();
        return economySecurityCheck;
    }
   
    /**
     * Generates check-in for business class passengers.
     * @return Warning message if any.
     */
    public synchronized static Queue<Passenger> generateBusinessDesk(Passenger pass, List<Flight> flightList){
        // Removes a passenger from the business check-in queue and processes their check-in.
//        Passenger pass = businessCheckIn.poll();
        deskBVacancy = false;
        Flight matchingFlight = findFlightByCode(pass.getFlightCode());
        LocalTime time = LocalTime.now(); // Current time
        // Set a hypothetical current time
        time =  time.withHour(6).withMinute(30).withSecond(20);
        LocalTime flightTime = fcs.getFlightTime(flightList,pass.getFlightCode());
        String warning = null;
        // Start timer to track if desk1Vacancy changes to true within 60 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!deskBVacancy) {
                    // If desk1Vacancy is still false after 60 seconds, set check-in success to false
                    pass.setCheckInSuccess(false);
                    deskBVacancy = true;
                    System.out.println("Check-in success set to false due to timeout.");
                }
            }
        }, 11000); // 60 seconds

        // Check if the passenger's check-in and fee payment are not yet completed
        if (matchingFlight != null && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = matchingFlight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                pass.setFeePaymentSuccess(true);
                pass.fee = fee;
                deskBVacancy = true;
                businessSecurityCheck.add(pass);
                FlightHold.checkInPassenger(pass.flightCode, pass.getLuggageWeight(), pass.getLuggageSize());     
            } else {
                warning = giveLateCheckInError(); // Generate a warning message for late check-in
                deskBVacancy = true;
            }
        } else {
            warning = giveRepeatCheckInError(); // Generate a warning message for repeated check-in
            deskBVacancy = true;
        }


//        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//        System.out.println(pass);
//        if(pass.getCheckInSuccess()== true) {
//        	FlightHold.checkInPassenger(pass.flightCode, pass.getLuggageWeight(), pass.getLuggageSize());     
////        	try {
////    			FlightHold.printFlightStats();
//////    			System.out.println("flight yes");
////    			
////    		} catch (IOException e) {
////    			// TODO Auto-generated catch block
////    			e.printStackTrace();
//////    			System.out.println("flight no");
////    		}
//        }
        Logger.getInstance().log(pass.name);
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
   
//    private static  FlightCheckIn getFlightStatus() {
//        return FlightHold;
//    }

    /**
     * Checks the state of a passenger.
     * @param p The passenger to be checked.
     * @return The state of the passenger.
     */
    public static boolean checkAPassenger(Passenger p){
        // This method checks the state of a passenger and returns it.
        return p.state();
    }

    public static void economySecurityCheck(Queue<Passenger> economySecurityCheck, Queue<Passenger> economySecurityCheck1, Queue<Passenger> economySecurityCheck2, Queue<Passenger> economySecurityCheck3 ) {
        Queue<Queue<Passenger>> queues = new ArrayBlockingQueue<>(3);
        queues.add(economySecurityCheck1);
        queues.add(economySecurityCheck2);
        queues.add(economySecurityCheck3);
//        notifyObservers();
        while (!economySecurityCheck.isEmpty()) {
            Queue<Passenger> shortestQueue = getShortestQueue(queues);
            Passenger passenger = economySecurityCheck.poll();
            if (passenger != null) {
                shortestQueue.add(passenger);
                System.out.println("Passenger added to shortest queue: " + passenger);
                sleep(5000); // 绛夊緟5绉�
            }
        }
    }

    public static Queue<Passenger> getShortestQueue(Queue<Queue<Passenger>> queues) {
        Queue<Passenger> shortestQueue = null;
        int minLength = Integer.MAX_VALUE;
        for (Queue<Passenger> queue : queues) {
            int length = queue.size();
            if (length < minLength) {
                minLength = length;
                shortestQueue = queue;
            }
        }
        return shortestQueue;
    }

    public static void sleep(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

 // 鍚姩瀹氭椂浠诲姟锛屾瘡鍏娓呴櫎闃熼鍏冪礌
    public static void startTimer(Queue<Passenger> queue) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!queue.isEmpty()) {
                    Passenger removedPassenger = queue.poll();
                    System.out.println("Element removed from queue: " + removedPassenger);
                }
            }
        }, 0, 6000);
    }

    public CheckInDesk(int flag, Queue<Passenger> businessCheckIn,Queue<Passenger> economyCheckIn, List<Flight> flightList) {
        this.businessCheckIn = businessCheckIn;
        this.flightList = flightList;
        this.economyCheckIn = economyCheckIn;
        this.flag = flag;

    }

//    public synchronized Passenger getEWaitingPass() {
//        return economyCheckIn;
//    }
//
//    public synchronized Passenger getBWaitingPass() {
//        return currentPassenger;
//    }

    public static Queue<Passenger> getEconomyQueue(){
        return economyCheckIn;
    }

    public static Queue<Passenger> getBusinessQueue(){
        return businessCheckIn;
    }
    public static Queue<Passenger> getEconomyDesk(){
        return economySecurityCheck;
    }
   
    public static Queue<Passenger> getBusinessDesk(){
        return businessSecurityCheck;
    }
    public synchronized Passenger getCurrentPassenger() {
        return currentPassenger;
    }
    public synchronized FlightCheckIn getFighthold() {
        return FlightHold;
    }


    @Override
    public void run() {
//      public Queue<Passenger> ..
       while(true) {
           if (flag == 1 && !economyCheckIn.isEmpty()) {
               synchronized (this) {
                   currentPassenger = economyCheckIn.poll();
               }
               try {
                   generateEconomyDesk(currentPassenger, flightList);
                   TimeUnit.SECONDS.sleep(rand.nextInt(10) + 0);
//                   TimeUnit.SECONDS.sleep(1);//为了测试先用了固定时间
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
                   TimeUnit.SECONDS.sleep(rand.nextInt(10) + 0);
//                   TimeUnit.SECONDS.sleep(3);//为了测试先使用固定时间
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
               // TODO Auto-generated catch block
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

//    @Override
//    public Queue<Passenger> call() throws Exception {
//        while(true) {
//            if (flag == 1 && !economyCheckIn.isEmpty()) {
//                synchronized (this) {
//                    currentPassenger = economyCheckIn.poll();
//                }
//                try {
//                    economySecurityCheck1 = generateEconomyDesk(currentPassenger, flightList);
//                    TimeUnit.SECONDS.sleep(rand.nextInt(10) + 0);
////                   TimeUnit.SECONDS.sleep(1);//为了测试先用了固定时间
//                    System.out.println("Desk e is processing: " + currentPassenger);
//                    System.out.println("Size of E waiting for security check is "+economySecurityCheck.size());
//
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            if (flag == 0 && !businessCheckIn.isEmpty()) {
//                synchronized (this) {
//                    currentPassenger = businessCheckIn.poll();
//                }
//                try {
//                    economySecurityCheck2 = generateBusinessDesk(currentPassenger, flightList);
//                    TimeUnit.SECONDS.sleep(rand.nextInt(10) + 0);
////                   TimeUnit.SECONDS.sleep(3);//为了测试先使用固定时间
//                    System.out.println("Size of B waiting for security check is "+businessSecurityCheck.size());
//                    System.out.println("Desk b is processing: " + currentPassenger);
//
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            try {
//                FlightHold.printFlightStats();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            if (businessCheckIn.isEmpty() || economyCheckIn.isEmpty()) {
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                    System.out.println("Wait for passengers...");
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
}
