import java.util.ArrayList;

import lejos.geom.Point;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class Navigator implements TimerListener {

	// Initialize objects
	private Odometer odometer;
	NXTRegulatedMotor rightMotor = Motor.B;
	NXTRegulatedMotor leftMotor = Motor.A;

	// Initialize variables
	private int counter = 0;
	private double deltaTheta;
	private double thetaDest;
	private static boolean status = false;
	private double wheelRadius = 2.85; // same for both sides
	private double width = 16.1;
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;
	private final double EPSILON = 0.01;

	// Set up the path
	private ArrayList<Point> waypoints = new ArrayList<Point>();

	// Constructor for the navigator object
	public Navigator(Odometer odometer) {
		Timer timer = new Timer(100, this);
		timer.start();
		this.odometer = odometer;

		// Set up the path
		Point a = new Point(60, 30);
		Point b = new Point(30, 30);
		Point c = new Point(30, 60);
		Point d = new Point(0, 60);
		waypoints.add(d);
		waypoints.add(c);
		waypoints.add(b);
		waypoints.add(a);
	}

	// timedOut method, that runs every 100 ms
	@Override
	public void timedOut() {
		// Travel to next point
		travelTo(waypoints.get(counter).getX(), waypoints.get(counter).getY());

		// Check if it has arrived
		if ((odometer.getX() + 1.0 < waypoints.get(counter).getX())
				&& (odometer.getY() + 1.0 < waypoints.get(counter).getY())) {
			counter++;
		}
	}

	// Travel to method
	public void travelTo(double x, double y) {
		status = true;

		// Finds the angle at which it has to travel
		thetaDest = Math.atan2(y, x);
		turnTo(thetaDest);

		// When done rotating, sets forward speed
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		leftMotor.setAcceleration(1000);
		rightMotor.setAcceleration(1000);
		
		leftMotor.forward();
		rightMotor.forward();
	}

	// Turn to method
	public void turnTo(double theta) {
		status = true;

		deltaTheta = calculateMinimalAngle(theta, odometer.getTheta());

		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);

		leftMotor.rotate(-convertAngle(wheelRadius, width, deltaTheta), true);
		rightMotor.rotate(convertAngle(wheelRadius, width, deltaTheta), false);
	}

	public boolean isNavigating() {
		return (status);
	}

	// This method minimizes the angle difference
	private double calculateMinimalAngle(double thetaD, double thetaR) {

		double delta = thetaD - thetaR;
		// Check both exceptional cases, or else return delta
		if ((delta + EPSILON) < -180) {
			return (delta + 360);
		} else if ((delta + EPSILON) > 180) {
			return (delta - 360);
		}
		return delta;
	}

	// Methods taken from SquareDriver
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}
