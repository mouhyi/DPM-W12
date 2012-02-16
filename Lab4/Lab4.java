import lejos.nxt.*;

public class Lab4 {

	public static void main(String[] args) {
		int buttonChoice;

		// setup the odometer, display, and ultrasonic and light sensors
		Robot patBot = new Robot(Motor.A, Motor.B);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		LightSensor ls = new LightSensor(SensorPort.S2);

		do {
			// clear the display
			LCD.clear();

			// ask the user whether the they want to perform rising edge or
			// falling edge localization
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Rising| Falling", 0, 2);
			LCD.drawString(" Edge  | Edge   ", 0, 3);

			buttonChoice = Button.waitForPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		if (buttonChoice == Button.ID_LEFT) {
			// Rising edge
			LCD.clear();
			Odometer odo = new Odometer(patBot, true);
			Navigation navigator = new Navigation(odo); // //
			LCDInfo lcd = new LCDInfo(odo);

			USLocalizer usl = new USLocalizer(odo, navigator, us,
					USLocalizer.LocalizationType.RISING_EDGE);
			usl.doLocalization();

			LightLocalizer lsl = new LightLocalizer(odo, ls);
			lsl.doLocalization();

		} else
		// Falling edge
		{
			LCD.clear();
			Odometer odo = new Odometer(patBot, true);
			Navigation navigator = new Navigation(odo); // //
			LCDInfo lcd = new LCDInfo(odo);

			USLocalizer usl = new USLocalizer(odo, navigator, us,
					USLocalizer.LocalizationType.FALLING_EDGE);
			LightLocalizer lsl = new LightLocalizer(odo, ls);

//			usl.doLocalization();
//			patBot.setSpeeds(2.0, 0.0);
			lsl.doLocalization();

		}

		// perform the light sensor localization

		while (Button.waitForPress() != Button.ID_ESCAPE)
			;
		System.exit(0);
	}

}
