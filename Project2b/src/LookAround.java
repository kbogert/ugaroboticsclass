import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;


/**
 * Sweep the robot around to look for objects.
 * 
 * @author kbogert
 *
 */
public class LookAround implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private int turnLeft = 360;
	private NavigatorWrapper nav;
	private InternalThread thread;
	
	public LookAround(NavigatorWrapper nav) {
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
		
		if (active ) {
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
		
		
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
		if (thread != null)
			thread.setActive(arg0);
	}

	private class InternalThread extends Thread {
		private LookAround parent;
		private boolean active = true;
		
		public InternalThread(LookAround p) {
			parent = p;
		}

		public void setActive(boolean b) {
			active = b;
		}
		
		public void run() {
			try {
				while(active) {
					IntelliBrain.getLcdDisplay().print(0, "Look Around");

					if (turnLeft <= 0) {
						turnLeft = 360;
						listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
						parent.setActive(false);
						return;
					}

					nav.turn((float)Math.toRadians(5), true);
					turnLeft -= 5;

					Thread.sleep(30);

				}
			} catch (InterruptedException e) {
			}
		}
	}
		
}
