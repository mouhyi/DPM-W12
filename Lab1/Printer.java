import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Printer extends Thread {
	
	private UltrasonicController cont;
	private final int option;
	
	public Printer(int option, UltrasonicController cont) {
		this.cont = cont;
		this.option = option;
	}
	
	public void run() {
		while (true) {
			LCD.clear();
			LCD.drawString("Controller Type is... ", 0, 0);
			if (this.option == Button.ID_LEFT)
				LCD.drawString("BangBang", 0, 1);
			else if (this.option == Button.ID_RIGHT)
				LCD.drawString("P type", 0, 1);
			LCD.drawString("US Distance: " + cont.readUSDistance(), 0, 2 );
			
			
			/**
			* Changes made to display the error and wheels speeds
			* This was useful for the PController as we could see
			* the speed adjutment proportional to the error
			*/
			int error = cont.readUSDistance() - 35;
			LCD.drawString("ERROR: " + error, 0, 3 );
			
			LCD.drawString("Left: " + cont.getLeftSpeed(), 0, 4 );
			LCD.drawString("Right: " + cont.getRightSpeed(), 0, 5 );
						
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
	
	public static void printMainMenu() {
		LCD.clear();
		LCD.drawString("left = bangbang",  0, 0);
		LCD.drawString("right = p type", 0, 1);
	}
}
