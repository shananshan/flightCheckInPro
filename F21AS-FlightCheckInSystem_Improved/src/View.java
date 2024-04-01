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
//		String economyQueueInfo = queueToString(CheckInDesk.getEconomyDesk());
//		String businessQueueInfo = queueToString(CheckInDesk.getBusinessDesk());
//
//		// 更新GUI组件显示的队列信息
//		economyQueueTextArea.setText(economyQueueInfo);
//		businessQueueTextArea.setText(businessQueueInfo);
//		 System.out.println(economyQueueInfo);
//		 System.out.println(businessQueueInfo);
//			}
//
//	private String queueToString(Queue<Passenger> queue) {
//	    StringBuilder sb = new StringBuilder();
//	    for (Passenger p : queue) {
//	        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append("\n");
//	    }
//	    return sb.toString();
//	}
//}
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class View extends JFrame implements Observer,ActionListener{

    private CheckInDesk checkInDesk1, checkInDesk2, checkInDesk3;
    private Securityqueue BSq;
    private Securityqueue ESq;
    private JTextArea desk1TextArea, desk2TextArea, desk3TextArea;
    private JTextArea flight1TextArea, flight2TextArea, flight3TextArea;
    private JTextArea  security1TextArea, security2TextArea;
    private JTextArea eWTextArea, bWTextArea;
    private JTextArea[] flight;
    private JPanel mainPanel;
    private JPanel deskPanel;
    FlightCheckIn flightInfo;
    String flightInfo1, flightInfo2, flightInfo3;
    String desk1Info,desk2Info,desk3Info;
//    FlightStats stats ;
    Map<String, FlightStats> Map ;
//    private static List<Observer> observers = new ArrayList<>();
    
    
    
    // Assume these labels are defined elsewhere in your class
    private JLabel desk1Label, desk2Label, desk3Label;
	private Timer timer;
	private JLabel label ;
	private int xPos = 0;
    
    public View(CheckInDesk checkInDesk1, CheckInDesk checkInDesk2, CheckInDesk checkInDesk3,Securityqueue BSq
    ,Securityqueue ESq) {
        super("Check-In Desks Status");
        this.checkInDesk1 = checkInDesk1;
        this.checkInDesk2 = checkInDesk2;
        this.checkInDesk3 = checkInDesk3;
        this.BSq = BSq;
        this.ESq = ESq;

        this.checkInDesk1.addObserver(this);
        this.checkInDesk2.addObserver(this);
        this.checkInDesk3.addObserver(this);
        this.BSq.addObserver(this);
        this.BSq.addObserver(this); 
        
        initUI();
    }
    

    private void initUI() {
    	
    	mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        label = new JLabel("Welcome to AirPort");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        mainPanel.add(label);
        timer = new Timer(20,this); // 设置定时器，每50毫秒触发一次
        timer.start(); // 启动定时器
        
        JPanel waitingPanel = createWaitingPanel();
        mainPanel.add(waitingPanel);
        
        deskPanel = createDeskPanel();
        mainPanel.add(deskPanel);
        
        JPanel SecurityPanel =createSecurityDeskPanel()  ;
        mainPanel.add(SecurityPanel);
        
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

        eWTextArea = new JTextArea(15, 20);
        bWTextArea = new JTextArea(5, 20);

        JScrollPane scrollPane1 = new JScrollPane(eWTextArea);
        JScrollPane scrollPane2 = new JScrollPane(bWTextArea);

      

        waitingPanel.add(scrollPane1);
        waitingPanel.add(scrollPane2);
        add(waitingPanel, BorderLayout.CENTER);

        return waitingPanel;
    }

    private JPanel createDeskPanel() {
    	JPanel countersPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 三个柜台横向布局

        desk1TextArea = new JTextArea(6, 25);
        desk2TextArea = new JTextArea(6, 25);
        desk3TextArea = new JTextArea(6, 25);

        JScrollPane scrollPane1 = new JScrollPane(desk1TextArea);
        JScrollPane scrollPane2 = new JScrollPane(desk2TextArea);
        JScrollPane scrollPane3 = new JScrollPane(desk3TextArea);

        countersPanel.add(scrollPane1);
        countersPanel.add(scrollPane2);
        countersPanel.add(scrollPane3);
        add(countersPanel, BorderLayout.CENTER);
        
		return countersPanel;
    	
    	
    }
    private JPanel createFlightStatusPanel() {
        // Add desk info here
       
          JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10)); // 三个柜台横向布局
          flight1TextArea = new JTextArea(6, 25);
          flight2TextArea = new JTextArea(6, 25);
          flight3TextArea = new JTextArea(6, 25);
          
          JScrollPane scrollPane1 = new JScrollPane(flight1TextArea);
          JScrollPane scrollPane2 = new JScrollPane(flight2TextArea);
          JScrollPane scrollPane3 = new JScrollPane(flight3TextArea);
          
          panel.add(scrollPane1);
          panel.add(scrollPane2);
          panel.add(scrollPane3);
         
          add(panel, BorderLayout.CENTER);
         
          return panel;
      }
    
    private JPanel createSecurityDeskPanel() {
        // Add desk info here
       
          JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10)); // 三个柜台横向布局
          security1TextArea = new JTextArea(6, 25);
          security2TextArea= new JTextArea(6, 25);
          
          
          JScrollPane scrollPane1 = new JScrollPane(security1TextArea);
          JScrollPane scrollPane2 = new JScrollPane(security2TextArea);
          
          
          panel.add(scrollPane1);
          panel.add(scrollPane2);

         
          add(panel, BorderLayout.CENTER);
         
          return panel;
      }

    @Override
    public void update() {
        // 更新Desk状态显示逻辑
    	String ewInfo = queueToString(checkInDesk1.getEconomyQueue());
    	String bwInfo = queueToString(checkInDesk3.getBusinessQueue());
    			
    	
    	eWTextArea.setText("There are currently "+checkInDesk1.getEconomyQueue().size()+" people waiting in the Economy Class Lane:\n"+ewInfo);
        bWTextArea.setText("There are currently "+checkInDesk3.getBusinessQueue().size()+" people waiting in the Business Class Priority Lane:\n"+bwInfo);

        if(checkInDesk1.getCurrentPassenger() == null) {
            desk1Info = "Waiting...";
        }else {
            desk1Info = passToString2(checkInDesk1.getCurrentPassenger());
        }
        if(checkInDesk2.getCurrentPassenger() == null) {
            desk2Info = "Waiting...";
        }else {
            desk2Info = passToString2(checkInDesk2.getCurrentPassenger());
        }
        if(checkInDesk3.getCurrentPassenger() == null) {
            desk3Info = "Waiting...";
        }else {
            desk3Info = passToString2(checkInDesk3.getCurrentPassenger());
        }

        desk1TextArea.setText("Economic Desk1:\n" + desk1Info);
        desk2TextArea.setText("Economic Desk2:\n"+ desk2Info);
        desk3TextArea.setText("Business Desk:\n" + desk3Info);

        desk1TextArea.setText("Economic Desk1:\n" + desk1Info);
        desk2TextArea.setText("Economic Desk2:\n"+ desk2Info);
        desk3TextArea.setText("Business Desk:\n" + desk3Info);
        
        flightInfo = checkInDesk1.getFighthold();
  
        List<String> flightCodes = Arrays.asList("DA-4562", "PA-5723", "BI-3892");
        Map = flightInfo.getFlightMap();
        for (String flightcode : flightCodes) {
        	FlightStats flightStats = flightInfo.getFlightStats(Map,flightcode); 	
        	if(flightStats == null) {
        		flight1TextArea.setText("Economic waiting...");
        		flight2TextArea.setText("Economic waiting...");
        		flight3TextArea.setText("Business waiting...");
        	}else {
        	if(flightcode.equals("DA-4562")) {
        		String a = String.format("Flight %s\n %d checked of 120\n ",flightcode, flightStats.getPassengerCount());	
        		String fs = FlightToString(flightcode,flightStats);
        		flight1TextArea.setText(a + fs);
        	}else if(flightcode.equals("PA-5723")) {
        		String b = String.format("Flight %s\n %d checked of 200\n ",flightcode, flightStats.getPassengerCount());
        		String fs = FlightToString(flightcode,flightStats);
        		flight2TextArea.setText(b + fs);
        	}else if(flightcode.equals("BI-3892")) {
        		String c = String.format("Flight %s\n %d checked of 300\n ",flightcode, flightStats.getPassengerCount());
        		String fs = FlightToString(flightcode,flightStats);
        		flight3TextArea.setText(c+fs);
        	}
        	
        	}
        }
        if(BSq.getCurrentPassenger() == null ) {
        	security1TextArea.setText("Business waiting...");
        }else {
        	String securityInfo1 = securityToString(BSq.getCurrentPassenger());
        	security1TextArea.setText("Business Security:\n"+ securityInfo1);
        }
        if(ESq.getCurrentPassenger()==null) {
        	security2TextArea.setText("Economic waiting...");
        }else {
        	
        	String securityInfo2 = securityToString(ESq.getCurrentPassenger());
       	 	security2TextArea.setText("Economic Security:\n" + securityInfo2);
        }
    		
       
   
    }
    
    public void actionPerformed(ActionEvent e) {
        // 当定时器触发时，更新标签的位置
        xPos++;
        label.setLocation(xPos, 0);

        // 当标签移出窗口边界时，将其移回窗口左侧
        if (xPos >= getWidth()) {
            xPos = -label.getWidth();
        }
    }

    private String passToString1(Passenger p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append(" - ").append("Luggage Size: "+p.getLuggageDimensions()).append(" - ").append("Weight:").append(p.getLuggageWeight()).append("kg").append("\n");
        return sb.toString();
    }

    private String passToString2(Passenger p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append("\n").append("Luggage Size: "+p.getLuggageDimensions()).append("\n").append("Weight:").append(p.getLuggageWeight()+"KG").append("\n").append("Fee: "+p.getFee());
        return sb.toString();
    }
    
    private String securityToString(Passenger p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append("\n");
        return sb.toString();
    }
    
    
    
    private String FlightToString(String fc, FlightStats fs) {
        StringBuilder sb = new StringBuilder();
        sb.append(fs.getSizeHold(fc)+"% of total Volume").append("\n").append(fs.getWeightHold(fc) + "% of total Weight");
        return sb.toString();
    }
    
    private String queueToString(Queue<Passenger> queue){
    	StringBuilder wairingPassengers = new StringBuilder();
    	for (Passenger pass : queue) {
    	wairingPassengers.append(passToString1(pass));
    	}
    	return wairingPassengers.toString();
    	}
   
}
