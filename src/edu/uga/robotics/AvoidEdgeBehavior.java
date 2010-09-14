package edu.uga.robotics;

import josx.platform.rcx.LCD;
import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import josx.robotics.Behavior;

public class AvoidEdgeBehavior implements Behavior, SensorListener {

	private final int MOVE_BACKWARD_DISTANCE = -15;
	private final float TURN_DEGREES = 30F;
	
	private final int THRESHOLD = 31;
	private int lastvalue = 0;
	
	AvoidEdgeBehavior() {
		Sensor.S1.addSensorListener(this);
		
	}
	
	public void action() {
		Project2a.curState = Project2a.RobotState.Avoid;
		
		// backup a bit, then turn a little
		// move forward into endzone, then stop
		Project2a.navigator.stop();
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
		Project2a.navigator.travel(MOVE_BACKWARD_DISTANCE);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		Project2a.curState = Project2a.RobotState.Stopped;
	}

	public void suppress() {
		Project2a.navigator.stop();

	}

	public boolean takeControl() {
		return lastvalue >= THRESHOLD && (Project2a.curState == Project2a.RobotState.Forward || Project2a.curState == Project2a.RobotState.Avoid);
	}

	public void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {

		lastvalue = aNewValue;
		LCD.showNumber (aNewValue);
	}

}
