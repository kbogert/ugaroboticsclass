package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;

public class TurnFromObjectBehavior implements Behavior {

	private final int blackThreshold = 39;
	private boolean abort = false;
	
	public void action() {


		Motor.A.setPower(4);
		Motor.B.setPower(4);

		if (BackupFromObjectBehavior.lastSensorValue < blackThreshold) {
	
			// turn left
			Project2a.navigator.rotate(45.0f);
			
		} else {
			// turn right
			Project2a.navigator.rotate(-45.0f);
			
		}
		
		if (abort) return;
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {

		}
		if (abort) return;
		
		Project2a.curState = Project2a.RobotState.MoveAroundObstacle;
	}

	public void suppress() {
		abort = true;
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;

	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.TurnFromObstacle;
	}

}
