package com.soft.industry.concurrent;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadRemove implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ThreadRemove.class);
	
	
	 SharedData shareMap;
	int delay = 1000;
	Random rand = new Random();
	
	public ThreadRemove(final SharedData map, final int delay) {
		this.shareMap=map;
		this.delay = delay;
	}

	@Override
	public void run() {
		for(;;) {
			int num =0;		
			try {
				int upperIndex = this.shareMap.getSize();
				if(upperIndex==0) {
					log.info("Warn shareMap size:{}",upperIndex);
					continue;
				}
				this.shareMap.remFromMap(num,delay);
			} catch (Exception ex) {
				log.error("Error remove num:{}",num,ex);
			}
	    }
	}

}
