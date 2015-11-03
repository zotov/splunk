package com.soft.industry.thread.annotation.read.persisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Invoice;
import com.soft.industry.entity.InvoiceDate;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;
import com.soft.industry.thread.annotation.ReadRec;
import com.soft.industry.thread.annotation.Semaphore;

@Component
@Scope("prototype")
public class ReadRecSer extends ReadRec {

	private static final Logger log = LoggerFactory.getLogger(ReadRecSer.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");

	static {
		semaphore = new Semaphore(Semaphore.WAIT);
	}

	@Autowired
	InvoiceDao invoiceAnnotDao;

	double balance;

	public ReadRecSer() {
		super.log = log;
		super.logerror = logerror;
	}

	@Override
	public void readInvoice() {
		try {
		   readRecordAmount();
		} catch (Exception ex) {
			log.error("Error save invoice:{}", invoice, ex);
		}
	}
	
	private void readRecordAmount() {
		 final StringBuffer buff = new StringBuffer();
		 if(this.invoice instanceof InvoiceDate){
			 buff.append("date");
		 } else if(this.invoice instanceof InvoiceNum){ 
			 buff.append("num");
		 }
		
		 Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					int invoiceCount = invoiceAnnotDao.getInvoiceCountUn(invoice.getClass());
					log.info("read {} invoice un count:{}",buff, invoiceCount);
				}
			 });			

			 Thread t2 = new Thread(new Runnable() {
				@Override
				public void run() {
					int invoiceCount = invoiceAnnotDao.getInvoiceCountCom(invoice.getClass());
					log.info("read {} invoice com count:{}", buff, invoiceCount);
				}
			 });		
			
			 Thread t3 = new Thread(new Runnable() {
				@Override
				public void run() {
					int invoiceCount = invoiceAnnotDao.getInvoiceCountRep(invoice.getClass());
					log.info("read {} invoice rep count:{}",buff, invoiceCount);
				}
			 });
			
			 Thread  t4 = new Thread(new Runnable() {
				@Override
				public void run() {
					int invoiceCount = invoiceAnnotDao.getInvoiceCountSer(invoice.getClass());
					log.info("read {} invoice ser count:{}",buff, invoiceCount);
				}
			 });
			
			t1.start();
			t2.start();
			t3.start();
			t4.start();
	}
	
}