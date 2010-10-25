import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Navigator;


/**
 * Only runs once, when the robot is started, to localize it's home location
 * 
 * Right now only records home on the map, but eventually we want to localize to help with finding home later
 * 
 * Basically spins in a circle to find the edges of the table, and if it can't find it, backup and try again
 * 
 * @author kbogert
 *
 */
public class IdentifyHome implements Behavior2 {

	private boolean isEnabled;
	private boolean hasRun = false;
	private Navigator nav;
	private Map map;
	private BehaviorListener listener;
	
	public IdentifyHome(Navigator myNav, Map theMap) {
		isEnabled = true;
		nav = myNav;
		map = theMap;
	}
	
	public void setEnabled(boolean arg0) {
		isEnabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {
		listener = arg0;
	}

	public boolean poll() {
		if (! isEnabled)
			return false;
	
		if (! hasRun) {
			
//			nav.moveTo(0, 0, true);
			MapObj start = new MapObj();
			start.setX(0);
			start.setY(0);
			start.setH(0);
			start.setType(MapObj.HOME);
			hasRun = true;
			
			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));

		}
		return false;
	}

	public void setActive(boolean arg0) {

	}

}
