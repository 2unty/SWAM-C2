import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainUI extends JFrame{
    private CardLayout cardLay;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JTextField ipField;
    private JButton ipEnterButton;
    private JLabel enterC2IPLabel;
    RequestCalls apiHandle;
    public MainUI(){
        cardLay = new CardLayout();
        mainPanel = new JPanel(cardLay);
        mainPanel.add(loginPanel,"Login");
        cardLay.show(mainPanel,"Login");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/SwaminHolder.png")));
        setSize(200,210);
        setLocationRelativeTo(null);
        setTitle("Enter IP");
        setResizable(false);
        setVisible(true);
        enterC2IPLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/Cross.png")).getImage().getScaledInstance(100, 100, Image.SCALE_REPLICATE)));
        ipEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    apiHandle = new RequestCalls(ipField.getText());
                    String fetchedIPs = apiHandle.findClients(3);
                    //JOptionPane.showMessageDialog(null, "Machines found:\n"+fetchedIPs);
                    //JOptionPane.showMessageDialog(null, "Machines found:\n"+fetchedIPs, "Found machines", JOptionPane.INFORMATION_MESSAGE,new ImageIcon(new ImageIcon("SwaminHolder.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                    new CustomDialog(fetchedIPs.split("\n").length+" hosts found\n"+fetchedIPs, "Found machines", "Cross.png");
                    new MenuForm(apiHandle);
                    MainUI.this.setVisible(false);
                } catch (IOException | InterruptedException ex) {
                    new CustomDialog("Couldn't fetch: "+ex, "Something went wrong!", "Warning.png");
                }
            }
        });
    }
}
