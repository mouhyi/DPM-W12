import lejos.nxt.*;

public class Lab3 {
	public static void main(String[] args){
		int buttonChoice;
		
		// Initialize any objects necessary
		
		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Float | Path  ", 0, 2);
			LCD.drawString(" One   | Two   ", 0, 3);

			buttonChoice = Button.waitForPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		
		if (buttonChoice == Button.ID_LEFT){
			// Path one
		}else{
			// Path two
		}
		
		while (Button.waitForPress() != Button.ID_ESCAPE)
			;
		System.exit(0);
	}
}
