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
	private static boolean desk1Vacancy = true;
	private static boolean deskBVacancy = true;
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

    public static String genrateEconomyDesk(){
    	Passenger pass = economyCheckIn.remove();
    	desk1Vacancy = false;
    	LocalTime time = LocalTime.now();
    	//假定一个当前时间
    	time.withHour(6).withMinute(30).withSecond(20);
    	LocalTime flightTime = Flight.getFlightTime();

    	String firstName = pass.getFirstName();
    	String flightCode = pass.getFlightCode();
    	Float luggageSize = pass.getLuggageSize();
    	Float luggageWeight = pass.getLuggageWeight();
    	Boolean checkInSuccess = pass.getCheckInSuccess();
    	Boolean feePayment = pass.getFeePayment();
    	float fee = 0;
    	String warning = null;

    	if(!checkInSuccess & !feePayment) {

    		if((time.getHour() < flightTime.getHour()) | ((time.getHour() == flightTime.getHour()) & (time.getMinute() < flightTime.getMinute())) | (time.getHour() == flightTime.getHour()) & (time.getMinute() == flightTime.getMinute()) & (time.getSecond() <= flightTime.getSecond())) {
        		fee = Flight.calulatefee(luggageSize,luggageWeight);
        		pass.checkInSuccess = true;
        		pass.feePayment = true;
        		desk1Vacancy = true;
        		economySecurityCheck.add(pass);
        		return warning;
    		}else {
    			warning = giveLateCheckInError();
    			desk1Vacancy = true;
    			return warning;
    		}

    	}else {
    		warning = giveRepeatCheckInError();
    		desk1Vacancy = true;
    		return warning;
    	}

    }


    public static String genrateBussinessDesk(){
    	Passenger pass = businessCheckIn.remove();
    	deskBVacancy = false;
    	LocalTime time = LocalTime.now();
    	//假定一个当前时间
    	time.withHour(6).withMinute(30).withSecond(20);
    	LocalTime flightTime = Flight.getFlightTime();

    	String firstName = pass.getFirstName();
    	String flightCode = pass.getFlightCode();
    	Float luggageSize = pass.getLuggageSize();
    	Float luggageWeight = pass.getLuggageWeight();
    	Boolean checkInSuccess = pass.getCheckInSuccess();
    	Boolean feePayment = pass.getFeePayment();
    	float fee = 0;
    	String warning = null;

    	if(!checkInSuccess & !feePayment) {

    		if((time.getHour() < flightTime.getHour()) | ((time.getHour() == flightTime.getHour()) & (time.getMinute() < flightTime.getMinute())) | (time.getHour() == flightTime.getHour()) & (time.getMinute() == flightTime.getMinute()) & (time.getSecond() <= flightTime.getSecond())) {
        		fee = Flight.calulatefee(luggageSize,luggageWeight);
        		pass.checkInSuccess = true;
        		pass.feePayment = true;
        		deskBVacancy = true;
        		businessSecurityCheck.add(pass);
        		return warning;
    		}else {
    			warning = giveLateCheckInError();
    			deskBVacancy = true;
    			return warning;
    		}

    	}else {
    		warning = giveRepeatCheckInError();
    		deskBVacancy = true;
    		return warning;
    	}

    }


	private static String giveLateCheckInError() {
		String lateCheckInWarning = "You are late!";
		return lateCheckInWarning;
	}

	private static String giveRepeatCheckInError() {
		String repeatCheckInWarning = "You have cheecked in!";
		return repeatCheckInWarning;
	}

	public static boolean checkAPassenger(Passenger p){
        boolean newState = p.state();
        return newState;
    }
}
