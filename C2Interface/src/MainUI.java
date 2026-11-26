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
    private JTextField portField;
    RequestCalls apiHandle;
    public MainUI(){
        cardLay = new CardLayout();
        mainPanel = new JPanel(cardLay);
        mainPanel.add(loginPanel,"Login");
        cardLay.show(mainPanel,"Login");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/SwaminHolder.png")));
        setSize(300,300);
        setLocationRelativeTo(null);
        setTitle("Enter IP");
        setResizable(false);
        setVisible(true);
        enterC2IPLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/SwaminHolder_Comp.png")).getImage().getScaledInstance(130, 130, Image.SCALE_REPLICATE)));
        ipEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ip = ipField.getText();
                    String port = portField.getText();
                    if(ip.isEmpty() || port.isEmpty()){
                        new CustomDialog("Please input the IP and the port", "Something went wrong!", "Warning.png");
                        return;
                    }
                    apiHandle = new RequestCalls(ip, port);
                    String fetchedIPs;
                    fetchedIPs = apiHandle.queryBackend("FINDCLIENTS", "", 3);
                    //JOptionPane.showMessageDialog(null, "Machines found:\n"+fetchedIPs);
                    //JOptionPane.showMessageDialog(null, "Machines found:\n"+fetchedIPs, "Found machines", JOptionPane.INFORMATION_MESSAGE,new ImageIcon(new ImageIcon("SwaminHolder.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                    new CustomDialog(fetchedIPs.split("\n").length+" hosts found\n"+fetchedIPs, "Found machines", "Cross.png");
                    new MenuForm(apiHandle);
                    MainUI.this.setVisible(false);
                } catch (IOException | InterruptedException | IllegalArgumentException ex) {
                    new CustomDialog( "Couldn't fetch: "+ex, "Something went wrong!", "Warning.png");
                }
            }
        });
    }
}
