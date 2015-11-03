package com.soft.industry.action.read.persisted;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.soft.industry.entity.Invoice;
import com.soft.industry.thread.annotation.ReadRec;
import com.soft.industry.thread.annotation.SaveRec;
import com.soft.industry.thread.annotation.Semaphore;

public abstract class ReadPersistedInvoice implements InitializingBean, Runnable {

	/** The Constant log. */
	protected Logger log ;
	
	@Value("#{appProp['pool.read.size']}")
	int pollReadSize;
	
	@Value("#{appProp['pool.read.save.size']}")
	int pollSaveSize;
	
	@Value("#{appProp['d.delay']}")
	long dDelay;//1000;
	
	@Value("#{appProp['read.save.delay']}")
	long delay;//1000;
	
	@Value("#{appProp['timeout.wait']}")
	int timeout;//1000;
	
	@Autowired
	ApplicationContext appContext;

	protected ThreadPoolExecutor poolSave;
	protected ThreadPoolExecutor poolRead;
	
	public void afterPropertiesSet() throws Exception {
		poolSave = (ThreadPoolExecutor)Executors.newFixedThreadPool(pollSaveSize);
		poolRead = (ThreadPoolExecutor)Executors.newFixedThreadPool(pollReadSize);
	}	

	public void run() {		
		startAction();
		closePool();
	}
	
	protected void startAction(final String beanSave,final String  beanRead,final Invoice invoice) {
	    startUpdateTask(beanSave);
	    startReadTask(beanRead,invoice);
		release();
		waitPoolExecuteAllTask();
	}
	
	private void release() {
		SaveRec.releasAll();
		//ReadRec.releasAll();
	}

	
	private  void startUpdateTask(final String beanSave) {
		SaveRec.block();
		SaveRec.dDelay = dDelay;
		//long delay = dDelay * pollSaveSize;
		Semaphore.setMaxDelay(delay);
		Semaphore.setTimeout(timeout);
		Semaphore.setdDelay(dDelay);
		
		SaveRec t = null ;
		for(int i=0; i < pollSaveSize; i++) {
			 t = (SaveRec) this.appContext.getBean(beanSave);
			 t.setBalance(i);
			 t.setDelay(delay);
			 delay -= dDelay;
			 poolSave.submit(t);
		}
		log.info("poolSave active task:{}",poolSave.getActiveCount());
	}
	
	private void startReadTask(final String beanRead,final Invoice invoice) {
		Semaphore.setTimeout(timeout);
		ReadRec t = null ;
		for(int i=0; i < pollReadSize; i++) {
			 t = (ReadRec) this.appContext.getBean(beanRead);
			 t.setInvoice(invoice);
		     poolRead.submit(t);
		}
		log.info("poolRead active task:{}",poolRead.getActiveCount());
	}
	
	private void waitPoolExecuteAllTask() {
		while(poolSave.getActiveCount() > 0 || poolRead.getActiveCount() > 0) {
			try {
				Thread.sleep(1000);
				log.debug("wait...");
			} catch (InterruptedException ex) {
				log.error("Error wait of end pool execution:{}",ex.getMessage());
			}
		}		
	}
	
	private void closePool() {
		poolSave.shutdown();
		poolRead.shutdown();
	}
	
	public abstract void startAction();
	
}
