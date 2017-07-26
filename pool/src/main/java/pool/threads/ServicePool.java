package pool.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicePool {

	private static final Logger log = LoggerFactory.getLogger(HubRequest.class);
	
	  int poolSize = 50;
	  int delay= 3000;
	  int threadSize = 100;
	  //int threadSize = 2;
	  
	 
	  
	private static final AtomicInteger sequenceNumber = new AtomicInteger(1);
	
	private int getNextUniqueId() {
		// roll over of the counter when it reaches the max value
		boolean result = sequenceNumber.compareAndSet(Integer.MAX_VALUE, 1);
		if (result)
			log.debug("reset of the counter");
	    return sequenceNumber.getAndIncrement();
	}
	
	private ThreadPoolExecutor pool =  (ThreadPoolExecutor)Executors.newFixedThreadPool(poolSize); 
	private final ReentrantLock lock = new ReentrantLock();
	//public synchronized void sendRequests(final String hub) throws InterruptedException {
	
	public void sendRequests(final String hub, String typeMethod, ControllerData cd) throws InterruptedException {
    
	 try{
		 log.info("***************************");
		 log.info("**********  {}  ***********", typeMethod);
		 log.info("***************************");
	  //lock.lock();
     
	  for(int i=0; i < threadSize; i++) {
		  cd.initData(hub, i);
		  HubRequest t = new HubRequest(getNextUniqueId(), delay, hub, typeMethod, cd, 0);		 
		  pool.execute(t);
		  log.info("get active count:{}, total count:{}, queue.size={}, {}",
		            pool.getActiveCount(), pool.getTaskCount(), pool.getQueue().size(), hub);
	  }
	 }finally {
		// lock.unlock();
	 }
	   while(((ThreadPoolExecutor)pool).getActiveCount() > 0) {
		   //log.info("wait complete all task..");
		   Thread.sleep(500);
	   }
	   pool.shutdown();
	}
	
	
	public void sendTwoRequests(final String hub,
								final String typeMethod1, 
								final String typeMethod2, 
								final ControllerData cd) throws InterruptedException {
	    
		 try {
			 log.info("***************************");
			 log.info("**********  1.{}: 2.{}  ***********", typeMethod1, typeMethod2);
			 log.info("***************************");
			  //lock.lock();
		     
			  for(int i=0; i < threadSize; i++) {
				  cd.initData(hub, i);
				  HubRequest t =null;
				  if(i%2 == 0) {
				    t = new HubRequest(getNextUniqueId(), delay, hub, typeMethod1, cd, 0);
				  } else {
					  t = new HubRequest(getNextUniqueId(), delay, hub,typeMethod2, cd, 0); 
				  }
				  pool.execute(t);
				  log.info("get active count:{}, total count:{}, queue.size={}, {}",
				            pool.getActiveCount(), pool.getTaskCount(), pool.getQueue().size(), hub);
			  }
		 } finally {
			// lock.unlock();
		 }
		 
		 while(((ThreadPoolExecutor)pool).getActiveCount() > 0) {
			   //log.info("wait complete all task..");
			   Thread.sleep(500);
		 }
		   pool.shutdown();
		}
	
	public void sendTwoRequestsTwoObjects(final String hub,
										 final String typeMethod1, 
										 final String typeMethod2, 
										 final ControllerData cd1,
										 final ControllerData cd2) throws InterruptedException {

		try {
			log.info("***************************");
			log.info("**********  1.{}: 2.{}  ***********", typeMethod1, typeMethod2);
			log.info("***************************");
			// lock.lock();

			for (int i = 0; i < threadSize; i++) {
				
				HubRequest t = null;
				if (i % 2 == 0) {
					cd1.initData(hub, i);
					t = new HubRequest(getNextUniqueId(), delay, hub, typeMethod1, cd1, 0);
				} else {
					cd2.initData(hub, i);
					t = new HubRequest(getNextUniqueId(), delay, hub, typeMethod2, cd2, 0);
				}
				pool.execute(t);
				log.info(
						"get active count:{}, total count:{}, queue.size={}, {}",
						pool.getActiveCount(), pool.getTaskCount(), pool
								.getQueue().size(), hub);
			}
		} finally {
			// lock.unlock();
		}

		while (((ThreadPoolExecutor) pool).getActiveCount() > 0) {
			// log.info("wait complete all task..");
			Thread.sleep(500);
		}
		pool.shutdown();
	}
	
	public void sendTwoExchangeRequests(final String hub, 
										final String typeMethod1,
										final String typeMethod2, 
										final ControllerData cd,
										final int size) throws InterruptedException {

		try {
			log.info("***************************");
			log.info("**********  1.{}: 2.{}  ***********", typeMethod1, typeMethod2);
			log.info("***************************");
			// lock.lock();

			
			for (int i = 0; i < threadSize; i++) {
				//cd.initData(hub, i);
				int num=getNextUniqueId();	
				HubRequest t1 = null;
				HubRequest t2 = null;
			
				t1 = new HubRequest(num, delay, hub, typeMethod1, cd, size);			
				t2 = new HubRequest(num, delay, hub, typeMethod2, cd, size);			
								
				pool.execute(t1);
				pool.execute(t2);				
				
				log.info(
						"get active count:{}, total count:{}, queue.size={}, {}",
						pool.getActiveCount(), pool.getTaskCount(), pool
								.getQueue().size(), hub);
			}
		} finally {
			// lock.unlock();
		}

		while (((ThreadPoolExecutor) pool).getActiveCount() > 0) {
			// log.info("wait complete all task..");
			Thread.sleep(500);
		}
		pool.shutdown();
	}
	
}
