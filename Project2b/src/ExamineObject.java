import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
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
	
	public ExamineObject(NavigatorWrapper nav, CMUcam2 camera) throws Exception{
		this.nav = nav;
		this.camera = camera;
		
        camera.open();
        camera.setRGBMode(true);
        camera.setWhiteBalance(true);
        camera.setAutoExposure(true);
        Thread.sleep(2000);
        camera.setWhiteBalance(false);
        camera.setAutoExposure(false);

        
        camera.trackColor(235, 255, 235, 255, 235, 255); // white block
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

			try {
//				camera.wake();
				
				
				
				// the cmucam has been set to only track the color (white) of our block, if this behavior is activated
				// and there is no significant tracking data, we either missed the block or it's the wrong color
				
				// if we found the block, maneuver using the camera's data to get it into the right position for pickup
				
				int noDetectCounter = 0;
				
				while (true) {
				    Thread.sleep(500);
				    CMUcamTrackingData trackingData = camera.getTrackingData();

				    if (trackingData.mx > 100)
				    	continue;

				    if (trackingData.confidence > 50 && trackingData.pixels > 100) {

				    	noDetectCounter = 0;


				    	if (trackingData.mx >=40 && trackingData.mx <= 60 && trackingData.my <= 60 && trackingData.confidence > 100) {
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

				    	nav.goForward(y / 10, true);

				    } else {
				    	noDetectCounter ++;
				    }
				    
				    if (noDetectCounter > 10)
				    	break;
				}

//				camera.sleepDeeply();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			} 

			setActive(false);
	        if (listener != null)
	        	listener.behaviorEvent(new BehaviorEvent(this, -1));
	        
		}
		return false;
	}

	public void setActive(boolean arg0) {
		active = arg0;
	}

}
