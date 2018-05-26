import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * this class will dispalay the values of throttle 
 * @Author Ateea Riaz
 */
public class ThrottleDisplay extends JPanel {
    private static final long serialVersionUID = 42l;

    /* Make GUI element to display throttle */
    JTextField throttle;
    /**
     * The Constructor for ThrottleDisplay class
     */
    public ThrottleDisplay() {
        /*  layout manager and add GUI elements to Panel*/
        super( new FlowLayout( FlowLayout.LEFT, 5, 3));
        setBorder( BorderFactory.createTitledBorder("Throttle percentage"));
        add(new JLabel("Throttle:"));
        throttle = new JTextField(5);
        add(this.throttle);
    }

    /**
     * set the value of throttle
     * Update display with throttle value
     * @Param t throttle precentage
     */
    public void setThrottle(float t) {
        throttle.setText( Float.toString(t) + "%");
    }
}
