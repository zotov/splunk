package pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pool.threads.HubRequest;
import pool.threads.ServicePool;

public class MainApp {
	
	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	
	private static ExecutorService pool =  Executors.newFixedThreadPool(100); 
	
	public static void main(String[]arg0) throws InterruptedException {
		
		long startTime = System.currentTimeMillis();
		final ServicePool s = new ServicePool();
		int num=10;
		for(int i = 0; i< num;i++){
			final int index = i;
			Thread t = new Thread(new Runnable(){
               
				public void run() {
					try {
						s.sendRequests("hub"+index);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}							
				}});
			
			pool.execute(t);
		}
		
		while(((ThreadPoolExecutor)pool).getActiveCount() > 0) {
			   //log.info("1wait complete all task...");
			   Thread.sleep(1000);
		   }
		pool.shutdown();
		log.info("all tasks complete, executeTime:{}sec",(System.currentTimeMillis() - startTime)/1000);
	}

}
