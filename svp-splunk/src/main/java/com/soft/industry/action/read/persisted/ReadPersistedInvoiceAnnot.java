package com.soft.industry.action.read.persisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import com.soft.industry.entity.InvoiceDate;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;


@Component
public class ReadPersistedInvoiceAnnot extends ReadPersistedInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(ReadPersistedInvoiceAnnot.class);
	
	public ReadPersistedInvoiceAnnot() {
		super.log = log;
	}			
	
    public void startAction() {
    	startAction("saveRecDef","readRecSer", new InvoiceEnt());
    	startAction("saveRecSer","readRecSer", new InvoiceEnt());
    	    	
    	startAction("saveRecNumDef","readRecSer", new InvoiceNum());
    	startAction("saveRecNumSer","readRecSer", new InvoiceNum());
    	
    	startAction("saveRecDateDef","readRecSer", new InvoiceDate());
    	startAction("saveRecDateSer","readRecSer", new InvoiceDate());
   }

    public void destroy() throws Exception {
	  if(poolSave != null) {
	   	   poolSave.shutdown();
	   	   log.info("Release resources...");
	    }
    }
}
