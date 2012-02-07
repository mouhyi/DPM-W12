import java.util.ArrayList;
import lejos.geom.Point;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class Navigator implements TimerListener {

	public static final int NAVIGATOR_PERIOD = 100;
	private static double FORWARD_SPEED = 8.0; // cm/s
	private static double ROTATION_SPEED = 20.0; // reduced
	private static double ROTATION_TOLERANCE = 1.0; // tolerable error while
	// turning -> degrees
	private static double DISTANCE_TOLERANCE = 4.0;
	Odometer odometer;
	private static boolean status = false;
	private Timer NavigatorTimer;
	// Set up the path
	private Point[] waypoints;
	Point a = new Point(60, 30);
	Point b = new Point(30, 30);
	Point c = new Point(30, 60);
	Point d = new Point(0, 60);

	/*
	 * Constructor
	 */
	public Navigator(Odometer odometer) {
		Timer timer = new Timer(25, this);
		timer.start();

		this.odometer = odometer;
		waypoints = new Point[] { a, b, c, d };
	}

	@Override
	public void timedOut() {
		for (Point p : waypoints) {
			travelTo(p.getX(), p.getY());
		}
	}

	/*
	 * This method causes the robot to travel to the absolute field location (x,
	 * y).
	 */
	public void travelTo(double x, double y) {
		status = true;
		Robot robot = odometer.getRobot();
		robot.setSpeeds(0, 0); // stop motors
		double theta = Math.atan2(y, x);
		turnTo(theta);
		double curX = odometer.getX();
		double curY = odometer.getY();
		double distanceErrorSquared = (curX * curX + curY * curY) - (x * x + y * y);

		do {
			curX = odometer.getX();
			curY = odometer.getY();
			distanceErrorSquared = (curX * curX + curY * curY) - (x * x + y * y);
			robot.setSpeeds(FORWARD_SPEED, 0.0);
		} while (distanceErrorSquared > DISTANCE_TOLERANCE);
	}

	/*
	 * This method causes the robot to turn (on point) to the absolute heading
	 * theta. This method turns a MINIMAL angle to it's target.
	 */
	public void turnTo(double theta) {
		Robot robot = odometer.getRobot();
		// stop motors
		robot.setSpeeds(0.0, 0.0);
		double headingError;
		double multipliedError; // We use this to get a larger figure for headingError so we can be more precise in our whie condition
		double curSpeed = ROTATION_SPEED;
		do {
			LCD.drawString("O theta: " + odometer.getTheta(), 0, 4);

			headingError = theta - odometer.getTheta();
			LCD.drawString("H error: " + headingError, 0, 5);

			if (headingError > Math.PI) {
				robot.setSpeeds(0.0, -curSpeed);
			} else if (headingError < -Math.PI) {
				robot.setSpeeds(0.0, curSpeed);
			} else if (headingError < 0.0) {
				robot.setSpeeds(0.0, -curSpeed);
			} else {
				robot.setSpeeds(0.0, curSpeed);
			}
			
		} while (Math.abs(headingError) > ROTATION_TOLERANCE);
	}

	public boolean isNavigating() {
		return (status);
	}
}
