package pool.threads;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HubRequest implements Runnable {
	
	
	

	private static final Logger log = LoggerFactory.getLogger(HubRequest.class);
	
	int delay;
	int num;
	int size;
	String hub;
	String typeMethod;
	ControllerData rd;
	
		
	public HubRequest(int num, int delay, String hub, String typeMethod, ControllerData rd, int size) {
		this.num = num;
		this.delay = delay;
		this.hub = hub;
		this.typeMethod=typeMethod;
		this.rd=rd;
		this.size=size;
	}
	
	public void run() {
		try {
			log.info("thread start num:{}, {}", num, hub);
			
			Thread.sleep(delay);
			switch (typeMethod){
				case ControllerData.SIMPLE_READ:
				case ControllerData.SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK:
				case ControllerData.SIMPLE_READ_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS:
				case ControllerData.SIMPLE_WRITE:
				case ControllerData.SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK:
				case ControllerData.SIMPLE_WRITE_METHOD_WITH_SYNCHRONIZED_BLOCK_BY_THIS:
					rd.exchange(typeMethod, num, size);	
					break;
					
				default:
					rd.print(typeMethod, num);
			}	
			
			
			log.info("thread complete num:{}, {}", num, hub);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	

}
