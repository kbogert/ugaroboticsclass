package edu.uga.robotics;

import josx.platform.rcx.TextLCD;
import josx.robotics.Behavior;

public class AvoidObstacleBehavior implements Behavior {

	private boolean isNearObject = false;
	
	AvoidObstacleBehavior() {
		Thread temp = new AvoidObstacleThread(this);
//		temp.setDaemon(true);
		temp.start();
	}
	
	public void action() {

		// move in a little closer,
		// get a reading from the sensor
		// Decide which way to go
		// backup a little, turn 50 degrees
		// move forward for a ways (need to setup another thread, so this doesn't block and we still check for obstacles with the prox sensor)
		// let the MoveForward behavior have control

		TextLCD.print("AVOID");
		Project2a.navigator.stop();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
		System.exit(0);

	}

	public void suppress() {
		// TODO Auto-generated method stub

	}

	public boolean takeControl() {
		synchronized (this) {
			return isNearObject;
		}
	}
	
	public class AvoidObstacleThread extends Thread {
		AvoidObstacleBehavior parent;
		
		AvoidObstacleThread(AvoidObstacleBehavior a) {
			parent = a;
		}
		
		public void run() {
			while (true) {
				try {
					Project2a.proxSensor.waitTillNear(1000);
					synchronized(parent) {
						parent.isNearObject = true;
					}
				} catch (InterruptedException e) {

				}
			}
		}
		
	}
}
