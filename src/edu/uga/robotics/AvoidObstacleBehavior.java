package edu.uga.robotics;

import josx.robotics.Behavior;

public class AvoidObstacleBehavior implements Behavior {

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
		// TODO Auto-generated method stub
		return false;
	}

}
