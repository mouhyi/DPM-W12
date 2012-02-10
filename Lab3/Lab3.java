/*
 * Mouhyi Eddine EL Bouhali 260363897
 * Anthony Delage 260476213
 *  Group 28
 */
import lejos.nxt.*;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class Lab3 {
	public static void main(String[] args) {
		int buttonChoice;

		// Initialize any objects necessary

		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Path  | Path   ", 0, 2);
			LCD.drawString(" One   | Two    ", 0, 3);

			buttonChoice = Button.waitForPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		// Two choices, two paths
		// Path one
		if (buttonChoice == Button.ID_LEFT) {
			// Start the odometer, display, and navigator
			Odometer odometer = new Odometer(new Robot(Motor.A, Motor.B));
			OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
			odometryDisplay.start();
			Navigator navigator = new Navigator(odometer);
		} else
		// Path two
		{
			Odometer odometer = new Odometer(new Robot(Motor.A, Motor.B));
			OdometryDisplay odometryDisplay = new OdometryDisplay(odometer);
			odometryDisplay.start();
			NavigatorWithObstacle navigator = new NavigatorWithObstacle(odometer);
			UltrasonicPoller us = new UltrasonicPoller();
		}

		while (Button.waitForPress() != Button.ID_ESCAPE)
			;
		System.exit(0);
	}
}
