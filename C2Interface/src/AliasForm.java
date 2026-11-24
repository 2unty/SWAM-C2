import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AliasForm extends JFrame {
    private JPanel aliasPanel;
    private JButton setAliasButton;
    private JTextField setField;
    private JLabel setLabel;
    public AliasForm(String IP, MenuForm menu){
        setContentPane(aliasPanel); // Set the content pane to the panel object you manually created
        setSize(400, 200); // Sets size
        setLocationRelativeTo(null);
        setLabel.setText("Set alias for "+IP);
        setTitle("Set Alias"); // Title
        setResizable(false); // Resizable
        setVisible(true);
        setAliasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setAlias(IP, setField.getText());
                setVisible(false);
            }
        });
    }

}
