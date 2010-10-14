import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.AnalogShaftEncoder;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorArbiter;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.ContinuousRotationServo;
import com.ridgesoft.robotics.DifferentialDriveNavigator;
import com.ridgesoft.robotics.DirectionListener;
import com.ridgesoft.robotics.Motor;
import com.ridgesoft.robotics.Navigator;
import com.ridgesoft.robotics.OdometricLocalizer;
import com.ridgesoft.robotics.behaviors.GoToBehavior;
import com.ridgesoft.robotics.behaviors.StopBehavior;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


public class Project2b {

    private static final float DISTANCE = 100.0f;

    private static final int GO_1 = 1;
    private static final int AT_1 = 2;
    private static final int GO_HOME = 3;
    private static final int AT_HOME = 4;
    private static final int DONE = 5;

    private static BehaviorArbiter mArbiter;
    private static int mState;
	private static SharpGP2D12 objectSensor;
	private static SharpGP2D12 tableEdgeSensor;
	private static TableSensor leftTableSensor;
	private static TableSensor rightTableSensor;
	private static OdometricLocalizer odometer;
    private static Behavior2 mGoHomeBehavior;
    private static Behavior2 mGoDest1Behavior;
    private static Behavior2 mStopBehavior;
    private static final Object mSemaphore = new Object();
    private static final BehaviorListener mListener = new Listener();
    public static int MAPSCALE = 6;  // size of each map square in inches (along a side)
    public static Map map = new Map(8, 14, 8); // table size is 4ft x 7ft, square size is 6"x6"
	
    private static byte robotState;
    public static byte getCurrentState() {
    	synchronized(mSemaphore) {
    		return robotState;
    		
    	}
    }
    
    public static final byte IDLE = 0;
    
	public static void main(String[] args) throws InterruptedException {

		objectSensor = new SharpGP2D12(IntelliBrain.getAnalogInput(1), null);
		tableEdgeSensor = new SharpGP2D12(IntelliBrain.getAnalogInput(2), null);
		leftTableSensor = new TableSensor(IntelliBrain.getAnalogInput(6));
		rightTableSensor = new TableSensor(IntelliBrain.getAnalogInput(7));
		AnalogShaftEncoder leftEncoder = new AnalogShaftEncoder(IntelliBrain.getAnalogInput(5), 250, 750, 30, Thread.MAX_PRIORITY);
		AnalogShaftEncoder rightEncoder = new AnalogShaftEncoder(IntelliBrain.getAnalogInput(4), 250, 750, 30, Thread.MAX_PRIORITY);
		
		odometer = new OdometricLocalizer(leftEncoder, rightEncoder, 2.65f, 4.55f, 16, Thread.MAX_PRIORITY - 1, 30);
		
        Motor leftMotor = new ContinuousRotationServo(IntelliBrain.getServo(1), 
                false, 14, (DirectionListener) leftEncoder);
        Motor rightMotor = new ContinuousRotationServo(IntelliBrain.getServo(2), 
                true, 14, (DirectionListener) rightEncoder);
        
        Navigator navigator = new DifferentialDriveNavigator(leftMotor,
                rightMotor, odometer, 8, 6, 25.0f, 0.5f, 0.08f,
                Thread.MAX_PRIORITY - 2, 50);

        mGoHomeBehavior = new GoToBehavior(navigator, 0.0f, 0.0f, false);
        mGoHomeBehavior.setListener(mListener);
        mGoDest1Behavior = new GoToBehavior(navigator, 20.0f, 0.0f, true);
        mGoDest1Behavior.setListener(mListener);
        mStopBehavior = new StopBehavior(navigator, true);


        mGoDest1Behavior.setActive(true);



        Behavior2 behaviors[] = new Behavior2[] { 
        		mGoHomeBehavior, 
        		mGoDest1Behavior, 
        		mStopBehavior 
        };

        mArbiter = new BehaviorArbiter(behaviors, IntelliBrain
        		.getStatusLed(), 500);
        mArbiter.setPriority(Thread.MAX_PRIORITY - 4);


        mState = GO_1;
        mArbiter.start();

        while (true) {
        	int state;
        	synchronized (mSemaphore) {
        		state = mState;
        	}

        	switch (state) {
        	case AT_1:
        		mGoDest1Behavior.setEnabled(false);
        		mState = GO_HOME;
        		mGoHomeBehavior.setEnabled(true);
        		break;

        	case AT_HOME:
        		navigator.stop();
        		System.exit(0);
        		break;
        	}

        	synchronized (mSemaphore) {
        		mSemaphore.wait();
        	}
        }
	}

    
	private static class Listener implements BehaviorListener {
        public void behaviorEvent(BehaviorEvent event) {
            try {
                synchronized (mSemaphore) {
                    if (event.type == BehaviorEvent.BEHAVIOR_COMPLETED) {
                        if (event.behavior == mGoDest1Behavior) {
                            mState = AT_1;
                            mSemaphore.notify();
                        }
                        else if (event.behavior == mGoHomeBehavior) {
                            mState = AT_HOME;
                            mSemaphore.notify();
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
