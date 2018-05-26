import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * this class will dispalay the status of low fuel
 * @Author Ateea Riaz
 */

class LowFuel extends JPanel {
    public static final long serialVersionUID = 2L;

    // Make the image icons for indicatingh fuel level
    ImageIcon[] indicator = {
            new ImageIcon("led-grey.png"),
            new ImageIcon("led-orange.png")
        };

    JLabel icon = new JLabel(); // make label 
    /**
     * the constructor for LowFuel class
     */
    public LowFuel() {
        super( new FlowLayout( FlowLayout.LEFT, 5, 3));
        setBorder( BorderFactory.createTitledBorder("Fuel status"));
        add(new JLabel ("Low Fuel: "));
        add(icon);
        icon.setIcon(indicator[0]);
    }

    /**
     * set status for fuel to orange
     * Update display with icon
     */
    public void on(){
        icon.setIcon(indicator[1]);
    }

    /**
     * set status for fuel to grey
     * Update display with icon
     */

    public void off(){
        icon.setIcon(indicator[0]);
    }
}
