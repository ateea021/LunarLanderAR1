import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class creates JFrame window to display and update status and value for lander game 
 * @Author Ateea Riaz
 */

public class LanderDash extends JFrame implements Runnable {
    public static final long serialVersionUID = 2L;
    public static void main ( String[] args ) throws UnknownHostException {
        SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    new LanderDash(args[0], Integer.parseInt(args[1]));
                }
            });
    }

    //Some Information from Lander to Display 
    //Some variables in order to hold Information
    //in order from the game controller to display

    private float altitude; // value for altitude displayed
    private float fuel; // value for fuel percentage displayed
    private int flying; // status for flying displayed
    private int crashed; // status for crashed displayed
    //private int orientation; // value for orientation displayed
    private float throttle; // value for throttle displayed
    private int vx, vy; // value for velocity vx and vy displayed

    private InetSocketAddress address;

    /* the field to show panels to display Information
     *  panel for Altitude
     *  panel for fuel
     *  panel for orientation
     *  panel for flying status
     *  panel for crash status
     *  panel for low fuel status
     *  panel for throttle
     *  Panel to display IP and port numnber
     */
    DatagramPanel connection = new DatagramPanel();
    FlyingDisplay flyingDisplay = new FlyingDisplay();
    CrashedDisplay crashedDisplay = new CrashedDisplay();
    LowFuel lowFuel = new LowFuel();
    ThrottleDisplay throttleDisplay = new ThrottleDisplay();
    AltitudeDisplay altitudeDisplay = new AltitudeDisplay();
    FuelDisplay fuelDisplay = new FuelDisplay();
    OrientationDisplay orientationDisplay = new OrientationDisplay();

    public LanderDash(String ip, int port){
        super("The Lunar Lander Dashboard");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(
            new BoxLayout(getContentPane(),BoxLayout.Y_AXIS) );

        /* Make and add pannels to window */
        address = new InetSocketAddress(ip, port);
        connection.setAddress(address);
        add( connection ) ;

        add(flyingDisplay);
        add(crashedDisplay);
        add(lowFuel);
        add(altitudeDisplay);
        add(throttleDisplay);
        add(fuelDisplay);
        add(orientationDisplay);

        pack();
        setVisible(true);
        (new Thread(this)).start();
    }

    /**
     * function to run and create the components in JFrame
     */
    public void run(){
        try {
            DatagramSocket socket = new DatagramSocket(address);
            while(true){
                /* set up socket for reception */
                if(socket!=null){
                    /* begin with fresh datagram packet */
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive( packet );
                    // Extract message and pick appart into
                    // Lines and key:value pairs
                    String message = new String(packet.getData());

                    String[] lines = message.trim().split("\n");
                    for(String l : lines) {
                        String[] pair = l.split(":");
                        //work with key values and setting properties to display

                        // set the value for throttle 
                        if (pair[0].equals("throttle")) {
                            throttle = Float.parseFloat(pair[1]);
                            throttleDisplay.setThrottle(throttle);
                        }

                        // set the value for altitude 
                        if (pair[0].equals("altitude")) {
                            altitude = Float.parseFloat(pair[1]);
                            altitudeDisplay.setAltitude(altitude);
                        }

                        // set the status for fuel
                        if(pair[0].equals("fuel")) {

                            fuel = Float.parseFloat(pair[1]);
                            fuelDisplay.setFuel(fuel);

                            if (fuel <=  35) {
                                lowFuel.on();
                            }
                            else {
                                lowFuel.off();
                            }

                        }

                        // set the status for flying
                        if(pair[0].equals("flying")) {
                            flying = Integer.parseInt(pair[1]); 
                            if(flying == 1) {
                                flyingDisplay.on();
                            }
                            else if(flying == 0) {
                                flyingDisplay.off();
                            }

                        }

                        // set the status for crashed
                        if(pair[0].equals("crashed")) {
                            crashed = Integer.parseInt(pair[1]);
                            if(crashed == 1) {
                                crashedDisplay.on();
                            }
                            else if(crashed == 0) {
                                crashedDisplay.off();
                            }
                        }

                        // set the value for horizontal Velocity
                        if(pair[0].equals("Velocity X")) {
                            vx = Integer.parseInt(pair[1]);
                            orientationDisplay.setVX(vx);
                        }

                        // set the value for vertical Velocity
                        if(pair[0].equals("Velocity Y")) {
                            vy = Integer.parseInt(pair[1]);
                            orientationDisplay.setVY(vy);
                        }
                    }
                }
                try{Thread.sleep(50);}catch(InterruptedException e){}
            }
        }
        catch(Exception e) {
            System.err.println(e);
            System.err.println("in LanderDash.run()");
            System.exit(-1);
        }
    }
}
