import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.Boolean.FALSE;

public class CheckInDeskTest {
    public static void main(String []args) throws IOException {
        List<Passenger> passengerList;
        List<Flight> flightList;
        Queue<Passenger> classType0 = new LinkedList<>();
        Queue<Passenger> classType1 = new LinkedList<>();
        ReadFiles fcs = new ReadFiles();
        Queue<Passenger> checked = new LinkedList<>();
        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        passengerList = fcs.getPassengerList();
        flightList = fcs.getFlightList();
        for(Passenger p: fcs.passengerList){
            System.out.println(p);
        }

        // Separate passengers into queues based on class type
        CheckInDesk.separatePassengersByClassType(passengerList, classType0, classType1);

        // Print the contents of the queues
//        System.out.println("Class Type 1 Queue:");
//        while (!classType0.isEmpty()) {
//            System.out.println(classType0.poll());
//        }
//
        System.out.println("Class Type 2 Queue:");
        while (!classType1.isEmpty()) {
            System.out.println(classType1.poll());
        }
        while (!classType0.isEmpty()){
            checked = CheckInDesk.generateEconomyDesk(classType0,flightList);
        }
//        checked = CheckInDesk.generateEconomyDesk(classType0,flightList);
        System.out.println("Check in ......");

//        System.out.println("Generate a check-in desk successfully.");
    }
}
