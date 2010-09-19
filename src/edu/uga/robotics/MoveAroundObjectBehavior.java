package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;

public class MoveAroundObjectBehavior implements Behavior {
	
	private boolean abort = false;
	
	public void action() {
		
		Motor.A.setPower(2);
		Motor.B.setPower(2);
		
		Project2a.navigator.travel(28);
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
