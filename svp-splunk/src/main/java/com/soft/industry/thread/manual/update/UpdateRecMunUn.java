package com.soft.industry.thread.manual.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.thread.annotation.Semaphore;
import com.soft.industry.thread.annotation.UpdateRec;

@Component
@Scope("prototype")
public class UpdateRecMunUn extends UpdateRec {
	
	private static final Logger log = LoggerFactory.getLogger(UpdateRecMunUn.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");
			
	static {
        semaphore = new Semaphore(Semaphore.WAIT);
	}
	
	@Autowired
	InvoiceDao invoiceDaoManualImpl;
	
	InvoiceEnt invoice;
	double balance;
	
	public UpdateRecMunUn() {
		super.log= log;
		super.logerror = logerror;
	}

	private String getComment(final Invoice invoice) {
		return invoice!=null?((InvoiceEnt)invoice).getComment():"null";
	}
	
	@Override
	public void updateInvoice() {
		try {
		     Invoice invoiceChanged = invoiceDaoManualImpl.updateInvoiceUn(InvoiceEnt.class, invoice.getId(),delay);
			 log.info(" trhead:{}, delay:{}",getComment(invoiceChanged),delay);
		} catch(Exception ex) {
			log.error("Error save invoice:{}, {}",invoice.getComment(),delay);
		}
	}
		
	@Override
	public void setBalance(double balance) {
		this.balance = balance;		
	}

	@Override
	public void setInvoice(Invoice invoice) {
		if(invoice !=null) {
		   this.invoice = (InvoiceEnt) invoice;
		}
	}
}
