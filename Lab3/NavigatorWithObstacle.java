import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class NavigatorWithObstacle implements TimerListener {

	private Timer timer;
	public static final int US_PERIOD = 100;
	Odometer odometer;
	public static final int NAVIGATOR_PERIOD = 100;
	private static double FORWARD_SPEED = 13.0; 	 // cm/s
	private static double ROTATION_SPEED = 40.0; // 
	private static double ROTATION_TOLERANCE = 1.0; // tolerable error while turning -> degrees
	private static double DISTANCE_TOLERANCE =3.0;	
	
	private static boolean firstTime = true;
	
	public NavigatorWithObstacle ( Odometer odometer) {
		this.odometer = odometer;
		timer = new Timer(25, this);
		timer.start();
	}
	
	@Override
	public void timedOut() {					
		
		
		if(firstTime){
			travelTo(0,60);
			firstTime = false;
		}
		
		travelTo(60,0);
		while(UltrasonicPoller.obstacle){
			travelTo(odometer.getX()+35,odometer.getY()+20 );
		}	
		
		if( Math.abs( odometer.getX() - 60) <3  &&   Math.abs(odometer.getY()) < 3  )    timer.stop();
		else	travelTo(60,0);
	}
	
	public void travelTo(double x, double y) {
		Robot robot =  odometer.getRobot();
		// stop motors
		robot.stop();
		double theta = Math.atan2(y - odometer.getY() , x - odometer.getX()); //// angle positive direction
		theta = 180.0 / Math.PI * theta ;
		theta = Odometer.adjustAngle(theta);			// bug here fixed
		
		LCD.drawString("theta "+theta, 0, 5);
		turnTo(theta);
		
		while(!UltrasonicPoller.obstacle){		//// exit if there is obstacle
			robot.setSpeeds(FORWARD_SPEED, 0);
			double curX = odometer.getX();
			double curY = odometer.getY();
			if (Math.abs(x-curX) <DISTANCE_TOLERANCE && Math.abs(y-curY) <DISTANCE_TOLERANCE ){
				robot.stop();
				break;
			}
		}
		robot.stop();
	}
	
	/* This method causes the robot to turn (on point) to the absolute heading
	*  theta (degrees). This method turns a MINIMAL angle to it's target.
	*/
    public void turnTo(double theta){
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
				
	    	robot.setSpeeds(0.0, curSpeed);
	    	}
	    while(Math.abs(headingError) > ROTATION_TOLERANCE);
	    
	    // stop motors
  		robot.stop();
  		
    }

}
