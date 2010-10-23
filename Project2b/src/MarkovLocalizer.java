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
		
		try {
			while (true) {
				Thread.sleep(30);

				Pose odometerPose = odometer.getPose();
				boolean [] observations = new boolean[4];
				observations[0] = objectSensor.getDistanceCm() > 7 && objectSensor.getDistanceCm() < 91;
				observations[1] = tableEdgeSensor.getDistanceCm() > 30;
				observations[2] = ! leftTableSensor.isOnTable();
				observations[3] = ! rightTableSensor.isOnTable();
				
				
				// add the delta odometer reading to our pose,
				// treat the map locations as a gradiant
				// pose is then 

				float deltax = odometerPose.x - lastOdometerReading.x;
				float deltay = odometerPose.y - lastOdometerReading.y;
				float deltah = odometerPose.heading - lastOdometerReading.heading;
				
				
				// build a lookup table to limit the amount of gaussian calculations we do
				float [] xGaussianLookup = new float[(map.getMaxX() - map.getMinX()) * 2];
				float offset = map.getMinX() - map.getMaxX() + (deltax / Project2b.MAPSCALE);
				calcGaussianLookupTable(xGaussianLookup, offset, xAxisVariance, xIota);

				float [] yGaussianLookup = new float[(map.getMaxX() - map.getMinX()) * 2];
				offset = map.getMinY() - map.getMaxY() + (deltay / Project2b.MAPSCALE);
				calcGaussianLookupTable(yGaussianLookup, offset, yAxisVariance, yIota);

				float [] hGaussianLookup = new float[(map.getMaxH() - map.getMinH()) * 2];
				offset = map.getMinH() - map.getMaxH() + (deltah / ((float)Math.PI / map.getMaxH() / 2));
				calcGaussianLookupTable(hGaussianLookup, offset, headingVariance, hIota);
				
				
				for (int iprime = map.getMinX(); iprime < map.getMaxX(); iprime ++) {
					for (int jprime = map.getMinY(); jprime < map.getMaxY(); jprime ++) {
						for (int kprime = map.getMinH(); kprime < map.getMaxH(); kprime ++) {
							
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
				
			}
		} catch (InterruptedException e) {
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
		
		return xGaussianLookup[fromx - tox + (xGaussianLookup.length / 2)] * yGaussianLookup[fromy - toy + (yGaussianLookup.length / 2)] * hGaussianLookup[fromh - toh + (hGaussianLookup.length / 2)];
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
		case 0:
			if (received) {
				// if moved towards an edge, high probability, otherwise low
			} else {
				// if moved away from an edge, high probability, otherwise low
				
			}
			break;
		}
		return 1.0f;
	}
	
	private void calcGaussianLookupTable(float [] table, float offset, float variance, float iota) {
		for (int i = 1; i < table.length; i ++) {
			table[i] = (float) Math.exp(-(offset + i) * (offset + i) / (2* xAxisVariance)) / xIota;			
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
