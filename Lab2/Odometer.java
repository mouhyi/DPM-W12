import lejos.nxt.*;

/*
 * Odometer.java
 */

public class Odometer extends Thread {
	// robot position
	private double x, y, theta;
	
	// Other private variables
	private double rightTachoCount, leftTachoCount;
	final private double wheelRadius = 2.85;
	final private double width = 16.1;
	private double rightArcLength, leftArcLength;
	private double deltaTheta, deltaRobotArcLength;

	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;

	// lock object for mutual exclusion
	private Object lock;

	// default constructor
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();
			
			// First, we get the current tacho count for each motor (in radians)
			rightTachoCount = convertToRadians(Motor.B.getTachoCount());
			leftTachoCount = convertToRadians(Motor.A.getTachoCount());
			//Once this is done, we reset them so we can get the right tacho count at the next step
			Motor.A.resetTachoCount();
			Motor.B.resetTachoCount();
			
			// We use a method detailed below to calculate the arc length traveled by each wheel
			rightArcLength = calculateArcLength(rightTachoCount, wheelRadius);
			leftArcLength = calculateArcLength(leftTachoCount, wheelRadius);

			// We calculate both our change in angle and our change in arc length
			deltaTheta = (rightArcLength-leftArcLength) / width;
			deltaRobotArcLength = (rightArcLength-leftArcLength) / 2;
			
			synchronized (lock) {
				// We set the x, y and theta variables based on our mathematical model
				x += deltaRobotArcLength * Math.cos(theta + (deltaTheta/2));
				y += deltaRobotArcLength * Math.sin(theta + (deltaTheta/2));
				theta += deltaTheta;

			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
	
	private double calculateArcLength(double tachoCount, double wheelRadius){
		return (tachoCount * wheelRadius);
	}
	
	private double convertToRadians(int tachoCount){
		return (tachoCount * 2 * Math.PI)/(360.0);
	}
}