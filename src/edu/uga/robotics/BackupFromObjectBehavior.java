package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.platform.rcx.Sensor;
import josx.robotics.Behavior;

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
		
		lastSensorValue = Sensor.S1.readValue();
		Motor.A.setPower(2);
		Motor.B.setPower(2);

		Project2a.navigator.travel(-12);
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

	public boolean takeControl() {
		return Project2a.getCurTime() - 200 < Project2a.getLastProxEvent() && (Project2a.curState == Project2a.RobotState.Forward || Project2a.curState == Project2a.RobotState.AvoidEdge || Project2a.curState == Project2a.RobotState.MoveAroundObstacle);
	}

}
