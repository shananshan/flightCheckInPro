import java.util.Objects;
import java.util.Random;

public class Passenger {
	static String name;
	String firstName;
	String lastName;
	String flightCode;
	Float luggageSize;
	Float luggageWeight;
	Boolean checkInSuccess;
	Boolean feePayment;
   	String classType; // "Economy" or "Business"
    
    //Constructor
    public Passenger(String firstName, String flightCode, String classType) {
        this.firstName = firstName;
        this.flightCode = flightCode;
        this.classType = classType;
        this.checkInSuccess = false; // Initialization set to incomplete boarding
        this.feePayment = false; // Initialization set to unpaid baggage fees
        this.luggageSize = generateLuggageSize();
        this.luggageWeight = generateLuggageWeight();
    }
    
    Passenger(String[] args) {
    	firstName = args[0];
    	lastName = args[1];
        name = args[0] + " " + args[1];
        flightCode = args[2];
        checkInSuccess = Objects.equals(args[3], "TRUE");
        feePayment = Objects.equals(args[4], "TRUE");
        classType = args[5];
    }

    //Randomly generated baggage volume: 125-512000
    private float generateLuggageSize() {
        return 125f + new Random().nextFloat() * (512000f - 125f); 
    }

    // Randomly generated baggage weight: 5 - 50 kilograms
    private float generateLuggageWeight() {
        return 5f + new Random().nextFloat() * (50f - 5f); // 
    }
	
    public boolean state() {
        // store the state of the passenger
        return false;
    }

	public String getName() {
		return name;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getFlightCode() {
		return flightCode;
	}

    public Float getLuggageSize(){
        return luggageSize;
    }

	public Float getLuggageWeight() {
		return luggageWeight;
	}

	public Boolean getCheckInSuccess() {
		return checkInSuccess;
	}

	public Boolean getFeePayment() {
		return feePayment;
	}
	
    public String toString() {
        return String.format(name + " , " + flightCode + " , " + luggageSize + " , " + luggageWeight + " , " + classType + " , " + (checkInSuccess ? "True" : "False"));
    }
 
}
