package edu.uga.robotics;

import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import josx.robotics.Behavior;

public class AvoidEdgeBehavior implements Behavior, SensorListener {

	private final int MOVE_BACKWARD_DISTANCE = -20;
	private final float TURN_DEGREES = 30F;
	
	private final int THRESHOLD = 40;
	private final int topTHRESHOLD = 44;
	private int curtime = 0;
	
	private EventMgr event;
	
	AvoidEdgeBehavior(EventMgr event) {
		Sensor.S2.addSensorListener(this);
		this.event = event;
	}
	
	public void action() {
		Project2a.curState = Project2a.RobotState.Avoid;
		
		// backup a bit, then turn a little
		// move forward into endzone, then stop
		Project2a.navigator.stop();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
		
		Project2a.navigator.travel(MOVE_BACKWARD_DISTANCE);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		
		if (Project2a.navigator.getY() < 0) {
			Project2a.navigator.rotate(-TURN_DEGREES);
		} else {
			Project2a.navigator.rotate(TURN_DEGREES);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		Project2a.curState = Project2a.RobotState.Stopped;
	}

	public void suppress() {
		Project2a.navigator.stop();

	}

	public synchronized boolean takeControl() {
		return curtime - event.getCurTime() < 100;
	}

	public synchronized void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {
	
		if (aNewValue >= THRESHOLD &&  aNewValue <= topTHRESHOLD) {

			curtime = event.getCurTime();
			event.setEvent();
		}
	}

}
