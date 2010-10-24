import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Pose;
import com.ridgesoft.robotics.sensors.SharpGP2D12;


/**
 * Integrates data from a number of sensors to estimate the robot's position
 * 
 * @author kbogert
 *
 */
public class MarkovLocalizer extends Thread implements Localizer {

	private Localizer odometer;
	private SharpGP2D12 objectSensor;
	private SharpGP2D12 tableEdgeSensor;
	private TableSensor leftTableSensor;
	private TableSensor rightTableSensor;
	private Map map;
	
	private Pose myPose;
	private Pose lastOdometerReading;
	
	
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
	}

	public synchronized void setPose(Pose arg0) {
		myPose = arg0;
	}

	public synchronized void setPose(float arg0, float arg1, float arg2) {
		myPose = new Pose(arg0, arg1, arg2);
	}

	public synchronized void setPosition(float arg0, float arg1) {
		myPose = new Pose(myPose.heading, arg0, arg1);
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

		int counter = 0;
		lastOdometerReading = odometer.getPose();
		try {
		while (true) {
				IntelliBrain.getLcdDisplay().print(0, "Markov Iter: " + counter);
				counter ++;

				Pose odometerPose = odometer.getPose();
				boolean [] observations = new boolean[4];
				observations[0] = objectSensor.getDistanceCm() > 7 && objectSensor.getDistanceCm() < 91;
				observations[1] = tableEdgeSensor.getDistanceCm() > 30;
				observations[2] = ! leftTableSensor.isOnTable();
				observations[3] = ! rightTableSensor.isOnTable();


				IntelliBrain.getLcdDisplay().print(1, "Read Sensors");
				// add the delta odometer reading to our pose,
				// treat the map locations as a gradiant
				// pose is then 

				float deltax = odometerPose.x - lastOdometerReading.x;
				float deltay = odometerPose.y - lastOdometerReading.y;
				float deltah = odometerPose.heading - lastOdometerReading.heading;
				if (deltah > Math.PI) deltah -= 2 * Math.PI;
				if (deltah < -Math.PI) deltah += 2 * Math.PI;				

				IntelliBrain.getLcdDisplay().print(1, "CalcTables... ");

				// build a lookup table to limit the amount of gaussian calculations we do
				float [] xGaussianLookup = new float[(map.getMaxX() - map.getMinX()) * 2];
				float offset = map.getMinX() - map.getMaxX() + (deltax / Project2b.MAPSCALE);
				calcGaussianLookupTable(xGaussianLookup, offset, xAxisVariance, xIota);

				float [] yGaussianLookup = new float[(map.getMaxY() - map.getMinY()) * 2];
				offset = map.getMinY() - map.getMaxY() + (deltay / Project2b.MAPSCALE);
				calcGaussianLookupTable(yGaussianLookup, offset, yAxisVariance, yIota);

				// this one's a little weird, since there's only half as many possibilities, due to the heading being circular
				float [] hGaussianLookup = new float[(map.getMaxH() - map.getMinH())];
				offset = ((map.getMinH() - map.getMaxH()) / 2) - (deltah / ((float)Math.PI / (map.getMaxH() / 2)));
				calcGaussianLookupTable(hGaussianLookup, offset, headingVariance, hIota);


				for (int iprime = map.getMinX(); iprime < map.getMaxX(); iprime ++) {
					for (int jprime = map.getMinY(); jprime < map.getMaxY(); jprime ++) {
						for (int kprime = map.getMinH(); kprime < map.getMaxH(); kprime ++) {
							IntelliBrain.getLcdDisplay().print(1, "k: " + kprime);

							// sum up the probability for all possible transitions given the action

							float sum = 0.0f;
							for (int i = map.getMinX(); i < map.getMaxX(); i ++) {
								for (int j = map.getMinY(); j < map.getMaxY(); j ++) {
									for (int k = map.getMinH(); k < map.getMaxH(); k ++) {
										sum += Transition(iprime, jprime, kprime, xGaussianLookup, yGaussianLookup, hGaussianLookup, i, j, k) * map.getPos(i, j, k); 

									}
								}
							}

							// set the probability of the current mapobj to the sum
							map.setNewPos(iprime, jprime, kprime, sum);

						}
					}
				}

				float largestSum = 0;
				int largesti = 0;
				int largestj = 0;
				int largestk = 0;

				for (int iprime = map.getMinX(); iprime < map.getMaxX(); iprime ++) {
					IntelliBrain.getLcdDisplay().print(0, "i: " + iprime);
					for (int jprime = map.getMinY(); jprime < map.getMaxY(); jprime ++) {
						for (int kprime = map.getMinH(); kprime < map.getMaxH(); kprime ++) {

							// sum up the probability for all possible observations given the action
							float sum = 0.0f;

							for (byte o = 0; o < observations.length; o ++) {
								for (int i = map.getMinX(); i < map.getMaxX(); i ++) {
									for (int j = map.getMinY(); j < map.getMaxY(); j ++) {
										for (int k = map.getMinH(); k < map.getMaxH(); k ++) {
											sum += Observation(o, observations[o], deltax, deltay, deltah, i, j, k) * map.getNewPos(i, j, k); 
										}
									}
								}
							}
							if (sum > largestSum) {
								largestSum = sum;
								largesti = iprime;
								largestj = jprime;
								largestk = kprime;

							}
							// set the probability of the current mapobj to the sum
							map.setNewPos(iprime, jprime, kprime, sum);

						}
					}
				}

				map.switchMaps();

				// update our believed position

				setPose(largestk * ((float)Math.PI /(map.getMaxH() / 2)), largesti, largestj);
				lastOdometerReading = odometerPose;
				
				IntelliBrain.getLcdDisplay().print(0, "Belief: " + largesti + ", " + largestj);
				IntelliBrain.getLcdDisplay().print(1, "Heading: " + largestk + ", " + map.getPos(largesti, largestj, largestk));

		}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			IntelliBrain.getLcdDisplay().print(1, "Err: " + e.getMessage());
		}
	}

	/**
	 * Returns the probability of ending up in position (tox, toy, toh) starting from (fromx, fromy, fromh) and performing (actionx, actiony, actionh) 
	 * 
	 * @param tox
	 * @param toy
	 * @param toh
	 * @param actionx
	 * @param actiony
	 * @param actionh
	 * @param fromx
	 * @param fromy
	 * @param fromh
	 * @return
	 */
	private float Transition(int tox, int toy, int toh, float [] xGaussianLookup, float [] yGaussianLookup, float [] hGaussianLookup, int fromx, int fromy, int fromh) {
		
		int headingDistance = toh - fromh;
		if (headingDistance >= 4) headingDistance -= 8;
		if (headingDistance < -4) headingDistance += 8;
		
		return xGaussianLookup[fromx - tox + (xGaussianLookup.length / 2)] + yGaussianLookup[fromy - toy + (yGaussianLookup.length / 2)] + hGaussianLookup[headingDistance + (hGaussianLookup.length / 2)];
		
	}
	
	/**
	 * Returns the probability of receiving an observation given an action and a starting state
	 * 
	 * @param observationNum
	 * 0 = Object Sensor triggered
	 * 1 = Front table edge sensor triggered
	 * 2 = Left Rear table edge sensor triggered
	 * 3 = Right rear table edge sensor triggered
	 *  
	 * @param received
	 * Has the sensor been triggered or not?
	 * @param actionx
	 * @param actiony
	 * @param actionh
	 * @param fromx
	 * @param fromy
	 * @param fromh
	 * @return
	 */
	private float Observation(byte observationNum, boolean received, float actionx, float actiony, float actionh, int fromx, int fromy, int fromh) {
		switch (observationNum) {
		case 0: // forward edge sensor
			if (received) {
				if (isLookingAtEdge(fromx, fromy, fromh))
					return .9f;
				return .1f;
			} else {
				if (isLookingAtEdge(fromx, fromy, fromh))
					return .1f;
				return .9f;
			}
		case 1: // object sensor
			if (received) {
				if (isLookingAtEdge(fromx, fromy, fromh))
					return .1f;
				return .6f - (.4f * percentDistanceFromCenter(fromx, fromy));
			} else {
				if (isLookingAtEdge(fromx, fromy, fromh))
					return .9f;
				return .2f + (.4f * percentDistanceFromCenter(fromx, fromy));
			}
		case 2: // left rear table sensor
			if (received) {
				if (isLeftAlignedToEdge(fromx, fromy, fromh))
					return .95f;
				return .01f;
				
			} else {
				if (isLeftAlignedToEdge(fromx, fromy, fromh))
					return .01f;
				return .95f;
				
			}
			
		case 3: // right rear table sensor
			if (received) {
				if (isRightAlignedToEdge(fromx, fromy, fromh))
					return .95f;
				return .01f;
				
			} else {
				if (isRightAlignedToEdge(fromx, fromy, fromh))
					return .01f;
				return .95f;
				
			}			
		}
		return 1.0f;
	}
	
	private float percentDistanceFromCenter(int x, int y) {
		float deltax = x - ((map.getMaxX() - map.getMinX()) / 2.0f);
		float deltay = y - ((map.getMaxY() - map.getMinY()) / 2.0f);
		
		int radius = (map.getMaxY() - map.getMinY()) / 2;
		if ((map.getMaxX() - map.getMinX())  / 2> radius)
			radius = (map.getMaxX() - map.getMinX()) / 2;
		
		return (deltax * deltax) + (deltay * deltay) / (float) (radius * radius);
		
	}
	
	private boolean isLeftAlignedToEdge(int x, int y, int h) {
		if (x == map.getMinX() && (h >=2 && h <= 4)) {
			return true;
		}
		if (x == map.getMaxX() && (h >=6 || h <= 0)) {
			return true;
		}
		if (y == map.getMinY() && (h >=0 && h <= 2)) {
			return true;
		}
		if (y == map.getMaxY() && (h >=4 && h <= 6)) {
			return true;
		}
		
		return false;
				
	}
	
	private boolean isRightAlignedToEdge(int x, int y, int h) {
		if (x == map.getMinX() && (h >=0 && h <= 2)) {
			return true;
		}
		if (x == map.getMaxX() && (h >=4 && h <= 6)) {
			return true;
		}
		if (y == map.getMinY() && (h <=0 || h >= 6)) {
			return true;
		}
		if (y == map.getMaxY() && (h >=2 && h <= 4)) {
			return true;
		}
		
		return false;
				
	}
	
	private boolean isLookingAtEdge(int x, int y, int h) {
		if (x == map.getMinX() && (h >=5 && h <= 7)) {
			return true;
		}
		if (x == map.getMaxX() && (h >=1 && h <= 3)) {
			return true;
		}
		if (y == map.getMinY() && (h >=3 && h <= 5)) {
			return true;
		}
		if (y == map.getMaxY() && (h <=1 || h >= 7)) {
			return true;
		}
		
		return false;
		
	}
	
	private void calcGaussianLookupTable(float [] table, float offset, float variance, float iota) {
		for (int i = 0; i < table.length; i ++) {
			table[i] = (float) Math.exp(-(offset + i) * (offset + i) / (2* variance)) / iota;			
		}
	}

	// these give the standard distribution squared for the transition between states for the 3 dimensions (x, y, and heading)
	private static final float xAxisVariance = 0.4f * 0.4f;
	private static final float yAxisVariance = 0.4f * 0.4f;
	private static final float headingVariance = 0.4f * 0.4f;
	
	private static final float xIota = (float)Math.sqrt(2 * Math.PI * xAxisVariance);
	private static final float yIota = (float)Math.sqrt(2 * Math.PI * yAxisVariance);
	private static final float hIota = (float)Math.sqrt(2 * Math.PI * headingVariance);
	
	
}
