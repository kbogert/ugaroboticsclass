import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Navigator;

/**
 * All parameters must be in inches and radians
 * 
 * @author kbogert
 *
 */
public class NavigatorWrapper {

	private Localizer loc;
	private Navigator nav;
	private Gradient obstacles;
	private Gradient goal;
	
	public NavigatorWrapper(Localizer odometer, Navigator nav) {
	
		this.loc = odometer;
		this.nav = nav;
	}
	
	/**
	 * Turn relative to the current heading
	 * 
	 * @param amount
	 */
	public void turn(float amount, boolean wait) {
		nav.turnTo(loc.getPose().heading + amount, wait);
	}
	
	public void goForward(float distance, boolean wait) {
		go(distance, true, wait);
	}
	
	public void goBackward(float distance, boolean wait) {
		go(distance, false, wait);		
	}
	
	/**
	 * Go the specified distance in the specified direction
	 * 
	 * @param distance
	 * @param direction
	 * true = forward
	 * false = backward
	 */
	public void go(float distance, boolean direction, boolean wait) {
		float angle = loc.getPose().heading;
		float x = loc.getPose().x;
		float y = loc.getPose().y;
		
		if (!direction)
			distance *= -1;
		
		float targetX = distance * (float)Math.cos(angle) + x;
		float targetY = distance * (float)Math.sin(angle) + y;
		
		nav.moveTo(targetX, targetY, wait);
		
				
	}
	
	public void stop() {
		nav.stop();
	}
	
	public void setGoal(float x, float y, float strength, float factor) {
		Gradient temp = new Gradient();
		temp.x = x;
		temp.y = y;
		temp.strength = -strength;
		temp.factor = factor;
		goal = temp;
	}
	
	public void addObstacle(float x, float y, float strength, float factor) {
		Gradient temp = new Gradient();
		temp.x = x;
		temp.y = y;
		temp.strength = strength;
		temp.factor = factor;

		if (obstacles == null) {
			obstacles = temp;
		} else {
			Gradient pointer = obstacles;
			while (pointer.next != null) {
				pointer = pointer.next;
			}
			pointer.next = temp;
		}
	}
	
	public boolean atGoal(float x, float y) {
		float distanceNotSqrt = (x - goal.x)*(x - goal.x) + (y - goal.y)*(y - goal.y);
		return distanceNotSqrt <  .04f;
	}
	
	/**
	 * 
	 * Calculates heading based on goal and obstacle weights
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float getHeadingFrom(float x, float y) {
		float [] Xs = new float[8];
		float [] Ys = new float[8];
		float [] elevations = new float[8];
		
		int min = 0;
		for (int i = 0; i < 8; i ++) {
			Xs[i] = .2f * (float)Math.cos((Math.PI / 4.0) * i) + x;
			Ys[i] = .2f * (float)Math.sin((Math.PI / 4.0) * i) + y;
			
			float distanceNotSqrt = (Xs[i] - goal.x)*(Xs[i] - goal.x) + (Ys[i] - goal.y)*(Ys[i] - goal.y);
			elevations[i] = goal.strength / (distanceNotSqrt * goal.factor);
			
			Gradient cursor = obstacles;
			while (cursor != null) {
				distanceNotSqrt = (Xs[i] - cursor.x)*(Xs[i] - cursor.x) + (Ys[i] - cursor.y)*(Ys[i] - cursor.y);
				elevations[i] += cursor.strength / (distanceNotSqrt * cursor.factor);
				
				cursor = cursor.next;
			}
			
			if (elevations[i] < elevations[min])
				min = i;

		}
		
		return (float)(Math.PI / 4.0f) * min;
		
	}
	
	public void clearGradients() {
		obstacles = null;
		goal = null;
	}
	
	private class Gradient {
		public float x;
		public float y;
		
		public float strength;
		public float factor;
		
		public Gradient next;
	}
}
