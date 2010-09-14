package edu.uga.robotics;

import josx.platform.rcx.LCD;
import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import josx.robotics.Behavior;

public class AvoidObstacleBehavior implements Behavior, SensorListener {

	private boolean isNearObject = false;
	
	AvoidObstacleBehavior() {
		new Thread(new AvoidObstacleThread(this));
	}
	
	public void action() {

		// move in a little closer,
		// get a reading from the sensor
		// Decide which way to go
		// backup a little, turn 50 degrees
		// move forward for a ways (need to setup another thread, so this doesn't block and we still check for obstacles with the prox sensor)
		// let the MoveForward behavior have control
	}

	public void suppress() {
		// TODO Auto-generated method stub

	}

	public boolean takeControl() {
		synchronized (this) {
			return isNearObject;
		}
	}

	public void stateChanged(Sensor arg0, int arg1, int arg2) {


		LCD.showNumber (arg2);
	}

	public class AvoidObstacleThread implements Runnable {
		AvoidObstacleBehavior parent;
		
		AvoidObstacleThread(AvoidObstacleBehavior a) {
			parent = a;
		}
		
		public void run() {
			synchronized(parent) {
				parent.isNearObject = true;
			}
			
		}
		
	}
}
