package com.soft.industry.action.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;


@Component
public class SaveInvoiceManual extends SaveInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(SaveInvoiceManual.class);
	
	public SaveInvoiceManual() {
		super.log = log;
	}			
	
    public void saveAction() {
	    startSaveThread("saveRecManualSer");
	    startSaveThread("saveRecManualNumSer");
	    startSaveThread("saveRecManualDateSer");
	}

	 public void destroy() throws Exception {
		if(pool != null) {
	    	   pool.shutdown();
	    	   log.info("Release resources...");
	       }
		
	 }
}
