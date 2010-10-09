import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Navigator;
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
	private boolean isActive;
	private SharpGP2D12 forwardSensor;
	private TableSensor leftRear;
	private TableSensor rightRear;
	private Navigator nav;
	private Localizer loc;
	
	AvoidEdge(Navigator nav, Localizer loc, SharpGP2D12 forward, TableSensor leftRear, TableSensor rightRear) {
		this.forwardSensor = forward;
		this.leftRear = leftRear;
		this.rightRear = rightRear;
		this.nav = nav;
		this.loc = loc;
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
					float turn = loc.getPose().heading + (float)(Math.PI / 2);
					
					nav.turnTo(turn, true);
					
				} else if ( ! rightRear.isOnTable()) {
					float turn = loc.getPose().heading - (float)(Math.PI / 2);

					nav.turnTo(turn, true);
					
				} else {
					nav.go(-3);
				}
				
			} else {
				// if we detected the edge in back, Move Forward
				
				nav.go(3);
				
			}
			
			
		}
		
		
		return false;
	}

	public void setActive(boolean arg0) {
		isActive = arg0;
	}

}
