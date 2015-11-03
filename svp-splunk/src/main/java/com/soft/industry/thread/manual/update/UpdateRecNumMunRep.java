package com.soft.industry.thread.manual.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;
import com.soft.industry.thread.annotation.Semaphore;
import com.soft.industry.thread.annotation.UpdateRec;

@Component
@Scope("prototype")
public class UpdateRecNumMunRep extends UpdateRec {
	
	private static final Logger log = LoggerFactory.getLogger(UpdateRecNumMunRep.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");
			
	static {
        semaphore = new Semaphore(Semaphore.WAIT);
	}
	
	@Autowired
	InvoiceDao invoiceDaoManualImpl;
	
	Invoice invoice;
	double balance;
	
	public UpdateRecNumMunRep() {
		super.log= log;
		super.logerror = logerror;
	}

	private String getComment(final Invoice invoice) {
		return invoice!=null?invoice.getComment():"null";
	}
	
	@Override
	public void updateInvoice() {
		long id = invoice.getId();
		try {
		     Invoice invoiceChanged = invoiceDaoManualImpl.updateInvoiceRep(InvoiceNum.class, id,delay);
			 log.info(" trhead:{}, delay:{}",getComment(invoiceChanged),delay);
		} catch(Exception ex) {
			log.error("Error update rep, id:{}, invoice:{}, delay:{}, error-mes:{}",id, invoice.getComment(), delay, ex.getMessage());
		}
	}
		
	@Override
	public void setBalance(double balance) {
		this.balance = balance;		
	}

	@Override
	public void setInvoice(Invoice invoice) {
		if(invoice !=null) {
		   this.invoice = invoice;
		}
	}
}
