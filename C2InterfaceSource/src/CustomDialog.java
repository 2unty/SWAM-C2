import javax.swing.*;
import java.awt.*;

public class CustomDialog {
    public CustomDialog(String message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE,new ImageIcon(new ImageIcon(getClass().getResource("/resources/SwaminHolder.png")).getImage().getScaledInstance(100, 100,Image.SCALE_DEFAULT)));
    }
    public CustomDialog(String message, String title, String image){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE,new ImageIcon(new ImageIcon(getClass().getResource("/resources/"+image)).getImage().getScaledInstance(100, 100,Image.SCALE_DEFAULT)));
    }
}
