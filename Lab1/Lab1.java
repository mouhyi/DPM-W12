import lejos.nxt.*;


public class Lab1 {
	
	private static final SensorPort usPort = SensorPort.S1;
	//private static final SensorPort lightPort = SensorPort.S2;
	
	private static final int bandCenter = 35, bandWidth = 3;			// Code changed
	private static final int motorLow = 100, motorHigh = 400;
	
	
	public static void main(String [] args) {
		/*
		 * Wait for startup button press
		 * Button.ID_LEFT = BangBang Type
		 * Button.ID_RIGHT = P Type
		 */
		
		int option = 0;
		Printer.printMainMenu();
		while (option == 0)
			option = Button.waitForPress();
		
		// Setup controller objects
		BangBangController bangbang = new BangBangController(bandCenter, bandWidth, motorLow, motorHigh);
		PController p = new PController(bandCenter, bandWidth);
		
		// Setup ultrasonic sensor
		UltrasonicSensor usSensor = new UltrasonicSensor(usPort);
		
		// Setup Printer
		Printer printer = null;
		
		// Setup Ultrasonic Poller
		UltrasonicPoller usPoller = null;
		
		switch(option) {
		case Button.ID_LEFT:
			usPoller = new UltrasonicPoller(usSensor, bangbang);
			printer = new Printer(option, bangbang);
			break;
		case Button.ID_RIGHT:
			usPoller = new UltrasonicPoller(usSensor, p);
			printer = new Printer(option, p);
			break;
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
		
		
		usPoller.start();
		printer.start();
		
		//Wait for another button press to exit
		Button.waitForPress();
		System.exit(0);
		
	}
	
	/* Getters added to the code */
	
	public static int getMotorlow() {
		return motorLow;
	}


	public static int getMotorhigh() {
		return motorHigh;
	}
	
	
}
