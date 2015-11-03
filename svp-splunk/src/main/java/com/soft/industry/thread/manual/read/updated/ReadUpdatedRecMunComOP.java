package com.soft.industry.thread.manual.read.updated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.thread.annotation.ReadRec;
import com.soft.industry.thread.annotation.Semaphore;

@Component
@Scope("prototype")
public class ReadUpdatedRecMunComOP extends ReadRec {

	private static final Logger log = LoggerFactory.getLogger(ReadUpdatedRecMunComOP.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");

	static {
		semaphore = new Semaphore(Semaphore.WAIT);
	}

	@Autowired
	InvoiceDao invoiceDaoManualImpl;

	double balance;

	public ReadUpdatedRecMunComOP() {
		super.log = log;
		super.logerror = logerror;
	}

	@Override
	public void readInvoice() {
		try {
			 log.info("try read invoice id:{}", invoice==null?"null":invoice.getId());
			 readRecordUpdated();
		} catch (Exception ex) {
			log.error("Error read invoice:{}, error mess:{}", invoice, ex.getMessage());
		}
	}	
	
	private void readRecordUpdated() {
		 final Invoice readInvoice = this.invoice;
		 
		     Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
			        Invoice invoice = invoiceDaoManualImpl.getInvoiceByIdComOP(readInvoice.getClass(),(int)getInvoice().getId());
			        log.info("read com invoice id:{}", invoice==null?"null":invoice.getId() + "-" + invoice.getComment());
				}
			 });	
		     
			t1.start();
	}
}