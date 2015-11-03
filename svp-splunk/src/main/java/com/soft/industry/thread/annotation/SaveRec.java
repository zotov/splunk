package com.soft.industry.thread.annotation;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.soft.industry.dao.InvoiceDao;



public abstract class SaveRec implements Runnable {
	
	protected   Logger log;
	protected   Logger logerror;
	
	protected final static AtomicLong transactionId = new AtomicLong(0);
	
    protected static Semaphore semaphore = new Semaphore(Semaphore.WAIT) ;
    
	private static boolean exit = false;
	
	protected long delay;
	
	public static long dDelay;
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	public void run() {
	
		log.info("start");
		try {
			  while(!semaphore.checkAction(Semaphore.START)) {
				  if(exit) {
					log.info(" exit = true ");
				  }
			  }	
			  
			  while(!semaphore.checkDealy(delay)) {
				  if(exit) {
					log.info(" exit = true ");
				  }
			  }
			  log.debug("delay:{}",delay);
			
			  //this.releasDelay();
			  saveInvoice();
			  			  
		} catch(Exception ex) {
			logerror.error("Error :{},{}",ex.getMessage(),ex.getCause(),ex);
		}
	}
	
		
	public static void releasAll() {
		if(semaphore !=null) {
		   semaphore.releaseAll();
		}
	}
	
	public static void releasDelay() {
		if(semaphore !=null) {
		   semaphore.releaseDelay();
		}
	}	
	
	public void setDelay(final long delay) {
		this.delay = delay;
	}
	
	public static void block() {
		semaphore.block();
	}
	
	abstract public void setBalance(final double balance);
	abstract public void saveInvoice();
}
