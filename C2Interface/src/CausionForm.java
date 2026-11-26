import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.IOException;

public class CausionForm extends JFrame {
    RequestCalls apiHandle;
    private JPanel cautionPanel;
    private JButton proceedButton;
    private JLabel areYouSureYouLabel;
    int OS;
    public CausionForm(String client, int OS, RequestCalls apiHandle){
        setContentPane(cautionPanel);
        setSize(700,150);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/SwaminHolder.png")));
        setTitle("Are you sure?");
        setResizable(false);
        setVisible(true);
        this.apiHandle = apiHandle;
        this.OS = OS;
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(client.equals("a")){
                        for(String id : apiHandle.queryBackend("FINDCLIENTS", "", OS).split("\n")){
                            DeleteMalware(id);
                        }
                    }else{
                        DeleteMalware(client);
                    }
                } catch (IOException | InterruptedException ex) {
                    new CustomDialog("Couldn't fetch: "+ex, "Something went wrong!", "Warning.png");
                }
                setVisible(false);
            }
        });
    }
    private void DeleteMalware(String id) throws IOException, InterruptedException {
        String payload = apiHandle.queryBackend("GETPAYLOAD", id, OS);
        String beacon = apiHandle.queryBackend("GETBEACON", id, OS);
        String command = "";
        if(payload.equals("None")||beacon.equals("None")){
            new CustomDialog("Couldn't fetch payload or beacon for: "+id, "Something went wrong!", "Warning.png");
        }
        if(OS == 1) {
            command = "rm -f /etc/systemd/system/"+payload+".service; systemctl daemon-reload; systemctl stop "+payload+".service";
        }else if(OS == 2){
            command = "C:\\Windows\\nssm.exe stop \""+payload+"\"; C:\\Windows\\nssm.exe remove \""+payload+"\" confirm; Remove-Item \"C:\\Windows\\nssm.exe\"; Remove-Item \"C:\\Windows\\"+beacon+".exe\" -Force";
        }
        System.out.println(command);
        apiHandle.Push(command, id, OS);
        Thread.sleep(5000); // Wait for service to get deleted
        apiHandle.queryBackend("REMOVECLIENT", id, OS);
    }
}