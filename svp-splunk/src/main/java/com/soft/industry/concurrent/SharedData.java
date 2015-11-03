package com.soft.industry.concurrent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soft.industry.dao.InvoiceDaoAnnotIml;

public class SharedData {
	
	private static final Logger log = LoggerFactory.getLogger(SharedData.class);
	
	private int counter;
	
	private static Map<String,String> sharedMap = new HashMap<String,String>();
		

	public int getCounter() {
		log.info("getCounter:{}",counter);
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
		log.info("getCounter:{}",counter);
	}	
	
	public void addToCounter(int data) {
		log.info("addToCounter prev:{},{}",counter,data);
		this.counter+=data;
		log.info("addToCounter after:{},{}",counter,data);
	}	
	
	public void decCounter(int data) {
		log.info("decCounter prev :{},{}",counter,data);
		this.counter-=data;
		log.info("decCounter after:{},{}",counter,data);
	}
	
	public void addToMap(final String key , final String value,final long delay) throws InterruptedException {
		 Thread.sleep(delay);
		 //synchronized(this){	
		 //synchronized(sharedMap) {	
		 synchronized(SharedData.class) {	 
		   sharedMap.put(key, value);
	    }
	}
	
	/*public void remFromMap(final String key) {
		 synchronized(this){		 
		
		this.sharedMap.remove(key);
		 }
	}*/
		
	
	public  void readFromMap(final int index, long delay) throws InterruptedException {
		
		log.info("try readFromMap index:{}, size:{}",index,this.sharedMap.size());
		//synchronized(this) {	
		//synchronized(sharedMap) {	
		synchronized(SharedData.class) {
			int num=0;
			for(Iterator<String>iterator=this.sharedMap.keySet().iterator();iterator.hasNext();) {
				String key = iterator.next();
				if( num++ == index) {	
					Thread.sleep(delay);
					String value = this.sharedMap.get(key);
					log.info("readFromMap index:{} num:{}, key:{},value:{}, map size:{}",index,num,key,value,this.sharedMap.size());
				}
			}
		}
	}
	
	public void remFromMap(final int index,long delay) throws InterruptedException {
		//Thread.sleep(delay);
		//synchronized(this) {	
		//synchronized(sharedMap) {	
		synchronized(SharedData.class) {
			int num=0;
			for(Iterator<String>iterator=this.sharedMap.keySet().iterator();iterator.hasNext();) {
				if(num++ == index) {
					String key = iterator.next();
					log.info("try removeFromMap index:{}, key:{}",index, key);
					Thread.sleep(delay);
					String value = this.sharedMap.get(key);
		            iterator.remove();
					log.info("removeFromMap index:{}, key:{},value:{},size:{}",index, key, value, this.sharedMap.size());
				}
			}
		}		
		//log.info("remFromMap  after size:{}",this.sharedMap.size());
	}
	
	public void updateromMap(final int index, final String value) {
		log.info("updateMap  prev index:{}, size:{}",index, this.getSize());
		int num=0;
		for(Iterator<String>iterator=this.sharedMap.keySet().iterator();iterator.hasNext();) {
			if(num++==index) {
				String key = iterator.next();
				this.sharedMap.put(key, value);
				log.info("remFromMap  key:{},value:{}",key, value);
			}
		}
		
		log.info("remFromMap  after size:{}",this.sharedMap.size());
	}
	
	public String getFromMap(final String key ) {
		log.info("getFromMap - key:{}",key);
		return this.sharedMap.get(key);
	}
	
	public int getSize() {
		int size=0; 
	
		//synchronized(this.sharedMap){
		  size = this.sharedMap.size();
		//}
		return  size;
	}

	public Map<String, String> getSharedMap() {
		return sharedMap;
	}

	public void setSharedMap(Map<String, String> sharedMap) {
		this.sharedMap = sharedMap;
	}	
}
