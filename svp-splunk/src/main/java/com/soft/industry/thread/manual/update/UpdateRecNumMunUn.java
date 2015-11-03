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
public class UpdateRecNumMunUn extends UpdateRec {
	
	private static final Logger log = LoggerFactory.getLogger(UpdateRecNumMunUn.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");
			
	static {
        semaphore = new Semaphore(Semaphore.WAIT);
	}
	
	@Autowired
	InvoiceDao invoiceDaoManualImpl;
	
	Invoice invoice;
	double balance;
	
	public UpdateRecNumMunUn() {
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
		     Invoice invoiceChanged = invoiceDaoManualImpl.updateInvoiceUn(InvoiceNum.class, invoice.getId(),delay);
			 log.info(", delay:{}, id:{}, trhead:{}",delay, id, getComment(invoiceChanged));
		} catch(Exception ex) {
			 log.error("Error update un, id:{}, delay:{}, invoice:{}, error-mess:{}",id,delay, invoice.getComment(),ex.getMessage());
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
