import com.sun.javafx.fxml.builder.ProxyBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SecurityCheckTest {
    private static final int CAPACITY = 10; // capacity of queue
    private static ArrayBlockingQueue<Passenger> eQueue = new ArrayBlockingQueue<>(CAPACITY); // passengerqueue
    private static ArrayBlockingQueue<Passenger> bQueue = new ArrayBlockingQueue<>(CAPACITY);

//    Queue<Passenger> eQueue = new LinkedList<>();
//    Queue<Passenger> bClass = new LinkedList<>();

    
    // producer
    class PassengerProducer implements Runnable {
        @Override
        public void run() {
            while (true) {
                ProxyBuilder passList = null;
                if (passList == null || passList.isEmpty()) {
                    throw new IllegalArgumentException("There are no passengers waiting.");
                }
                Random random = new Random();
                int index = random.nextInt(passList.size());
                Passenger randompass = (Passenger) passList.get(index);
                // Separate passengers by class type
                    CheckInDesk.separatePassengerByClassType(randompass, eQueue, bQueue);
                    System.out.println(eQueue);
                    System.out.println(bQueue);
//                    System.out.println("Class Type 0 Queue:"+eClass.size());
//                    System.out.println("Class Type 1 Queue:"+bClass.size());
//                    Passenger passenger = generatePassenger(); // 生成乘客
//                    passengerQueue.put(passenger); // 将乘客放入队列
                try {
                    Thread.sleep(200); // 模拟乘客到达间隔
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    // customer
    class CheckIn implements Runnable {




        @Override
        public void run() {
//            try {
//                while (true) {
//                    Passenger passenger = passengerQueue.take(); //
//                    checkIn(passenger);
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
        }

        // 值机逻辑
//        private void checkIn(Passenger passenger) {
//             实现值机逻辑
//            System.out.println("Passenger checked in: " + passenger.getName());
//        }
    }

    // 主函数：启动生产者和消费者线程
    public static void main(String[] args) throws IOException {

        ReadFiles fcs = new ReadFiles();

        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        List<Passenger> passengerList = fcs.getPassengerList();
        List <Flight> flightList = fcs.getFlightList();

        SecurityCheckTest checkInSystem = new SecurityCheckTest();
        Thread producerThread = new Thread(checkInSystem.new PassengerProducer());
        Thread consumerThread = new Thread(checkInSystem.new CheckIn());
        producerThread.start();
        consumerThread.start();
    }

}
