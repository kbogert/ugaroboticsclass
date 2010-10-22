import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorListener;


public class Navigate implements Behavior2 {

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
		// TODO Auto-generated method stub
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
		
	}

}
