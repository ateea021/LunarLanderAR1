import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * This class will dispalay the crash status 
 * @Author Ateea Riaz
 */

class CrashedDisplay extends JPanel {
    public static final long serialVersionUID = 2L;

    // Display the image icons
    ImageIcon[] indicator = {
            new ImageIcon("led-grey.png"),
            new ImageIcon("led-red.png")
        };

    JLabel icon = new JLabel();
    
    public CrashedDisplay() {
        super( new FlowLayout( FlowLayout.LEFT, 5, 3));
        setBorder( BorderFactory.createTitledBorder("Status of the crash"));
        add(new JLabel ("Crashed: "));
        add(icon);
        icon.setIcon(indicator[0]);
    }

    /**
     * set status for crashed to red
     * Update display with icon
     */
    public void on(){
        icon.setIcon(indicator[1]);
    }

    /**
     * set status for crashed to grey
     * Update display with icon
     */
    public void off(){
        icon.setIcon(indicator[0]);
    }
}
