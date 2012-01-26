import lejos.nxt.*;

public class BangBangController implements UltrasonicController {
	private final int bandCenter, bandWidth;
	private final int motorLow, motorHigh;
	private final int motorStraight = 250;  // changed
	private final int FILTER_OUT = 20;  	// added to the code
	private int delta = 60;  				// added to the code
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int filterControl, leftSpeed, rightSpeed;  // added to the code

	public BangBangController(int bandCenter, int bandWidth, int motorLow,
			int motorHigh) {
		// Default Constructor
		this.bandCenter = bandCenter;
		this.bandWidth = bandWidth;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		this.leftSpeed = motorStraight; 	// added to the code
		this.rightSpeed = motorStraight;	// added to the code
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
		leftMotor.forward();
		rightMotor.forward();

	}
	/**
	* This method process a movement based on the us distance passed in (BANG-BANG style)
	*/
	@Override
	public void processUSData(int distance) {

		// rudimentary filter -> helps the robot deal woth gaps
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}


		// Wall on the LEFT
		int error = distance - this.bandCenter;
		if (Math.abs(error) <= bandWidth) { /* Within tolerance, set speeds to motorStraight */
			rightSpeed = motorStraight;
			leftSpeed = motorStraight;
			leftMotor.setSpeed(rightSpeed);
			leftMotor.setSpeed(leftSpeed);
			LCD.drawString("Within Bandwith", 0, 6);
		
		}else if (error < 0) { 			/* Too close */
			if (error < -10 ){			// way too close ->  make delta larger
				delta = 150;   
			}
			// increase left speed and decrease right speed
			leftSpeed = motorStraight + delta;
			rightSpeed = motorStraight - delta;
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too close", 0, 6);
		
		}else { 						/* Too far */
			if (error > 20 ){ 			// way too far -> make delta larger
				delta = 85;   
			}
			// increase right speed and decrease left speed
			leftSpeed = motorStraight - delta;
			rightSpeed = motorStraight + delta;
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too far", 0, 6);
		}
	}

	/* Getters */
	
	public int getLeftSpeed() {
		return leftSpeed;
	}

	public int getRightSpeed() {
		return rightSpeed;
	}

@Override
public int readUSDistance() {
	return this.distance;
}
}
