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
import com.ridgesoft.robotics.sensors.SharpGP2D12;


public class Project2b {

    private static BehaviorArbiter mArbiter;
    private static int mState;
	private static SharpGP2D12 objectSensor;
	private static SharpGP2D12 tableEdgeSensor;
	private static TableSensor leftTableSensor;
	private static TableSensor rightTableSensor;
	private static OdometricLocalizer odometer;
    
	private static Behavior2 mAvoidEdgeBehavior;
    private static Behavior2 mAvoidObstacleBehavior;
    private static Behavior2 mExamineObjectBehavior;
    private static Behavior2 mExploreBehavior;
    private static Behavior2 mIdentifyHomeBehavior;
    private static Behavior2 mLookAroundBehavior;
    private static Behavior2 mMoveToHomeBehavior;
    private static Behavior2 mMoveToObjectBehavior;
    private static Behavior2 mNavigateBehavior;
    
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
    public static final byte NAVIGATE = 1;
    public static final byte LOOK_AROUND = 2;
    public static final byte MOVE_TO_OBJECT = 3;
    public static final byte AVOID = 4;
    public static final byte EXAMINE_OBJECT = 5;
    public static final byte MOVE_TO_HOME = 6;
    public static final byte IDENTIFY_HOME = 7;
    public static final byte FINISHED = 8;
    
    
    
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

        NavigatorWrapper navWrap = new NavigatorWrapper(odometer, navigator);
        MarkovLocalizer localizer = new MarkovLocalizer(odometer, objectSensor, tableEdgeSensor, leftTableSensor, rightTableSensor, map);
        

        mAvoidEdgeBehavior = new AvoidEdge(navWrap, tableEdgeSensor, leftTableSensor, rightTableSensor);
        mAvoidObstacleBehavior = new AvoidObstacle(objectSensor, navWrap, localizer);
        mExamineObjectBehavior = new ExamineObject();
        mExploreBehavior = new Explore(navWrap, map);
        mIdentifyHomeBehavior = new IdentifyHome(navigator, map);
        mLookAroundBehavior = new LookAround(navWrap);
        mMoveToHomeBehavior = new MoveToHome(navWrap);
        mMoveToObjectBehavior = new MoveToObject(navWrap, objectSensor, localizer, map);
        mNavigateBehavior = new Navigate(navWrap, localizer);
        
        

        mIdentifyHomeBehavior.setActive(true);



        Behavior2 behaviors[] = new Behavior2[] { 
        		mAvoidEdgeBehavior, 
        		mAvoidObstacleBehavior, 
        		mExamineObjectBehavior,
        		mExploreBehavior,
        		mIdentifyHomeBehavior,
        		mLookAroundBehavior,
        		mMoveToHomeBehavior,
        		mMoveToObjectBehavior,
        		mNavigateBehavior
        };

        for (int i = 0; i < behaviors.length; i ++) {
        	behaviors[i].setListener(mListener);
        	behaviors[i].setEnabled(true);
        }
        
        mArbiter = new BehaviorArbiter(behaviors, IntelliBrain
        		.getStatusLed(), 500);
        mArbiter.setPriority(Thread.MAX_PRIORITY - 4);


        mState = IDENTIFY_HOME;
        mIdentifyHomeBehavior.setActive(true);
        mArbiter.start();

        while (true) {
        	int state;
        	synchronized (mSemaphore) {
        		state = mState;
        	}

        	switch (state) {
        	case NAVIGATE:
        		mNavigateBehavior.setActive(true);
        		break;

        	case LOOK_AROUND:
        		mLookAroundBehavior.setActive(true);
        		break;
        		
        	case EXAMINE_OBJECT:
        		mExamineObjectBehavior.setActive(true);
        		break;
        		
        	case FINISHED:
        		navigator.stop();
        		Thread.sleep(200);
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
                        if (event.behavior == mIdentifyHomeBehavior) {
                            mState = IDLE;
                        }
                        else if (event.behavior == mAvoidEdgeBehavior) {
                            mState = IDLE;
                        }
                        else if (event.behavior == mAvoidObstacleBehavior) {
                        	mState = NAVIGATE;
                        }
                        else if (event.behavior == mExamineObjectBehavior) {
                        	mState = IDLE;
                        }
                        else if (event.behavior == mExploreBehavior) {
                        	mState = NAVIGATE;
                        }
                        else if (event.behavior == mNavigateBehavior) {
                        	mState = LOOK_AROUND;
                        }
                        else if (event.behavior == mLookAroundBehavior) {
                        	mState = IDLE;
                        }
                        else if (event.behavior == mMoveToHomeBehavior) {
                        	mState = NAVIGATE;
                        }
                        else if (event.behavior == mMoveToObjectBehavior) {
                        	mState = EXAMINE_OBJECT;
                        }
                        mSemaphore.notify();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
