package com.soft.industry.thread.annotation;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;

/**
 * 
 * @author denis
 *
 */
public abstract class UpdateRec implements Runnable {
	
	protected   Logger log;
	protected   Logger logerror;
	
	protected final static AtomicLong transactionId = new AtomicLong(0);
	
    protected static Semaphore semaphore ;
    
	private static boolean exit = false;
	
	protected long delay;
	
	public static long dDelay;
	
	private int  entityId;
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	public void run() {
	
		log.info("start:"+delay);
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
			  updateInvoice();
			  			  
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
	
	public static void block() {
		if(semaphore !=null) {
			semaphore.block();
		}
	}
	
	public void setDelay(final long delay) {
		this.delay = delay;
	}
		
	public int getEntityId() {
		return entityId;
	}


	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	abstract public void setBalance(final double balance);
	abstract public void updateInvoice();
	abstract public void setInvoice(final Invoice invoice);
}
