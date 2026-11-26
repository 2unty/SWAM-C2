import javax.swing.*;
import java.awt.*;

public class CommandOutputForm extends JFrame {
    private JPanel CmdPanel;
    private JTextArea CommandText;
    private JScrollPane CommandScroll;
    public CommandOutputForm(String text){
        setContentPane(CmdPanel);
        //CommandText = new JTextArea(600,600);
        CommandScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        CommandScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setTitle("Command Output");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/SwaminHolder.png")));
        setResizable(true);
        setVisible(true);
        CommandText.setText(text);
    }
}
