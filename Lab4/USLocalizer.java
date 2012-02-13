import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType {
		FALLING_EDGE, RISING_EDGE
	};

	public static double ROTATION_SPEED = 30;

	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;

	public USLocalizer(Odometer odo, UltrasonicSensor us,
			LocalizationType locType) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.locType = locType;

		// switch off the ultrasonic sensor
		us.off();
	}

	public void doLocalization() {
		double[] pos = new double[3];
		double angleA = 0, angleB = 0;
		double dTheta;
		final double DISTANCE_THRESHOLD = 40;
		int counter = 0;
		odo.getPosition(pos);

		if (locType == LocalizationType.FALLING_EDGE) {

			robot.setSpeeds(0.0, ROTATION_SPEED); // Robot turns
			// If it detects a distance larger than 50, increment the counter
			if (getFilteredData() > DISTANCE_THRESHOLD) {
				counter++;
			}
			// If it detects a wall and has a counter value over 0, meaning it
			// has passed through the "empty" space, stop and set angle
			if ((getFilteredData() < DISTANCE_THRESHOLD) && counter > 0) {
				robot.setSpeeds(0.0, 0.0);
				odo.getPosition(pos);
				angleA = pos[2];
				counter = 0;
			}

			// Now turn the other way
			robot.setSpeeds(0.0, -ROTATION_SPEED);
			// The next wall it detects will be at angle B
			if (getFilteredData() < DISTANCE_THRESHOLD) {
				robot.setSpeeds(0.0, 0.0);
				odo.getPosition(pos);
				angleB = pos[2];
			}

			// Calculate dTheta
			dTheta = 45 - (angleA + angleB) / 2;

			odo.setPosition(new double[] { 0.0, 0.0, dTheta }, new boolean[] {
					true, true, true });
		} else {

			robot.setSpeeds(0.0, ROTATION_SPEED); // Robot turns
			// If the robot detects a wall, increment the counter
			if (getFilteredData() < DISTANCE_THRESHOLD) {
				counter++;
			}
			// Once the robot can't detect a wall and the counter is greater
			// than 0, stop the robot and set the angle (B since we're detecting
			// the one to the left)
			if ((getFilteredData() > DISTANCE_THRESHOLD) && counter > 0) {
				robot.setSpeeds(0.0, 0.0);
				odo.getPosition(pos);
				angleB = pos[2];
			}

			// Now turn the other way
			robot.setSpeeds(0.0, -ROTATION_SPEED);
			// The next gap it detects will be at angle A
			if (getFilteredData() < DISTANCE_THRESHOLD) {
				robot.setSpeeds(0.0, 0.0);
				odo.getPosition(pos);
				angleA = pos[2];
			}
			
			// Calculate dTheta
			dTheta = 45 - (angleA + angleB) / 2;

			odo.setPosition(new double[] { 0.0, 0.0, dTheta }, new boolean[] {
					true, true, true });
		}
	}

	private int getFilteredData() {
		int distance;

		// do a ping
		us.ping();

		// wait for the ping to complete
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}

		// there will be a delay here
		distance = us.getDistance();

		// filter out large values
		if (distance > 50) {
			distance = 255;
		}

		return distance;
	}

}
