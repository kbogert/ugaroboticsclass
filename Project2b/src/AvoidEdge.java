import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Pose;
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
	private NavigatorWrapper nav;
	private BehaviorListener listener;
	private boolean active;
	private InternalThread thread;
	private Localizer loc;
	
	AvoidEdge(NavigatorWrapper nav, SharpGP2D12 forward, Localizer l) {
		this.forwardSensor = forward;
		this.nav = nav;
		this.loc = l;
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

		if (Project2b.getCurrentState() == Project2b.MOVE_TO_OBJECT || Project2b.getCurrentState() == Project2b.PICKUP_OBJECT || Project2b.getCurrentState() == Project2b.PUTDOWN_OBJECT)
			return false;
			
		forwardSensor.ping();
//		IntelliBrain.getLcdDisplay().print(1, "Edge: " + forwardSensor.getDistanceInches());
		if (forwardSensor.getDistanceInches() > 24 || forwardSensor.getDistanceInches() < 0) {
			// If we are within 10 inches of the home, ignore the edge sensor, since otherwise we'll enter an endless loop
			Pose p = loc.getPose();
			float distanceToHomeNotSqrt = (p.x - 0)*(p.x - 0) + (p.y - 0)*(p.y - 0);
			if ((Project2b.getProgramState() == Project2b.PROGRAM_RETURN_FIRST_BLOCK || Project2b.getProgramState() == Project2b.PROGRAM_RETURN_SECOND_BLOCK) &&
				distanceToHomeNotSqrt <= 100)
				return false;
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
				if (forwardSensor.getDistanceInches() > 24 || forwardSensor.getDistanceInches() < 0){
					Pose pose = loc.getPose();
					float x = 8 * (float)Math.cos(pose.heading) + pose.x;
					float y = 8 * (float)Math.sin(pose.heading) + pose.y;

					nav.addObstacle(x * Project2b.MAPSCALE, y * Project2b.MAPSCALE, 10, 2);
					
					nav.turn(-(float)Math.PI / 2, true);
					//nav.goBackward(4, true);
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
