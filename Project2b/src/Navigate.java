import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Pose;

/**
 * Move one block at a time according to the navigatorWrapper gradient 
 * 
 * @author kbogert
 *
 */
public class Navigate implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private NavigatorWrapper nav;
	private Localizer loc;
	
	public Navigate(NavigatorWrapper nav, Localizer loc) {
		this.nav = nav;
		this.loc = loc;
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
		
		if (active) {
			// Turn to where the navigatorWrapper says, then go forward 1 unit
			Pose pose = loc.getPose();
			float heading = nav.getHeadingFrom(pose.x, pose.y);
			
			float diff = Math.abs(heading - pose.heading);
			
			if (diff > 0.1f) {
				nav.stop();
				nav.turn(heading - pose.heading, true);
			}
			
			nav.goForward(Project2b.MAPSCALE, true);
			
			pose = loc.getPose();
			if (nav.atGoal(pose.x, pose.y)) {
				if (listener != null) {
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

