import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Securityqueue implements Runnable {
    private Queue<Passenger> checkInQueue; // The queue of passengers waiting for security check.
    private Queue<Passenger> securityQueue; // The queue of passengers who have passed the security check.
    private String type; // The type of security check (Business or Economy).
    private static List<Observer> observers = new ArrayList<>(); // A list of observers that are notified of changes.
    private volatile Passenger currentPassenger; // The current passenger being processed by the security check.    

    // Constructor initializes the security queue with the given check-in queue, security queue, and type.
    public Securityqueue(Queue<Passenger> checkInQueue, Queue<Passenger> securityQueue, String type) {
        this.checkInQueue = checkInQueue;
        this.securityQueue = securityQueue;
        this.type = type;
    }
    
    // Adds an observer to the list of observers.
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Removes an observer from the list of observers.
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Notifies all observers of a change.
    protected static void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    
    // Returns the current passenger being processed for security check.
    public synchronized Passenger getCurrentPassenger() {
        return currentPassenger;
    }
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                // Polls the next passenger from the check-in queue to be processed.
                currentPassenger = checkInQueue.poll();
            }
            if (currentPassenger != null) {
//            	if (securityQueue.isEmpty()==false){
                // Adds the current passenger to the security queue after processing.
                securityQueue.offer(currentPassenger);
                // Notifies observers about the change (i.e., a new passenger has been added to the security queue).
                notifyObservers();
            }else {
            	currentPassenger = null;
            	notifyObservers();
            }
            try {
                Thread.sleep(1000); // Simulates the time required for security check.
            } catch (InterruptedException e) {
                // Interrupts the current thread if it's interrupted during sleep.
                Thread.currentThread().interrupt();
            }
        }
    }
}
