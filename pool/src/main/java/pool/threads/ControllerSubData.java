package pool.threads;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerSubData  {
	
	
	public static final String SIMPLE_METHOD = "SIMPLE_METHOD";
	public static final String SYNCHRONIZED_METHOD = "SYNCHRONIZED_METHOD";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK";
	public static final String SIMPLE_METHOD_WITH_PARAM_WITH_SYNCHRONIZED_BLOCK = "SIMPLE_METHOD_WITH_PARAM_WITH_SYNCHRONIZED_BLOCK";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS_WITH_STATIC_DATA = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS_WITH_STATIC_DATA";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_SUBOBJECT = "SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_SUBOBJECT";
	public static final String SIMPLE_METHOD_WITH_SYNCHRONIZED_METHOD_BY_SUBOBJECT_IN_PARAM = "SIMPLE_METHOD_WITH_SYNCHRONIZED_METHOD_BY_SUBOBJECT_IN_PARAM";
	
	public static final String STATIC_METHOD = "STATIC_METHOD";
	public static final String SYNCHRONIZED_STATIC_METHOD = "SYNCHRONIZED_STATIC_METHOD";
	public static final String SYNCHRONIZED_STATIC_METHOD_WITH_PARAM = "SYNCHRONIZED_STATIC_WITH_PARAM_METHOD";
	public static final String STATIC_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS = "STATIC_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS";

	private static final Logger log = LoggerFactory.getLogger(ControllerSubData.class);
	
	int delay;
	
	Map<Integer, List<String>>resultData=new HashMap<Integer, List<String>>();
	static Map<Integer, List<String>>resultStaticData=new HashMap<Integer, List<String>>();
		
		
	public void initData(String hub, int num) {
		List<String>data = new ArrayList<String>();
		
		for(int i=0; i < 10;i++) {
		   data.add(hub +":" + num + ":" + (i+1));
		}
		resultData.put(num, data);
		resultStaticData.put(num, data);
	}
	
	
	public void print(String typeMethod, int num) {
		try {
						
			switch(typeMethod) {
			
//			  case SYNCHRONIZED_METHOD:
//				  printSubDataSynchronized(num);
//		  	  break;
//		  	  
//			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK:
//				  printDataWithSynchronizedBlock(num);
//				  break;
//				  
//			  case SIMPLE_METHOD_WITH_PARAM_WITH_SYNCHRONIZED_BLOCK:
//				  printDataWithSynchronizedBlockWithParam(num, resultData);
//				  break;		
//				  
//				  
//			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS:
//				  printDataWithSynchronizedBlockByClass(num);
//				  break;		
//				  
//			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS_WITH_STATIC_DATA:
//				  printDataWithSynchronizedBlockByClassWithStaticData(num);
//				  break;
//				  
//			  case SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS:
//				  printDataWithSynchronizedBlockByThis(num);
//				  break;		
		  	  		  					  
			  case  SIMPLE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_SUBOBJECT:
				  printDataWithSynchronizedBlockBySubObject(num);
				  break;
				  
			  case  SIMPLE_METHOD_WITH_SYNCHRONIZED_METHOD_BY_SUBOBJECT_IN_PARAM:	  
				  printDataWithSynchronizedMethodBySubObject(num);
				  break;
				  
				  
				  
//			  case  STATIC_METHOD:
//				  printDataStatic(num);
//				  break;				  
//				  
//			  case SYNCHRONIZED_STATIC_METHOD :
//				  printDataStaticSynchronized(num);
//				  break;
//							  
//			  case SYNCHRONIZED_STATIC_METHOD_WITH_PARAM:
//				  printDataStaticSynchronizedWithParam(num, resultData);				  
//				  break;
//				  
//			  case STATIC_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_CLASS:
//				  printDataStaticWithSynchronizedBlockByClass(num);				  
//				  break;
			
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
//	
//	public synchronized void printSubDataSynchronized(int num ) throws InterruptedException {
//		
//		log.info("printSubDataSynchronized start:{}", num);
//		int index=1;
//		for(String data:this.resultData.get(num)) {
//			log.info("{}. {}", index, data);
//			//Thread.sleep(100);
//			index++;
//		}
//		log.info("printSubDataSynchronized end:{}", num);
//	}
//	
//	
//	public void printDataWithSynchronizedBlock(int num) throws InterruptedException {
//		synchronized(this.resultData) {
//			log.info("printDataWithSynchronizedBlock start:{}", num);
//			int index=1;
//			for(String data:this.resultData.get(num)) {
//				log.info("{}. {}", index, data);
//				//Thread.sleep(100);
//				index++;
//			}
//			log.info("printDataWithSynchronizedBlock end:{}", num);
//		}
//	}
//	
//	
//	public void printDataWithSynchronizedBlockWithParam(int num, Map<Integer, List<String>>resultData) throws InterruptedException {
//		synchronized(resultData) {
//			log.info("printDataWithSynchronizedBlockWithParam start:{}", num);
//			int index=1;
//			for(String data:resultData.get(num)) {
//				log.info("{}. {}", index, data);
//				//Thread.sleep(100);
//				index++;
//			}
//			log.info("printDataWithSynchronizedBlockWithParam end:{}", num);
//		}
//	}
//	
//	
//	public void printDataWithSynchronizedBlockByClass(int num) throws InterruptedException {
//		synchronized(ControllerSubData.class) {
//			log.info("printDataWithSynchronizedBlockByClass start:{}", num);
//			int index=1;
//			for(String data:this.resultData.get(num)) {
//				log.info("{}. {}", index, data);
//				//Thread.sleep(100);
//				index++;
//			}
//			log.info("printDataWithSynchronizedBlockByClass end:{}", num);
//		}
//	}
//	
//	
//	public void printDataWithSynchronizedBlockByClassWithStaticData(int num) throws InterruptedException {
//		synchronized(ControllerSubData.class) {
//			log.info("printDataWithSynchronizedBlockByClassWithStaticData start:{}", num);
//			int index=1;
//			for(String data:resultStaticData.get(num)) {
//				log.info("{}. {}", index, data);
//				//Thread.sleep(100);
//				index++;
//			}
//			log.info("printDataWithSynchronizedBlockByClassWithStaticData end:{}", num);
//		}
//	}
//	
//	public void printDataWithSynchronizedBlockByThis(int num) throws InterruptedException {
//		synchronized(this) {
//			log.info("printDataWithSynchronizedBlockByThis start:{}", num);
//			int index=1;
//			for(String data:this.resultData.get(num)) {
//				log.info("{}. {}", index, data);
//				//Thread.sleep(100);
//				index++;
//			}
//			log.info("printDataWithSynchronizedBlockByThis end:{}", num);
//		}
//	}
	
	
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
	
	
	
	public void printDataWithSynchronizedMethodBySubObject(int num) throws InterruptedException {
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

	/*** STATIC ***/
//    public static void printDataStatic(int num) throws InterruptedException {
//		
//		log.info("printDataStatic start:{}", num);
//		int index=1;
//		for(String data:resultStaticData.get(num)) {
//			log.info("{}. {}", index, data);
//			//Thread.sleep(100);
//			index++;
//		}
//		log.info("printDataStatic end:{}", num);
//	}
//    
//    
//     public static synchronized void printDataStaticSynchronized(int num) throws InterruptedException {
//		
//		log.info("printDataStaticSynchronized start:{}", num);
//		int index=1;
//		for(String data:resultStaticData.get(num)) {
//			log.info("{}. {}", index, data);
//			//Thread.sleep(100);
//			index++;
//		}
//		log.info("printDataStaticSynchronized end:{}", num);
//	}
//     
//         
//     
//     public static synchronized void printDataStaticSynchronizedWithParam(int num, Map<Integer, List<String>>resultData) throws InterruptedException {
// 		
// 		log.info("printDataStaticSynchronizedWithParam start:{}", num);
// 		int index=1;
// 		for(String data:resultData.get(num)) {
// 			log.info("{}. {}", index, data);
// 			//Thread.sleep(100);
// 			index++;
// 		}
// 		log.info("printDataStaticSynchronizedWithParam end:{}", num);
// 	}
//     
//     
//     public static void printDataStaticWithSynchronizedBlockByClass(int num) throws InterruptedException {
// 		synchronized(ControllerSubData.class) {
// 			log.info("printDataStaticWithSynchronizedBlockByClass start:{}", num);
// 			int index=1;
// 			for(String data:resultStaticData.get(num)) {
// 				log.info("{}. {}", index, data);
// 				//Thread.sleep(100);
// 				index++;
// 			}
// 			log.info("printDataStaticWithSynchronizedBlockByClass end:{}", num);
// 		}
// 	}
}
