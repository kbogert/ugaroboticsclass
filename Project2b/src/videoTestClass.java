import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.AnalogShaftEncoder;
import com.ridgesoft.robotics.ContinuousRotationServo;
import com.ridgesoft.robotics.DifferentialDriveNavigator;
import com.ridgesoft.robotics.DirectionListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Motor;
import com.ridgesoft.robotics.Navigator;
import com.ridgesoft.robotics.OdometricLocalizer;
import com.ridgesoft.robotics.Pose;
import com.ridgesoft.robotics.sensors.CMUcam.CMUcam2;
import com.ridgesoft.robotics.sensors.CMUcam.CMUcamTrackingData;


public class videoTestClass {

	public static void main(String[] args) throws Throwable {
		
		AnalogShaftEncoder leftEncoder = new AnalogShaftEncoder(IntelliBrain.getAnalogInput(5), 250, 750, 30, Thread.MAX_PRIORITY);
		AnalogShaftEncoder rightEncoder = new AnalogShaftEncoder(IntelliBrain.getAnalogInput(4), 250, 750, 30, Thread.MAX_PRIORITY);
		
		Localizer odometer = new OdometricLocalizer(leftEncoder, rightEncoder, 2.65f, 4.55f, 16, Thread.MAX_PRIORITY - 1, 30);
		
        Motor leftMotor = new ContinuousRotationServo(IntelliBrain.getServo(1), 
                false, 14, (DirectionListener) leftEncoder);
        Motor rightMotor = new ContinuousRotationServo(IntelliBrain.getServo(2), 
                true, 14, (DirectionListener) rightEncoder);
        
    	Navigator navigator = new DifferentialDriveNavigator(leftMotor,
                rightMotor, odometer, 8, 6, 25.0f, 0.5f, 0.08f,
                Thread.MAX_PRIORITY - 2, 50);
		
        Display display = IntelliBrain.getLcdDisplay();
        CMUcam2 camera = new CMUcam2(IntelliBrain.getCom2(), 115200);
        camera.open();
        camera.setRGBMode(true);
        camera.setWhiteBalance(true);
        camera.setAutoExposure(true);
        Thread.sleep(2000);
        camera.setWhiteBalance(false);
        camera.setAutoExposure(false);
        // tracking parameters vary based on the specific object being
        // tracked, the sensor's characteristics and the lighting conditions
//      camera.trackColor(150, 175, 80, 180, 85, 120); // red cup
//        camera.trackColor(83, 125, 60, 100, 200, 255); // blue cup
        camera.trackColor(235, 255, 235, 255, 235, 255); // white block
        display.print(0,"BEGIN!");
        display.print(1,"");
        
        while (true) {
           Thread.sleep(500);
            CMUcamTrackingData trackingData = camera.getTrackingData();
            if (trackingData != null) {
                display.print(0, "mx: " + trackingData.mx + "  my: "
                        + trackingData.my);
                display.print(1, "c: " + trackingData.confidence + "  p: "
                        + trackingData.pixels);

                if (trackingData.mx > 100)
                	continue;

                if (trackingData.confidence > 50 && trackingData.pixels > 100) {

                	
                	
                	if (trackingData.mx >=40 && trackingData.mx <= 60 && trackingData.my <= 60 && trackingData.confidence > 100) {
                		grabObject();
                		camera.setCameraPower(false);
                		return;
                	}
                	
                	// greater values of x = turn left
                	// lesser values of x = turn right
                	// greater values of y = go forward
                	
                	
                	if (trackingData.mx > 60) {
                		navigator.turnTo((float)Math.PI / 12 + odometer.getPose().heading, true);
                		continue;
                	}
                	if (trackingData.mx < 40) {
                		navigator.turnTo((float)Math.PI / -12 + odometer.getPose().heading, true);
                		continue;
                		
                	}
                	float y = 0;
                	
                	if (trackingData.my > 60)
                		y = trackingData.my - 60;
                	
                	Pose curPose = odometer.getPose();
                	
                	if ( y / 10 > 3)
                		y = 30;
                	
                	float targetX = (y / 10) * (float)Math.cos(curPose.heading) + curPose.x;
            		float targetY = (y / 10) * (float)Math.sin(curPose.heading) + curPose.y;

            		display.print(0, "MoveTo " + targetX + ", " + targetY);
            		display.print(1, "y " + (y / 10));
            		
   //         		Thread.sleep(1500);
                	navigator.moveTo(targetX, targetY, true);
                	
                	
                	
                }
            }
            else {
                display.print(0, "No camera data");
                display.print(1, "");
            }
        }
		
	}

	private static void grabObject() {
		ContinuousRotationServo closeMotor = new ContinuousRotationServo(IntelliBrain.getServo(3), 
				false);
		ContinuousRotationServo raiseMotor = new ContinuousRotationServo(IntelliBrain.getServo(4), 
				true, 14, null);

		closeMotor.setPower(30);

		try {
			Thread.sleep(450);
		} catch (InterruptedException e) {

		}

		closeMotor.stop();

		raiseMotor.setPower(8);

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {

		}

		raiseMotor.stop();
		
		closeMotor.setPower(-30);

		try {
			Thread.sleep(450);
		} catch (InterruptedException e) {

		}


		raiseMotor.setPower(-8);

		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {

		}

		raiseMotor.stop();
	}
}
