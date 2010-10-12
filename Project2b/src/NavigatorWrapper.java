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
		
		if (angle > 3 * Math.PI / 2) {
			angle -= 3 * Math.PI / 2;
		} else if (angle > Math.PI ) {
			angle -= Math.PI;
		} else if (angle > Math.PI/ 2) {
			angle -= Math.PI;
		}
		
		float opposite = (float)(distance * Math.sin(angle));
		float adjacent = (float)(distance * Math.cos(angle));
		
		if (! direction) {
			opposite *= -1;
			adjacent *= -1;
		}
		
		angle = loc.getPose().heading;
		
		if (angle > 3 * Math.PI / 2) {
			nav.moveTo(x + opposite, y - adjacent, wait);
		} else if (angle > Math.PI ) {
			nav.moveTo(x - adjacent, y - opposite, wait);			
		} else if (angle > Math.PI/ 2) {
			nav.moveTo(x - opposite, y + adjacent, wait);			
		} else {
			nav.moveTo(x + adjacent, y + opposite, wait);
		}
				
	}
	
}
