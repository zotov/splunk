package com.soft.industry.thread.annotation.save;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.InvoiceDate;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;
import com.soft.industry.thread.annotation.SaveRec;
import com.soft.industry.thread.annotation.Semaphore;

@Component
@Scope("prototype")
public class SaveRecDateDef extends SaveRec {
	
	private static final Logger log = LoggerFactory.getLogger(SaveRecDateDef.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");
		
	final static AtomicLong transactionId = new AtomicLong(0);
	final static Semaphore semaphore = new Semaphore(Semaphore.WAIT);
	
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	InvoiceDate invoice;
	double balance;
	
	public SaveRecDateDef() {
		super.log= log;
		super.logerror = logerror;
	}

	@Override
	public void saveInvoice() {
		try {
			 invoice = new InvoiceDate();
			 this.invoice.setComment(Thread.currentThread().getName());
			 this.invoice.setTransactionId(transactionId.incrementAndGet());
			 this.invoice.setDate(new Timestamp(System.currentTimeMillis()));
			 this.invoice.setBalance(balance);
			 invoiceAnnotDao.saveInvoiceDef(invoice,delay);
			 int count = invoiceAnnotDao.getInvoiceCountSer(invoice.getClass());
			 log.info("save trhead:{}, total count:{} delay:{}",invoice.getComment(),count,delay);
		} catch(Exception ex) {
			log.error("Error save invice:{}",invoice,ex);
		}
   }
	
	@Override
	public void setBalance(double balance) {
		this.balance = balance;		
	}

}
