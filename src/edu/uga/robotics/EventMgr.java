package edu.uga.robotics;


public class EventMgr {

	private boolean hasEvent = false;
	private int eventTime = 0;

	public synchronized void setEvent() {
		hasEvent = true;
		eventTime = getCurTime();
	}
	
	public synchronized void clearEvent() {
		hasEvent = false;
	}
	
	public synchronized boolean getEvent() {
		return hasEvent && (getCurTime() - eventTime < 250);
	}
	
	public int getCurTime() {
		return (int)System.currentTimeMillis();

	}
}
