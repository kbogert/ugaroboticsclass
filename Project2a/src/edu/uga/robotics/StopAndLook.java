package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.robotics.Behavior;

/**
 * 
 * This behavior activates after a set amount of time of moving forward
 * It's purpose is to scan ahead to the left and right to make sure there
 * are no obstacles out of range of the proximity sensor that we could still
 * run in to.
 * 
 * If an obstacle is detected, a minor course correction is performed to avoid it.
 * 
 * @author kbogert
 *
 */
public class StopAndLook implements Behavior {

	private boolean rotateDirection = false;
	private boolean abort = false;
	
	private final static float ROTATE_AMOUNT = 10.0f; // provided from testing
	
	public void action() {
		
		Project2a.curState = Project2a.RobotState.Scan;
		Project2a.navigator.stop();
		abort = false;
		boolean obstacleLeft = false;
		boolean obstacleRight = false;
		Motor.A.setPower(3);
		Motor.B.setPower(3);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;

		// After stopping, we move the robot in one direction ...
		int startTime = Project2a.getCurTime();
		rotateDirection = ! rotateDirection;
		Project2a.navigator.rotate(rotateDirection ? -ROTATE_AMOUNT : ROTATE_AMOUNT);

		if (abort) return;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;
		// and record if an object is detected during the sweep...
		if (Project2a.getLastProxEvent() > startTime) {
			obstacleLeft = true;
		}
		
		// then rotate back, we'll end up looking a little in the other direction from the original position 
		Project2a.navigator.rotate(rotateDirection ? ROTATE_AMOUNT*2 : -ROTATE_AMOUNT*2);
		
		if (abort) return;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;

		// now rotate back to the original position
		startTime = Project2a.getCurTime();
		Project2a.navigator.rotate(rotateDirection ? -ROTATE_AMOUNT : ROTATE_AMOUNT);
		
		if (abort) return;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;
		// we record only detections on the way back, since if we did it earlier, it's possible
		// that an obstacle on the first side we look at would be recorded as on the second 
		if (Project2a.getLastProxEvent() > startTime) {
			obstacleRight = true;
		}
		
		// make a minor course correction, note if no obstacles or obstacles on both sides we go straight
		// this is the best behavior, as we technically have not come in contact with them yet.
		if (obstacleLeft && ! obstacleRight) {
			// obstacle on the left, turn right
			Project2a.navigator.rotate(-ROTATE_AMOUNT);
			
		} else if (obstacleRight && ! obstacleLeft) {
			// obstacle on the right, turn left
			Project2a.navigator.rotate(ROTATE_AMOUNT);
		}
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

	/**
	 * Activate a certain amount of time after we've been moving forward
	 */
	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Forward && Project2a.getCurTime() - 500 > MoveForward.startTime;
	}

}
