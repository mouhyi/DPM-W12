import java.util.ArrayList;
import lejos.geom.Point;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class Navigator implements TimerListener {

	public static final int NAVIGATOR_PERIOD = 100;
	private static double FORWARD_SPEED = 13.0; 	 // cm/s
	private static double ROTATION_SPEED = 40.0; // 
	private static double ROTATION_TOLERANCE = 1.0; // tolerable error while turning -> degrees
	private static double DISTANCE_TOLERANCE =3.0;	// too large
	Odometer odometer;
	private static boolean status = false;
	private Timer timer;
	

	/*
	 * Constructor
	 */
	public Navigator(Odometer odometer) {
		timer = new Timer(25, this);
		timer.start();

		this.odometer = odometer;

	}

	@Override
	public void timedOut() {							//// coordinates problem
		
		
		travelTo(60,30);	
		travelTo(30,30);
		travelTo(30,60);
		travelTo(60,0);
		
		timer.stop();
	}
	
	
	/*
    * This method causes the robot to travel to the absolute field location (x, y).
    */
	public void travelTo(double x, double y) {
		status = true;
		Robot robot =  odometer.getRobot();
		// stop motors
		robot.stop();
		double theta = Math.atan2(y - odometer.getY() , x - odometer.getX()); //// angle positive direction
		theta = 180.0 / Math.PI * theta ;
		theta = Odometer.adjustAngle(theta);			// bug here fixed
		
		LCD.drawString("theta "+theta, 0, 5);
		turnTo(theta);
		
		while(true){
			robot.setSpeeds(FORWARD_SPEED, 0);
			double curX = odometer.getX();
			double curY = odometer.getY();
			if (Math.abs(x-curX) <DISTANCE_TOLERANCE && Math.abs(y-curY) <DISTANCE_TOLERANCE ){
				robot.stop();
				break;
			}
		}
		status = false;
	}
	    
    /* This method causes the robot to turn (on point) to the absolute heading
	*  theta (degrees). This method turns a MINIMAL angle to it's target.
	*/
    public void turnTo(double theta){
    	status = true;
    	Robot robot =  odometer.getRobot();
    	robot.stop();
    	double headingError;
    	double curSpeed = ROTATION_SPEED;
    	do{
	    	headingError = Odometer.adjustAngle (theta - odometer.getTheta() );
	    	if(headingError>180)  headingError -= 360;
			//LCD.drawString("H "+headingError, 0, 6);
	    	
	    	//note: positive angle -> counter-clock wise
			if(headingError <0) curSpeed = -ROTATION_SPEED;
			
	    	/*if(headingError * curSpeed<0.0){	
	    	// if you the robot has turned past the right heading, invert and slow down rotation
				curSpeed *= -0.5;
			}*/	
	    	robot.setSpeeds(0.0, curSpeed);
	    	}
	    while(Math.abs(headingError) > ROTATION_TOLERANCE);
	    
	    // stop motors
  		robot.stop();
  		
  		status = false;
    }
    
    public boolean isNavigating() {
		return (status);
	}
	

}
