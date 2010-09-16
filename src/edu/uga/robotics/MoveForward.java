package edu.uga.robotics;

import josx.robotics.Behavior;

public class MoveForward implements Behavior {

	private EventMgr event;
	
	public MoveForward(EventMgr event) {
		this.event = event;
	}
	
	public void action() {

		try {
			while (true) {
				int startTime = event.getCurTime();
				
				Project2a.curState = Project2a.RobotState.Forward;
				Project2a.navigator.forward();
				while (startTime - event.getCurTime() < 500) {
					if (event.getEvent())
						return;
					Thread.sleep(10);
				}
				
				Project2a.navigator.stop();
				Project2a.curState = Project2a.RobotState.Scan;
				
				Thread.sleep(100);
				
				if (event.getEvent())
					return;

				Project2a.navigator.rotate(-10f);
				if (event.getEvent())
					return;

				Thread.sleep(100);
				if (event.getEvent())
					return;
				
				Project2a.navigator.rotate(20f);
				if (event.getEvent())
					return;
				
				Thread.sleep(100);
				if (event.getEvent())
					return;
				
				Project2a.navigator.rotate(-10f);
								
				if (event.getEvent())
					return;

				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
		}
	}

	public void suppress() {

		Project2a.navigator.stop();
	}

	public boolean takeControl() {
		return Project2a.curState == Project2a.RobotState.Stopped && ! event.getEvent();
	}

}
