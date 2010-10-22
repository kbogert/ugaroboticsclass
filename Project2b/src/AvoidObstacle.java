import java.util.Random;

import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


/**
 * If we are exploring, don't run into any obstacles in front of us
 * 
 * @author kbogert
 *
 */
public class AvoidObstacle implements Behavior2 {

	private boolean enabled;
	private BehaviorListener listener;
	private SharpGP2D12 objectSensor;
	private NavigatorWrapper nav;
	
	public void setEnabled(boolean arg0) {
		enabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {
		listener = arg0;
	}

	public AvoidObstacle(SharpGP2D12 objectSensor, NavigatorWrapper nav) {
		this.objectSensor = objectSensor;
		this.nav = nav;
	}
	
	public boolean poll() {
		if (! enabled) {
			return false;
		}
		
		if (Project2b.getCurrentState() == Project2b.NAVIGATE && objectSensor.getDistanceInches() < 6) {

			nav.stop();
			
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

			}
			
			Random rand = new Random();
			
			nav.turn(rand.nextInt(8) * (float)Math.PI / 4, true);
			
			
		}
		return false;
		
	}

	public void setActive(boolean arg0) {

	}

}
