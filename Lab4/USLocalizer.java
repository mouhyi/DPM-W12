import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 30;

	private Odometer odo;
	private Robot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	private Navigation navigator;
	
	public USLocalizer(Odometer odo, Navigation navigator, UltrasonicSensor us, LocalizationType locType) {
		this.odo = odo;
		this.robot = odo.getRobot();
		this.us = us;
		this.locType = locType;
		this.navigator = navigator;
		
		// switch off the ultrasonic sensor
		us.off();
	}
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		
		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
            robot.rotate(ROTATION_SPEED);
            while (getFilteredData() < 30);
         
            
			// keep rotating until the robot sees a wall, then latch the angle
			while (getFilteredData() > 25);
            odo.getPosition(pos);
            angleA = pos[2];
           
            
			// switch direction and wait until it sees no wall
			robot.rotate(-ROTATION_SPEED);
            while (getFilteredData() < 30);
        
            
			// keep rotating until the robot sees a wall, then latch the angle
			while (getFilteredData() > 25);
            odo.getPosition(pos);
            angleB = pos[2];
         
            
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			if (angleA < angleB)
                navigator.turnTo( (angleA + angleB + 270) / 2, true);
            else
                navigator.turnTo( (angleA + angleB - 90) / 2, true);
                
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
			
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			//rotate until we see a wall
			robot.rotate(ROTATION_SPEED);
            while (getFilteredData() > 25);
           
            
            //keep rotating until we no see wall, then latch the angle
            robot.rotate(ROTATION_SPEED);
            while (getFilteredData() < 30);
            odo.getPosition(pos);
            angleA = pos[2];
          
            
            // switch direction and wait until it sees a wall
			robot.rotate(-ROTATION_SPEED);
            while (getFilteredData() > 25);
           
            
            // keep rotating until the robot sees no wall, then latch the angle
			robot.rotate(-ROTATION_SPEED);
            while (getFilteredData() < 30);
            odo.getPosition(pos);
            angleB = pos[2];
            
            
            //finally ROTATE TO North
            if (angleA > angleB)
                navigator.turnTo( (angleA + angleB + 270) / 2, true);
            else
                navigator.turnTo( (angleA + angleB - 90) / 2, true);
            System.out.println("Ah, at last here we are...  maybe?");
            
            // update the odometer position
            odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
		}
	}
	
	private int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = us.getDistance();
		
		if(distance > 50)
        	distance = 50;
 				
		return distance;
	}
	
	

}
