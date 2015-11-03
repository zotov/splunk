package com.soft.industry.action.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;
import com.soft.industry.entity.InvoiceDate;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;


@Component
public class UpdateInvoiceAnnot extends UpdateInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(UpdateInvoiceAnnot.class);
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	public UpdateInvoiceAnnot() {
		super.log = log;
	}			
	
    public void updateAction() {
    	Invoice invoice;
    	//invoice = invoiceAnnotDao.getInvoice(InvoiceEnt.class);
    	//startUpdateThread("updateRecUn", invoice);
    	//startUpdateThread("updateRecCom", invoice);
    	//startUpdateThread("updateRecRep", invoice);
	    //startUpdateThread("updateRecSer", invoice);
	    
	    //invoice = invoiceAnnotDao.getInvoice(InvoiceNum.class);
	    //startUpdateThread("updateRecUn", invoice);
    	//startUpdateThread("updateRecCom", invoice);
    	//startUpdateThread("updateRecRep", invoice);
	    //startUpdateThread("updateRecSer", invoice);
	    
	    
	    invoice = invoiceAnnotDao.getInvoice(InvoiceDate.class);
	    startUpdateThread("updateRecUn", invoice);
    	startUpdateThread("updateRecCom", invoice);
    	startUpdateThread("updateRecRep", invoice);
	    startUpdateThread("updateRecSer", invoice);
    }
    

	 public void destroy() throws Exception {
		if(pool != null) {
	    	   pool.shutdown();
	    	   log.info("Release resources...");
	       }
		
	 }
}
