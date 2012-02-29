import lejos.nxt.*;

public class Lab5 {

	public static void main(String[] args) {

		NXTRegulatedMotor loadMotor = Motor.C;

		final float loadSpeed = 100;
		final int loadAngle = 30;
		
		// Set the motor power level to 85% for each (forward)
		MotorPort.A.controlMotor(85, 1);
		MotorPort.B.controlMotor(85, 1);
		
		loadMotor.setSpeed(loadSpeed);

		// Make the robot wait  a while before shooting to let the wheels reach their top speed
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		// Rotate the loader to feed the ball into the wheels
		loadMotor.rotate(loadAngle);
		
		// Wait half a second before rotating the loader back to its original position
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		loadMotor.rotate(-loadAngle);

		while (Button.waitForPress() != Button.ID_ESCAPE)
			;
		System.exit(0);

	}
}
