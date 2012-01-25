
public interface UltrasonicController {
	
	public void processUSData(int distance);
	
	public int readUSDistance();
	
	public int getLeftSpeed();
	public int getRightSpeed();
}
