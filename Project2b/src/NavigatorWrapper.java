import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Navigator;


public class NavigatorWrapper {

	private Localizer loc;
	private Navigator nav;
	
	public NavigatorWrapper(Localizer odometer, Navigator nav) {
	
		this.loc = odometer;
		this.nav = nav;
	}
	
	/**
	 * Turn relative to the current heading
	 * 
	 * @param amount
	 */
	public void turn(float amount, boolean wait) {
		nav.turnTo(loc.getPose().heading + amount, wait);
	}
	
	public void goForward(float distance, boolean wait) {
		go(distance, true, wait);
	}
	
	public void goBackward(float distance, boolean wait) {
		go(distance, false, wait);		
	}
	
	/**
	 * Go the specified distance in the specified direction
	 * 
	 * @param distance
	 * @param direction
	 * true = forward
	 * false = backward
	 */
	public void go(float distance, boolean direction, boolean wait) {
		float angle = loc.getPose().heading;
		float x = loc.getPose().x;
		float y = loc.getPose().y;
		
		if (!direction)
			distance *= -1;
		
		float targetX = distance * (float)Math.cos(angle) + x;
		float targetY = distance * (float)Math.sin(angle) + y;
		
		nav.moveTo(targetX, targetY, wait);
		
				
	}
	
	public void stop() {
		nav.stop();
	}
}
