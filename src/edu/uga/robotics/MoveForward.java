package edu.uga.robotics;

import josx.robotics.Behavior;

public class MoveForward implements Behavior {

	private EventMgr event;
	
	public MoveForward(EventMgr event) {
		this.event = event;
	}
	
	public void action() {

		Project2a.navigator.forward();
		Project2a.curState = Project2a.RobotState.Scan;
	}

	public void suppress() {
		// TODO Auto-generated method stub

	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Stopped;
	}

}
