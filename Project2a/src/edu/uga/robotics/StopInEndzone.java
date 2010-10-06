package edu.uga.robotics;

import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import josx.platform.rcx.Sound;
import josx.platform.rcx.TextLCD;
import josx.robotics.Behavior;

/**
 * Simple behavior to turn off the robot if we detect the 
 * end zone conditions.
 * 
 * @author kbogert
 *
 */
public class StopInEndzone implements Behavior, SensorListener {

	private final int THRESHOLD = 45; // provided from testing
	private int lastvalue = 0;
	
	StopInEndzone() {
		Sensor.S2.addSensorListener(this);
		
	}
	
	public void action() {

		if (Project2a.curState == Project2a.RobotState.Finished) 
			return;
		
		Project2a.curState = Project2a.RobotState.Finished;
		Project2a.navigator.stop();

		Sound.beep();
		TextLCD.print("END");

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
		}

		System.exit(0);
		
	}

	public void suppress() {
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Finished;
	}

	/**
	 * To prevent false positives, only allow the robot to stop if it's in one of the following states:
	 * Moving Forward, Avoiding an edge, Scanning, or Moving around an obstacle.
	 */
	public synchronized boolean takeControl() {
		return lastvalue >= THRESHOLD && (Project2a.curState == Project2a.RobotState.Forward || Project2a.curState == Project2a.RobotState.AvoidEdge  || Project2a.curState == Project2a.RobotState.Scan || Project2a.curState == Project2a.RobotState.MoveAroundObstacle);
	}

	public synchronized void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {

		lastvalue = aNewValue;
	
	}

}
