package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import josx.robotics.Behavior;

public class AvoidEdgeBehavior implements Behavior, SensorListener {

	// values provided during testing
	private final int MOVE_BACKWARD_DISTANCE = -15;  // amount to move backward after detecting the edge
	private final float TURN_DEGREES = 30F;	// amount to turn after moving backward
	
	private final int THRESHOLD = 40; // lower threshold for detecting the tape
	private final int topTHRESHOLD = 44;	// upper threshold for detecting the tape, needed to prevent an end-zone reading from triggering this behavior.
	private int lastvalue = 0;
	
	AvoidEdgeBehavior() {
		Sensor.S2.addSensorListener(this);
		
	}
	
	public void action() {
		Project2a.curState = Project2a.RobotState.AvoidEdge;
		
		// backup a bit, then turn a little
		Project2a.navigator.stop();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
		Motor.A.setPower(4);	// increase power to provide a "jerk" behavior, in case the sled is stuck on the tape
		Motor.B.setPower(4);
		Project2a.navigator.travel(MOVE_BACKWARD_DISTANCE);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		Motor.A.setPower(3);
		Motor.B.setPower(3);
		
		if (Project2a.navigator.getY() > 0) {	// ask the navigator if we're on the left side or right side of the obstacle course,
												// then turn appropriately.  Note that this is only a rough estimate (using a positive or negative
												// y-coordinate, so it's very probable that with enough error due to turns this test will be wrong)
			Project2a.navigator.rotate(-TURN_DEGREES);
		} else {
			Project2a.navigator.rotate(TURN_DEGREES);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		Motor.A.setPower(2);
		Motor.B.setPower(2);

		Project2a.curState = Project2a.RobotState.Stopped;
	}

	public void suppress() {
		Project2a.navigator.stop();

	}

	/**
	 * As the penalty for going out of the course is severe (falling robots gather no moss), we make sure that the only
	 * requirements for activating this behavior is a sensor reading within the threshholds
	 */
	public synchronized boolean takeControl() {
		return lastvalue >= THRESHOLD &&  lastvalue <= topTHRESHOLD;
	}

	public synchronized void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {
	
		lastvalue = aNewValue;
	}

}
