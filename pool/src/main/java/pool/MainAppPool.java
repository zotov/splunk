package pool;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pool.threads.ControllerData;
import pool.threads.HubRequest;
import pool.threads.ServicePool;

public class MainAppPool {
	
	private static final Logger log = LoggerFactory.getLogger(MainAppPool.class);
	
	private static ExecutorService pool =  Executors.newFixedThreadPool(100); 
	
	private static Map<Long, String> map = new ConcurrentHashMap<Long, String>();
	final static int size=10000;
	final static int timeout=100;
	
	public static void main(String[]arg0) throws InterruptedException {
		 startHubRequest();
		//testRemoveFromConcurrentMap();	    
		//testWait(new MainAppPool());
		 
//		Long i=5L;
//		testPrimitiveWraps(i);
//		log.info("2. i={}", i);
//		
//		Long j=5L;
//		testPrimitiveWraps_(j);
//		log.info("2. j={}", j);
	}
		
	
    static private void testPrimitiveWraps(Long i) {
		log.info("1. i={}", ++i);
	}
    
    static private void testPrimitiveWraps_(long j) {
		log.info("1. j={}", ++j);
	}
	
	public static void testWait(final MainAppPool mapl) throws InterruptedException {
		
		  ThreadB b = mapl.new ThreadB();
		  ThreadB2 b2 = mapl.new ThreadB2();
		  ThreadB1 b1 = mapl.new ThreadB1();
		  ThreadA a1 = mapl.new ThreadA();
		  ThreadA a = mapl.new ThreadA();
		  ThreadC c = mapl.new ThreadC();
		  Data d =  mapl.new  Data();
		  Data b3 =  mapl.new  Data();
	      //b.start();
		  a.title="ThreadA";
		  a1.title="ThreadA1";
		  
		  a.b1=b;
		  a1.b1=b;
		  a.d=d;
		  a1.d=d;
		  //c.b1=b;
		  //ThreadB b = a.b1;
		  b.a=a;
		  b1.a=a;
		  b2.a=a;
		  b.start();
		  b1.start();
		  b2.start();
	      //a.start();	    
	      //a1.start();
	      //c.start();
	 
	       /* synchronized(b)
	        {
	            try {
	                System.out.println("synchronized(b)...");
	               // Thread.sleep(3000);
	                System.out.println("Waiting for b to complete...");
	                b.wait();
	            } catch(Exception e){
	                e.printStackTrace();
	            }
	 
	            System.out.println("Total is: " + b.total);
	        }*/
	       
	}
	
	class ThreadA extends Thread {
		String title;
		ThreadB b1= new ThreadB();
		Thread t;
		Thread t1;
		int c=0;
		Data d = new  Data();
		 @Override
		    public void run() {
			 try {
				System.out.println(title + " run ");
				//b2.processData(title);
				//ThreadB b = d.b2;
				//d.processData(title, b);
				
				//b1.start();
				t = new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							processData_();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}});
				
				t1 = new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							processData1_();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}});
				t.start();
				t1.start();
				Thread.sleep(500);
				waitStopThreadB();
				//porcessData();
				//porcessData(b1);
				/* synchronized(b1){
					System.out.println(titel + " synchronized(b1) ");
		            for(int i=0; i<1000 ; i++){
		                b1.total += i;
		                //System.out.println("total: " + b1.total);
		            }
		            Thread.sleep(1000);
		            b1.notify();
		            System.out.println(titel + " total: " + b1.total);
		        }*/
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		 
		// public  void waitStopThreadB() throws InterruptedException {
		 public synchronized void waitStopThreadB() throws InterruptedException {
			 t.interrupt();
			 while(t.getState() != Thread.State.TERMINATED) {				
				 t.join(100);
				 //t.wait(1000);
				 
				 //b1.wait(1000);
				 System.out.println("t: has termination");
			 } 
			 System.out.println("t: has termination yet");			
		 }
		 
		 
		 public  void processData_() throws InterruptedException  {
			 //Thread.sleep(2000);
			 for(int i=0; i<1000000000 ; i++){		    	
		    	  if(i %100000000 == 0) {
	                 System.out.println("i = " + i + ", interrupted=" + Thread.currentThread().isInterrupted());
	              }               
	         }
			 processData();			 
		 }

		 
		 public synchronized void processData()  {
			 for(int i=0; i<1000000000 ; i++){		    		
		    		if(i %100000000 == 0) {	                	
	                	System.out.println("i1 = " + i);
	                }
	               
	            }
		 }
		 
		 public  void processData1_() throws InterruptedException  {
			 //Thread.sleep(2000);
			 for(int i=0; i<100000000 ; i++){		    	
		    	  if(i %100000000 == 0) {
	                 System.out.println("k = " + i + ", interrupted=" + Thread.currentThread().isInterrupted());
	              }               
	         }
			 processData1();			 
		 }

		 
		 public synchronized void processData1()  {
			 for(int i=0; i<1000000000 ; i++){		    		
		    		if(i %100000000 == 0) {	                	
	                	System.out.println("k1 = " + i );
	                }
	               
	            }
		 }
		 
		 public synchronized void processData2()  {
			 System.out.println("m1" );
			 for(int i=0; i<1000000000 ; i++){		    		
		    		if(i %100000000 == 0) {	                	
	                	System.out.println("m1 = " + i );
	                }
	               
	            }
		 }
		 
		 
		 public synchronized void processData3()  {
			 System.out.println("f1" );
			 for(int i=0; i<1000000000 ; i++){		    		
		    		if(i %100000000 == 0) {	                	
	                	System.out.println("f1 = " + i );
	                }
	               
	            }
		 }
		 
		 
		 
		 
		 
		 public synchronized void porcessData(ThreadB b) throws InterruptedException {
			 System.out.println(title + " porcessData(ThreadB b) ");
			 for(int i=0; i<1000 ; i++){
	                b.total += i;
	                //System.out.println("total: " + b.total);
	         }
			 Thread.sleep(1000);
			 System.out.println(title + " total(b): " + b.total);
			 //b.notify();
		 }
		 
		 public synchronized void porcessData() throws InterruptedException {
			 System.out.println(title + " porcessData() ");
			 for(int i=0; i<1000 ; i++){
	                b1.total += i;
	                //System.out.println("total: " + b1.total);
	         }
			 Thread.sleep(1000);
			 System.out.println(title + " total: " + b1.total);
			// b1.notify();
		 }
	}
	
	
class ThreadC extends Thread {
		
		ThreadB b1= new ThreadB();
		 @Override
		    public void run() {
			 try {
				System.out.println("ThreadC run ");
				//porcessData();
				//porcessData(b1);
				synchronized(b1){
					System.out.println("ThreadC synchronized(b1) ");
		            for(int i=0; i<1000 ; i++){
		                b1.total += i;
		                //System.out.println("total: " + b1.total);
		            }
		            Thread.sleep(1000);		            
		            b1.notify();
		            System.out.println("ThreadC total: " + b1.total);
		        }
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		 
		 
		 public synchronized void porcessData(ThreadB b) throws InterruptedException {
			 System.out.println("ThreadC  porcessData(ThreadB b) ");
			 for(int i=0; i<1000 ; i++){
	                b.total += i;
	                //System.out.println("total: " + b.total);
	         }
			 Thread.sleep(1000);
			 System.out.println("ThreadC total(b): " + b.total);
			 //b.notify();
		 }
		 
		 public synchronized void porcessData() throws InterruptedException {
			 System.out.println("ThreadC porcessData()");
			 for(int i=0; i<1000 ; i++){
	                b1.total += i;
	                //System.out.println("total: " + b1.total);
	         }
			 Thread.sleep(1000);
			 
			 System.out.println("ThreadC total: " + b1.total);
			 //b1.notify();
		 }
	}
	
class Data  {
	ThreadB b2 = new ThreadB ();
	
	public synchronized void processData(String title, ThreadB b2) throws InterruptedException {
		System.out.println(title + " b2 processData()");
		 for(int i=0; i<1000 ; i++){
               b2.total += i;
               //System.out.println("total: " + b1.total);
        }
		 Thread.sleep(1000);
		 
		 System.out.println(title + " b2 data total: " + b2.total);
	}
	
	
	public synchronized void processData(String title) throws InterruptedException {
		System.out.println(title + " b2 processData()");
		 for(int i=0; i<1000 ; i++){
               b2.total += i;
               //System.out.println("total: " + b1.total);
        }
		Thread.sleep(1000);
		 
		System.out.println(title + " b2 data total: " + b2.total);
	  }
   }


	class ThreadB extends Thread {
	    int total=0;
	    ThreadA a;
	    @Override
	    public void run() {
	    	try {
	    		System.out.println("ThreadB started");
	    		
	    		a.processData1();
				//calculation();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	       /* synchronized(this){
	            for(int i=0; i<1000 ; i++){
	                total += i;
	                System.out.println("total: " + total);
	            }
	            notify();
	        }*/
	   }
	    
	    public  void calculation() throws InterruptedException {
	    	for(int i=0; i<1000000000 ; i++){
	    		//for(int j=0; i<10 ; j++){
                
	    		//}
	    		//System.out.println("ThreadB i=" + i);
	    		if(i %100000000 == 0) {
                	//Thread.sleep(1000);
                	 System.out.println("i = " + i + ", interrupted=" + Thread.currentThread().isInterrupted());
                }
               
            }
		 }
	}
	
	class ThreadB1 extends Thread {
	    int total=0;
	    ThreadA a;
	    @Override
	    public void run() {
	    	try {
	    		System.out.println("ThreadB1 started");
	    		
	    		a.processData2();
				//calculation();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	       /* synchronized(this){
	            for(int i=0; i<1000 ; i++){
	                total += i;
	                System.out.println("total: " + total);
	            }
	            notify();
	        }*/
	   }
	    
	    public  void calculation() throws InterruptedException {
	    	for(int i=0; i<1000000000 ; i++){
	    		//for(int j=0; i<10 ; j++){
                
	    		//}
	    		//System.out.println("ThreadB i=" + i);
	    		if(i %100000000 == 0) {
                	//Thread.sleep(1000);
                	 System.out.println("i = " + i + ", interrupted=" + Thread.currentThread().isInterrupted());
                }
               
            }
		 }
	}
	
	
	class ThreadB2 extends Thread {
	    int total=0;
	    ThreadA a;
	    @Override
	    public void run() {
	    	try {
	    		System.out.println("ThreadB2 started");
	    		
	    		a.processData3();
				//calculation();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	       /* synchronized(this){
	            for(int i=0; i<1000 ; i++){
	                total += i;
	                System.out.println("total: " + total);
	            }
	            notify();
	        }*/
	   }
	    
	    public  void calculation() throws InterruptedException {
	    	for(int i=0; i<1000000000 ; i++){
	    		//for(int j=0; i<10 ; j++){
                
	    		//}
	    		//System.out.println("ThreadB i=" + i);
	    		if(i %100000000 == 0) {
                	//Thread.sleep(1000);
                	 System.out.println("i = " + i + ", interrupted=" + Thread.currentThread().isInterrupted());
                }
               
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
		//int num=10;
		int num=1;
		for(int i = 0; i< num;i++){
			final int index = i;
			Thread t = new Thread(new Runnable(){
               
				public void run() {
					try {
						 ControllerData cd1 = new ControllerData();
						 ControllerData cd2 = new ControllerData();
						//s.sendRequests("hub" + index, ControllerData.SIMPLE_METHOD, cd);
						//s.sendRequests("hub" + index, ControllerData.SYNCHRONIZED_METHOD, cd );
						// s.sendRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, cd1);
						//s.sendRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_PARAM_WITH_SYNCHRONIZED_BLOCK);
						//s.sendRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS_WITH_STATIC_DATA);
						//s.sendRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS);
						 
						//s.sendTwoRequests("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SIMPLE_METHOD, cd );//-
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, ControllerData.SIMPLE_METHOD, cd );//-
						//s.sendTwoRequests("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, cd1 );//-
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, cd );//+
						//s.sendTwoRequests("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS, cd );//+
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, cd );//-
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, ControllerData.SYNCHRONIZED_METHOD, cd1 );//+
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_SUBOBJECT, cd1 );//+
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, ControllerData.SYNCHRONIZED_SUB_DATA_METHOD, cd1 );//-
						
						//s.sendRequests("hub" + index, ControllerData.STATIC_METHOD);
						//s.sendRequests("hub" + index, ControllerData.SYNCHRONIZED_STATIC_METHOD);						
						//s.sendRequests("hub" + index, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM);
						//s.sendRequests("hub" + index, ControllerData.STATIC_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS);
						 
						//s.sendTwoRequests("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM, cd );//-
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM, cd );//-
						//s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM, cd );//+
						// s.sendTwoRequests("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM, cd );//-
						 
						 
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SYNCHRONIZED_METHOD, cd1, cd2 );//-
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SYNCHRONIZED_METHOD, cd1, cd1 );//+
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, cd1, cd1 );//+
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SYNCHRONIZED_METHOD, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, cd1, cd1 );//-
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK, cd1, cd1 );//+
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, cd1, cd2 );//-
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM, cd1, cd2 );//+
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS, cd1, cd2 );//+
						 //s.sendTwoRequestsTwoObjects("hub" + index, ControllerData.SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS, ControllerData.SYNCHRONIZED_STATIC_METHOD_WITH_PARAM, cd1, cd2 );//+
						 
						  //s.sendTwoExchangeRequests("hub" + index, ControllerData.SIMPLE_READ,  ControllerData.SIMPLE_WRITE, cd1, 10);
						  //s.sendTwoExchangeRequests("hub" + index, ControllerData.SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK,  ControllerData.SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK, cd1, 10);
						  //s.sendTwoExchangeRequests("hub" + index, ControllerData.SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS,  ControllerData.SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, cd1, 10);						  
						  s.sendTwoExchangeRequests("hub" + index, ControllerData.SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK,  ControllerData.SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS, cd1, 10);
						  
						 
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}							
				}
			});
			
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
