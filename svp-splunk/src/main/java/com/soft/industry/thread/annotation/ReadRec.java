package com.soft.industry.thread.annotation;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;

public abstract class ReadRec implements Runnable {
	
	protected   Logger log;
	protected   Logger logerror;
	
	protected final static AtomicLong transactionId = new AtomicLong(0);
	
    protected static Semaphore semaphore ;
    
	private static boolean exit = false;
	
	protected Invoice invoice = null;
	protected boolean readUpdatedInvoice =false;
		
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
			 		 
			  readInvoice();		  
		} catch(Exception ex) {
			logerror.error("Error :{},{}",ex.getMessage(),ex.getCause(),ex);
		}
	}
	
		
	public static void releasAll() {
		if(semaphore !=null) {
		  semaphore.releaseAll();
		}
	}
	
	public static void block() {
		if(semaphore !=null) {
		  semaphore.block();;
		}
	}
	
	public static void releasDelay() {
		if(semaphore !=null) {
		   semaphore.releaseDelay();
		}
	}	
			
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public boolean isReadUpdatedInvoice() {
		return readUpdatedInvoice;
	}

	public void setReadUpdatedInvoice(boolean readUpdatedInvoice) {
		this.readUpdatedInvoice = readUpdatedInvoice;
	}

	abstract public void readInvoice();
}