package edu.uga.robotics;


public class EventMgr {

	private boolean hasEvent = false;
	private int eventTime = 0;

	public synchronized void setEvent() {
		hasEvent = true;
		eventTime = Project2a.getCurTime();
	}
	
	public synchronized void clearEvent() {
		hasEvent = false;
	}
	
	public synchronized boolean getEvent() {
		return hasEvent && (Project2a.getCurTime() - eventTime < 250);
	}

}
