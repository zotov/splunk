package com.soft.industry.action.read.persisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import com.soft.industry.entity.InvoiceDate;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;


@Component
public class ReadPersistedInvoiceManual extends ReadPersistedInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(ReadPersistedInvoiceManual.class);
	
	public ReadPersistedInvoiceManual() {
		super.log = log;
	}			
	
    public void startAction() {
    	
    	//startAction("saveRecManual","readRecManualUn", new InvoiceEnt());
    	//startAction("saveRecManual","readRecManualCom", new InvoiceEnt());
       	//startAction("saveRecManual","readRecManualRep", new InvoiceEnt());
    	//startAction("saveRecManual","readRecManualSer", new InvoiceEnt());
    
    	
    	//startAction("saveRecNumManual","readRecManualUn", new InvoiceNum());
        //startAction("saveRecNumManual","readRecManualCom", new InvoiceNum());
    	//startAction("saveRecNumManual","readRecManualComOP", new InvoiceNum());
    	//startAction("saveRecNumManual","readRecManualComOPFI", new InvoiceNum());
    	//startAction("saveRecNumManual","readRecManualRep", new InvoiceNum());
    	//startAction("saveRecNumManual","readRecManualSer", new InvoiceNum());
    	//startAction("saveRecNumManual","readRecManualSerFI", new InvoiceNum());
    	
    	startAction("saveRecDateManual","readRecManualUn", new InvoiceDate());
    	startAction("saveRecDateManual","readRecManualCom", new InvoiceDate());
    	startAction("saveRecDateManual","readRecManualComOP", new InvoiceDate());
    	startAction("saveRecDateManual","readRecManualComOPFI", new InvoiceDate());
    	startAction("saveRecDateManual","readRecManualRep", new InvoiceDate());
    	startAction("saveRecDateManual","readRecManualSer", new InvoiceDate());
    	startAction("saveRecDateManual","readRecManualSerFI", new InvoiceDate());
 
   }

    public void destroy() throws Exception {
	  if(poolSave != null) {
	   	   poolSave.shutdown();
	   	   log.info("Release resources...");
	    }
    }
}
