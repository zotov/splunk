package com.soft.industry.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FillSharedData {
	
	
	private ExecutorService pool =  Executors.newFixedThreadPool(10);
	
	
	
	public void start() throws InterruptedException {
		
		int counter = 5;
		int delay = 50;
		SharedData sd = new SharedData();
		for(int i=0; i< counter;i++) {
			sd.addToMap("" +i, "data"+i,1L);
		}
		
		for(int i =0; i < 3;i++) {
			ThreadAdd tadd = new ThreadAdd(sd,delay/3,counter);
		    pool.execute(tadd);
		}
		
		ThreadRead tread = new ThreadRead(sd,delay/2);
		pool.execute(tread);
		
		ThreadRemove trem = new ThreadRemove(sd,delay);
		pool.execute(trem);
		
		pool.shutdown();
		
    }
	
}
