import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorListener;


public class PutdownObject implements Behavior2 {

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
			// if we have already dropped a block, find it and maneuver so that it's underneath our gripper
			
			// how? need some way to detect the first block while we're holding one.
			// Camera will be blocked
			// IR sensor is blocked
			
			// need another sensor? or else keep the IR sensor fixed rather than raise it up
			
			// lower the block and open the gripper
			
		}
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
