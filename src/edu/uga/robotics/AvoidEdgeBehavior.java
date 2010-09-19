package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import josx.robotics.Behavior;

public class AvoidEdgeBehavior implements Behavior, SensorListener {

	private final int MOVE_BACKWARD_DISTANCE = -15;
	private final float TURN_DEGREES = 30F;
	
	private final int THRESHOLD = 40;
	private final int topTHRESHOLD = 44;
	private int lastvalue = 0;
	
	AvoidEdgeBehavior() {
		Sensor.S2.addSensorListener(this);
		
	}
	
	public void action() {
		Project2a.curState = Project2a.RobotState.AvoidEdge;
		
		// backup a bit, then turn a little
		// move forward into endzone, then stop
		Project2a.navigator.stop();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
		Motor.A.setPower(2);
		Motor.B.setPower(2);
		Project2a.navigator.travel(MOVE_BACKWARD_DISTANCE);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		Motor.A.setPower(3);
		Motor.B.setPower(3);
		
		if (Project2a.navigator.getY() > 0) {
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

	public synchronized boolean takeControl() {
		return lastvalue >= THRESHOLD &&  lastvalue <= topTHRESHOLD;
	}

	public synchronized void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {
	
		lastvalue = aNewValue;
	}

}
