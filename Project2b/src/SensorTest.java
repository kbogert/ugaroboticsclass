import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


public class SensorTest {


	public static void main(String[] args) {

		// depending on the analog dial's position, sample 
		
        AnalogInput thumbwheel = IntelliBrain.getThumbWheel();
        Display display = IntelliBrain.getLcdDisplay();

        SharpGP2D12 objectSensor = new SharpGP2D12(IntelliBrain.getAnalogInput(2), null);
        SharpGP2D12 tableEdgeSensor = new SharpGP2D12(IntelliBrain.getAnalogInput(1), null);

        while (true) {
        	int thumbWheelPos = (int) ((float)thumbwheel.sample() / thumbwheel.getMaximum() * 6) + 1;
        	
        	
        	display.print(0, "A Sensor: " + Integer.toString(thumbWheelPos));
        	
        	if (thumbWheelPos == 2 ) {
                objectSensor.ping();
        		display.print(1, Float.toString(objectSensor.getDistanceInches()));
        	} else if (thumbWheelPos == 1 ) {
                tableEdgeSensor.ping();
        		display.print(1, Float.toString(tableEdgeSensor.getDistanceInches()));
        	} else {
        		AnalogInput sensor = IntelliBrain.getAnalogInput(thumbWheelPos);
        	
            	display.print(1, Integer.toString(sensor.sample()));
        	}
            
            try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

			}
            
        }
        
	}

}
