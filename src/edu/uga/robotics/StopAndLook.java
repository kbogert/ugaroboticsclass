package edu.uga.robotics;

import josx.robotics.Behavior;

public class StopAndLook implements Behavior {

	private boolean rotateDirection = false;
	private boolean abort = false;
	
	private final static float ROTATE_AMOUNT = 5.0f;
	
	public void action() {
		
		Project2a.curState = Project2a.RobotState.Scan;
		Project2a.navigator.stop();
		abort = false;
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;

		rotateDirection = ! rotateDirection;
		Project2a.navigator.rotate(rotateDirection ? -ROTATE_AMOUNT : ROTATE_AMOUNT);

		if (abort) return;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;
		
		Project2a.navigator.rotate(rotateDirection ? ROTATE_AMOUNT*2 : -ROTATE_AMOUNT*2);
		
		if (abort) return;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;

		Project2a.navigator.rotate(rotateDirection ? ROTATE_AMOUNT : -ROTATE_AMOUNT);
		
		if (abort) return;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;
		
		Project2a.curState = Project2a.RobotState.Stopped;

	}

	public void suppress() {
		abort = true;
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;

	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Forward && Project2a.getCurTime() - 500 > MoveForward.startTime;
	}

}
