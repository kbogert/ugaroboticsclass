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
				turnLeft = 360;
				thread = new InternalThread(this);
				thread.start();
			} else if (! thread.isAlive() || thread.isInterrupted()) {
				return false;
			}

			return true;
			
		}
		if (thread != null) {
			thread.setActive(false);
			thread = null;
		}
		
		
		return Project2b.getCurrentState() == Project2b.LOOK_AROUND && (Project2b.getProgramState() == Project2b.PROGRAM_FIND_FIRST_BLOCK || Project2b.getProgramState() == Project2b.PROGRAM_FIND_SECOND_BLOCK);
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
					Project2b.setCurrentState(Project2b.LOOK_AROUND);

					if (turnLeft <= 0) {
						turnLeft = 360;
						listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
						parent.setActive(false);
						return;
					}

					nav.turn((float)Math.PI / 12, true);
					turnLeft -= 30;

					Thread.sleep(400);  // should be long enough for the moveToObject behavior to detect an object and take over

				}
			} catch (InterruptedException e) {
				IntelliBrain.getLcdDisplay().print(1, "CRASH: Look Arnd");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
				
			} finally {
				nav.turn(-(float)Math.PI / 12, true);
				parent.setActive(false);
			}
		}
	}
		
}
