import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.sensors.SharpGP2D12;

/**
 * Halt the robot immediately if an edge of the table is detected.
 * 
 * Also, if we're running parallel to the edge, turn away from it
 * 
 * @author kbogert
 *
 */
public class AvoidEdge implements Behavior2 {

	private boolean isEnabled;
	private SharpGP2D12 forwardSensor;
	private TableSensor leftRear;
	private TableSensor rightRear;
	private NavigatorWrapper nav;
	private BehaviorListener listener;
	private boolean active;
	private InternalThread thread;
	
	AvoidEdge(NavigatorWrapper nav, SharpGP2D12 forward, TableSensor leftRear, TableSensor rightRear) {
		this.forwardSensor = forward;
		this.leftRear = leftRear;
		this.rightRear = rightRear;
		this.nav = nav;
	}
	
	public void setEnabled(boolean arg0) {
		isEnabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {
		listener = arg0;
	}

	public boolean poll() {
		if (! isEnabled) {
			return false;
		}
		
		if (active) {
			if (thread == null) {
				nav.stop();
				thread = new InternalThread(this);
				thread.start();
			}
			return true;
		}
		
		if (thread != null) {
			thread.setActive(false);
			thread = null;
		}
		
		if (forwardSensor.getDistanceInches() > 18 ||
				! leftRear.isOnTable() || ! rightRear.isOnTable()) {
			return true;
		}
		return false;
			
	}

	public void setActive(boolean arg0) {
		active = arg0;
		if (thread != null)
			thread.setActive(arg0);
	}

	
	
	private class InternalThread extends Thread {
		private AvoidEdge parent;
		private boolean active = true;
		
		public InternalThread(AvoidEdge p) {
			parent = p;
		}

		public void setActive(boolean b) {
			active = b;
		}
		
		
		public void run() {
			
			IntelliBrain.getLcdDisplay().print(0, "Avoid Edge");
			
			Project2b.setCurrentState(Project2b.AVOID);
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

			}

			// if we detected the edge in front of us, backup
			if (forwardSensor.getDistanceInches() > 18) {
				
				// if both, turn away from the rear detection
				if (! leftRear.isOnTable()) {
					
					nav.turn((float)(Math.PI / 2), true);
					
				} else if ( ! rightRear.isOnTable()) {

					nav.turn(-(float)(Math.PI / 2), true);
					
				} else {
					nav.goBackward(4, true);
				}
				
			} else {
				// if we detected the edge in back, Move Forward
				
				nav.goForward(3, true);
				
			}
			
			parent.setActive(false);
			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));

			
		}
	}
}
