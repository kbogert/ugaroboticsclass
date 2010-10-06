package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;

/**
 * After moving around a detected object, we turn back toward the endzone before allowing
 * the MoveForward behavior to have control again.
 * 
 * @author kbogert
 *
 */
public class TurnTowardsObjectBehavior implements Behavior {

	private final int blackThreshold = 39; // provided from testing
	
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
