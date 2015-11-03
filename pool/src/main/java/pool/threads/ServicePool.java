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
	
	  int poolSize = 10;
	  int delay= 3000;
	  int threadSize = 100;
	  
	private static final AtomicInteger sequenceNumber = new AtomicInteger(1);
	
	private int getNextUniqueId() {
		// roll over of the counter when it reaches the max value
		boolean result = sequenceNumber.compareAndSet(Integer.MAX_VALUE, 1);
		if (result)
			log.debug("reset of the counter");
	    return sequenceNumber.getAndIncrement();
	}
	
	private ExecutorService pool =  Executors.newFixedThreadPool(poolSize); 
	private final ReentrantLock lock = new ReentrantLock();
	//public synchronized void sendRequests(final String hub) throws InterruptedException {
	public void sendRequests(final String hub) throws InterruptedException {
    
	 try{
	  //lock.lock();
	  for(int i=0; i < threadSize; i++) {
		  HubRequest t = new HubRequest(getNextUniqueId(),delay,hub);
		  log.info("get active count:{},total count:{},hub:{}",((ThreadPoolExecutor)pool).getActiveCount()
				  ,((ThreadPoolExecutor)pool).getTaskCount()
				  ,hub);
		  pool.execute(t);
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
}
