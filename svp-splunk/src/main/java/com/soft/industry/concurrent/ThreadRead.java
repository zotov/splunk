package com.soft.industry.concurrent;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadRead implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ThreadRead.class);
	
	
	SharedData shareMap;
	int delay = 1000;
	Random rand = new Random();
	
	public ThreadRead(final SharedData data, final int delay) {
		this.shareMap=data;
		this.delay = delay;
	}

	@Override
	public void run() {
		for(;;) {		
	    try{
	    	int mapSize = shareMap.getSize();
	    	if(mapSize==0) {
	    	   log.info("Warn mapSize:{}",mapSize);
	    	   continue;
	    	}
			int num = rand.nextInt(mapSize);
			shareMap.readFromMap(num,delay);
		}catch(Exception ex) {
			log.error("Error read:{}",ex.getMessage());
		}
		}
	}

}
