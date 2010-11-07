import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Motor;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


public class PutdownObject implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private Motor grabMotor;
	private Motor raiseMotor;
	private NavigatorWrapper nav;
	private SharpGP2D12 objectSensor;
	
	public PutdownObject(Motor grabMotor, Motor raiseMotor, NavigatorWrapper navWrap, SharpGP2D12 objectSensor) {
		this.grabMotor = grabMotor;
		this.raiseMotor = raiseMotor;
		nav = navWrap;
		this.objectSensor = objectSensor;
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
			// if we have already dropped a block, find it and maneuver so that it's underneath our gripper
			
			// how? need some way to detect the first block while we're holding one.
			// Camera will be blocked
			// IR sensor is blocked
			
			// need another sensor? or else keep the IR sensor fixed rather than raise it up
			
			// lower the block and open the gripper
			
			// for right now, just drop the thing
			IntelliBrain.getLcdDisplay().print(0, "Putdown Obj");
			Project2b.setCurrentState(Project2b.PUTDOWN_OBJECT);

			nav.turnTo((float)Math.PI, true);
			
			// if this is the second block we're dropping:
			if (Project2b.getProgramState() == Project2b.PROGRAM_RETURN_SECOND_BLOCK) {
				// find the original block
				float divisor = 24;
				for (int i = 0; i < 5; i ++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {

					}
					objectSensor.ping();
					
					if (objectSensor.getDistanceInches() <= 12 && objectSensor.getDistanceInches() > 0)
						break;
					
					nav.turn((float)Math.PI / divisor, true);
					divisor = divisor / -2;
				}
				
				// move forward until the block is 4.5 in away
				objectSensor.ping();
				
				if (objectSensor.getDistanceInches() > 4.5 && objectSensor.getDistanceInches() <= 24)
					nav.goForward(objectSensor.getDistanceInches() - 4.5f, true);
				
				// center on block
				
				// TODO
				
				
			}
			
			grabMotor.setPower(-30);

			try {
				Thread.sleep(450);
			} catch (InterruptedException e) {

			}
			
			grabMotor.stop();

			nav.goBackward((float)2.5, true);

			setActive(false);
			
			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));			
			
			return false;
			
		}
		return Project2b.getCurrentState() == Project2b.LOOK_AROUND && ! (Project2b.getProgramState() == Project2b.PROGRAM_FIND_FIRST_BLOCK || Project2b.getProgramState() == Project2b.PROGRAM_FIND_SECOND_BLOCK);
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
