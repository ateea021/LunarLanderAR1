import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
/**
 * this class will dispalay the values of fules 
 * @Author Ateea Riaz
 */

public class FuelDisplay extends JPanel {
    private static final long serialVersionUID = 42l;

    /* Make GUI elements to display fuel state and related information*/
    JTextField fuel; 
    /**
     * constuctor for FuelDisplay class
     */
    public FuelDisplay() {
        /* Add layout manager, add GUI elements to Panel,*/
        super( new FlowLayout( FlowLayout.LEFT, 5, 3));
        setBorder( BorderFactory.createTitledBorder("Fuel percentage"));
        add(new JLabel("Fuel:"));
        fuel = new JTextField(5);
        add(this.fuel);
    }
    /**
     * set the values for lander's fuel.
     * Update display with new fuel quantity value
     * @param percent fuel precentage 
     */
    public void setFuel(float percent) {
        fuel.setText(Float.toString(percent) + "%");
    }
}
