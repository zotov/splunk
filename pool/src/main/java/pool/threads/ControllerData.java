package pool.threads;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerData  {
	
	
	public static final String SIMPLE_METHOD = "SIMPLE_METHOD";
	public static final String SYNCHRONIZED_METHOD = "SYNCHRONIZED_METHOD";
	public static final String SYNCHRONIZED_SUB_DATA_METHOD = "SYNCHRONIZED_SUB_DATA_METHOD";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK";
	public static final String SIMPLE_METHOD_WITH_PARAM_WITH_SYNCHRONIZED_BLOCK = "SIMPLE_METHOD_WITH_PARAM_WITH_SYNCHRONIZED_BLOCK";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS_WITH_STATIC_DATA = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS_WITH_STATIC_DATA";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_SUBOBJECT = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_SUBOBJECT";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_METHOD_BY_SUBOBJECT_WITH_PARAM = "SIMPLE_METHOD_WITH_SYNCHRONIZED_METHOD_BY_SUBOBJECT_IN_PARAM";
	
	public static final String STATIC_METHOD = "STATIC_METHOD";
	public static final String SYNCHRONIZED_STATIC_METHOD = "SYNCHRONIZED_STATIC_METHOD";
	public static final String SYNCHRONIZED_STATIC_METHOD_WITH_PARAM = "SYNCHRONIZED_STATIC_WITH_PARAM_METHOD";
	public static final String STATIC_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS = "STATIC_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS";
	
	
	public static final String SIMPLE_READ = "SIMPLE_READ";
	public static final String SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK = "SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK";
	public static final String SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS = "SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS";
	
	public static final String SIMPLE_WRITE = "SIMPLE_WRITE";	
	public static final String SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK = "SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK";	
	public static final String SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS = "SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS";	
	

	private static final Logger log = LoggerFactory.getLogger(ControllerData.class);
	
	int delay;
	long rwdelay=1;
	
	Map<Integer, List<String>>resultData=new HashMap<Integer, List<String>>();
	static Map<Integer, List<String>>resultStaticData=new HashMap<Integer, List<String>>();
	ControllerSubData csd= new ControllerSubData();
		
		
	public void initData(String hub, int num) {
		List<String>data = new ArrayList<String>();
		
		for(int i=0; i < 10;i++) {
		   data.add(hub +":" + num + ":" + (i+1));
		}
		resultData.put(num, data);
		resultStaticData.put(num, data);
		
		csd.initData(hub, num);
	}
	
	
	public void exchange(String typeMethod, int num, int size) throws InterruptedException {
		switch(typeMethod) {
		
	  /*** READ ****/
	  case SIMPLE_READ:
		  this.readData(num);		
		break;
		  
	  case SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK :
		  this.readDataWithSynchronizedBlock(num);		
		break;
		  
	  case SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS:
		  this.readDataWithSynchronizedBlockByThis(num);		
		break;
		  
	  /*** WRITE ****/				  
	  case SIMPLE_WRITE:
		  this.writeData(num, size); 
		break;
		  
	  case SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK:
		  this.writeDataSyncrhonizedBlock(num, size); 
		break;		 
		
	  case SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS:
		  this.writeDataWithSynchronizedBlockByThis(num, size); 
		break;
		  
	  }
   }
	
	
	public void print(String typeMethod, int num) {
		try {
						
			switch(typeMethod) {
			
			 /*** PRINT ****/
			  case SYNCHRONIZED_METHOD:
				  printDataSynchronized(num);
		  	  break;
		  	  
			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK:
				  printDataWithSynchronizedBlock(num);
				  break;
				  
			  case SIMPLE_METHOD_WITH_PARAM_WITH_SYNCHRONIZED_BLOCK:
				  printDataWithSynchronizedBlockWithParam(num, resultData);
				  break;		
				  
				  
			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS:
				  printDataWithSynchronizedBlockByClass(num);
				  break;		
				  
			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS_WITH_STATIC_DATA:
				  printDataWithSynchronizedBlockByClassWithStaticData(num);
				  break;
				  
			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS:
				  printDataWithSynchronizedBlockByThis(num);
				  break;		
		  	  		  					  
			  case  SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_SUBOBJECT:
				  printDataWithSynchronizedBlockBySubObject(num);
				  break;
				  
			  case  SIMPLE_METHOD_WITH_SYNCHRONIZED_METHOD_BY_SUBOBJECT_WITH_PARAM:	  
				  printDataWithSynchronizedMethodBySubObjectWithParam(num);
				  break;
				  
				  
			  case  SYNCHRONIZED_SUB_DATA_METHOD:  
				  pringDataSynchonyzedMethodSubData(num);
				  break;
				
			 
				  
			  /*** PRINT ****/
			  case  STATIC_METHOD:
				  printDataStatic(num);
				  break;				  
				  
			  case SYNCHRONIZED_STATIC_METHOD :
				  printDataStaticSynchronized(num);
				  break;
				 			 
							  
			  case SYNCHRONIZED_STATIC_METHOD_WITH_PARAM:
				  printDataStaticSynchronizedWithParam(num, resultData);				  
				  break;
				  
			  case STATIC_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS:
				  printDataStaticWithSynchronizedBlockByClass(num);				  
				  break;
			
			  default://SIMPLE_METHOD
			    printData(num);
			}
		
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	public void printData(int num) throws InterruptedException {
		log.info("printData start:{}", num);
		int index=1;
		for(String data:this.resultData.get(num)) {
			log.info("{}. {}", index, data);
			//Thread.sleep(100);
			index++;
		}
		log.info("printData end:{}", num);
	}
	
	public synchronized void printDataSynchronized(int num ) throws InterruptedException {
		
		log.info("printDataSynchronized start:{}", num);
		int index=1;
		for(String data:this.resultData.get(num)) {
			log.info("{}. {}", index, data);
			//Thread.sleep(100);
			index++;
		}
		log.info("printDataSynchronized end:{}", num);
	}
	
	
	public void printDataWithSynchronizedBlock(int num) throws InterruptedException {
		synchronized(this.resultData) {
			log.info("printDataWithSynchronizedBlock start:{}", num);
			int index=1;
			for(String data:this.resultData.get(num)) {
				log.info("{}. {}", index, data);
				//Thread.sleep(100);
				index++;
			}
			log.info("printDataWithSynchronizedBlock end:{}", num);
		}
	}
	
	
	public void printDataWithSynchronizedBlockWithParam(int num, Map<Integer, List<String>>resultData) throws InterruptedException {
		synchronized(resultData) {
			log.info("printDataWithSynchronizedBlockWithParam start:{}", num);
			int index=1;
			for(String data:resultData.get(num)) {
				log.info("{}. {}", index, data);
				//Thread.sleep(100);
				index++;
			}
			log.info("printDataWithSynchronizedBlockWithParam end:{}", num);
		}
	}
	
	
	public void printDataWithSynchronizedBlockByClass(int num) throws InterruptedException {
		synchronized(ControllerData.class) {
			log.info("printDataWithSynchronizedBlockByClass start:{}", num);
			int index=1;
			for(String data:this.resultData.get(num)) {
				log.info("{}. {}", index, data);
				//Thread.sleep(100);
				index++;
			}
			log.info("printDataWithSynchronizedBlockByClass end:{}", num);
		}
	}
	
	
	public void printDataWithSynchronizedBlockByClassWithStaticData(int num) throws InterruptedException {
		synchronized(ControllerData.class) {
			log.info("printDataWithSynchronizedBlockByClassWithStaticData start:{}", num);
			int index=1;
			for(String data:resultStaticData.get(num)) {
				log.info("{}. {}", index, data);
				//Thread.sleep(100);
				index++;
			}
			log.info("printDataWithSynchronizedBlockByClassWithStaticData end:{}", num);
		}
	}
	
	public void printDataWithSynchronizedBlockByThis(int num) throws InterruptedException {
		synchronized(this) {
			log.info("printDataWithSynchronizedBlockByThis start:{}", num);
			int index=1;
			for(String data:this.resultData.get(num)) {
				log.info("{}. {}", index, data);
				//Thread.sleep(100);
				index++;
			}
			log.info("printDataWithSynchronizedBlockByThis end:{}", num);
		}
	}
	
	
	public void printDataWithSynchronizedBlockBySubObject(int num) throws InterruptedException {
		List<String> listData = resultData.get(num);
		synchronized(listData) {
			log.info("printDataWithSynchronizedBlockByThis start:{}", num);
			int index=1;
			for(String data:listData) {
				log.info("{}. {}", index, data);
				//Thread.sleep(100);
				index++;
			}
			log.info("printDataWithSynchronizedBlockByThis end:{}", num);
		}
	}
	
	
	
	public void printDataWithSynchronizedMethodBySubObjectWithParam(int num) throws InterruptedException {
		List<String> listData = resultData.get(num);
		pringDataSynchonyzedMethodSubObject(listData, num);
	}
	
	public synchronized void pringDataSynchonyzedMethodSubObject(final List<String> listData, final int num) {
		log.info("pringDataSynchonyzedMethodSubObject start:{}", num);
		int index=1;
		for(String data:listData) {
			log.info("{}. {}", index, data);
			//Thread.sleep(100);
			index++;
		}
		log.info("pringDataSynchonyzedMethodSubObject end:{}", num);
	}
	
	public void printDataWithSynchronizedMethodBySubObject(int num) throws InterruptedException {
		List<String> listData = resultData.get(num);
		pringDataSynchonyzedMethodSubObject(listData, num);
	}
	
	
	public  void pringDataSynchonyzedMethodSubData(final int num) {
		log.info("pringDataSynchonyzedMethodSubData start:{}", num);
		this.csd.print(ControllerSubData.SYNCHRONIZED_METHOD, num);
		log.info("pringDataSynchonyzedMethodSubData end:{}", num);
	}
	
	
	/*** WRITE 
	 * @throws InterruptedException ***/	
	public void writeData(int num, int size) throws InterruptedException {
		log.info("writeData start:{}", num);	
		List<String> listData = new ArrayList<String>();
		resultData.put(num, listData);
		//log.info("write ");
		for(int i=0; i< size; i++) {
			String data=num +":" + i;
			listData.add(data);
			log.info("write data={}:{}",(i+1), data);
			Thread.sleep(rwdelay);		
		}
		
		log.info("writetData end:{}", num);		
	}
	
	public void writeDataSyncrhonizedBlock(int num, int size) throws InterruptedException {
		log.info("writeData start:{}", num);	
		List<String> listData = new ArrayList<String>();
		this.resultData.put(num, listData);
		//log.info("write ");
		synchronized(this.resultData) {		
			for(int i=0; i< size; i++) {
				String data=num +":" + i;
				listData.add(data);
				log.info("write data={}:{}",(i+1), data);
				Thread.sleep(rwdelay);		
			}
		}
		log.info("writetData end:{}", num);		
	}
	
	
	public void writeDataWithSynchronizedBlockByThis(int num, int size) throws InterruptedException {
		log.info("writeData start:{}", num);	
		List<String> listData = new ArrayList<String>();
		this.resultData.put(num, listData);
		//log.info("write ");
		synchronized (this) {		
			for(int i=0; i< size; i++) {
				String data=num +":" + i;
				listData.add(data);
				log.info("write data={}:{}",(i+1), data);
				Thread.sleep(rwdelay);		
			}
		}
		log.info("writetData end:{}", num);		
	}
	
	
	/*** READ ***/	
	public  void readData(int num) throws InterruptedException {
		log.info("readData start:{}", num);		
		List<String> listData =	null;
		
		do {
		    listData =resultData.get(num);		    
		} while(listData == null || listData.size() < 1);		
		
		int index=1;
		for(String data:listData) {
			log.info("read data={}:{}", index, data);
			Thread.sleep(rwdelay);
			index++;
		}
		
		log.info("readData end:{}", num);		
	}
	
	public  void readDataWithSynchronizedBlock(int num) throws InterruptedException {
		log.info("readData start:{}", num);		
		List<String> listData =	null;
		
		do {
		    listData =resultData.get(num);		    
		} while(listData == null || listData.size() < 1);		
		
		synchronized(resultData){
			int index=1;
			for(String data:listData) {
				log.info("read data={}:{}", index, data);
				Thread.sleep(rwdelay);
				index++;
			}
		}
		log.info("readData end:{}", num);		
	}
	
	public  void readDataWithSynchronizedBlockByThis(int num) throws InterruptedException {
		log.info("readData start:{}", num);		
		List<String> listData =	null;
		
		do {
		    listData =resultData.get(num);		    
		} while(listData == null || listData.size() < 1);		
		
		synchronized(this){
			int index=1;
			for(String data:listData) {
				log.info("read data={}:{}", index, data);
				Thread.sleep(rwdelay);
				index++;
			}
		}
		log.info("readData end:{}", num);		
	}

	/*** STATIC ***/
    public static void printDataStatic(int num) throws InterruptedException {
		
		log.info("printDataStatic start:{}", num);
		int index=1;
		for(String data:resultStaticData.get(num)) {
			log.info("{}. {}", index, data);
			//Thread.sleep(100);
			index++;
		}
		log.info("printDataStatic end:{}", num);
	}
    
    
     public static synchronized void printDataStaticSynchronized(int num) throws InterruptedException {
		
		log.info("printDataStaticSynchronized start:{}", num);
		int index=1;
		for(String data:resultStaticData.get(num)) {
			log.info("{}. {}", index, data);
			//Thread.sleep(100);
			index++;
		}
		log.info("printDataStaticSynchronized end:{}", num);
	}
     
         
     
     public static synchronized void printDataStaticSynchronizedWithParam(int num, Map<Integer, List<String>>resultData) throws InterruptedException {
 		
 		log.info("printDataStaticSynchronizedWithParam start:{}", num);
 		int index=1;
 		for(String data:resultData.get(num)) {
 			log.info("{}. {}", index, data);
 			//Thread.sleep(100);
 			index++;
 		}
 		log.info("printDataStaticSynchronizedWithParam end:{}", num);
 	}
     
     
     public static void printDataStaticWithSynchronizedBlockByClass(int num) throws InterruptedException {
 		synchronized(ControllerData.class) {
 			log.info("printDataStaticWithSynchronizedBlockByClass start:{}", num);
 			int index=1;
 			for(String data:resultStaticData.get(num)) {
 				log.info("{}. {}", index, data);
 				//Thread.sleep(100);
 				index++;
 			}
 			log.info("printDataStaticWithSynchronizedBlockByClass end:{}", num);
 		}
 	}
}
