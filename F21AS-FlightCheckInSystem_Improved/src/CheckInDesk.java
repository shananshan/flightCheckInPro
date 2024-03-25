import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CheckInDesk implements Runnable{
    /*
    This class is used to simulate check-in process
    need a queue, include the waiting passengers' info: name, booking code, flight code, luggage[dimension, weight], state
    is one of the thread
     */
	// Vacancy status of check-in desks
    private static boolean desk1Vacancy = true;		//economy
    private static boolean deskBVacancy = true;		//business
    
    // Queues for different check-in and security check processes
    static Queue<Passenger> economyCheckIn = new LinkedList<>();
    static Queue<Passenger> businessCheckIn= new LinkedList<>();
    static Queue<Passenger> economySecurityCheck= new LinkedList<>();
    static Queue<Passenger> businessSecurityCheck= new LinkedList<>();

    static Queue<Passenger> economySecurityCheck1= new LinkedList<>();
    static Queue<Passenger> economySecurityCheck2= new LinkedList<>();
    static Queue<Passenger> economySecurityCheck3= new LinkedList<>();

	public static void separatePassengersByClassType(List<Passenger> passengerList, Queue<Passenger> economy, Queue<Passenger> business) {
		for (Passenger passenger : passengerList) {
			if (passenger.classType.equals("1")) {
				economy.offer(passenger);
			} else if (passenger.classType.equals("0")) {
				business.offer(passenger);
			}
		}
	}

 /**
     * Generates check-in for economy class passengers.
     * @return Warning message if any.
     */
    public static Queue<Passenger> generateEconomyDesk(Queue<Passenger> economyCheckIn, List<Flight> flightList){
        // Removes a passenger from the economy check-in queue and processes their check-in.
        Passenger pass = economyCheckIn.poll();
        desk1Vacancy = false;
        LocalTime time = LocalTime.now(); // Current time
        time.withHour(6).withMinute(30).withSecond(20); // Set a hypothetical current time
        LocalTime flightTime = ReadFiles.getFlightTime(pass.flightCode);
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
        }, 60000); // 60 seconds

        // Check if the passenger's check-in and fee payment are not yet completed
        if (!pass.getCheckInSuccess() && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = Flight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                pass.setFeePaymentSuccess(true);
                pass.fee = fee;
                desk1Vacancy = true;
                economySecurityCheck.add(pass);
            } else {
                warning = giveLateCheckInError(); // Generate a warning message for late check-in
                desk1Vacancy = true;
            }
        } else {
            warning = giveRepeatCheckInError(); // Generate a warning message for repeated check-in
            desk1Vacancy = true;
        }
        System.out.println("------------------------------------------");
        System.out.println(pass);
        return economySecurityCheck;
    }
    
    /**
     * Generates check-in for business class passengers.
     * @return Warning message if any.
     */
    public static Queue<Passenger> generateBusinessDesk(Queue<Passenger> businessCheckIn, List<Flight> flightList){
        // Removes a passenger from the business check-in queue and processes their check-in.
        Passenger pass = businessCheckIn.poll();
        deskBVacancy = false;
        LocalTime time = LocalTime.now(); // Current time
        time.withHour(6).withMinute(30).withSecond(20); // Set a hypothetical current time
        LocalTime flightTime = ReadFiles.getFlightTime(pass.flightCode);
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
        }, 60000); // 60 seconds

        // Check if the passenger's check-in and fee payment are not yet completed
        if (!pass.getCheckInSuccess() && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = Flight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                pass.setFeePaymentSuccess(true);
                pass.fee = fee;
                deskBVacancy = true;
                businessSecurityCheck.add(pass);
            } else {
                warning = giveLateCheckInError(); // Generate a warning message for late check-in
                deskBVacancy = true;
            }
        } else {
            warning = giveRepeatCheckInError(); // Generate a warning message for repeated check-in
            deskBVacancy = true;
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(pass);
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

    public static Flight getFlight(Passenger p, List<Flight> flightList) {
        String code = p.flightCode;
        Flight f1 = null;
        for(Flight f: flightList) {
            if (Objects.equals(f.flightCode, code))
                f1 = f;
        }
        return f1;
    }

    public static void economySecurityCheck(Queue<Passenger> economySecurityCheck, Queue<Passenger> economySecurityCheck1, Queue<Passenger> economySecurityCheck2, Queue<Passenger> economySecurityCheck3 ) {
    	Queue<Queue<Passenger>> queues = new ArrayBlockingQueue<>(3);
        queues.add(economySecurityCheck1);
        queues.add(economySecurityCheck2);
        queues.add(economySecurityCheck3);

        while (!economySecurityCheck.isEmpty()) {
            Queue<Passenger> shortestQueue = getShortestQueue(queues);
            Passenger passenger = economySecurityCheck.poll();
            if (passenger != null) {
                shortestQueue.add(passenger);
                System.out.println("Passenger added to shortest queue: " + passenger);
                sleep(5000); // 等待5秒
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

 // 启动定时任务，每六秒清除队首元素
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

    public CheckInDesk(Queue<Passenger> economyCheckIn, Queue<Passenger> businessCheckIn, List<Flight> flightList) {
        this.economyCheckIn = economyCheckIn;
        this.businessCheckIn = businessCheckIn;
//        this.flightList = flightList;
    }

    @Override
    public void run() {
        while (!economyCheckIn.isEmpty()) {
            while (!businessCheckIn.isEmpty()) {
            generateBusinessDesk(businessCheckIn, ReadFiles.flightList);
            System.out.println(businessSecurityCheck.size());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            generateEconomyDesk(economyCheckIn, ReadFiles.flightList);
//            int rest = economySecurityCheck.size();
//            System.out.println("Processed "+rest +" passengers from the economy check-in queue.");
            System.out.println(economySecurityCheck.size());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
