//import java.awt.GridLayout;
//import java.util.Queue;
//
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//
//public class View extends JFrame implements Observer{
//	
//	private CheckInDesk checkInDesk;
//	private JTextArea economyQueueTextArea;
//	private JTextArea businessQueueTextArea;
//	private JLabel economyLabel;
//    private JLabel businessLabel;
//    private JLabel desk1Label;
//    private JLabel desk2Label;
//    private JLabel desk3Label;
//	  
//	public View(CheckInDesk checkInDesk) {
//		    super("Check-In Desks Status");
//	        this.checkInDesk = checkInDesk;
//	        this.checkInDesk.addObserver(this);   
//	        desk1Label = new JLabel("Desk 1: ");
//	        desk2Label = new JLabel("Desk 2: ");
//	        desk3Label = new JLabel("Desk 3: ");
//	        this.setLayout(new GridLayout(3, 1)); // 使用网格布局，每个Desk一行
//	        this.add(desk1Label);
//	        this.add(desk2Label);
//	        this.add(desk3Label);
//	        
//	        this.setSize(400, 200);
//	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        this.setVisible(true);
//	        
//	        
//	        
//	        setLayout(new GridLayout(2, 1)); // 简单布局：两行一列
//	        economyLabel = new JLabel("Economy Queue Size: 0");
//	        businessLabel = new JLabel("Business Queue Size: 0");
//	        
//	        JPanel panel = new JPanel();
//	        panel.add(economyLabel);
//	        panel.add(businessLabel);
//	
//	        economyQueueTextArea = new JTextArea();
//	        businessQueueTextArea = new JTextArea();
//	
//	        add(new JScrollPane(economyQueueTextArea)); // 为文本区域添加滚动条
//	        add(new JScrollPane(businessQueueTextArea));
//	        add(panel);
//	
//	        
//	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	        setVisible(true);
//	    }
//	
//
//	@Override
//	public void update() {
//		// TODO Auto-generated method stub
//		System.out.println("-------------------------------------------------");
//		 // 假设有方法将队列转换为字符串表示
//		String economyQueueInfo = passToString(CheckInDesk.getEconomyDesk());
//		String businessQueueInfo = passToString(CheckInDesk.getBusinessDesk());
//
//		// 更新GUI组件显示的队列信息
//		economyQueueTextArea.setText(economyQueueInfo);
//		businessQueueTextArea.setText(businessQueueInfo);
//		 System.out.println(economyQueueInfo);
//		 System.out.println(businessQueueInfo);
//			}
//
//	private String passToString(Queue<Passenger> queue) {
//	    StringBuilder sb = new StringBuilder();
//	    for (Passenger p : queue) {
//	        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append("\n");
//	    }
//	    return sb.toString();
//	}
//}
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class View extends JFrame implements Observer {

    private CheckInDesk checkInDesk1, checkInDesk2, checkInDesk3;
    private JTextArea eWTextArea, bWTextArea;
    private JTextArea desk1TextArea, desk2TextArea, desk3TextArea;
    private JTextArea flight1TextArea, flight2TextArea, flight3TextArea;
    private JTextArea[] flight;
    private JPanel mainPanel;
//    private JPanel deskPanel;
//    FlightCheckIn flightInfo1;
    String flightInfo1, flightInfo2, flightInfo3;
    String eWaitingInfo, bWaitingInfo;
    Map<String, FlightStats> Map ;

    // Assume these labels are defined elsewhere in your class
    private JLabel desk1Label, desk2Label, desk3Label;

    public View(CheckInDesk checkInDesk1, CheckInDesk checkInDesk2, CheckInDesk checkInDesk3) {
        super("Check-In Desks Status");
        this.checkInDesk1 = checkInDesk1;
        this.checkInDesk2 = checkInDesk2;
        this.checkInDesk3 = checkInDesk3;

        this.checkInDesk1.addObserver(this);
        this.checkInDesk2.addObserver(this);
        this.checkInDesk3.addObserver(this);

        initUI();
    }


    private void initUI() {

    	mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel waitingPanel = createWaitingPanel();
        mainPanel.add(waitingPanel);

        JPanel deskPanel = createDeskPanel();
        mainPanel.add(deskPanel);

        JPanel flightPanel = createFlightStatusPanel() ;
        mainPanel.add(flightPanel);

        add(mainPanel);
        pack(); // 根据组件大小自动调整窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createWaitingPanel() {
        // two waiting queue,one for Economy Class Lane, another for Business Class Priority Lane
        JPanel waitingPanel = new JPanel(new GridLayout(2, 1, 5, 10));

        eWTextArea = new JTextArea(8, 20);
        bWTextArea = new JTextArea(8, 20);

        JScrollPane scrollPane1 = new JScrollPane(eWTextArea);
        JScrollPane scrollPane2 = new JScrollPane(bWTextArea);

        eWTextArea.setText("There are currently "+" people waiting in the Economy Class Lane:\n");
        bWTextArea.setText("There are currently "+" people waiting in the Business Class Priority Lane:\n");

        waitingPanel.add(scrollPane1);
        waitingPanel.add(scrollPane2);
        add(waitingPanel, BorderLayout.CENTER);

        return waitingPanel;
    }

    private JPanel createDeskPanel() {
    	JPanel countersPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 三个柜台横向布局

        desk1TextArea = new JTextArea(5, 20);
        desk2TextArea = new JTextArea(5, 20);
        desk3TextArea = new JTextArea(5, 20);

        JScrollPane scrollPane1 = new JScrollPane(desk1TextArea);
        JScrollPane scrollPane2 = new JScrollPane(desk2TextArea);
        JScrollPane scrollPane3 = new JScrollPane(desk3TextArea);

        countersPanel.add(scrollPane1);
        countersPanel.add(scrollPane2);
//        countersPanel.add(new JLabel("Desk 3 (Business):"));
        countersPanel.add(scrollPane3);
        add(countersPanel, BorderLayout.CENTER);

		return countersPanel;


    }
    private JPanel createFlightStatusPanel() {
        // Add desk info here
          JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10)); // 三个柜台横向布局
          flight1TextArea = new JTextArea(5, 20);
          flight2TextArea = new JTextArea(5, 20);
          flight3TextArea = new JTextArea(5, 20);

          JScrollPane scrollPane1 = new JScrollPane(flight1TextArea);
          JScrollPane scrollPane2 = new JScrollPane(flight2TextArea);
          JScrollPane scrollPane3 = new JScrollPane(flight3TextArea);

          panel.add(scrollPane1);
          panel.add(scrollPane2);
          panel.add(scrollPane3);

          add(panel, BorderLayout.CENTER);

          return panel;
      }

    @Override
    public void update() {
        // 更新Desk状态显示逻辑
        eWaitingInfo = queueToString(checkInDesk1.getEconomyQueue());
        bWaitingInfo = queueToString(checkInDesk3.getBusinessQueue());
        String desk1Info = passToString(checkInDesk1.getCurrentPassenger());
        String desk2Info = passToString(checkInDesk2.getCurrentPassenger());
        String desk3Info = passToString(checkInDesk3.getCurrentPassenger());
        try {
			flightInfo1 = queueToString1(checkInDesk1.getFighthold());
			flightInfo2 = queueToString1(checkInDesk2.getFighthold());
			flightInfo3 = queueToString1(checkInDesk3.getFighthold());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        eWTextArea.setText("There are currently "+" people waiting in the Economy Class Lane:\n"+eWaitingInfo);
        bWTextArea.setText("There are currently "+" people waiting in the Business Class Priority Lane:\n"+bWaitingInfo);

        desk1TextArea.setText("Desk1:\n" + desk1Info);
        desk2TextArea.setText("Desk2:\n"+ desk2Info);
        desk3TextArea.setText("Desk3:\n" + desk3Info);

        flight1TextArea.setText(flightInfo1);
        flight2TextArea.setText(flightInfo2);
        flight3TextArea.setText(flightInfo3);
//
//        List<String> flightCodes = Arrays.asList("DA-4562", "PA-5723", "BI-3892");
//        Map = flightInfo.getFlightMap();
//        for (String flightcode : flightCodes) {
//            String flightStats = getFlightStatsString(flightcode);
//            // 假设有方法来获取对应航班号的文本区域
//            JTextArea textArea = getTextAreaForFlight(flightcode);
//            textArea.setText(flightStats);
//        }

//        StringBuilder statusBuilder = new StringBuilder();
//        for(Flight flight : flightList) {  // 假设flightList存储了所有航班对象
//            String status = flight.getCurrentStatus();  // 获取当前航班状态的方法
//            statusBuilder.append(status).append("\n");  // 将每个航班的状态添加到构建器
//        }
//        flightStatusTextArea.setText(statusBuilder.toString());



    }

//    private String getFlightStatsString(String flightCode) {
//        FlightStats stats = FlightCheckIn.getFlightStats(flightCode); // 获取特定航班的统计信息
//        // 格式化航班状态信息为字符串
//        return String.format("Flight %s: %d passengers, %f kg total weight, %f m^3 total volume",
//                             flightCode, stats.getPassengerCount(), stats.getTotalWeight(), stats.getTotalVolume());
//    }
    private String queueToString(Queue<Passenger> queue){
        StringBuilder wairingPassengers = new StringBuilder();
        for (Passenger pass : queue) {
            wairingPassengers.append(passToString(pass));
        }
        return wairingPassengers.toString();
    }

    private String passToString(Passenger p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append(" - ").append(p.getCheckInSuccess()).append("\n");
        return sb.toString();
    }

    public String queueToString1(FlightCheckIn f) throws IOException {
        StringBuilder sb = new StringBuilder();
        f.getFlightMap().forEach((flightNumber, stats) -> {
            sb.append("Flight Number: ").append(flightNumber).append("\n");
            sb.append("Passenger Count: ").append(stats.getPassengerCount()).append("\n");
//            sb.append("Total Weight: ").append(stats.getTotalWeight()).append(" kg\n");
//            sb.append("Total Volume: ").append(stats.getTotalVolume()).append(" cubic meters\n");
            sb.append("Weight Hold: ").append(stats.getWeightHold(flightNumber)).append("%\n");
			sb.append("Volume Hold: ").append(stats.getSizeHold(flightNumber)).append("%\n\n");
        });
        return sb.toString();
    }

    
   
  
}


