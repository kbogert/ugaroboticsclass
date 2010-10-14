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
				observations[0] = objectSensor.getDistanceCm() > 7;
				observations[1] = tableEdgeSensor.getDistanceCm() > 30;
				observations[2] = ! leftTableSensor.isOnTable();
				observations[3] = ! rightTableSensor.isOnTable();
				
				
				// add the delta odometer reading to our pose,
				// treat the map locations as a gradiant
				// pose is then 

				float deltax = odometerPose.x - lastOdometerReading.x;
				float deltay = odometerPose.y - lastOdometerReading.y;
				float deltah = odometerPose.heading - lastOdometerReading.heading;
				
				for (int iprime = map.getMinX(); iprime < map.getMaxX(); iprime ++) {
					for (int jprime = map.getMinY(); jprime < map.getMaxY(); jprime ++) {
						for (int kprime = map.getMinH(); kprime < map.getMaxH(); kprime ++) {
							
							// sum up the probability for all possible transitions given the action

							float sum = 0.0f;
							for (int i = map.getMinX(); i < map.getMaxX(); i ++) {
								for (int j = map.getMinY(); j < map.getMaxY(); j ++) {
									for (int k = map.getMinH(); k < map.getMaxH(); k ++) {
										sum += Transition(iprime, jprime, kprime, deltax, deltay, deltah, i, j, k) * map.getPos(i, j, k); 
										
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
				
				setPose(largestk * ((float)Math.PI /6.0f), largesti, largestj);
				
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
	private float Transition(int tox, int toy, int toh, float actionx, float actiony, float actionh, int fromx, int fromy, int fromh) {
		if (Math.abs(Math.abs(tox) - Math.abs(fromx)) <= 1 && Math.abs(Math.abs(toy) - Math.abs(fromy)) <= 1 && Math.abs(Math.abs(toh) - Math.abs(fromh)) <= 2) {
			
			/*
			 * The heading can vary based on distance travelled.  If toh = fromh but x and y does not, multiple by an uncertainty based on the distance travelled
			 * 
			 * Based on the heading, the given action x and y, it is more or less likely for the following:
			 * Move up - toy != fromy, and heading is 2,3,4
			 * Move down - toy != fromy, and heading is 8,9,10
			 * Move left - tox != fromx, and heading is 5,6,7
			 * Move right - tox != fromx, and heading is 1,0,11
			 * Move up-right - tox != fromx, toy != fromy, and heading is 0,1,2,3
			 * move up-left - tox != fromx, toy != fromy, and heading is 6,5,4,3
			 * move down-right - tox != fromx, toy != fromy, and heading is 6,7,8,9
			 * move down-left - tox != fromx, toy != fromy, and heading is 9,10,11,0
			 * 
			 */
			
			
		} else {
			/*
			 * The robot is assumed to stop before turning
			 * 
			 *  if tox == fromx and toy = fromy
			 *  	convert actionh from radians into our simple index
			 *  	assume that the robot can correctly turn in a direction, then if actionh is positive, and toh < fromh the probability is 0
			 *  	same for reverse
			 *  	otherwise, multiple actionh * the distance between toh and fromh * the probability of actually turning that much to get the new probability  
			 */  
		}
		// if the distance between the x and y's is greater than one, probability is zero
		
		// if the actionh is positive, and toh < fromh, return zero (except if we go over the zero point

		// TEMPORARY, TAKE THE ODOMETER AT FACE VALUE AS OUR LOCATION
		Pose pose = odometer.getPose();
		if (Project2b.MAPSCALE * tox >= pose.x && Project2b.MAPSCALE * (tox - 1) <= pose.x
				&& Project2b.MAPSCALE * toy >= pose.y && Project2b.MAPSCALE * (toy - 1) <= pose.x &&
				(float)(toh + 1) >= pose.heading / Math.PI / 4 && (float)toh <= pose.heading / Math.PI / 4 )
			return 1.0f;
		return 0.0f;
		
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
		
		}
		return 1.0f;
	}
}
