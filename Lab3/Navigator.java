import java.util.ArrayList;
import lejos.geom.Point;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class Navigator implements TimerListener {

	private static final long NAVIGATOR_PERIOD = 100;
	private static double FORWARD_SPEED = 8.0;	 // cm/s
    private static double ROTATION_SPEED = 45.0; // deg/s
    private static double ROTATION_TOLERANCE = 0.5; // tolerable error while turning ->  degrees
    private static double DISTANCE_TOLERANCE = 4.0;
    Odometer odometer;
    private static boolean status = false;
    private Timer NavigatorTimer;
    // Set up the path
	private Point[] waypoints = {Point(60, 30), Point(30, 30), Point(30, 60), Point(0, 60)} ;
	
	/*
	* Constructor
	*/
	public Navigator(Odometer odometer){
	
		this.odometer = odometer;
	
	}
    
    @Override public void timedOut() {
    	for(Point p: waypoints){
    		travelTo(p.getX(), p.getY());
    	}
    }
    
    /*
    * This method causes the robot to travel to the absolute field location (x, y).
    */
	public void travelTo(double x, double y) {
		status = true;
		Robo robot =  odometer.getRobot();
		robot.setSpeeds(0, 0); // stop motors
		double theta = Math.atan2(y,x);
		turnTo(theta);
		double distanceError;
		
		do{
			double curX = odometer.getX;
			double curY = odometer.getY;
			distanceError = (curX*curX + curY*curY) - (x*x+ y*y);
			robot.setSpeeds(FORWARD_SPEED, 0.0);
		}while(distanceError>DISTANCE_TOLERANCE)
		
		robot.setSpeeds(0, 0); // stop motors
		
	}	
    
    /* This method causes the robot to turn (on point) to the absolute heading
	*  theta. This method turns a MINIMAL angle to it's target.
	*/
    public void turnTo(double theta){
    	Robo robot =  odometer.getRobot();
    	// stop motors
    	robot.setSpeeds(0.0 , 0.0);
    	double headingError;
    	double curSpeed = ROTATION_SPEED;
    	do{
	    	headingError = Odometer.adjustAngle (theta - odometer.getTheta() );
	    	if(headingError>180)  headingError -= 360;
	    	
	    	//note: positive angle -> clock wise
	    	if(headingError * curSpeed<0.0){	
	    	// if you the robot has turned past the right heading, invert and slow down rotation
				curSpeed *= -0.5;
			}	
	    	robot.setSpeeds(0.0, curSpeed);	
	    	}
	    while(Math.abs(headingError) > ROTATION_TOLERANCE);
	    
	    // stop motors
  		robot.setSpeeds(0, 0);	
    }
    
    public boolean isNavigating() {
		return (status);
	}
}
