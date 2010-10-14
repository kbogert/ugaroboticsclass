import com.ridgesoft.robotics.Behavior2;
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

	}

	public boolean poll() {
		if (! isEnabled) {
			return false;
		}
		
		if (forwardSensor.getDistanceInches() > 12 ||
				! leftRear.isOnTable() || ! rightRear.isOnTable()) {
			
			nav.stop();
			
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

			}

			// if we detected the edge in front of us, backup
			if (forwardSensor.getDistanceInches() > 12) {
				
				// if both, turn away from the rear detection
				if (! leftRear.isOnTable()) {
					
					nav.turn((float)(Math.PI / 2), true);
					
				} else if ( ! rightRear.isOnTable()) {

					nav.turn(-(float)(Math.PI / 2), true);
					
				} else {
					nav.goBackward(-3, true);
				}
				
			} else {
				// if we detected the edge in back, Move Forward
				
				nav.goForward(3, true);
				
			}
			
			
		}
		
		
		return false;
	}

	public void setActive(boolean arg0) {
	}

}
