package edu.uga.robotics;

import josx.platform.rcx.Sensor;

public class EventMgr {

	private boolean hasEvent = false;
	private long eventTime = 0l;
	private EventMgr event;
	
	EventMgr(EventMgr event) {
		this.event = event;
	}
	
	public void setEvent() {
		hasEvent = true;
		eventTime = System.currentTimeMillis();
	}
	
	public void clearEvent() {
		hasEvent = false;
	}
	
	public boolean getEvent() {
		return hasEvent && (System.currentTimeMillis() - eventTime < 100);
	}
}
