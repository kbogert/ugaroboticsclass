import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.AnalogInput;


public class SensorTest {


	public static void main(String[] args) {

		// depending on the analog dial's position, sample 
		
        AnalogInput thumbwheel = IntelliBrain.getThumbWheel();
        Display display = IntelliBrain.getLcdDisplay();

        while (true) {
        	int thumbWheelPos = (int) ((float)thumbwheel.sample()) / thumbwheel.getMaximum() * 8;
        	
        	display.print(0, "A Sensor: " + Integer.toString(thumbWheelPos));
        	
        	AnalogInput sensor = IntelliBrain.getAnalogInput(thumbWheelPos);
        	
            display.print(1, Integer.toString(sensor.sample()));
            
            try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

			}
            
        }
        
	}

}
