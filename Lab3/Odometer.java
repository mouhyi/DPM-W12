import lejos.util.Timer;
import lejos.util.TimerListener;

public class Odometer implements TimerListener {

	// odometer update period, in ms
	public static final int ODOMETER_PERIOD = 25;
	private Robot robot;
	private Timer odometerTimer;
	// Position Variables
	private double x, y, theta, displacement; // Theta in degrees
	private double dTheta, dDisplacement;
	// lock object for mutual exclusion
	private Object lock;

	/*
	 * Constructor
	 */
	public Odometer(Robot robot) {
		Timer timer = new Timer(25, this);
		timer.start();

		this.robot = robot;
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
	}

	public void timedOut() {

		dTheta = robot.getHeading() - theta;
		dDisplacement = robot.getDisplacement() - displacement;

		// Update x, y and theta values based on our mathematical model
		synchronized (lock) {
			this.x += dDisplacement
					* Math.cos(convertToRadians(theta + dTheta / 2));
			this.y += dDisplacement
					* Math.sin(convertToRadians(theta + dTheta / 2));
			this.theta += dTheta;
			// Map theta to [0, 2pi)
			if (this.theta > (2 * Math.PI)) {
				this.theta -= (2 * Math.PI);
			}
			if (this.theta < 0) {
				this.theta += (2 * Math.PI);
			}
		}
		displacement += dDisplacement;

	}

	// This method converts angles is degrees to angles in radians
	public static double convertToRadians(double angle) {
		return (angle * Math.PI) / (180.0);
	}

	// // Map theta to [0,360)
	// public static double adjustAngle(double wanted, double current) {
	// delta = wanted - current;
	// if (delta < -Math.PI) {
	//
	// }
	// }

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
		synchronized (lock) {
			return x;
		}
	}

	public double getY() {
		synchronized (lock) {
			return y;
		}
	}

	public double getTheta() {
		synchronized (lock) {
			return theta;
		}
	}

	public Robot getRobot() {
		return robot;
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
}
