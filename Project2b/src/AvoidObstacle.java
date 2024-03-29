import com.ridgesoft.intellibrain.IntelliBrain;
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
	private boolean active;
	private InternalThread thread;
	
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
		
		if (active) {
			if (thread == null) {
				nav.stop();
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
		
		objectSensor.ping();
		if (Project2b.getCurrentState() == Project2b.NAVIGATE && objectSensor.getDistanceInches() < 6 && objectSensor.getDistanceInches() > 0) {
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
		private AvoidObstacle parent;
		private boolean active = true;
		
		public InternalThread(AvoidObstacle p) {
			parent = p;
		}

		public void setActive(boolean b) {
			active = b;
		}
		
		public void run() {
			try {
				IntelliBrain.getLcdDisplay().print(0, "Avoid Obstacle");
				Project2b.setCurrentState(Project2b.AVOID);

				// add an obstacle to nav
				objectSensor.ping();
				float distance = (objectSensor.getDistanceInches() / Project2b.MAPSCALE);
				
				Pose pose = loc.getPose();
				float x = distance * (float)Math.cos(pose.heading) + pose.x;
				float y = distance * (float)Math.sin(pose.heading) + pose.y;

				nav.addObstacle(x * Project2b.MAPSCALE, y * Project2b.MAPSCALE, 10, 2);
				
				Thread.sleep(200);

				parent.setActive(false);
				
				if (listener != null)
					listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
			} catch (Exception e) {
				IntelliBrain.getLcdDisplay().print(1, "CRASH: Avd Obst");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
			} finally {
				parent.setActive(false);
			}
			
		}
	}
}
