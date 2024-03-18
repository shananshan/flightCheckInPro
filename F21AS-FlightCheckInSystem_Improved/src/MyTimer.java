import java.util.*;

public class MyTimer {
    private static Timer timer = new Timer();

    // passenger enter the queue
    public static void passengerArrival(List<Passenger> passengerList, Queue<Passenger> economy, Queue<Passenger> business) {
        // every once in a while, a new passenger enter the queue
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CheckInDesk.separatePassengersByClassType(passengerList, economy, business);

                System.out.println("New passenger: ");
            }
        }, 0, 1000); // 1s
    }

    public static void flightDeparture(int departureTimeInSeconds, Queue<Passenger> passengerQueue) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                CheckInDesk.closeDesk();
//                System.out.println("Flight is departing. Check-in closed.");
                System.out.println("Processing...");
                processPassengers(passengerQueue);
            }
        }, departureTimeInSeconds * 1000); // * 1000ms
    }

    private static void processPassengers(Queue<Passenger> passengerQueue) {
        while (!passengerQueue.isEmpty()) {
            CheckInDesk.genrateEconomyDesk(passengerQueue);
            System.out.println("Processing check-in for passenger: " + passengerQueue.poll().getName());
        }
    }

}
