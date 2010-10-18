import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;


/**
 * Sweep the robot around to look for objects.
 * 
 * @author kbogert
 *
 */
public class LookAround implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private int turnLeft = 360;
	private NavigatorWrapper nav;
	
	public LookAround(NavigatorWrapper nav) {
		this.nav = nav;
	}
	
	public void setEnabled(boolean arg0) {
		enabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {
		listener = arg0;
	}

	public boolean poll() {
		if (!enabled)
			return false;
		
		if (active ) {
			
			if (turnLeft <= 0) {
				turnLeft = 360;
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));
				setActive(false);
				return false;
			}
	
			nav.turn((float)Math.toRadians(5), true);
			
		}
		return true;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
