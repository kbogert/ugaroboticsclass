package edu.uga.robotics;

import josx.platform.rcx.Sensor;

public class EventMgr {

	private boolean hasEvent = false;
	private long eventTime = 0l;
	
	public synchronized void setEvent() {
		hasEvent = true;
		eventTime = System.currentTimeMillis();
	}
	
	public synchronized void clearEvent() {
		hasEvent = false;
	}
	
	public synchronized boolean getEvent() {
		return hasEvent && (System.currentTimeMillis() - eventTime < 100);
	}
}
