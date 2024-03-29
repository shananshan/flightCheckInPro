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
        
        label = new JLabel("Welcome to Scrolling GUI");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        mainPanel.add(label);
        timer = new Timer(50,this); // 设置定时器，每50毫秒触发一次
        timer.start(); // 启动定时器
        
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
        pack(); // 根据组件大小自动调整窗口大小
    }
  
    private JPanel createecoPassenger() {
    	JPanel panel = new JPanel();
    	// 创建包含乘客信息的文本区域
    	
        queuedPassengersArea = new JTextArea(8, 70);
        StringBuilder sb = new StringBuilder(); // 使用 StringBuilder 来构建最终的字符串
        for (Passenger p : fcs.getPassengerList()) {
            sb.append(p.toString()).append("\n"); // 将每个乘客的信息添加到 StringBuilder 中
        }
        queuedPassengersArea.setText(sb.toString()); // 将构建好的字符串设置给 JTextArea
        queuedPassengersArea.setEditable(false); // 设为不可编辑
        // 将文本区域放入滚动面板中
        queuedPassengersScroll = new JScrollPane(queuedPassengersArea);
        panel.add(queuedPassengersScroll);

        // Queued passengers
//        queuedPassengers = new JLabel("Queued Passengers: List of passengers");
//        panel.add(queuedPassengers);
        return panel;
    }
    private JPanel createBusinessPassenger() {
    	JPanel panel = new JPanel();
    	// 创建包含乘客信息的文本区域
        queuedPassengersArea = new JTextArea(8, 70);
       
        StringBuilder sb = new StringBuilder(); // 使用 StringBuilder 来构建最终的字符串
        for (Passenger p : fcs.getPassengerList()) {
            sb.append(p.toString()).append("\n"); // 将每个乘客的信息添加到 StringBuilder 中
        }
        queuedPassengersArea.setText(sb.toString()); // 将构建好的字符串设置给 JTextArea
        queuedPassengersArea.setEditable(false); // 设为不可编辑
        // 将文本区域放入滚动面板中
        queuedPassengersScroll = new JScrollPane(queuedPassengersArea);
        panel.add(queuedPassengersScroll);

        // Queued passengers
       

        return panel;
    }

    private JPanel createDeskPanel() {
        // Add desk info here
        deskAreas = new JTextArea[3];
        JPanel countersPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 三个柜台横向布局

        for (int i = 0; i < deskAreas.length; i++) {
            deskAreas[i] = new JTextArea(5, 20);
            deskAreas[i].setEditable(false); // 设置文本区域不可编辑
            JScrollPane scrollPane = new JScrollPane(deskAreas[i]); // 为每个文本区域添加滚动条
            // 假设给每个柜台添加乘客信息
            deskAreas[i].setText("Desk " + (i + 1) + " Queue:\n" +"PAssenger...");

            countersPanel.add(scrollPane);
        }

        add(countersPanel, BorderLayout.CENTER);

        return countersPanel;
    }

    private JPanel createSecurityCheckPanel() {
    	 // Add desk info here
    	securityAreas = new JTextArea[3];
        JPanel countersPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 三个柜台横向布局

        for (int i = 0; i < deskAreas.length; i++) {
        	securityAreas[i] = new JTextArea(5, 20);
        	securityAreas[i].setEditable(false); // 设置文本区域不可编辑
            JScrollPane scrollPane = new JScrollPane(securityAreas[i]); // 为每个文本区域添加滚动条
            // 假设给每个柜台添加乘客信息
            securityAreas[i].setText("Security " + (i + 1) + " Queue:\n" +"PAssenger...");

            countersPanel.add(scrollPane);
        }

        add(countersPanel, BorderLayout.CENTER);
       
        return countersPanel;
    }

    private JPanel createFlightStatusPanel() {
    	 // Add desk info here
    	flight = new JTextArea[3];
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10)); // 三个柜台横向布局

        for (int i = 0; i < deskAreas.length; i++) {
        	securityAreas[i] = new JTextArea(5, 20);
        	securityAreas[i].setEditable(false); // 设置文本区域不可编辑
            JScrollPane scrollPane = new JScrollPane(securityAreas[i]); // 为每个文本区域添加滚动条

            // 假设给每个柜台添加乘客信息
            securityAreas[i].setText("Flight " + (i + 1) + " Queue:\n" +"PAssenger...");

            panel.add(scrollPane);
        }

        add(panel, BorderLayout.CENTER);
       
        return panel;
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
