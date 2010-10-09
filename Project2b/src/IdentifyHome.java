import Map.MapObj;

import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorListener;


/**
 * Only runs once, when the robot is started, to localize it's home location
 * 
 * Basically spins in a circle to find the edges of the table, and if it can't find it, backup and try again
 * 
 * @author kbogert
 *
 */
public class IdentifyHome implements Behavior2 {

	private boolean isEnabled;
	private boolean hasRun = false;
	
	public IdentifyHome() {
		isEnabled = true;
	}
	
	public void setEnabled(boolean arg0) {
		isEnabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {

	}

	public boolean poll() {
		if (! isEnabled)
			return false;
	
		if (! hasRun) {
			Map.MapObj start = Project2b.map.getPos(0, 0);
			start.setType(Map.MapObj.HOME);
			hasRun = true;
		}
		return false;
	}

	public void setActive(boolean arg0) {

	}

}
