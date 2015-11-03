package com.soft.industry.action.read.updated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soft.industry.action.read.persisted.ReadPersistedInvoiceAnnot;
import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;
import com.soft.industry.entity.InvoiceDate;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;

@Component
public class ReadUpdatedInvoiceAnnot extends ReadUpdatedInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(ReadPersistedInvoiceAnnot.class);
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	public ReadUpdatedInvoiceAnnot() {
		super.log = log;
	}			
	
    public void startAction() {
    	Invoice invoice;
    	//invoice =   invoiceAnnotDao.getInvoice(InvoiceEnt.class);
        //startAction("updateRecUn", "readUpdatedRecSer", invoice);
    	//startAction("updateRecCom","readUpdatedRecSer", invoice);
    	//startAction("updateRecRep","readUpdatedRecSer", invoice);
    	//startAction("updateRecSer","readUpdatedRecSer", invoice);
    	
    	//invoice =   invoiceAnnotDao.getInvoice(InvoiceNum.class);
    	//startAction("updateRecUn", "readUpdatedRecSer", invoice);
    	//startAction("updateRecCom","readUpdatedRecSer", invoice);
    	//startAction("updateRecRep","readUpdatedRecSer", invoice);
    	//startAction("updateRecSer","readUpdatedRecSer", invoice);
    	    	
    	invoice =   invoiceAnnotDao.getInvoice(InvoiceDate.class);
    	startAction("updateRecUn", "readUpdatedRecSer", invoice);
    	startAction("updateRecCom","readUpdatedRecSer", invoice);
    	startAction("updateRecRep","readUpdatedRecSer", invoice);
    	startAction("updateRecSer","readUpdatedRecSer", invoice);
    	
   }

    public void destroy() throws Exception {
	  if(poolUpdate != null) {
	   	   poolUpdate.shutdown();
	   	   log.info("Release resources...");
	    }
    }
}