import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class SystemGUI extends JFrame implements ActionListener{
    private JPanel mainPanel;
    private Timer timer;
    private JLabel label ;
    private int xPos = 0;
    private JLabel queuedPassengers1;
    private JLabel queuedPassengers2;
    private JPanel deskPanel;
    private JPanel securityCheckPanel;
    private JPanel flightStatusPanel;
    private JTextArea queuedPassengersArea;
    private JScrollPane queuedPassengersScroll;
    private JTextArea[] deskAreas;
    private JTextArea[] securityAreas;
    private JTextArea[] flight;
    
    ReadFiles fcs = new ReadFiles();
    Queue<Passenger> checked = new LinkedList<>();
	

    public SystemGUI() throws IOException {
        setTitle("Airport Check-In System");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() throws IOException {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        fcs.readPassengers("NEW_passenger_bookings_2.0.csv");
        fcs.readFlights("Flight Detail.csv");
        
        label = new JLabel("Welcome to Scrolling GUI, " + CheckInDesk.flightLate1);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        mainPanel.add(label);
        timer = new Timer(50,this); // 璁剧疆瀹氭椂鍣紝姣�50姣瑙﹀彂涓�娆�
        timer.start(); // 鍚姩瀹氭椂鍣�
        
        queuedPassengers1 = new JLabel("Queued Passengers: List of eco passengers");
        mainPanel.add(queuedPassengers1);
        JPanel queued1 = createecoPassenger();
        mainPanel.add(queued1);
        queuedPassengers2 = new JLabel("Queued Passengers: List of Business passengers");
        mainPanel.add(queuedPassengers2);
        JPanel queued2 = createBusinessPassenger();
        mainPanel.add(queued2);
        

        // Check-in desks
        deskPanel = createDeskPanel();
        mainPanel.add(deskPanel);

        // Security checks
        securityCheckPanel = createSecurityCheckPanel();
        mainPanel.add(securityCheckPanel);

        // Flight status
        flightStatusPanel = createFlightStatusPanel();
        mainPanel.add(flightStatusPanel);

        add(mainPanel);
        pack(); // 鏍规嵁缁勪欢澶у皬鑷姩璋冩暣绐楀彛澶у皬
    }
  
    private JPanel createecoPassenger() {
    	JPanel panel = new JPanel();
    	// 鍒涘缓鍖呭惈涔樺淇℃伅鐨勬枃鏈尯鍩�
    	
        queuedPassengersArea = new JTextArea(8, 70);
        StringBuilder sb = new StringBuilder(); // 浣跨敤 StringBuilder 鏉ユ瀯寤烘渶缁堢殑瀛楃涓�
        for (Passenger p : fcs.getPassengerList()) {
            sb.append(p.toString()).append("\n"); // 灏嗘瘡涓箻瀹㈢殑淇℃伅娣诲姞鍒� StringBuilder 涓�
        }
        queuedPassengersArea.setText(sb.toString()); // 灏嗘瀯寤哄ソ鐨勫瓧绗︿覆璁剧疆缁� JTextArea
        queuedPassengersArea.setEditable(false); // 璁句负涓嶅彲缂栬緫
        // 灏嗘枃鏈尯鍩熸斁鍏ユ粴鍔ㄩ潰鏉夸腑
        queuedPassengersScroll = new JScrollPane(queuedPassengersArea);
        panel.add(queuedPassengersScroll);

        // Queued passengers
//        queuedPassengers = new JLabel("Queued Passengers: List of passengers");
//        panel.add(queuedPassengers);
        return panel;
    }
    private JPanel createBusinessPassenger() {
    	JPanel panel = new JPanel();
    	// 鍒涘缓鍖呭惈涔樺淇℃伅鐨勬枃鏈尯鍩�
        queuedPassengersArea = new JTextArea(8, 70);
       
        StringBuilder sb = new StringBuilder(); // 浣跨敤 StringBuilder 鏉ユ瀯寤烘渶缁堢殑瀛楃涓�
        for (Passenger p : fcs.getPassengerList()) {
            sb.append(p.toString()).append("\n"); // 灏嗘瘡涓箻瀹㈢殑淇℃伅娣诲姞鍒� StringBuilder 涓�
        }
        queuedPassengersArea.setText(sb.toString()); // 灏嗘瀯寤哄ソ鐨勫瓧绗︿覆璁剧疆缁� JTextArea
        queuedPassengersArea.setEditable(false); // 璁句负涓嶅彲缂栬緫
        // 灏嗘枃鏈尯鍩熸斁鍏ユ粴鍔ㄩ潰鏉夸腑
        queuedPassengersScroll = new JScrollPane(queuedPassengersArea);
        panel.add(queuedPassengersScroll);

        // Queued passengers
       

        return panel;
    }

    private JPanel createDeskPanel() {
        // Add desk info here
        deskAreas = new JTextArea[3];
        JPanel countersPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 涓変釜鏌滃彴妯悜甯冨眬

        for (int i = 0; i < deskAreas.length; i++) {
            deskAreas[i] = new JTextArea(5, 20);
            deskAreas[i].setEditable(false); // 璁剧疆鏂囨湰鍖哄煙涓嶅彲缂栬緫
            JScrollPane scrollPane = new JScrollPane(deskAreas[i]); // 涓烘瘡涓枃鏈尯鍩熸坊鍔犳粴鍔ㄦ潯
            // 鍋囪缁欐瘡涓煖鍙版坊鍔犱箻瀹俊鎭�
            deskAreas[i].setText("Desk " + (i + 1) + " Queue:\n" +"PAssenger...");

            countersPanel.add(scrollPane);
        }

        add(countersPanel, BorderLayout.CENTER);

        return countersPanel;
    }

    private JPanel createSecurityCheckPanel() {
    	 // Add desk info here
    	securityAreas = new JTextArea[3];
        JPanel countersPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 涓変釜鏌滃彴妯悜甯冨眬

        for (int i = 0; i < deskAreas.length; i++) {
        	securityAreas[i] = new JTextArea(5, 20);
        	securityAreas[i].setEditable(false); // 璁剧疆鏂囨湰鍖哄煙涓嶅彲缂栬緫
            JScrollPane scrollPane = new JScrollPane(securityAreas[i]); // 涓烘瘡涓枃鏈尯鍩熸坊鍔犳粴鍔ㄦ潯
            // 鍋囪缁欐瘡涓煖鍙版坊鍔犱箻瀹俊鎭�
            securityAreas[i].setText("Security " + (i + 1) + " Queue:\n" +"PAssenger...");

            countersPanel.add(scrollPane);
        }

        add(countersPanel, BorderLayout.CENTER);
       
        return countersPanel;
    }

    private JPanel createFlightStatusPanel() {
    	 // Add desk info here
    	flight = new JTextArea[3];
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10)); // 涓変釜鏌滃彴妯悜甯冨眬

        for (int i = 0; i < deskAreas.length; i++) {
        	securityAreas[i] = new JTextArea(5, 20);
        	securityAreas[i].setEditable(false); // 璁剧疆鏂囨湰鍖哄煙涓嶅彲缂栬緫
            JScrollPane scrollPane = new JScrollPane(securityAreas[i]); // 涓烘瘡涓枃鏈尯鍩熸坊鍔犳粴鍔ㄦ潯

            // 鍋囪缁欐瘡涓煖鍙版坊鍔犱箻瀹俊鎭�
            securityAreas[i].setText("Flight " + (i + 1) + " Queue:\n" +"PAssenger...");

            panel.add(scrollPane);
        }

        add(panel, BorderLayout.CENTER);
       
        return panel;
    }
    
    public void actionPerformed(ActionEvent e) {
        // 褰撳畾鏃跺櫒瑙﹀彂鏃讹紝鏇存柊鏍囩鐨勪綅缃�
    	
        xPos++;
        label.setLocation(xPos, 0);

        // 褰撴爣绛剧Щ鍑虹獥鍙ｈ竟鐣屾椂锛屽皢鍏剁Щ鍥炵獥鍙ｅ乏渚�
        if (xPos >= getWidth()) {
            xPos = -label.getWidth();
        }
    }

    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(() -> {
            try {
				new SystemGUI().setVisible(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
}
