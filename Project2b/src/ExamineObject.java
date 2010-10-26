import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;


/**
 * Get a reading from the cmucam as to the color of the object in front of us
 * 
 * Record the object's color in the map
 * 
 * If it's the one we're looking for, set an appropriate state
 * 
 * @author kbogert
 *
 */
public class ExamineObject implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	
	public void setEnabled(boolean arg0) {
		enabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {
		listener = arg0;
	}

	public boolean poll() {
		if (!enabled)
			return false;
		
		if (active) {
			
			// the cmucam has been set to only track the color (white) of our block, if this behavior is activated
			// and there is no significant tracking data, we either missed the block or it's the wrong color
			
			// if we found the block, maneuver using the camera's data to get it into the right position for pickup
			
			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));
		}
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
