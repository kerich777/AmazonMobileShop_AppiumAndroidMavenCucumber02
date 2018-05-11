package com.Utilities.General;




public class Timer {
	
	private long startTime;
	private long elapsedTime;
	private long timeout;
	private long defaultTimeout = 4000;
	
	public Timer() {
		this.startTime = System.currentTimeMillis();
		this.elapsedTime = this.startTime;
		this.timeout = this.defaultTimeout;
	}
	
	public boolean isDone() {
		this.elapsedTime = System.currentTimeMillis() - this.startTime;
		return (this.timeout - this.elapsedTime) < 0;
	}
	
	public void startTimer() {
		this.startTimer(0);
	}
	
	public void startTimer(long timeoutInMilliseconds) {
		this.resetTimer();
		this.startTime = System.currentTimeMillis();
		
		if(timeoutInMilliseconds != 0) {
			this.timeout = timeoutInMilliseconds;
		}else {
			this.timeout = this.defaultTimeout;
		}
	}
	
	public void startTimerInMinutes(int minutes) {
		
		if(minutes == 0) {
			minutes = 1;
		}
		
		this.resetTimer();
		this.startTime = System.currentTimeMillis();
		this.timeout = minutes * 60 * 1000;
	}
	
	public void startTimerInSeconds(int seconds) {
		if(seconds == 0) {
			seconds = 4;
		}
		
		this.resetTimer();
		this.startTime = System.currentTimeMillis();
		this.timeout = seconds * 1000;
	}
	
	public void resetTimer() {
		this.startTime = 0;
		this.timeout = 0;
		this.elapsedTime = 0;
	}
	
	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}