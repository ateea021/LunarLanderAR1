import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * This class will dispalay the flying status 
 * @Author Ateea Riaz
 */
class FlyingDisplay extends JPanel {
    public static final long serialVersionUID = 2L;
    // the image icons 
    ImageIcon[] indicator = {
            new ImageIcon("led-grey.png"),
            new ImageIcon("led-green.png")
        };
    //label to// for flying status icon
    JLabel icon = new JLabel();
    /**
     * Constructor for FlyingDisplay class
     */
    public FlyingDisplay() {
        super( new FlowLayout( FlowLayout.LEFT, 5, 3));
        setBorder( BorderFactory.createTitledBorder("Status of Flying"));
        add(new JLabel ("Flying: "));
        add(icon);
        icon.setIcon(indicator[0]);
    }

    /**
     * set status for flying to grey icon
     * Update display with icon
     */
    public void on(){
        icon.setIcon(indicator[1]);
    }

    /**
     * set status for flying to green icon
     * Update display with icon
     */
    public void off(){
        icon.setIcon(indicator[0]);
    }
}
