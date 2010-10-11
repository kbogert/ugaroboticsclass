import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Pose;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


/**
 * Integrates data from a number of sensors to estimate the robot's position
 * 
 * @author kbogert
 *
 */
public class MarkovLocalizer implements Localizer, Runnable {

	private Localizer odometer;
	private SharpGP2D12 objectSensor;
	private SharpGP2D12 tableEdgeSensor;
	private TableSensor leftTableSensor;
	private TableSensor rightTableSensor;
	private Map map;
	
	private Pose myPose;
	
	public MarkovLocalizer(Localizer odometerLocalizer, SharpGP2D12 objectSensor, SharpGP2D12 tableEdgeSensor, TableSensor leftTableSensor, TableSensor rightTableSensor, Map map) {
		odometer = odometerLocalizer;
		this.objectSensor = objectSensor;
		this.tableEdgeSensor = tableEdgeSensor;
		this.leftTableSensor = leftTableSensor;
		this.rightTableSensor = rightTableSensor;
		this.map = map;
		
		myPose = odometer.getPose();
	}
	
	public synchronized Pose getPose() {
		return myPose;
	}

	public synchronized void setHeading(float arg0) {
		myPose = new Pose(arg0, myPose.x, myPose.y);
		odometer.setPose(myPose);
	}

	public synchronized void setPose(Pose arg0) {
		myPose = arg0;
		odometer.setPose(arg0);
	}

	public synchronized void setPose(float arg0, float arg1, float arg2) {
		myPose = new Pose(arg0, arg1, arg2);
		odometer.setPose(myPose);
	}

	public synchronized void setPosition(float arg0, float arg1) {
		myPose = new Pose(myPose.heading, arg0, arg1);
		odometer.setPose(myPose);
	}

	public void run() {
		/*
		 * Every x milliseconds:
		 * 	1.  Get updated position from the odometer
		 *  2.  Perform a markovian belief update using this action
		 *  2.  Get instantaneous readings from the sensors
		 *  3.  Perform a belief update for the sensor readings
		 *  4.  Set pose = the most likely location for the robot (keep the highest belief as we go so we don't have to look again)
		 */
		
		
	}

}
