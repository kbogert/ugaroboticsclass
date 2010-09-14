package edu.uga.robotics;

import josx.platform.rcx.Motor;
import josx.platform.rcx.ProximitySensor;
import josx.platform.rcx.Sensor;
import josx.robotics.Arbitrator;
import josx.robotics.Behavior;
import josx.robotics.TimingNavigator;


public class Project2a {
	
	public class RobotState {
	
		public static final byte Stopped = 0;
		public static final byte Forward = 1;
		public static final byte Avoid = 2;
		public static final byte Finished = 3;
		
	}
	
	public static byte curState;
	
	public static ProximitySensor proxSensor;
	public static TimingNavigator navigator;
	
	public static ProximitySensor getProxSensor() {
		return proxSensor;
	}

	private static void initializeSensors() {
		proxSensor = new ProximitySensor(Sensor.S1);
		
		Sensor.S2.setTypeAndMode(3, 0x80); // light sensor, percentage mode
		Sensor.S2.activate();
	}
	
	public static void main(String[] args) {

		curState = RobotState.Stopped;
		Motor.A.setPower(2);
		Motor.B.setPower(2);
		navigator = new TimingNavigator(Motor.B,Motor.A, 2.5f,1.3f);
//		navigator.setMomentumDelay(1);
		
		initializeSensors();
		
		Behavior [] behaviors = new Behavior[4];
		
		behaviors[0] = new MoveForward();
		behaviors[1] = new StopInEndzone();
		behaviors[2] = new AvoidObstacleBehavior();
		behaviors[3] = new AvoidEdgeBehavior();
		
		Arbitrator arb = new Arbitrator(behaviors);
		arb.start();
		
		
	}
	

}
