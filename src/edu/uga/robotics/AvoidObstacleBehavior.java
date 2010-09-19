package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.platform.rcx.Sensor;
import josx.robotics.Behavior;

public class AvoidObstacleBehavior implements Behavior {

	private boolean isNearObject = false;
	private final int blackThreshold = 39;
	private final int whiteThreshold = 49;
	
	public void action() {

		// move in a little closer,
		// get a reading from the sensor
		// Decide which way to go
		// backup a little, turn 50 degrees
		// move forward for a ways (need to setup another thread, so this doesn't block and we still check for obstacles with the prox sensor)
		// let the MoveForward behavior have control

		Project2a.curState = Project2a.RobotState.BackupFromObstacle;

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		int sensorValue = Sensor.S1.readValue();
		Motor.A.setPower(2);
		Motor.B.setPower(2);

		Project2a.navigator.travel(-12);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}

		Motor.A.setPower(4);
		Motor.B.setPower(4);

		if (sensorValue < blackThreshold) {
	
			// turn left
			Project2a.navigator.rotate(45.0f);
			
		} else {
			// turn right
			Project2a.navigator.rotate(-45.0f);
			
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		Motor.A.setPower(2);
		Motor.B.setPower(2);
		
		Project2a.navigator.travel(28);
		Motor.A.setPower(4);
		Motor.B.setPower(4);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

		}
		Motor.A.setPower(3);
		Motor.B.setPower(3);
		if (sensorValue < blackThreshold) {
			
			// turn left
			Project2a.navigator.rotate(-45.0f);
			
		} else {
			// turn right
			Project2a.navigator.rotate(45.0f);
			
		}		Project2a.curState = Project2a.RobotState.Stopped;

		Motor.A.setPower(2);
		Motor.B.setPower(2);

		synchronized (this) {
			isNearObject = false;
		}

	}

	public void suppress() {
		// TODO Auto-generated method stub

	}

	public boolean takeControl() {
		synchronized (this) {
			return isNearObject;
		}
	}

}
