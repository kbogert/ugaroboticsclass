import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;


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

	public MoveToHome(NavigatorWrapper nav) {
		this.nav = nav;
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
			IntelliBrain.getLcdDisplay().print(0, "Move To Home");
			Project2b.setCurrentState(Project2b.MOVE_TO_HOME);

			nav.setGoal(0 * Project2b.MAPSCALE, 0 * Project2b.MAPSCALE, 20, .2f); 

			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));
			return false;
		}
		return Project2b.getCurrentState() == Project2b.MOVE_TO_HOME;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
