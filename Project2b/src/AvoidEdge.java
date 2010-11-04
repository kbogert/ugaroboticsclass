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
			} else if (! thread.isAlive() || thread.isInterrupted() ) {
				return false;
			}
			return true;
		}
		
		if (thread != null) {
			thread.setActive(false);
			thread = null;
		}

		if (Project2b.getProgramState() == Project2b.PROGRAM_RETURN_FIRST_BLOCK || Project2b.getProgramState() == Project2b.PROGRAM_RETURN_SECOND_BLOCK || Project2b.getCurrentState() == Project2b.PICKUP_OBJECT || Project2b.getCurrentState() == Project2b.PUTDOWN_OBJECT)
			return false;
			
		forwardSensor.ping();
	//	IntelliBrain.getLcdDisplay().print(1, "Edge: " + forwardSensor.getDistanceInches());
		if (forwardSensor.getDistanceInches() > 18 ) {
			
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
			
			try {
				IntelliBrain.getLcdDisplay().print(0, "Avoid Edge");
				
				Project2b.setCurrentState(Project2b.AVOID);
				
				Thread.sleep(200);

				

				// if we detected the edge in front of us, backup
				forwardSensor.ping();
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
				
			} catch (Exception e) {
				IntelliBrain.getLcdDisplay().print(1, "CRASH: Avd Edge");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
			} finally {
				parent.setActive(false);
				if (listener != null)
					listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
			}

			
		}
	}
}
