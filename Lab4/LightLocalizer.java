import lejos.nxt.LightSensor;

public class LightLocalizer {
	private Odometer odo;
	private Robot robot;
	private LightSensor ls;
	
	public static double STRAIGHT_SPEED = 3, ROTATION_SPEED = 30;
    public static double LS_RADIUS = 11.4;
    public static double LS_THRESHOLD = 45;				////  7
    public static double TURN_ANGLE = 90;
	
	public LightLocalizer(Odometer odo, LightSensor ls) {
		this.odo = odo;
		this.robot = odo.getRobot();
		this.ls = ls;
		
		// turn on the light
		ls.setFloodlight(true);
	}
	
	public void doLocalization() {
		// drive to location listed in tutorial
		// start rotating and clock all 4 gridlines
		// do trig to compute (0,0) and 0 degrees
		// when done travel to (0,0) and turn to 0 degrees
		
	
		int oldReading = ls.getNormalizedLightValue();
		            
		//go forward until we see a line
		robot.advance(STRAIGHT_SPEED);
		while((oldReading - ls.getNormalizedLightValue()) < LS_THRESHOLD)
		        oldReading = ls.getNormalizedLightValue();
		
		//then bring our center back to the line
		robot.advance(-STRAIGHT_SPEED);
		try {Thread.sleep((int)(LS_RADIUS*1000/STRAIGHT_SPEED));} catch(Exception e){}
		
		//rotate until we see the other line
		robot.rotate(ROTATION_SPEED);
		try {Thread.sleep((int)(70*1000/ROTATION_SPEED));} catch(Exception e){}
		oldReading = ls.getNormalizedLightValue();
		while((oldReading - ls.getNormalizedLightValue()) < LS_THRESHOLD)
		        oldReading = ls.getNormalizedLightValue();
		
		//then go back by 90 degrees
		robot.rotate(-ROTATION_SPEED);
		try {Thread.sleep((int)(TURN_ANGLE*1000/ROTATION_SPEED));} catch(Exception e){}
		
		//go back by 3 cm to avoid the line we just saw
		robot.advance(-STRAIGHT_SPEED);
		try {Thread.sleep((int)(3*1000/STRAIGHT_SPEED));} catch(Exception e){}
		
		//then turn again by 90 degrees (parallel to the line)
		robot.rotate(ROTATION_SPEED);
		try {Thread.sleep((int)(TURN_ANGLE*1000/ROTATION_SPEED));} catch(Exception e){}
		
		//go forward until we see a line again
		
		robot.advance(STRAIGHT_SPEED);
		oldReading = ls.getNormalizedLightValue();
		while((oldReading - ls.getNormalizedLightValue()) < LS_THRESHOLD)
		        oldReading = ls.getNormalizedLightValue();
		

		//then bring our center back to the line
		robot.advance(-STRAIGHT_SPEED);
		try {Thread.sleep((int)(LS_RADIUS*1000/STRAIGHT_SPEED));} catch(Exception e){}
		
		//again rotate by 90 degrees until we see a line
		robot.rotate(-ROTATION_SPEED);
		oldReading = ls.getNormalizedLightValue();
		while((oldReading - ls.getNormalizedLightValue()) < LS_THRESHOLD)
		        oldReading = ls.getNormalizedLightValue();

		//then go back forward by 3 cm
		robot.advance(STRAIGHT_SPEED);
		try {Thread.sleep((int)(3*1000/STRAIGHT_SPEED));} catch(Exception e){}
		robot.stop();
		
		//and finally set the odometer as needed.
		odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
		            
		
	}
	
	

}
