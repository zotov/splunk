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
public class UpdateInvoiceManual extends UpdateInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(UpdateInvoiceManual.class);
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	public UpdateInvoiceManual() {
		super.log = log;
	}			
	
    public void updateAction() {
    	Invoice invoice;
    	
    	
    	invoice = invoiceAnnotDao.getInvoice(InvoiceEnt.class);
	    startUpdateThread("updateRecMunUn", invoice);
	    startUpdateThread("updateRecMunCom", invoice);
	    startUpdateThread("updateRecMunRep", invoice);
	    startUpdateThread("updateRecMunSer", invoice);	 
	    
    	
        invoice = invoiceAnnotDao.getInvoice(InvoiceNum.class);
	    //!startUpdateThread("updateRecNumMunUn", invoice);
	    //!startUpdateThread("updateRecNumMunCom", invoice);
	    //!startUpdateThread("updateRecNumMunComOP", invoice);
	    //!startUpdateThread("updateRecNumMunComOPFI", invoice);
	    //startUpdateThread("updateRecNumMunRep", invoice);
	    //startUpdateThread("updateRecNumMunSer", invoice);
	    //startUpdateThread("updateRecNumMunSerFI", invoice);
        
    		    
	    invoice = invoiceAnnotDao.getInvoice(InvoiceDate.class);
	    //!startUpdateThread("updateRecDateMunUn", invoice);
	    //!startUpdateThread("updateRecDateMunCom", invoice);
	    //!startUpdateThread("updateRecDateMunComOP", invoice);
	    //!startUpdateThread("updateRecDateMunComOPFI", invoice);
	    //startUpdateThread("updateRecDateMunRep", invoice);
	    //startUpdateThread("updateRecDateMunSer", invoice);
	    //startUpdateThread("updateRecDateMunSerFI", invoice);	    
	   
    }

	 public void destroy() throws Exception {
		if(pool != null) {
	    	   pool.shutdown();
	    	   log.info("Release resources...");
	       }
	 }
}
