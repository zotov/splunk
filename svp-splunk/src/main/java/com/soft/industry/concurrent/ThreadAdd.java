package com.soft.industry.concurrent;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadAdd implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ThreadAdd.class);
	
	
	 SharedData shareMap;
	int delay = 1000;
	Random rand = new Random();
	int counter;
	
	public ThreadAdd(final SharedData map, final int delay, final int counter) {
		this.shareMap=map;
		this.delay=delay;
		this.counter=counter;
	}

	@Override
	public void run() {
		for(;;) {
			
			String key= ++counter +"";
			String value="data"+counter;
			log.info("try add -  key:{}, value:{}",key,value);
			try{
			   this.shareMap.addToMap(key, value,delay);
			   log.info("added key:{}, value:{}, size:{}, delay:{} ",key ,value, this.shareMap.getSize(), delay);
			}catch(Exception ex) {
				log.error("Error add:{}",ex.getMessage());
			}
		}
	}
	
	

}
