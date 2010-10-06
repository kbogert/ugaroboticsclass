package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;

/**
 * Simple behavior that sets the robot moving forward and then returns (call is async)
 * Should have the least priority
 * 
 * @author kbogert
 *
 */
public class MoveForward implements Behavior {

	public static int startTime = 0;
	
	public void action() {
		startTime = Project2a.getCurTime();
		
		Motor.A.setPower(3);
		Motor.B.setPower(3);
		
		Project2a.curState = Project2a.RobotState.Forward;
		Project2a.navigator.forward();

	}

	public void suppress() {

		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;
	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Stopped;
	}

}
