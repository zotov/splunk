package pool.threads;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HubRequest implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(HubRequest.class);
	
	int delay;
	int num;
	String hub;
		
	public HubRequest(int num,int delay,String hub) {
		this.num = num;
		this.delay = delay;
		this.hub = hub;
	}
	
	public void run() {
		try {
			log.info("thread start num:{}, hub:{}",num,hub);
			Thread.sleep(delay);
			log.info("thread complete num:{}, hub:{}",num,hub);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}
