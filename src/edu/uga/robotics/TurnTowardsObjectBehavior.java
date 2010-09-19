package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;

public class TurnTowardsObjectBehavior implements Behavior {

	private final int blackThreshold = 39;
	
	public void action() {
		
		Project2a.navigator.stop();
		Motor.A.setPower(4);
		Motor.B.setPower(4);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (BackupFromObjectBehavior.lastSensorValue < blackThreshold) {
			
			// turn left
			Project2a.navigator.rotate(-45.0f);
			
		} else {
			// turn right
			Project2a.navigator.rotate(45.0f);
			
		}		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		
		Project2a.curState = Project2a.RobotState.Stopped;


	}

	public void suppress() {

	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.TurnTowardsObstacle;
	}

}
