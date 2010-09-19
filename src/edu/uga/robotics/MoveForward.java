package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;

public class MoveForward implements Behavior {

	public static int startTime = 0;
	
	public void action() {
		startTime = Project2a.getCurTime();
		
		Motor.A.setPower(1);
		Motor.B.setPower(1);
		
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
