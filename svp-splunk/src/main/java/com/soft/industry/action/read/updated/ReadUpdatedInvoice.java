package com.soft.industry.action.read.updated;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.soft.industry.entity.Invoice;
import com.soft.industry.thread.annotation.ReadRec;
import com.soft.industry.thread.annotation.Semaphore;
import com.soft.industry.thread.annotation.UpdateRec;

public abstract class ReadUpdatedInvoice implements InitializingBean, Runnable {

	/** The Constant log. */
	protected Logger log ;
	
	@Value("#{appProp['pool.read.size']}")
	int pollReadSize;
	
	@Value("#{appProp['pool.read.update.size']}")
	int pollUpdateSize;
	
	@Value("#{appProp['d.delay']}")
	long dDelay;//1000;
	
	@Value("#{appProp['read.update.delay']}")
	long delay;//1000;
	
	@Value("#{appProp['timeout.wait']}")
	int timeout;//1000;
	
	@Autowired
	ApplicationContext appContext;

	protected ThreadPoolExecutor poolUpdate;
	protected ThreadPoolExecutor poolRead;
	
	public void afterPropertiesSet() throws Exception {
		poolUpdate = (ThreadPoolExecutor)Executors.newFixedThreadPool(pollUpdateSize);
		poolRead = (ThreadPoolExecutor)Executors.newFixedThreadPool(pollReadSize);
	}	

	public void run() {		
		startAction();
		closePool();
	}
	
	protected void startAction(final String beanUpdate,final String  beanRead, final Invoice invoice) {
	    startUpdateTask(beanUpdate,invoice);
	    startReadTask(beanRead,invoice);
		release();
		waitPoolExecuteAllTask();
	}
	
	private void release() {
		UpdateRec.releasAll();
		//ReadRec.releasAll();
	}
	
	private  void startUpdateTask(final String beanUpdate, final Invoice invoice) {
		
		UpdateRec.dDelay = dDelay;
		//long delay = dDelay * pollSaveSize;
		Semaphore.setMaxDelay(delay);
		Semaphore.setTimeout(timeout);
		Semaphore.setdDelay(dDelay);
		
		UpdateRec t = null ;
		for(int i=0; i < pollUpdateSize; i++) {
			 t = (UpdateRec) this.appContext.getBean(beanUpdate);
			 t.setBalance(i);
			 t.setDelay(delay);
			 t.setInvoice(invoice);
			 delay -= dDelay;
			 poolUpdate.submit(t);
		}
		log.info("poolSave active task:{}",poolUpdate.getActiveCount());
	}
	
	private void startReadTask(final String beanRead,final Invoice invoice) {
		Semaphore.setTimeout(timeout);
		ReadRec.block();
		ReadRec t = null ;
		for(int i=0; i < pollReadSize; i++) {			
			 t = (ReadRec) this.appContext.getBean(beanRead);
			 t.setInvoice(invoice);
		     poolRead.submit(t);
		}
		log.info("poolRead active task:{}",poolRead.getActiveCount());
	}
	
	private void waitPoolExecuteAllTask() {
		while(poolUpdate.getActiveCount() > 0 || poolRead.getActiveCount() > 0) {
			try {
				Thread.sleep(1000);
				log.debug("wait...");
			} catch (InterruptedException ex) {
				log.error("Error wait of end pool execution:{}",ex.getMessage());
			}
		}		
	}
	
	private void closePool() {
		poolUpdate.shutdown();
		poolRead.shutdown();
	}
	
	public abstract void startAction();
	
}