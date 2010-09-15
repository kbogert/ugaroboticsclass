package edu.uga.robotics;

import josx.platform.rcx.LCD;
import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import josx.platform.rcx.TextLCD;
import josx.robotics.Behavior;

public class StopInEndzone implements Behavior, SensorListener {

	private final int MOVE_FORWARD_DISTANCE = 30;
	private final int THRESHOLD = 41;
	private int lastvalue = 0;
	
	StopInEndzone() {
		Sensor.S1.addSensorListener(this);
		
	}
	
	public void action() {

		if (Project2a.curState == Project2a.RobotState.Finished) 
			return;
		
		// move forward into endzone, then stop
		Project2a.curState = Project2a.RobotState.Finished;
		Project2a.navigator.stop();
		Project2a.navigator.travel(MOVE_FORWARD_DISTANCE);

		TextLCD.print("END");

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
		}

		LCD.showNumber(lastvalue);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		System.exit(0);
		
	}

	public void suppress() {
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Finished;
	}

	public synchronized boolean takeControl() {
		return lastvalue >= THRESHOLD && (Project2a.curState == Project2a.RobotState.Forward || Project2a.curState == Project2a.RobotState.Avoid);
	}

	public synchronized void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {

		lastvalue = aNewValue;
	
	}

}
