import java.util.Objects;
import java.util.Random;

public class Passenger {
	String name;
	String firstName;
	String lastName;
	String flightCode;
	int luggageSize;
	Float luggageWeight;
	Boolean checkInSuccess;
	Boolean feePaymentSuccess;	//payment success or not
   	String classType; // "Economy" or "Business"
	String luggageDimensions;
	public float fee;

    //Constructor
    Passenger(String[] args) {
    	firstName = args[1];
    	lastName = args[2];
        name = args[1] + " " + args[2];
        flightCode = args[3];
//        checkInSuccess = Objects.equals(args[4], "TRUE");
		checkInSuccess = false;
		feePaymentSuccess = false;
        classType = args[5];
		fee = 0;
        generateLuggageSize();
        luggageWeight = generateLuggageWeight();
    }

    private void generateLuggageSize() {
        Random random = new Random();
        int length = 5 + random.nextInt(96); // 5-100
	int width = 5 + random.nextInt(96);
	int height = 5 + random.nextInt(96);
		luggageSize = length * width * height;
		luggageDimensions = length + " cm x " + width + " cm x " + height + " cm";
    }

	public String getLuggageDimensions() {
		return luggageDimensions;
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

	public int getLuggageSize(){
	        return luggageSize;
	}

	public Float getLuggageWeight() {
		return luggageWeight;
	}

	public Float getFee() {
		return fee;
	}
	
	public Boolean getCheckInSuccess() {
		return checkInSuccess;
	}

	public boolean getFeePaymentSuccess() {
		return feePaymentSuccess;
	}

	public void setCheckInSuccess(boolean b) {
		this.checkInSuccess = true; 
		
	}

	public void setFeePaymentSuccess(boolean b) {
		this.feePaymentSuccess = true;
		
	}

	public String toString() {
		return String.format(name + " , " + flightCode + " , " + luggageDimensions + " , " + luggageWeight + " kg");
	}


}
