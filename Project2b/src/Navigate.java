import com.ridgesoft.intellibrain.IntelliBrain;
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
	private InternalThread thread;
	
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

			if (thread == null) {
				thread = new InternalThread(this);
				thread.start();
			}
			
			return true;
		}
		if (thread != null) {
			thread.setActive(false);
			thread = null;
		}
		return Project2b.getCurrentState() == Project2b.NAVIGATE;
	}

	public void setActive(boolean arg0) {
		active = arg0;
		if (thread != null)
			thread.setActive(arg0);
		
	}
	
	private class InternalThread extends Thread {
		private Navigate parent;
		private boolean active = true;
		
		public InternalThread(Navigate p) {
			parent = p;
		}

		public void setActive(boolean b) {
			active = b;
		}
		
		public void run() {
			try {
				while (active) {
					IntelliBrain.getLcdDisplay().print(0, "Navigate");
					Project2b.setCurrentState(Project2b.NAVIGATE);

					// Turn to where the navigatorWrapper says, then go forward 1 unit
					Pose pose = loc.getPose();
					float heading = nav.getHeadingFrom(pose.x, pose.y);

					float diff = Math.abs(heading - pose.heading);

					if (diff > 0.1f) {
						nav.stop();			
						if (!active)
							return;

						nav.turn(heading - pose.heading, true);
					}
					if (!active)
						return;
					IntelliBrain.getLcdDisplay().print(1, "At:" + pose.x + "," + pose.y);

					nav.goForward(4 * Project2b.MAPSCALE, true);
					if (!active)
						return;

					pose = loc.getPose();
					if (nav.atGoal(pose.x * Project2b.MAPSCALE, pose.y * Project2b.MAPSCALE)) {
						IntelliBrain.getLcdDisplay().print(1, "REACHED GOAL");
						Thread.sleep(1000);
					
						parent.setActive(false);
						if (listener != null) {
							listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
						}

					}
					
					Thread.sleep(100);
						
				}
			} catch (Exception e) {
				IntelliBrain.getLcdDisplay().print(1, "CRASH: Navigate");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
				parent.setActive(false);
			}
		}
	}

}

