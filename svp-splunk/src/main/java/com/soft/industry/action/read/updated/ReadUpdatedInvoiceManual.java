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
public class ReadUpdatedInvoiceManual extends ReadUpdatedInvoice implements  DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(ReadUpdatedInvoiceManual.class);
	
	public ReadUpdatedInvoiceManual() {
		super.log = log;
	}	
	
	@Autowired
	InvoiceDao invoiceDaoManualImpl;
	
    public void startAction() {
    	this.testEnt(invoiceDaoManualImpl.getInvoice(InvoiceEnt.class));
        //this.testNum(invoiceDaoManualImpl.getInvoice(InvoiceNum.class));
    	//this.testDate(invoiceDaoManualImpl.getInvoice(InvoiceDate.class));
    }
    
    
    public void testEnt(final Invoice invoice) {
    	
    	//startAction("updateRecMunUn","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecMunUn","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecMunUn","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecMunUn","readUpdatedRecMunSer", invoice);
     	     	
     	//startAction("updateRecMunCom","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecMunCom","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecMunCom","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecMunCom","readUpdatedRecMunSer", invoice);
     	     	
    	//startAction("updateRecMunRep","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecMunRep","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecMunRep","readUpdatedRecMunRep", invoice);
     	startAction("updateRecMunRep","readUpdatedRecMunSer", invoice);
     	
    	//startAction("updateRecMunSer","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecMunSer","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecMunSer","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecMunSer","readUpdatedRecMunSer", invoice);
     	    	
     }    
    
     public void testNum(final Invoice invoice) {
    	//startAction("updateRecNumMunUn","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecNumMunUn","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecNumMunUn","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecNumMunUn","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecNumMunUn","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecNumMunUn","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecNumMunUn","readUpdatedRecMunSerFI", invoice);
     	
     	
     	//startAction("updateRecNumMunCom","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecNumMunCom","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecNumMunCom","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecNumMunCom","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecNumMunCom","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecNumMunCom","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecNumMunCom","readUpdatedRecMunSerFI", invoice);    	
     	
     	
     	//startAction("updateRecNumMunComOP","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecNumMunComOP","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecNumMunComOP","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecNumMunComOP","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecNumMunComOP","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecNumMunComOP","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecNumMunComOP","readUpdatedRecMunSerFI", invoice);    	
     	   	
     	
     	//startAction("updateRecNumMunComOPFI","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecNumMunComOPFI","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecNumMunComOPFI","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecNumMunComOPFI","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecNumMunComOPFI","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecNumMunComOPFI","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecNumMunComOPFI","readUpdatedRecMunSerFI", invoice);  
     	
     	
     	//startAction("updateRecNumMunRep","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecNumMunRep","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecNumMunRep","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecNumMunRep","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecNumMunRep","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecNumMunRep","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecNumMunRep","readUpdatedRecMunSerFI", invoice);  
     	    	 
     	
     	//startAction("updateRecNumMunSer","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecNumMunSer","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecNumMunSer","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecNumMunSer","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecNumMunSer","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecNumMunSer","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecNumMunSer","readUpdatedRecMunSerFI", invoice);  
     	
     	
     	//startAction("updateRecNumMunSerFI","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecNumMunSerFI","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecNumMunSerFI","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecNumMunSerFI","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecNumMunSerFI","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecNumMunSerFI","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecNumMunSerFI","readUpdatedRecMunSerFI", invoice);  
     }
     
    
     public void testDate(final Invoice invoice) {
    	 
    	//startAction("updateRecDateMunUn","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecDateMunUn","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecDateMunUn","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecDateMunCom","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecDateMunUn","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecDateMunUn","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecDateMunUn","readUpdatedRecMunSerFI", invoice);
     	
     	
     	//startAction("updateRecDateMunCom","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecDateMunCom","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecDateMunCom","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecDateMunCom","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecDateMunCom","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecDateMunCom","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecDateMunCom","readUpdatedRecMunSerFI", invoice);    	
     	
     	
     	//startAction("updateRecDateMunComOP","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecDateMunComOP","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecDateMunComOP","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecDateMunComOP","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecDateMunComOP","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecDateMunComOP","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecDateMunComOP","readUpdatedRecMunSerFI", invoice);    	
     	   	
     	
     	//startAction("updateRecDateMunComOPFI","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecDateMunComOPFI","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecDateMunComOPFI","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecDateMunComOPFI","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecDateMunComOPFI","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecDateMunComOPFI","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecDateMunComOPFI","readUpdatedRecMunSerFI", invoice);  
     	
     	
     	//startAction("updateRecDateMunRep","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecDateMunRep","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecDateMunRep","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecDateMunRep","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecDateMunRep","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecDateMunRep","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecDateMunRep","readUpdatedRecMunSerFI", invoice);  
     	    	 
     	
     	//startAction("updateRecDateMunSer","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecDateMunSer","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecDateMunSer","readUpdatedRecMunComOP", invoice);
     	//!startAction("updateRecDateMunSer","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecDateMunSer","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecDateMunSer","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecDateMunSer","readUpdatedRecMunSerFI", invoice);  
     	
     	
     	//startAction("updateRecDateMunSerFI","readUpdatedRecMunUn", invoice);
     	//startAction("updateRecDateMunSerFI","readUpdatedRecMunCom", invoice);
     	//startAction("updateRecDateMunSerFI","readUpdatedRecMunComOP", invoice);
     	//startAction("updateRecDateMunSerFI","readUpdatedRecMunComOPFI", invoice);
     	//startAction("updateRecDateMunSerFI","readUpdatedRecMunRep", invoice);
     	//startAction("updateRecDateMunSerFI","readUpdatedRecMunSer", invoice);
     	//startAction("updateRecDateMunSerFI","readUpdatedRecMunSerFI", invoice);  
    	
     }    

     public void destroy() throws Exception {
	  if(poolUpdate != null) {
	   	   poolUpdate.shutdown();
	   	   log.info("Release resources...");
	    }
     }
}