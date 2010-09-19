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
		public static final byte Scan = 4;
		public static final byte PickDirection = 5;
		public static final byte AvoidEdge = 2;
		public static final byte Finished = 3;
		public static final byte BackupFromObstacle = 6;
		public static final byte TurnFromObstacle = 7;
		public static final byte MoveAroundObstacle = 8;
		public static final byte TurnTowardsObstacle = 9;
		
		
	}
	
	public static byte curState;
	
	public static ProximitySensor proxSensor;
	public static TimingNavigator navigator;
	
	private static int lastProxEvent;
	
	public static synchronized int getLastProxEvent() {
		return lastProxEvent;
	}
	
	protected static synchronized void setLastProxEvent() {
		lastProxEvent = getCurTime();
	}
	
	public static ProximitySensor getProxSensor() {
		return proxSensor;
	}
	
	public static int getCurTime() {
		return (int)System.currentTimeMillis();

	}
	
	private static void initializeSensors() {
		proxSensor = new ProximitySensor(Sensor.S1);
		Thread temp = new Project2a.AvoidObstacleThread();
//		temp.setDaemon(true);
		temp.start();

		Sensor.S2.setTypeAndMode(3, 0x80); // light sensor, percentage mode
		Sensor.S2.activate();
	}
	
	public static void main(String[] args) {

		EventMgr eventMgr = new EventMgr();
		
		curState = RobotState.Stopped;
		Motor.A.setPower(2);
		Motor.B.setPower(2);
		navigator = new TimingNavigator(Motor.B,Motor.A, 2.5f,1.3f);
//		navigator.setMomentumDelay(1);
		
		initializeSensors();
		
		Behavior [] behaviors = new Behavior[5];
		
		behaviors[0] = new MoveForward(eventMgr);
		behaviors[1] = new StopAndLook();		
		behaviors[2] = new StopInEndzone(eventMgr);
		behaviors[3] = new AvoidObstacleBehavior(eventMgr);
		behaviors[4] = new AvoidEdgeBehavior(eventMgr);
		
		Arbitrator arb = new Arbitrator(behaviors);
		arb.start();
		
		
	}
	
	
	private static class AvoidObstacleThread extends Thread {
		
		public void run() {
			while (true) {
				try {
					Project2a.proxSensor.waitTillNear(0);

					Project2a.setLastProxEvent();

				} catch (InterruptedException e) {

				}
			}
		}
		
	}
	

}
