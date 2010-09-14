package edu.uga.robotics;

import josx.robotics.Behavior;

public class MoveForward implements Behavior {

	public void action() {

		if (Project2a.curState == Project2a.RobotState.Forward) {
			return;
		}
		Project2a.navigator.gotoAngle(0f);
		Project2a.navigator.forward();
		Project2a.curState = Project2a.RobotState.Forward;
	}

	public void suppress() {
		// TODO Auto-generated method stub
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;

	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Stopped || Project2a.curState == Project2a.RobotState.Forward;
	}

}
