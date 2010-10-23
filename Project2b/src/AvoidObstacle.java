import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Pose;
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
	private Localizer loc;
	
	public void setEnabled(boolean arg0) {
		enabled = arg0;
	}

	public void setListener(BehaviorListener arg0) {
		listener = arg0;
	}

	public AvoidObstacle(SharpGP2D12 objectSensor, NavigatorWrapper nav, Localizer loc) {
		this.objectSensor = objectSensor;
		this.nav = nav;
		this.loc = loc;
	}
	
	public boolean poll() {
		if (! enabled) {
			return false;
		}
		
		if (Project2b.getCurrentState() == Project2b.NAVIGATE && objectSensor.getDistanceInches() < 6) {

			nav.stop();
			
			// add an obstacle to nav
			float distance = (objectSensor.getDistanceInches() / Project2b.MAPSCALE);
			
			Pose pose = loc.getPose();
			float x = distance * (float)Math.cos(pose.heading) + pose.x;
			float y = distance * (float)Math.sin(pose.heading) + pose.y;

			nav.addObstacle(x * Project2b.MAPSCALE, y * Project2b.MAPSCALE, 10, 2);
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

			}
			
			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));
		}
		return false;
		
	}

	public void setActive(boolean arg0) {

	}

}
