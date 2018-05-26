import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.net.*;
import java.io.*;

/**
 * class below is a place holder for ip-address and port number for internet addressing
 * @Author Ateea Riaz
 */

public class DatagramPanel extends JPanel {
    public static final long serialVersionUID = 2L;
    // Create GUI element to display ip address and port number 
    JTextField ipAddress, port;
    /**
     * constructor for DatagramPanel class
     * create component for ip address and port number
     */
    public DatagramPanel() {
        /* create a JPanel populated with border and text fields ****/
        super( new FlowLayout( FlowLayout.LEFT, 5, 0));
        setBorder( BorderFactory.createTitledBorder("Socket Address")); // make border for ip address and port number
        add(new JLabel("IP Address:"));
        ipAddress = new JTextField(10);
        add(this.ipAddress);
        add(new JLabel("Port Number:"));
        port = new JTextField(5);
        add(this.port);
    }
    
    /**
     * set the value for ip address and get the port number
     * @param where ip address and port
     */
    void setAddress(InetSocketAddress where){
        ipAddress.setText(where.getHostString());
        port.setText( Integer.toString(where.getPort()) );
    }
}
