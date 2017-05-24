package pool;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pool.threads.HubRequest;
import pool.threads.ServicePool;

public class MainAppPool {
	
	private static final Logger log = LoggerFactory.getLogger(MainAppPool.class);
	
	private static ExecutorService pool =  Executors.newFixedThreadPool(100); 
	
	private static Map<Long, String> map = new ConcurrentHashMap<Long, String>();
	final static int size=10000;
	final static int timeout=100;
	
	public static void main(String[]arg0) throws InterruptedException {
		//testRemoveFromConcurrentMap();	    
		testWait(new MainAppPool());
	}
		
	
	public static void testWait(final MainAppPool mapl) throws InterruptedException {
		
		  ThreadB b = mapl.new ThreadB();
	        b.start();
	 
	        synchronized(b){
	            try{
	                System.out.println("Waiting for b to complete...");
	                b.wait();
	            }catch(InterruptedException e){
	                e.printStackTrace();
	            }
	 
	            System.out.println("Total is: " + b.total);
	        }
	       
	}
	
	
	class ThreadB extends Thread {
	    int total=0;
	    @Override
	    public void run() {
	        synchronized(this){
	            for(int i=0; i<1000 ; i++){
	                total += i;
	                System.out.println("total: " + total);
	            }
	            notify();
	        }
	   }
	}
	
	
	public static void testRemoveFromConcurrentMap() {
		
		
		
		for(long i=0; i< size; i++) {
			map.put(i, "value [" + i + "]");
		}
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
			  System.out.println("start add values to map=" + map.size());	
			 synchronized(MainAppPool.class) {
			   
			   int index=0;
				   for(long i= size; i < size * 2 ; i++) {
				       map.put(i, "value [" +  i + "]");
				       if(++index > timeout) {
				    	   index=0;
					       System.out.println("add new values, key=" + i + ", size of map=" + map.size());
					       try {
							  Thread.sleep(500L);
						    } catch (InterruptedException e) {						
								e.printStackTrace();
						    }
				       }
				    }
				}
			    System.out.println("end add values to map=" + map.size());
		    }			
		});
		
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				
				
					/*try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {					
						e.printStackTrace();
					}*/
					System.out.println("start remove values to map=" + map.size());
					synchronized(MainAppPool.class) {
				    int index=0;
					for(Iterator<Long>iterator=map.keySet().iterator(); iterator.hasNext();) {
						Long key = iterator.next();
						iterator.remove();
						if(++index > timeout) {
					        index=0;
					        System.out.println("remove values, key=" + key + ", size of map=" + map.size());
					        try {
								Thread.sleep(500L);
							} catch (InterruptedException e) {						
								e.printStackTrace();
							}
					     }
					}
				}
				System.out.println("end remove values to map=" + map.size());
		    }			
		});
				
		t1.start();
		t2.start();
	}
	
	
	public static void startHubRequest() throws InterruptedException {
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
