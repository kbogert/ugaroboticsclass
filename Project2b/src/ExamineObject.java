import com.ridgesoft.robotics.Behavior2;
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

	public void setEnabled(boolean arg0) {

	}

	public void setListener(BehaviorListener arg0) {

	}

	public boolean poll() {
		return false;
	}

	public void setActive(boolean arg0) {

	}

}
