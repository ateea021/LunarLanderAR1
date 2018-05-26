import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class will dispalay the altitude values
 * @Author Ateea Riaz
 */
public class AltitudeDisplay extends JPanel {
    private static final long serialVersionUID = 42l;

    //Create GUI elements to display altitude and related information
   
    JTextField altitude; 
    /**
     * constuctor for AltitudeDisplay class
     */
    public AltitudeDisplay() {
        // layout manager,GUI elements to Panel,
        super( new FlowLayout( FlowLayout.LEFT, 5, 3));
        setBorder( BorderFactory.createTitledBorder("Display Altitude")); // broder layout for altitude
        add(new JLabel("Altitude:"));
        altitude = new JTextField(5);
        add(this.altitude);
    }
    /***
     * set values for altitude
     * Update display with altitue value
     * @param alt value of altitue
     */
    public void setAltitude(float alt) {
        altitude.setText( Float.toString(alt) );
    }
}
