package com.soft.industry.action.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;


@Component
public class SaveInvoiceAnnot extends SaveInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(SaveInvoiceAnnot.class);
	
	public SaveInvoiceAnnot() {
		super.log = log;
	}			
	
    public void saveAction() {
	    startSaveThread("saveRecSer");
		startSaveThread("saveRecUn");
		startSaveThread("saveRecNumUn");
		startSaveThread("saveRecNumSer");
		startSaveThread("saveRecDateUn");
		startSaveThread("saveRecDateSer");
    }

	 public void destroy() throws Exception {
		if(pool != null) {
	    	   pool.shutdown();
	    	   log.info("Release resources...");
	       }
		
	 }
}
