/* Game Controller */
#include <mbed.h>
#include <EthernetInterface.h>
#include <rtos.h>
#include <mbed_events.h>
#include <FXOS8700Q.h>
#include <C12832.h>

/* LEDs set initially to off */
DigitalOut red(PTB22,1); // For crashed lander
DigitalOut green(PTE26,1); // For flying lander
DigitalOut topRed(D5,1); // For low fuel

/* Speaker */
PwmOut speaker(D6); // used when fuel is low

/* display */
C12832 lcd(D11, D13, D12, D7, D10);

/* event queue and thread support */
Thread dispatch;
EventQueue periodic;

/* Accelerometer */
I2C i2c(PTE25, PTE24);
FXOS8700QAccelerometer acc(i2c, FXOS8700CQ_SLAVE_ADDR1);

/* Input from Potentiometers */
AnalogIn  left(A0); // For variable throttle control

/* Digital Joystick input */
DigitalIn joyUp(A2); // For throttle
DigitalIn joyLeft(A4); // For roll
DigitalIn joyRight(A5); // For roll

/* User input states */
float throttle = 0;
float roll = 0;

/* Turn LEDs either on or off */
void on(DigitalOut colour) {
  colour.write(0);
}
void off(DigitalOut colour) {
  colour.write(1);
}

/* Function to determine if digital button is pressed */
bool isPressed(DigitalIn button) {
  if (!button.read()) {
    return false;
  }
  else {
    return true;
  }
}

/* Task for polling sensors */
void user_input(void) {

  // Get accelerometer information
  motion_data_units_t a;
  acc.getAxis(a);

  // Digital throttle control from joystick
  if(isPressed(joyUp)) {
    throttle = 100;
  }
  else {
    // Variable throttle from left potentiometer
    throttle = left.read() * 100;

    // Allow throttle to read 100
    if (throttle >= 99.5) {
      throttle = 100;
    }
  }

  // Digital roll control from joystick
  if(isPressed(joyLeft)) {
    roll =- 1;
  }
  else if (isPressed(joyRight)) {
    roll =+ 1;
  }
  else {
    // Set up angle for accelerometer roll control
    float magnitude = sqrt((a.x*a.x) + (a.y*a.y) + (a.z*a.z));
    a.x = a.x/magnitude;

    float angle = asin(a.x);

    // Set angle deadband
    if (angle <= 0.1 && angle >= -0.1) {
      angle = 0;
    }

    roll = -(angle); // Correct orientation
  }
}

/* States from Lander */
float altitude = 0;
float fuel = 100;
bool isFlying = 0;
bool crashed = 0;
int orientation = 0;
int xVelocity = 0;
int yVelocity = 0;

/* YOU will have to hardwire the IP address in here */
SocketAddress lander("192.168.80.16",65200);
SocketAddress dash("192.168.80.16",65250);

EthernetInterface eth;
UDPSocket udp;

/* Task for synchronous UDP communications with lander */
void communications(void){
  SocketAddress source;

  /* Format the message to send to the Lander */
  char buffer[512];
  //sprintf(buffer, "command:!\nthrottle:%f\nroll:%1.3f",throttle,roll); // No Spaces
  sprintf(buffer, "command:=\nthrottle:%f\nroll:%1.3f\naltitude:%1.2f\nfuel:%1.2f\nflying:%d\ncrashed:%d\norientation:%d\nVdx:%d\nVy:%d",
  throttle,roll, altitude,fuel,isFlying,crashed,orientation,xVelocity,yVelocity);
  /* Send and recieve messages */
  udp.sendto( lander, buffer, strlen(buffer));

  nsapi_size_or_error_t  n = udp.recvfrom(&source, buffer, sizeof(buffer));
  buffer[n] = '\0';

  /* Unpack incomming message */
  char *nextline, *line;

  for(
    line = strtok_r(buffer, "\r\n", &nextline);
    line != NULL;
    line = strtok_r(NULL, "\r\n", &nextline)
  ) {

    /* Split into key value pairs */
    char *key, *value;
    key = strtok(line, ":");
    value = strtok(NULL, ":");

    /* Convert value strings into state variables */
    if(strcmp(key,"altitude")==0) {
      altitude = atof(value);
    }
    else if(strcmp(key, "fuel")==0) {
      fuel = atof(value);
    }
    else if(strcmp(key, "flying")==0) {
      isFlying = atoi(value);
    }
    else if(strcmp(key, "crashed")==0) {
      crashed = atoi(value);
    }
    else if(strcmp(key, "orientation")==0) {
      orientation = atoi(value);
    }
    else if(strcmp(key, "Vx")==0) {
      xVelocity = atoi(value);
    }
    else if(strcmp(key, "Vy")==0) {
      yVelocity = atoi(value);
    }
  }
}

/* Task for asynchronous UDP communications with dashboard */
void dashboard(void){

  /* Create and format a message to the Dashboard */
  SocketAddress source;
  char buffer[512];

  sprintf(buffer, "command:=\nthrottle:%1.2f\naltitude:%1.2f\nfuel:%1.2f\nflying:%d\ncrashed:%d\nVelocity X:%d\nVelocity Y:%d",
  throttle,altitude,fuel,isFlying,crashed,xVelocity,yVelocity); // No Spaces
  //sprintf(buffer, "command:!\nthrottle:%f\nroll:%1.3f",throttle,roll);
  /* Send the message to the dashboard */
  udp.sendto( dash, buffer, strlen(buffer));

}

int main() {
  // Enable accelerometer
  acc.enable();

  /* ethernet connection : usually takes a few seconds */
  printf("Connecting \n");
  eth.connect();
  /* write obtained IP address to serial monitor */
  const char *ip = eth.get_ip_address();
  printf("IP address is: %s\n", ip ? ip : "No IP");

  /* open udp for communications on the ethernet */
  udp.open( &eth);

  printf("lander is on %s/%d\n",lander.get_ip_address(),lander.get_port() );
  printf("dash   is on %s/%d\n",dash.get_ip_address(),dash.get_port() );

  /* Call periodic tasks */
  // 50ms used for responsiveness
  periodic.call_every(50, communications);
  periodic.call_every(50, dashboard);
  periodic.call_every(50, user_input);

  /* start event dispatching thread */
  dispatch.start( callback(&periodic, &EventQueue::dispatch_forever) );

  while(1) {
    /* update display at whatever rate is possible */
    /* Show user information on the LCD */
    lcd.locate(0,0);
    lcd.printf("Altitude: %d \nfuel: %d \troll: %1.2f\nVelocity X: %d   Y: %d ",int(altitude),int(fuel),roll,xVelocity,yVelocity);

    /* Set LEDs as appropriate to show boolean states */
    if(isFlying) {
      off(red);
      on(green);
    }
    else if(crashed) {
      off(green);
      on(red);
    }

    // Flash light and sound speaker if low fuel and not crashed.
    if(crashed == 1) {
      speaker.period(1.0/440);
      speaker.write(0.5);
      on(topRed);
      wait(0.25);
      speaker.write(0);
      off(topRed);
      // lcd.locate(0,13);
      // lcd.cls();
      // lcd.printf("Lander crashed, %s\n");
    }

    wait(0.5);/* You may want to change this time
    to get a responsive display */
  }
}
