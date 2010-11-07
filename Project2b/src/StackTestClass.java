import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.AnalogShaftEncoder;
import com.ridgesoft.robotics.ContinuousRotationServo;
import com.ridgesoft.robotics.DifferentialDriveNavigator;
import com.ridgesoft.robotics.DirectionListener;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Motor;
import com.ridgesoft.robotics.Navigator;
import com.ridgesoft.robotics.OdometricLocalizer;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


public class StackTestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		
    	
		ContinuousRotationServo closeMotor = new ContinuousRotationServo(IntelliBrain.getServo(3), 
				false);
		Motor raiseMotor = new ContinuousRotationServo(IntelliBrain.getServo(4), 
                true, 14, null);
        
		SharpGP2D12 objectSensor = new SharpGP2D12(IntelliBrain.getAnalogInput(2), null);

		NavigatorWrapper nav = new NavigatorWrapper(odometer, navigator);


		closeMotor.setPower(-30);

		try {
			Thread.sleep(450);
		} catch (InterruptedException e) {

		}

		closeMotor.stop();

		try {
			Thread.sleep(2550);
		} catch (InterruptedException e) {

		}
		

		closeMotor.setPower(30);

		try {
			Thread.sleep(450);
		} catch (InterruptedException e) {

		}

		closeMotor.stop();
		
		
		float turn = (float)Math.PI / 12;
		for (int i = 0; i < 8; i ++) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {

			}
			objectSensor.ping();
			
			if (objectSensor.getDistanceInches() <= 12 && objectSensor.getDistanceInches() > 0)
				break;
			
			nav.turn(turn, true);
			turn *= -1.2;
		}
		IntelliBrain.getLcdDisplay().print(0, "Object: " + objectSensor.getDistanceInches());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// move forward until the block is 4.5 in away
		objectSensor.ping();
		
		if (objectSensor.getDistanceInches() > 4.5 && objectSensor.getDistanceInches() <= 24 )
			nav.goForward(objectSensor.getDistanceInches() - 4.5f, true);
		else
			return;
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		raiseMotor.setPower(-8);

		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {

		}

		raiseMotor.stop();

		

		closeMotor.setPower(-30);

		try {
			Thread.sleep(450);
		} catch (InterruptedException e) {

		}

		closeMotor.stop();
		
		closeMotor.setPower(30);

		try {
			Thread.sleep(450);
		} catch (InterruptedException e) {

		}

		closeMotor.stop();
		
		
		raiseMotor.setPower(8);

		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {

		}

		raiseMotor.stop();
		
	}

}
