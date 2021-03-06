package com.soft.industry.thread.manual.read.persisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.thread.annotation.ReadRec;
import com.soft.industry.thread.annotation.Semaphore;

@Component
@Scope("prototype")
public class ReadRecManualComOPFI extends ReadRec {
	
	private static final Logger log = LoggerFactory.getLogger(ReadRecManualComOPFI.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");
		
	static {
       semaphore = new Semaphore(Semaphore.WAIT);
	}
	
	@Autowired
	InvoiceDao invoiceDaoManualImpl;
	

	double balance;
	
	public ReadRecManualComOPFI() {
		super.log= log;
		super.logerror = logerror;
	}

	@Override
	public void readInvoice() {
		try {
					
			 Thread t1 = new Thread(new Runnable(){
			      public void run() {
			    	 try {
			           int  invoiceCount = invoiceDaoManualImpl.getInvoiceCountComOPFI(invoice.getClass());
			   		  log.info("read invoice manual comOPFI count:{}",invoiceCount);
			    	 } catch (Exception ex) {
			    		 log.error("Error read compOPFI, error message:{}",ex.getMessage());
			    	 }
			      }
	    	 });
			 			 			 
			 t1.start();
			 
			 						 
		} catch(Exception ex) {
			log.error("Error ReadRecManualComOPFI ",ex);
		}
	}

	
}