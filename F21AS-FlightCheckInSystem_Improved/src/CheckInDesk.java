import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CheckInDesk {
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

	public static void separatePassengersByClassType(List<Passenger> passengerList, Queue<Passenger> economy, Queue<Passenger> business) {
		for (Passenger passenger : passengerList) {
			if (passenger.classType.equals("1")) {
				economy.offer(passenger);
			} else if (passenger.classType.equals("0")) {
				business.offer(passenger);
			}
		}
	}

	public static void closeDesk() {
		// close the desk after take off
	}


	public int deskNum(){
        int desknum = 1;
        desknum += 1;
        return desknum;
    }

    public boolean gatState(boolean state){
        boolean checkornot = state;
        return checkornot;
    }

    public void baggage(Passenger p){
        float dimension = p.luggageSize;
        float weight = p.luggageWeight;
        float fee = Flight.calulatefee(dimension,weight);

    }

 /**
     * Generates check-in for economy class passengers.
     * @return Warning message if any.
     */
    public static String genrateEconomyDesk(){
        // Removes a passenger from the economy check-in queue and processes their check-in.
        Passenger pass = economyCheckIn.remove();
        desk1Vacancy = false;
        LocalTime time = LocalTime.now(); // Current time
        time.withHour(6).withMinute(30).withSecond(20); // Set a hypothetical current time
        LocalTime flightTime = Flight.getFlightTime();
        String warning = null;

        // Check if the passenger's check-in and fee payment are not yet completed
        if (!pass.getCheckInSuccess() && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = Flight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                pass.setFeePaymentSuccess(true);
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
        return warning;
    }
    
    /**
     * Generates check-in for business class passengers.
     * @return Warning message if any.
     */
    public static String genrateBussinessDesk(){
        // Removes a passenger from the business check-in queue and processes their check-in.
        Passenger pass = businessCheckIn.remove();
        deskBVacancy = false;
        LocalTime time = LocalTime.now(); // Current time
        time.withHour(6).withMinute(30).withSecond(20); // Set a hypothetical current time
        LocalTime flightTime = Flight.getFlightTime();
        String warning = null;

        // Check if the passenger's check-in and fee payment are not yet completed
        if (!pass.getCheckInSuccess() && !pass.getFeePaymentSuccess()) {
            // Check if the current time is before or equal to the flight time
            if (time.isBefore(flightTime) || time.equals(flightTime)) {
                // Calculate baggage fee, set check-in success and fee payment flags, and move to security check queue
                float fee = Flight.calulatefee(pass.getLuggageSize(), pass.getLuggageWeight());
                pass.setCheckInSuccess(true);
                pass.setFeePaymentSuccess(true);
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
        return warning;
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
}

