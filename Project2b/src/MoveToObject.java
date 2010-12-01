import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Motor;
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
	private InternalThread thread;
	private Motor leftWheel;
	private Motor rightWheel;
	private Motor raiseMotor;
	
	MoveToObject(NavigatorWrapper n, SharpGP2D12 objectSensor, Motor leftWheel, Motor rightWheel, Motor raiseMotor) {
		nav = n;
		this.objectSensor = objectSensor;
		this.leftWheel = leftWheel;
		this.rightWheel = rightWheel;
		this.raiseMotor = raiseMotor;
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
		
		if (getActive()) {
			if (thread == null) {
				// all stop
				nav.stop();
				thread = new InternalThread(this);
				thread.start();
			} else if (! thread.isAlive() || thread.isInterrupted()) {
				setActive(false);
				return false;
			}

			return true;
		}
		if (thread != null) {
			thread.setActive(false);
			thread = null;
		}

		if (Project2b.getProgramState() == Project2b.PROGRAM_RETURN_FIRST_BLOCK || Project2b.getProgramState() == Project2b.PROGRAM_RETURN_SECOND_BLOCK)
			return false;
			
		objectSensor.ping();
		float distance = objectSensor.getDistanceInches();
		if (distance <= 18 && distance > 0) {
			// detected an object, check if we already have seen it
/*			distance = distance / Project2b.MAPSCALE;
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
			*/
//			IntelliBrain.getLcdDisplay().print(0, "Object at " + distance);

			return true;
		}
		
		return false;
	}

	public synchronized void setActive(boolean arg0) {
		active = arg0;
		if (thread != null)
			thread.setActive(arg0);
	}
	
	public synchronized boolean getActive() {
		return active;
		
	}
	
	private class InternalThread extends Thread {
		private MoveToObject parent;
		private boolean active;
		
		public InternalThread(MoveToObject p) {
			parent = p;
			active = true;
		}

		public void setActive(boolean b) {
			active = b;
		}

		public void run() {
			boolean notifiedListener = false;
			
			try {
				IntelliBrain.getLcdDisplay().print(0, "Move To Object");
				Project2b.setCurrentState(Project2b.MOVE_TO_OBJECT);

				
				Thread.sleep(500);
				
				if (!active)
					return;
				// make sure we're still pointed at the object, if not:
					// search a little to the left
					// if not there, search a little to the right
					// if not there, return as this behavior is done
				
				objectSensor.ping();
				float distance = objectSensor.getDistanceInches();

				IntelliBrain.getLcdDisplay().print(1, "Distance: " + distance);
				Thread.sleep(1000);
				
				if (distance > 18 || distance < 0) {

					nav.turn((float)Math.PI / 12, true);
					if (!active)
						return;

					Thread.sleep(250);
	
					if (!active)
						return;
					
					objectSensor.ping();
					distance = objectSensor.getDistanceInches();
					
					if (distance > 18 || distance < 0) {
						
						Thread.sleep(250);

						if (!active)
							return;
						nav.turn((float)Math.PI / -12, true);
						nav.turn((float)Math.PI / -12, true);
						if (!active)
							return;
						
						Thread.sleep(250);
						
						objectSensor.ping();
						distance = objectSensor.getDistanceInches();
						if (distance > 18 || distance < 0) {
							
							parent.setActive(false);

							listener.behaviorEvent(new BehaviorEvent(parent, -1));
							notifiedListener = true;
							

							return;
						}
					}
				}
				
				objectSensor.ping();				
				while (objectSensor.getDistanceInches() <= 6.5) {
					leftWheel.setPower(-8);
					rightWheel.setPower(-8);
					
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {

					}
					leftWheel.setPower(0);
					rightWheel.setPower(0);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {

					}
					objectSensor.ping();
				}
				
				// move forward towards the object, when we're < 6 inches stop and return
				distance = objectSensor.getDistanceInches();
				
				raiseMotor.setPower(-12);

				try {
					Thread.sleep(1700);
				} catch (InterruptedException e) {

				}

				raiseMotor.stop();
				
				nav.goForward(distance - 6, true);
				if (!active)
					return;

				parent.setActive(false);
				
				listener.behaviorEvent(new BehaviorEvent(parent, BehaviorEvent.BEHAVIOR_COMPLETED));
				notifiedListener = true;
			} catch (Exception e) {
				IntelliBrain.getLcdDisplay().print(1, "CRASH: Move To Object");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
				parent.setActive(false);
				if (!notifiedListener)
					listener.behaviorEvent(new BehaviorEvent(parent, -1));
			}
			

		}
		
	}

}
