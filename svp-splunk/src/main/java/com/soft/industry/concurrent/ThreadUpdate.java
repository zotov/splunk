package com.soft.industry.concurrent;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUpdate implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ThreadUpdate.class);
	
	
	 SharedData shareMap;
	int delay = 1000;
	Random rand = new Random();
	
	public ThreadUpdate(final SharedData map, final int delay) {
		this.shareMap=map;
		this.delay = delay;
	}

	@Override
	public void run() {
		for(;;) {
			long timeout=0;
			try {
			    timeout=rand.nextInt(delay);
				Thread.sleep(timeout);
			} catch (InterruptedException ex) {
				log.error("Error sleep:{},{}",delay,ex.getMessage());
			}
			int num = rand.nextInt(this.shareMap.getSize());
			this.shareMap.updateromMap(num, "data" + timeout);
		}
	}

}
