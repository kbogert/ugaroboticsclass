import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorListener;


public class PickupObject implements Behavior2 {

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
			
			// grab the object in front of us, then raise it up
			
		}
		
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
