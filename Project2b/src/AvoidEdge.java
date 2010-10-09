import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorListener;

/**
 * Halt the robot immediately if an edge of the table is detected.
 * 
 * Also, if we're running parallel to the edge, turn away from it
 * 
 * @author kbogert
 *
 */
public class AvoidEdge implements Behavior2 {

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
