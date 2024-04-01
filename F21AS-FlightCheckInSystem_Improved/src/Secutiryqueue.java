import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Secutiryqueue implements Runnable {
    private Queue<Passenger> checkInQueue; // 待安检的队列
    private ConcurrentLinkedQueue<Passenger> securityQueue; // 安检队列
    private String type; // 安检窗口类型（商务舱或经济舱）

    public Secutiryqueue(Queue<Passenger> checkInQueue, ConcurrentLinkedQueue<Passenger> securityQueue, String type) {
        this.checkInQueue = checkInQueue;
        this.securityQueue = securityQueue;
        this.type = type;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Passenger passenger = checkInQueue.poll(); // 从待安检队列中取出乘客
            if (passenger != null) {
                // 模拟安检处理过程
                System.out.println("Processing " + passenger.getName() + " at " + type + " security check."+ passenger.getCheckInSuccess());
                // 将乘客加入到安检队列
                securityQueue.offer(passenger);
            }
            // 为了简化示例，这里使用Thread.sleep模拟处理时间
            try {
                Thread.sleep(1000); // 模拟安检所需时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}