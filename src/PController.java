import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class PController implements UltrasonicController {

	private final int bandCenter, bandWidth;
	private final int motorStraight = 250, FILTER_OUT = 60;				/////
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	private double turnCoefficient; //
	private int leftSpeed, rightSpeed;

	public PController(int bandCenter, int bandWidth) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandWidth = bandWidth;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		filterControl = 0;
		turnCoefficient = 5;
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
		
		
		// TODO: process a movement based on the us distance passed in (P style)
		// Wall on the left
		int error = distance - this.bandCenter;
		
		if (Math.abs(error) <= bandWidth){ 	// Within acceptable range
			this.turnCoefficient = 0;
			LCD.drawString("Within Bandwith", 0, 6);
		}	
		else if(error > 20){			// too far
			this.turnCoefficient = (error > 150) ?  0.2 : 5;
			LCD.drawString("Too far", 0, 6);
		}
		else if(error < -8){			// too close
			this.turnCoefficient = 35;
			LCD.drawString("Too close", 0, 6);
		}
		leftSpeed = this.adjustSpeed(error);
		leftMotor.setSpeed(leftSpeed);
		rightSpeed = this.adjustSpeed(-error);
		rightMotor.setSpeed(rightSpeed);
		
		
		/*
		
		else if (error < 0 ){ 				// Too close
			turnCoefficient = 5;
			if (error < -7){ // Way too close
				turnCoefficient = 8;
			}
			rightSpeed = motorStraight + turnCoefficient*error;
			leftSpeed = motorStraight - turnCoefficient*error;
			// Signs for motor speeds are inverted because error is negative

			
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too close.", 0, 4);
			//LCD.drawString("Error: " + error, 0, 5);

		}
		else{ // Too far
			turnCoefficient = 4;
			if (error > 30){  // Way too far
				turnCoefficient = 6;
			}
			// Here we cap the inside and outside wheel speeds
			if (error*turnCoefficient > motorStraight){
				leftSpeed = 5;
			}else{
				leftSpeed = motorStraight - turnCoefficient*error;
			}
			if ((error*turnCoefficient + motorStraight) > 350){
				rightSpeed = 350;
			}else{
				rightSpeed = motorStraight + turnCoefficient*error;
			}
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too far.", 0, 4);
			//LCD.drawString("Error: " + error, 0, 5);
		}        */
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
	
	private int adjustSpeed(int error){
		int val = Math.max(  Lab1.getMotorlow() ,  motorStraight - (int)(turnCoefficient*error));
		val = Math.min(val,  Lab1.getMotorhigh());
		return val;
	}

}
