import lejos.nxt.*;

public class Robot {
	public static final double LEFT_RADIUS = 2.85;
	public static final double RIGHT_RADIUS = 2.85;
	public static final double WIDTH = 16.1;
	private NXTRegulatedMotor leftMotor, rightMotor;
	private double forwardSpeed, rotationSpeed;

	/**
	 * Constructor
	 * 
	 * @param lMotor
	 *            The left motor of the robot.
	 * @param rMotor
	 *            The right motor of the robot.
	 */
	public Robot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}

	/**
	 * computes the total displacement of the robot relative to its original
	 * position in cm.
	 */
	public double getDisplacement() {
		return (convertToRadians(leftMotor.getTachoCount()) * LEFT_RADIUS + convertToRadians(rightMotor
				.getTachoCount()) * RIGHT_RADIUS) / 2;

	}

	/**
	 * return the robot's heading relative to its original orientation in
	 * radians.
	 */
	public double getHeading() {
		return (convertToRadians(leftMotor.getTachoCount()) * LEFT_RADIUS - convertToRadians(rightMotor
				.getTachoCount()) * RIGHT_RADIUS) / WIDTH;
	}

	/**
	 * Sets both the forward(cm/s) and rotation(deg/s) speeds of the robot..
	 */
	public void setSpeeds(double forwardSpeed, double rotationSpeed) {
		double leftSpeed, rightSpeed;

		this.forwardSpeed = forwardSpeed;
		this.rotationSpeed = rotationSpeed;

		// convert forwardspeed -> deg/sec and use the formulas on Navigation
		// Tutorial
		leftSpeed = (forwardSpeed + rotationSpeed * WIDTH / 2) / (LEFT_RADIUS);
		rightSpeed = (forwardSpeed - rotationSpeed * WIDTH / 2)
				/ (RIGHT_RADIUS);

		// set motor speeds
		if (leftSpeed > 900) {
			leftMotor.setSpeed(900);
		} else {
			leftMotor.setSpeed((int) leftSpeed);
		}

		if (rightSpeed > 900) {
			rightMotor.setSpeed(900);
		} else {
			rightMotor.setSpeed((int) rightSpeed);
		}

		// set motor directions
		if (leftSpeed > 0) {
			leftMotor.forward();
		} else {
			leftMotor.backward();
			leftSpeed = -leftSpeed;
		}

		if (rightSpeed > 0) {
			rightMotor.forward();
		} else {
			rightMotor.backward();
			rightSpeed = -rightSpeed;
		}

	}

	// This method converts angles is degrees to angles in radians
	private double convertToRadians(int angleDeg) {
		return (angleDeg * 2 * Math.PI) / (360.0);
	}

}
