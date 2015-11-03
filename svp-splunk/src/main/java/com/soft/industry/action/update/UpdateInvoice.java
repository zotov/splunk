package com.soft.industry.action.update;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.soft.industry.entity.Invoice;
import com.soft.industry.thread.annotation.SaveRec;
import com.soft.industry.thread.annotation.Semaphore;
import com.soft.industry.thread.annotation.UpdateRec;
/*
 * 
 */
public abstract class UpdateInvoice implements InitializingBean, Runnable {

	/** The Constant log. */
	protected Logger log ;
	
	@Value("#{appProp['update.pool.size']}")
	int pollSize;
	
	@Value("#{appProp['update.d.delay']}")
	long dDelay;//1000;
	
	@Value("#{appProp['update.timeout.wait']}")
	int timeout;//1000;
		
	
	@Autowired
	ApplicationContext appContext;

	protected ThreadPoolExecutor pool;
	
	public void afterPropertiesSet() throws Exception {
		pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(pollSize);
	}	

	public void run() {		
		updateAction();
		closePool();
	}
	
	protected void startUpdateThread(final String beanName, final Invoice invoice) {
		
		UpdateRec t = null;
		
		UpdateRec.block();		
		SaveRec.dDelay = dDelay;
		long delay = dDelay * pollSize;
		Semaphore.setMaxDelay(delay);
		Semaphore.setTimeout(timeout);
		Semaphore.setdDelay(dDelay);
				
		for(int i=0; i < pollSize; i++) {
			 t = (UpdateRec) this.appContext.getBean(beanName);
			 t.setBalance(i);
			 t.setDelay(delay);
			 t.setInvoice(invoice);
			 delay -= dDelay;
			 pool.submit(t);
		}
		
		log.info("pool active task:{}",pool.getActiveCount());
		UpdateRec.releasAll();
		waitPoolExecuteAllTask();
	}
	
	private void waitPoolExecuteAllTask() {
		while(pool.getActiveCount() > 0) {
			try {
				Thread.sleep(1000);
				log.debug("wait...");
			} catch (InterruptedException ex) {
				log.error("Error wait of end pool execution:{}",ex.getMessage());
			}
		}		
	}
	
	private void closePool() {
		pool.shutdown();
	}
		
	public abstract void updateAction();
	
}
