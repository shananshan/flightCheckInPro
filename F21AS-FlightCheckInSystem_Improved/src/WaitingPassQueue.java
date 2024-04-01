import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class WaitingPassQueue implements Runnable{

    static ReadFiles fcs = new ReadFiles();
    //    private final Queue<Passenger> passQueue;
    private List<Passenger> passengerList = fcs.getPassengerList();
    static Queue<Passenger> economy = new LinkedList<>();
    static Queue<Passenger> business = new LinkedList<>();

    public WaitingPassQueue(List<Passenger> passengerList, Queue<Passenger> economy, Queue<Passenger> business) {
        this.passengerList = passengerList;
        this.economy = economy;
        this.business = business;

    }

    public static void separatePassengers(List<Passenger> passengerList, Queue<Passenger> economy, Queue<Passenger> business) {
        Random random = new Random();
        while (economy.size()<10) {

            if (!passengerList.isEmpty()) {
                int index = random.nextInt(passengerList.size());
                Passenger passenger = passengerList.remove(index);
                if (passenger.checkInSuccess.equals(false) && passenger.classType.equals("1")) {
                    economy.offer(passenger);
                } else if (passenger.checkInSuccess.equals(false) && passenger.classType.equals("0")) {
                    business.offer(passenger);
                } else if (passenger.checkInSuccess.equals(true)){

                }
            }
        }
    }

    public static Queue<Passenger> getWEQueue(){
        return economy;
    }

    public static Queue<Passenger> getWBQueue(){
        return business;
    }

    @Override
    public void run() {
        while(true){
            separatePassengers(passengerList,economy,business);
        }
    }
}
