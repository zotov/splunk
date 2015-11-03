package com.soft.industry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.soft.industry.server.StandaloneServer;

/**
 * 
 * @author Home
 *
 */
public class Main {
	
		
	public static void main(String[] args) throws Exception {
				      
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml",
				 																	 "hibernate-config.xml"});
        StandaloneServer server = context.getBean(StandaloneServer.class);
        server.executeActions();
     }
	 
}
