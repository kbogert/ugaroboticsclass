package edu.uga.robotics;

import josx.robotics.Behavior;

public class StopAndLook implements Behavior {

	private boolean rotateDirection = false;
	
	
	public void action() {
		
		Project2a.navigator.stop();
		rotateDirection = ! rotateDirection;
		Project2a.navigator.rotate(rotateDirection ? -10f : 10f);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		
		Project2a.navigator.rotate(rotateDirection ? 20f : -20f);
		
		Project2a.curState = Project2a.RobotState.Stopped;

	}

	public void suppress() {

	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Scan;
	}

}
