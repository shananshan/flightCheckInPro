import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//public class SystemGUI {

    public class SystemGUI extends JFrame implements ActionListener {
        private JLabel label;
        private Timer timer;
        private int xPos = 0;

        public SystemGUI() {
            setTitle("Scrolling GUI");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 100);

            label = new JLabel("Welcome to Scrolling GUI");
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            add(label);

            timer = new Timer(50, this); // 设置定时器，每50毫秒触发一次
            timer.start(); // 启动定时器

            setVisible(true);
        }

        @Override
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
            SwingUtilities.invokeLater(() -> new SystemGUI());
        }
    }

//}
