import com.ridgesoft.robotics.AnalogInput;


public class TableSensor {

	private final static int LOW_THRESHOLD = 100;
	
	private AnalogInput mySensor;
	
	public TableSensor (AnalogInput sensor) {
		mySensor = sensor;
	}
	
	public boolean isOnTable() {
		return mySensor.sample() < LOW_THRESHOLD;
		
	}
	
}
