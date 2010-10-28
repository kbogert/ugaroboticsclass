import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.ContinuousRotationServo;


public class grabTestClass {

	public static void main(String[] args) {

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

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {

		}

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
