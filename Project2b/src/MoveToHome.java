import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Pose;


/**
 * Once having found the object we're looking for, navigate home
 * 
 * @author kbogert
 *
 */
public class MoveToHome implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private NavigatorWrapper nav;
	private Localizer loc;
	private long lastRun;
	
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
			if (System.currentTimeMillis() - lastRun > 500) {
				Pose pose = loc.getPose();
				if (pose.x >= 0.1 || pose.x <= -0.9 || pose.y >= 0.1 || pose.y <= -0.9) {

					float theta = (-pose.y) / (-pose.x);
					float turnto = pose.heading - theta;

					float distance = (float)(Math.sqrt((pose.x*pose.x) + (pose.y*pose.y)));


					nav.turn(turnto, true);
					nav.goForward(distance, false);

					lastRun = System.currentTimeMillis();
					return true;
				}
				else {
					if (listener != null)
						listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));
				}
			}
		}
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
