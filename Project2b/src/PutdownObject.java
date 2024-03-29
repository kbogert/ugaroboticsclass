import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Localizer;
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
	private Motor leftWheel;
	private Motor rightWheel;
	private Localizer loc;
	
	public PutdownObject(Motor grabMotor, Motor raiseMotor, NavigatorWrapper navWrap, SharpGP2D12 objectSensor, Motor leftWheel, Motor rightWheel, Localizer loc) {
		this.grabMotor = grabMotor;
		this.raiseMotor = raiseMotor;
		nav = navWrap;
		this.objectSensor = objectSensor;
		this.leftWheel = leftWheel;
		this.rightWheel = rightWheel;
		this.loc = loc;
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
			
			IntelliBrain.getLcdDisplay().print(0, "Putdown Obj");
			Project2b.setCurrentState(Project2b.PUTDOWN_OBJECT);


			// if this is the second block we're dropping:
			if (Project2b.getProgramState() == Project2b.PROGRAM_RETURN_SECOND_BLOCK) {

				objectSensor.ping();
				
				if (objectSensor.getDistanceInches() < 5 || objectSensor.getDistanceInches() > 24) {
					nav.turnTo((float)Math.PI, true);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}
				}
				
				int turn = 480;
				while (turn > 0) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {

					}
					objectSensor.ping();
					
					while (objectSensor.getDistanceInches() < 5 && objectSensor.getDistanceInches() > 0) {
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
					
					if (objectSensor.getDistanceInches() <= 24 && objectSensor.getDistanceInches() > 0)
						break;
					
					nav.turn((float)Math.PI / 24, true);
					turn -= 15;
					
				}
				IntelliBrain.getLcdDisplay().print(0, "Object: " + objectSensor.getDistanceInches());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {

				}
				
				// move forward until the block is 6 in away
				objectSensor.ping();
				
				float savedHeading = loc.getPose().heading;  // this is needed thanks to the wonderfully shitty navigator class allowing the wheels to jerk after a stop
				
				if (objectSensor.getDistanceInches() > 6 && objectSensor.getDistanceInches() <= 24 )
					nav.goForward((float)Math.max(objectSensor.getDistanceInches() - 6f, 1), true);
				else if (objectSensor.getDistanceInches() < 4 || objectSensor.getDistanceInches() > 24)
					return true; // problem, didn't find the block, repeat the behavior as often as necessary
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}

				nav.turnTo(savedHeading, true);
				
				raiseMotor.setPower(-12);

				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {

				}

				raiseMotor.stop();

				

				grabMotor.setPower(-30);

				try {
					Thread.sleep(450);
				} catch (InterruptedException e) {

				}

				grabMotor.stop();
				
				
				raiseMotor.setPower(12);

				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {

				}

				raiseMotor.stop();
				

				leftWheel.setPower(-8);
				rightWheel.setPower(-8);
				
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {

				}
				leftWheel.setPower(0);
				rightWheel.setPower(0);


			} else {
				nav.turnTo((float)Math.PI, true);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				
				raiseMotor.setPower(-12);

				try {
					Thread.sleep(1200);
				} catch (InterruptedException e) {

				}

				raiseMotor.stop();

				

				grabMotor.setPower(-30);

				try {
					Thread.sleep(450);
				} catch (InterruptedException e) {

				}

				grabMotor.stop();
				
				
				raiseMotor.setPower(12);

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {

				}

				raiseMotor.stop();

			}
			

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
