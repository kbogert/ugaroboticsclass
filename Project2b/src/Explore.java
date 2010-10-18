import java.util.Random;

import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;


/**
 * Choose a square on our map we haven't visited yet and move to it
 * 
 * @author kbogert
 *
 */
public class Explore implements Behavior2 {

	private boolean enabled = true;
	private boolean active = false;
	private BehaviorListener listener;
	private NavigatorWrapper nav;
	
	public Explore(NavigatorWrapper n) {
		nav = n;
	}
	
	public void setEnabled(boolean arg0) {
		enabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {
		listener = arg0;
	}

	public boolean poll() {
		if (! enabled)
			return false;
		if (active) {
			
			// for right now, choose a random direction and distance and try to go there
			// change this when we get a working map
			Random random = new Random();
			
			int distance = random.nextInt(18);
			int direction = random.nextInt(5) - 2;

			nav.turn(direction * (float)Math.PI / 4, true);
			nav.goForward(distance, false);
			
			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));
		
			return false;
		}
		
		// if the robot is in an idle state, request that this behavior be activated
		return Project2b.getCurrentState() == Project2b.IDLE;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}