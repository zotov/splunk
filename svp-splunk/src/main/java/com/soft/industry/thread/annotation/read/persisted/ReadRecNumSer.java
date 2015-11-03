package com.soft.industry.thread.annotation.read.persisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.entity.InvoiceNum;
import com.soft.industry.thread.annotation.ReadRec;
import com.soft.industry.thread.annotation.Semaphore;

@Component
@Scope("prototype")
public class ReadRecNumSer extends ReadRec {
	
	private static final Logger log = LoggerFactory.getLogger(ReadRecNumSer.class);
	private static final Logger logerror = LoggerFactory.getLogger("ERROR_LOG");
		
	static {
       semaphore = new Semaphore(Semaphore.WAIT);
	}
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	InvoiceEnt invoice;
	double balance;
	
	public ReadRecNumSer() {
		super.log= log;
		super.logerror = logerror;
	}

	@Override
	public void readInvoice() {
		try {
			 //int invoiceCount = invoiceAnnotDao.getInvoiceCountSerVer(InvoiceNum.class,null);
			 //log.info("read invoice coutn:{}",invoiceCount);
		} catch(Exception ex) {
			log.error("Error save invice:{}",invoice,ex);
		}
	}
	
}