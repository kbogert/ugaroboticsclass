package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.platform.rcx.Sensor;
import josx.robotics.Behavior;

/**
 * High-priority behavior that causes the robot to stop and backup a little
 * from the detected object, after saving the light sensor's reading of the object. 
 * 
 * @author kbogert
 *
 */
public class BackupFromObjectBehavior implements Behavior {

	public static int lastSensorValue = 0;
	private boolean abort = false;
	
	public void action() {

		abort = false;
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.BackupFromObstacle;

		if (abort) return;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}
		if (abort) return;
		
		lastSensorValue = Sensor.S1.readValue(); // get a reading of the obstacle in front of us, saved for the next behavior
		Motor.A.setPower(2);
		Motor.B.setPower(2);

		Project2a.navigator.travel(-12); // amount to backup, provided from testing
		if (abort) return;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		if (abort) return;
		
		Project2a.curState = Project2a.RobotState.TurnFromObstacle;
	}

	public void suppress() {

		abort = true;
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;
		
	}

	/**
	 * To prevent random proximity events from activating this behavior, we limit them to once every .2 seconds
	 * and only when the robot is moving forward, avoiding an edge, or trying to move around an obstacle
	 */
	public boolean takeControl() {
		return Project2a.getCurTime() - 200 < Project2a.getLastProxEvent() && (Project2a.curState == Project2a.RobotState.Forward || Project2a.curState == Project2a.RobotState.AvoidEdge || Project2a.curState == Project2a.RobotState.MoveAroundObstacle);
	}

}
