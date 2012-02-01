/*
 * SquareDriver.java 
 */
import lejos.nxt.*;

public class SquareDriver {
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;

	// We added the isTurning boolean to know when to perform corrections and when not to
	public static boolean isTurning = false;

	public static void drive(NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, double leftRadius,
			double rightRadius, double width) {
		// reset the motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor,
				rightMotor }) {
			motor.stop();
			motor.setAcceleration(1000);
		}

		// wait 5 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the odometer will be interrupted by another thread
		}

		for (int i = 0; i < 4; i++) {

			isTurning = false; // //
			// drive forward two tiles
			leftMotor.setSpeed(FORWARD_SPEED);
			rightMotor.setSpeed(FORWARD_SPEED);

			leftMotor.rotate(convertDistance(leftRadius, 91.44), true);
			rightMotor.rotate(convertDistance(rightRadius, 91.44), false);

			// turn 90 degrees clockwise
			isTurning = true;
			leftMotor.setSpeed(ROTATE_SPEED);
			rightMotor.setSpeed(ROTATE_SPEED);

			// We made the robot go around the square counter-clockwise, such
			// that we were in a proper cartesian plane with an angle relative
			// to the x-axis
			leftMotor.rotate(-convertAngle(leftRadius, width, 90.0), true);
			rightMotor.rotate(convertAngle(rightRadius, width, 90.0), false);
		}
	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}
