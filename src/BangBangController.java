import lejos.nxt.*;

public class BangBangController implements UltrasonicController {
	private final int bandCenter, bandWidth;
	private final int motorLow, motorHigh;
	private final int motorStraight = 250;  //
	private final int FILTER_OUT = 20;  //
	private int delta = 60;  //
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int filterControl, leftSpeed, rightSpeed;  //

	public BangBangController(int bandCenter, int bandWidth, int motorLow,
			int motorHigh) {
		// Default Constructor
		this.bandCenter = bandCenter;
		this.bandWidth = bandWidth;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		this.leftSpeed = motorStraight; //
		this.rightSpeed = motorStraight;
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
		leftMotor.forward();
		rightMotor.forward();

	}

	@Override
	public void processUSData(int distance) {

		// rudimentary filter
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

		// TODO: process a movement based on the us distance passed in (BANG-BANG style)

		// Wall on the LEFT
		int error = distance - this.bandCenter;
		if (Math.abs(error) <= bandWidth) { /* Within tolerance */
			rightSpeed = motorStraight;
			leftSpeed = motorStraight;
			leftMotor.setSpeed(rightSpeed);
			leftMotor.setSpeed(leftSpeed); /* 0 bias */
			LCD.drawString("Within Bandwith", 0, 6);
		
		}else if (error < 0) { 			/* Too close */
			if (error < -10 ){			// way too close
				delta = 150;   
			}
			leftSpeed = motorStraight + delta;
			rightSpeed = motorStraight - delta;
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too close", 0, 6);
		
		}else { 						/* Too far */
			if (error > 20 ){ 			// way too far
				delta = 85;   
			}
			leftSpeed = motorStraight - delta;
			rightSpeed = motorStraight + delta;
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too far", 0, 6);
		}
	}


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
