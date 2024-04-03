import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    // Attributes to store flight details.
    int numOfPassengers; // Current number of passengers on the flight.
    int maximumPassengers; // Maximum number of passengers 
    double maximumBaggageWeight; // Maximum total weight of baggage 
    double maxbaggageVolume; // Maximum volume of baggage allowed per passenger.
    double extraVolumeFee; // Fee for each unit volume of baggage exceeding the maximum allowed.
    double extraWeightFee; // Fee for each unit weight of baggage exceeding the maximum allowed.
    static double maxFlightVolume; // Maximum total volume of baggage allowed on the flight.
    static double maxFlightWeight; // Maximum total weight of baggage allowed on the flight.
    String flightCode;
    String destination;
    String carrier;
    String TakeoffTime;

    // List to store passengers on the flight.
    List<Passenger> passengerList;

    /**
     * Constructor that initializes a new Flight object with data.
     *
     * @param data An array of Strings containing the flight details.
     */
    Flight(String[] data) {
        passengerList = new ArrayList<>();
        flightCode = data[0];
        destination = data[2];
        carrier = data[3];

        maximumPassengers = Integer.parseInt(data[4]);
        maximumBaggageWeight = Double.parseDouble(data[6]);
        maxbaggageVolume = Double.parseDouble(data[7]);

        extraVolumeFee = Double.parseDouble(data[10]);
        extraWeightFee = Double.parseDouble(data[8]);

        maxFlightWeight = Double.parseDouble(data[11]);
        maxFlightVolume = Double.parseDouble(data[12]);
        TakeoffTime = data[13];

    }

    /**
     * Calculates additional fees based on baggage weight and volume.
     *
     * @param dimension The volume of the baggage.
     * @param weight    The weight of the baggage.
     * @return The total extra fee calculated.
     */
    public float calulatefee(float dimension, float weight) {
        double extraWeightFee = 0.0;
        double extraVolumeFee = 0.0;

        // If the weight exceeds the maximum, calculate the additional weight fee.
        if (weight > this.maximumBaggageWeight) {
            extraWeightFee = (weight - this.maximumBaggageWeight) * this.extraWeightFee;
        }

        // If the volume exceeds the maximum, calculate the additional volume fee.
        if (dimension > this.maxbaggageVolume) {
            extraVolumeFee = (dimension - this.maxbaggageVolume) * this.extraVolumeFee;
        }
        return (float) (extraWeightFee + extraVolumeFee);
    }

    public static float calulateload(float dimension, float weight) {
        return 0;
    }

    public static double getmaximumBaggageWeight() {
        return maxFlightWeight;
    }

    public static double getmaxbaggageVolumet() {
        return maxFlightVolume;
    }

    public double getmaxpassenger() {
        return maximumPassengers;
    }

    public String gettakeoftime() {
        return TakeoffTime;
    }

    // Constructs a string by concatenating various flight attributes with commas.
    public String toString() {
        return String.format(flightCode + " , " + destination + " , " + carrier + " , " + maximumPassengers + " , " + maximumBaggageWeight + " , " + maxbaggageVolume + " ,"
                + +extraVolumeFee + "," + extraWeightFee + " , " + maxFlightWeight + " , " + maxFlightVolume + " , " + TakeoffTime);
    }


}
