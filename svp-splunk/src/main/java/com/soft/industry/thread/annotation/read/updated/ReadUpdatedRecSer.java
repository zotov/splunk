package com.soft.industry.thread.annotation.read.updated;

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
public class ReadUpdatedRecSer extends ReadRec {

	private static final Logger log = LoggerFactory.getLogger(ReadUpdatedRecSer.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");

	static {
		semaphore = new Semaphore(Semaphore.WAIT);
	}

	@Autowired
	InvoiceDao invoiceAnnotDao;


	double balance;

	public ReadUpdatedRecSer() {
		super.log = log;
		super.logerror = logerror;
	}

	@Override
	public void readInvoice() {
		try {
			 log.info("try read invoice id:{}", invoice==null?"null":invoice.getId());
			readRecordUpdated();
		} catch (Exception ex) {
			log.error("Error save invoice:{}", invoice, ex);
		}
	}	
	
	private void readRecordUpdated() {
		 Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					Class<?> invoiceClass = invoice.getClass();
			        Invoice invoice = invoiceAnnotDao.getInvoiceByIdUn(invoiceClass,(int)getInvoice().getId());
			        log.info("read un invoice id:{}", invoice==null?"null":invoice.getId() + "-" + invoice.getComment());
				}
			 });			

			 Thread t2 = new Thread(new Runnable() {
				@Override
				public void run() {
					Class<?> invoiceClass = invoice.getClass();
			        Invoice invoice = invoiceAnnotDao.getInvoiceByIdCom(invoiceClass,(int)getInvoice().getId());
			        log.info("read com invoice id:{}", invoice==null?"null":invoice.getId() + "-" + invoice.getComment());
				}
			 });		
			
			 Thread t3 = new Thread(new Runnable() {
				@Override
				public void run() {
					Class<?> invoiceClass = invoice.getClass();
			        Invoice invoice = invoiceAnnotDao.getInvoiceByIdRep(invoiceClass,(int)getInvoice().getId());
			        log.info("read rep invoice id:{}", invoice==null?"null":invoice.getId() + "-" + invoice.getComment());
				}
			 });
			
			 Thread  t4 = new Thread(new Runnable() {
				@Override
				public void run() {
					Class<?> invoiceClass = invoice.getClass();
			        Invoice invoice = invoiceAnnotDao.getInvoiceByIdSer(invoiceClass,(int)getInvoice().getId());
			        log.info("read ser invoice id:{}", invoice==null?"null":invoice.getId() + "-" + invoice.getComment());
				}
			 });
			
			t1.start();
			t2.start();
			t3.start();
			t4.start();
	}
	
}