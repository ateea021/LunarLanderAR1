import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * this class will dispalay the values of orientation 
 * @Author Ateea Riaz
 */

public class OrientationDisplay extends JPanel {
    private static final long serialVersionUID = 42l;
    // Make the GUI element to display velocity vlaue
    private JLabel vXL, vYL;
    private JTextField vXBox, vYBox;
    
    public OrientationDisplay(){
        super( new FlowLayout( FlowLayout.LEFT, 5, 0));
        // allow borders for the component 
        setBorder( BorderFactory.createTitledBorder("Display Orientation (Lander velocity)"));
        
        vXL = new JLabel();
        vXL.setText("Vx :");
        add(vXL);
        
        vXBox =  new JTextField(5);
        vXBox.setEditable(false);
        add(vXBox);
        
        vYL = new JLabel();
        vYL.setText("Vy :");
        add(vYL);
        
        vYBox =  new JTextField(5);
        vYBox.setEditable(false);
        add(vYBox);
    }
    
    /**
     * set the value of vertical velocity
     * Update display with new velocity value
     * @param vY horizontal velocity 
     */
    public void setVX(int vX){
        vXBox.setText(Integer.toString(vX));
    }
    /**
     * set the value of vertical velocity
     * Update display with new velocity value
     * @param vY vertical velocity 
     */
    public void setVY(int vY){
        vYBox.setText(Integer.toString(vY));
    }
}
