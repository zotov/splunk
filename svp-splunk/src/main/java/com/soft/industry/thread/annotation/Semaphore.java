package com.soft.industry.thread.annotation;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
/**
 * 
 * @author denis
 *
 */
public class Semaphore {
	
	private static final Logger log = LoggerFactory.getLogger(Semaphore.class);
		
	public static final String START = "START";
	public static final String WAIT = "WAIT";
		
	
	static int timeout;
		
	static long dDelay;
	
	private String  action;
	
	private static AtomicLong maxDealy = new AtomicLong(0);
	
	public Semaphore(final String action) {
		this.action = action;
	}
	
	public synchronized boolean checkAction(final String action) {
		
		   if(this.action.equals(action)) {
			   return true;
		   }
		   try {
				log.debug("start wait:{},",action); 
				this.wait(timeout);
				log.debug("end wait:{}",action);
			} catch (InterruptedException e) {
				log.error("Error, wait:{}",action);
			};
			return false;
	}
	
	public synchronized boolean checkDealy(final long delay) {
		
		   if(maxDealy.get() == delay) {
			  log.debug("maxDelay:{}",maxDealy);
			  return true;
		   }
		   
		   try {
				log.debug("start wait delay:{},maxDelay:{}",delay,maxDealy.get()); 
				this.wait(timeout);
				log.debug("end wait delay:{},maxDelay:{}",delay,maxDealy.get());
			} catch (InterruptedException e) {
				log.error("Error, wait:{},maxDelay:{}",delay,maxDealy.get());
			};
			
			return false;
	}
	
	public static void decrement(final long delay) {
		  maxDealy.compareAndSet(delay, delay-dDelay);
	}
	
	public synchronized void releaseAll() {
		action = START;
		this.notifyAll();
	}
	
	public synchronized void releaseDelay() {
		this.notifyAll();
	}
	
	public synchronized void block() {
		action = WAIT;
	}
	
	public static void setMaxDelay(final long maxDelay) {
		maxDealy.set(maxDelay);
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		Semaphore.timeout = timeout;
	}

	public static long getdDelay() {
		return dDelay;
	}

	public static void setdDelay(long dDelay) {
		Semaphore.dDelay = dDelay;
	}
	
}
