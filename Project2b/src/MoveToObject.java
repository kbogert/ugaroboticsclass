import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Pose;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


/**
 * If we found an object that we haven't explored yet, move to it.
 *  
 * @author kbogert
 *
 */
public class MoveToObject implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private NavigatorWrapper nav;
	private SharpGP2D12 objectSensor;
	private Localizer localizer;
	private Map myMap;
	private InternalThread thread;
	
	MoveToObject(NavigatorWrapper n, SharpGP2D12 objectSensor, Localizer l, Map m) {
		nav = n;
		this.objectSensor = objectSensor;
		localizer = l;
		myMap = m;
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
				// all stop
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
		float distance = objectSensor.getDistanceInches();
		if (distance <= 24) {
			// detected an object, check if we already have seen it
			distance = distance / Project2b.MAPSCALE;
			Pose pose = localizer.getPose();			
			
			int targetX = Math.round(distance * (float)Math.cos(pose.heading) + pose.x);
			int targetY = Math.round(distance * (float)Math.sin(pose.heading) + pose.y);
			
			// search the map objects for a match
			
			MapObj curObj = myMap.getMapObjs();
			boolean found = false;
			
			while (curObj != null) {
				if (curObj.getType() == MapObj.OBJECT && curObj.getX() == targetX && curObj.getY() == targetY) {
					found = true;
					break;
				}
				curObj = curObj.getNext();
			}
			
			return ! found;
		}
		
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
		if (thread != null)
			thread.setActive(arg0);
	}
	
	private class InternalThread extends Thread {
		private MoveToObject parent;
		private boolean active = true;
		
		public InternalThread(MoveToObject p) {
			parent = p;
		}

		public void setActive(boolean b) {
			active = b;
		}

		public void run() {
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			if (!active)
				return;
			// make sure we're still pointed at the object, if not:
				// search a little to the left
				// if not there, search a little to the right
				// if not there, return as this behavior is done
			
			float distance = objectSensor.getDistanceInches();
			
			if (distance > 24) {
				nav.turn((float)Math.toRadians(5), true);
				if (!active)
					return;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}	
				if (!active)
					return;
				distance = objectSensor.getDistanceInches();
				
				if (distance > 24) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					if (!active)
						return;
					nav.turn((float)Math.toRadians(-10), true);
					if (!active)
						return;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
					distance = objectSensor.getDistanceInches();
					if (distance > 24) {
						listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
						
						parent.setActive(false);
					}
				}
			}
			
			// move forward towards the object, when we're < 4 inches stop and return
			distance = objectSensor.getDistanceInches();
			nav.goForward(distance - 4, true);
			if (!active)
				return;
		
			listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
			
			parent.setActive(false);
		}
		
	}

}
