package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;
/**
 * 
 * Part of the more complex "avoid obstacle behavior", this 
 * behavior is triggered after making a decision as to which way to move around
 * a detected object.  
 * 
 * @author kbogert
 *
 */
public class MoveAroundObjectBehavior implements Behavior {
	
	private boolean abort = false;
	
	public void action() {
		abort = false;
		
		Motor.A.setPower(2);
		Motor.B.setPower(2);
		
		Project2a.navigator.travel(28); // value provided from testing
		if (abort) return;
		
		Project2a.curState = Project2a.RobotState.TurnTowardsObstacle;
	}

	public void suppress() {
		abort = true;
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;
	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.MoveAroundObstacle;
	}

}
