package edu.uga.robotics;

import josx.robotics.Behavior;
import josx.util.Timer;
import josx.util.TimerListener;

public class MoveForward implements Behavior, TimerListener {

	private boolean makeTurn;
	private boolean turnSwitch;
	
	MoveForward() {
		Timer timer = new Timer(500, this);
		timer.start();
	}
	
	public void action() {

		if (Project2a.curState == Project2a.RobotState.Forward) {
			if (makeTurn) {
				makeTurn = false;
				turnSwitch = !turnSwitch;
				Project2a.navigator.rotate(turnSwitch? -10f : 10f);

				Project2a.navigator.forward();
			}
			return;
		}
		Project2a.navigator.gotoAngle(0f);
		Project2a.navigator.forward();
		Project2a.curState = Project2a.RobotState.Forward;
	}

	public void suppress() {
		// TODO Auto-generated method stub
		Project2a.navigator.stop();
		Project2a.curState = Project2a.RobotState.Stopped;

	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Stopped || Project2a.curState == Project2a.RobotState.Forward;
	}

	public void timedOut() {

		makeTurn = true;
	}

}
