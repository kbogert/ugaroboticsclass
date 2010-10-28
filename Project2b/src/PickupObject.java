import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Motor;


public class PickupObject implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private Motor grabMotor;
	private Motor raiseMotor;
	
	public PickupObject(Motor grabMotor, Motor raiseMotor) {
		this.grabMotor = grabMotor;
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
		
		if (active) {
			IntelliBrain.getLcdDisplay().print(0, "Pickup Obj");

			Project2b.setCurrentState(Project2b.PICKUP_OBJECT);
			// grab the object in front of us, then raise it up
			
			grabMotor.setPower(30);

			try {
				Thread.sleep(450);
			} catch (InterruptedException e) {

			}

			grabMotor.stop();

			raiseMotor.setPower(8);

			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {

			}

			raiseMotor.stop();

			setActive(false);
			
			if (listener != null)
				listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));			
		
			return false;
		}
		
		return Project2b.getCurrentState() == Project2b.PICKUP_OBJECT;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
