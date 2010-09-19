package edu.uga.robotics;

import josx.robotics.Behavior;

public class MoveForward implements Behavior {

	private EventMgr event;
	
	public MoveForward(EventMgr event) {
		this.event = event;
	}

	public static int startTime = 0;
	
	public void action() {
		startTime = event.getCurTime();
		
		Project2a.curState = Project2a.RobotState.Forward;
		Project2a.navigator.forward();

	}

	public void suppress() {

		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;
	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Stopped && ! event.getEvent();
	}

}
