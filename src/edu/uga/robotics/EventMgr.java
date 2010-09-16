package edu.uga.robotics;


public class EventMgr {

	private boolean hasEvent = false;
	private int eventTime = 0;
	private int curTime = 0;
	
	public EventMgr() {
		TimerHack timer = new TimerHack(this);
		timer.start();
	}
	
	public synchronized void setEvent() {
		hasEvent = true;
		eventTime = getCurTime();
	}
	
	public synchronized void clearEvent() {
		hasEvent = false;
	}
	
	public synchronized boolean getEvent() {
		return hasEvent && (getCurTime() - eventTime < 100);
	}
	
	public synchronized int getCurTime() {
		return curTime;
	}
	
	public class TimerHack extends Thread {

		private EventMgr parent;
	
		TimerHack(EventMgr e) {
			parent = e;
		}
		
		public void run() {
			while(true) {
				try {
					Thread.sleep(1);
					synchronized(parent) {
						curTime += 1;
					}
				} catch (InterruptedException e) {

				}
				
				
			}
		}
		
	}
}
