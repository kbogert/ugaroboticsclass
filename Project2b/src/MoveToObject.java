import com.ridgesoft.robotics.Behavior2;
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
	}

}
