import lejos.nxt.UltrasonicSensor;


public class UltrasonicPoller extends Thread{
	private UltrasonicSensor us;
	private UltrasonicController cont;
	
	public UltrasonicPoller(UltrasonicSensor us, UltrasonicController cont) {
		this.us = us;
		this.cont = cont;
	}
	
	public void run() {
		while (true) {
			//process collected data
			cont.processUSData(us.getDistance());
			try { Thread.sleep(10); } catch(Exception e){}			////
		}
	}

}
