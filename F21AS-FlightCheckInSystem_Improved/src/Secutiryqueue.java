import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Secutiryqueue implements Runnable {
    private Queue<Passenger> checkInQueue; // 待安检的队列
    private ConcurrentLinkedQueue<Passenger> securityQueue; // 安检队列
    private String type; // 安检窗口类型（商务舱或经济舱）
    private static List<Observer> observers = new ArrayList<>();
    private volatile Passenger currentPassenger; // 当前正在处理的乘客    

    public Secutiryqueue(Queue<Passenger> checkInQueue, ConcurrentLinkedQueue<Passenger> securityQueue, String type) {
        this.checkInQueue = checkInQueue;
        this.securityQueue = securityQueue;
        this.type = type;
    }
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected static void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    public synchronized Passenger getCurrentPassenger() {
        return currentPassenger;
    }
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
//            Passenger passenger = checkInQueue.poll(); // 从待安检队列中取出乘客
            synchronized (this) {
                currentPassenger = checkInQueue.poll();
            }
            if (currentPassenger != null) {
                // 模拟安检处理过程
//                System.out.println("Processing " + currentPassenger.getName() + " at " + type + " security check."+ currentPassenger.getCheckInSuccess());
                // 将乘客加入到安检队列
                securityQueue.offer(currentPassenger);
                notifyObservers();
            }
           
            try {
                Thread.sleep(1000); // 模拟安检所需时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
