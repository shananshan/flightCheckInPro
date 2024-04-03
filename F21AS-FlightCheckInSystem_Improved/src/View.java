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
    private Securityqueue BSq; //Business class security screening
    private Securityqueue ESq; //Economy class security screening
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
    Map<String, FlightStats> Map ;
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
        //Add observer to these models.
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
        timer = new Timer(20,this); // Set timer
        timer.start(); 
        
        JPanel waitingPanel = createWaitingPanel();
        mainPanel.add(waitingPanel);
        
        deskPanel = createDeskPanel();
        mainPanel.add(deskPanel);
        
        JPanel SecurityPanel =createSecurityDeskPanel()  ;
        mainPanel.add(SecurityPanel);
        
        JPanel flightPanel = createFlightStatusPanel() ;
        mainPanel.add(flightPanel);
        
        add(mainPanel);
        pack(); // Automatic window resizing based on component size
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
    	//create check in desks panel 
    	JPanel countersPanel = new JPanel(new GridLayout(1, 3, 10, 10)); 

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
        // create flight status panel here
          JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10)); // three horizontal layout
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
       //create security desk panels here.
          JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10)); 
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
        // update each desk info
    	String ewInfo = queueToString(checkInDesk1.getEconomyQueue());
    	String bwInfo = queueToString(checkInDesk3.getBusinessQueue());
    			
    	
    	eWTextArea.setText("There are currently "+checkInDesk1.getEconomyQueue().size()+" people waiting in the Economy Class Lane:\n"+ewInfo);
        bWTextArea.setText("There are currently "+checkInDesk3.getBusinessQueue().size()+" people waiting in the Business Class Priority Lane:\n"+bwInfo);

        if(checkInDesk1.getCurrentPassenger() == null) {
            desk1Info = "Waiting..."; // if there is not passenger,print waiting..
        }else {
        	//set current passenger info to deskInfo
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
        //get flight status here
        for (String flightcode : flightCodes) {
        	FlightStats flightStats = flightInfo.getFlightStats(Map,flightcode); 	
        	if(flightStats == null) {
        		//if flightStats is null show waiting in the panel
        		flight1TextArea.setText("Economic waiting...");
        		flight2TextArea.setText("Economic waiting...");
        		flight3TextArea.setText("Business waiting...");
        	}else {
        	if(flightcode.equals("DA-4562")) {
        		//show flightInfo in flightTextArea
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
        	// if BSq is null show waiting...
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
    	 // update the position of the label when the timer is triggered
        xPos++;
        label.setLocation(xPos, 0);

        // move the tab back to the left side of the window when it moves out of the window boundary
        if (xPos >= getWidth()) {
            xPos = -label.getWidth();
        }
    }

    private String passToString1(Passenger p) {
    	// Change the passenger information to a String variable 
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append(" - ").append("Luggage Size: "+p.getLuggageDimensions()).append(" - ").append("Weight:").append(p.getLuggageWeight()).append("kg").append("\n");
        return sb.toString();
    }

    private String passToString2(Passenger p) {
    	// Change the passenger information to a String variable and Show it to CheckinDesk
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append("\n").append("Luggage Size: "+p.getLuggageDimensions()).append("\n").append("Weight:").append(p.getLuggageWeight()+"KG").append("\n").append("Fee: £"+p.getFee()+"");
        return sb.toString();
    }
    
    private String securityToString(Passenger p) {
    	// Change the passenger information to a String variable and Show it to Securitydesk
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" - ").append(p.getFlightCode()).append("\n");
        return sb.toString();
    }
    
    private String FlightToString(String fc, FlightStats fs) {
    	// Change the passenger information to a String variable and Show it to Securitydesk
        StringBuilder sb = new StringBuilder();
        sb.append("Volume Hold:"+fs.getSizeHold(fc)+"%").append("\n").append("Weight Hold:"+fs.getWeightHold(fc) + "%").append("\n");
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
