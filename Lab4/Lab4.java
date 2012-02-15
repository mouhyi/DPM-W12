import lejos.nxt.*;

public class Lab4 {

	public static void main(String[] args) {
		// setup the odometer, display, and ultrasonic and light sensors
		Robot patBot = new Robot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		LCDInfo lcd = new LCDInfo(odo);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S2);
		LightSensor ls = new LightSensor(SensorPort.S1);
		Navigation navigator = new Navigation(odo);		////
		
		// perform the ultrasonic localization
		USLocalizer usl = new USLocalizer(odo,navigator,  us, USLocalizer.LocalizationType.FALLING_EDGE);
		usl.doLocalization();
		
		// perform the light sensor localization
		/*
		LightLocalizer lsl = new LightLocalizer(odo, ls);
		lsl.doLocalization();			
		*/                           
		Button.waitForPress();
		
		System.exit(0);
	}

}
