import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.Motor;
import com.ridgesoft.robotics.sensors.CMUcam.CMUcam2;
import com.ridgesoft.robotics.sensors.CMUcam.CMUcamTrackingData;


/**
 * Get a reading from the cmucam as to the color of the object in front of us
 * 
 * Record the object's color in the map
 * 
 * If it's the one we're looking for, set an appropriate state
 * 
 * @author kbogert
 *
 */
public class ExamineObject implements Behavior2 {

	private boolean enabled;
	private boolean active;
	private BehaviorListener listener;
	private NavigatorWrapper nav;
	private CMUcam2 camera;
	private Motor raiseMotor;
	
	public ExamineObject(NavigatorWrapper nav, CMUcam2 camera, Motor r) throws Exception{
		this.nav = nav;
		this.camera = camera;
		this.raiseMotor = r;
		
        camera.open();
        camera.setRGBMode(true);
        camera.setWhiteBalance(true);
        camera.setAutoExposure(true);
        Thread.sleep(2000);
        camera.setWhiteBalance(false);
        camera.setAutoExposure(false);

        
        camera.trackColor(220, 255, 220, 255, 220, 255); // white block
//        camera.sleep();
        
        
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
			IntelliBrain.getLcdDisplay().print(0, "Examine Obj");
			Project2b.setCurrentState(Project2b.EXAMINE_OBJECT);

			try {
//				camera.wake();
				
				
				
				// the cmucam has been set to only track the color (white) of our block, if this behavior is activated
				// and there is no significant tracking data, we either missed the block or it's the wrong color
				
				// if we found the block, maneuver using the camera's data to get it into the right position for pickup
				
				int noDetectCounter = 0;
				
				raiseMotor.setPower(-8);

				try {
					Thread.sleep(1200);
				} catch (InterruptedException e) {

				}

				raiseMotor.stop();
				
				Display display = IntelliBrain.getLcdDisplay();
				while (true) {
				    Thread.sleep(750);
				    CMUcamTrackingData trackingData = camera.getTrackingData();
	                display.print(0, "mx: " + trackingData.mx + "  my: "
	                        + trackingData.my);
	                display.print(1, "c: " + trackingData.confidence + "  p: "
	                        + trackingData.pixels);

				    if (trackingData.mx > 100)
				    	continue;

				    if (trackingData.confidence > 50 && trackingData.pixels > 60) {

				    	noDetectCounter = 0;


				    	if (trackingData.mx >=40 && trackingData.mx <= 60 && trackingData.my <= 60 && trackingData.confidence > 70 && trackingData.pixels > 75) {
				    		// we're done, grab it.
//				    		camera.sleepDeeply();

				    		setActive(false);
				    		
				    		if (listener != null)
					        	listener.behaviorEvent(new BehaviorEvent(this, BehaviorEvent.BEHAVIOR_COMPLETED));
				    		
				    		return false;
				    	}

				    	// greater values of x = turn left
				    	// lesser values of x = turn right
				    	// greater values of y = go forward


				    	if (trackingData.mx > 60) {
				    		nav.turn((float)Math.PI / 12, true);
				    		continue;
				    	}
				    	if (trackingData.mx < 40) {
				    		nav.turn((float)Math.PI / -12, true);
				    		continue;

				    	}
				    	float y = 0;

				    	if (trackingData.my > 60)
				    		y = trackingData.my - 60;

				    	if ( y / 10 > 3)
				    		y = 30;
				    	
				    	if (y / 10 < 1)
				    		y = 1;

				    	nav.goForward(y / 10, true);

				    } else {
				    	noDetectCounter ++;
				    }
				    
				    if (noDetectCounter > 10) {

				    	break;
				    }
				}

//				camera.sleepDeeply();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			} 

			raiseMotor.setPower(8);

			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {

			}

			raiseMotor.stop();
			
			setActive(false);
	        if (listener != null)
	        	listener.behaviorEvent(new BehaviorEvent(this, -1));
	        return false;
		}
		return Project2b.getCurrentState() == Project2b.EXAMINE_OBJECT;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
