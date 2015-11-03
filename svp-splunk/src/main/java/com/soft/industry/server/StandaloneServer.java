package com.soft.industry.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.soft.industry.action.read.persisted.ReadPersistedInvoiceAnnot;
import com.soft.industry.action.read.persisted.ReadPersistedInvoiceManual;
import com.soft.industry.action.read.updated.ReadUpdatedInvoiceAnnot;
import com.soft.industry.action.read.updated.ReadUpdatedInvoiceManual;
import com.soft.industry.action.save.SaveInvoiceAnnot;
import com.soft.industry.action.save.SaveInvoiceManual;
import com.soft.industry.action.update.UpdateInvoiceAnnot;
import com.soft.industry.action.update.UpdateInvoiceManual;
import com.soft.industry.company.Company;
import com.soft.industry.concurrent.FillSharedData;
import com.soft.industry.dao.InvoiceDao;

@Component
public class StandaloneServer implements InitializingBean, DisposableBean {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(StandaloneServer.class);
	
	@Autowired
	ApplicationContext appContext;
	
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	@Value("#{appProp['pool.size.server']}")
	int pollSize;

	private ExecutorService pool;	

	public void afterPropertiesSet() throws Exception {
		pool = Executors.newFixedThreadPool(pollSize);
    }
	
	public void executeActions() throws Exception {
		
		log.info("executeActions start");
		Thread t;

			
		//1.1
		/*ReadPersistedInvoiceAnnot taskRead = this.appContext.getBean(ReadPersistedInvoiceAnnot.class);
		t  = new Thread(taskRead);
	    t.start();*/
	    //1.2	    
		/*ReadPersistedInvoiceManual taskReadManual = this.appContext.getBean(ReadPersistedInvoiceManual.class);
	   	t  = new Thread(taskReadManual);
		t.start();	*/			
		
		//2.1
		/*UpdateInvoiceAnnot updateInvoiceAnnot = this.appContext.getBean(UpdateInvoiceAnnot.class);
	   	t  = new Thread(updateInvoiceAnnot);
		t.start();*/	
		
		//2.2		
		/*UpdateInvoiceManual updateInvoiceManual = this.appContext.getBean(UpdateInvoiceManual.class);
	   	t  = new Thread(updateInvoiceManual);
		t.start();*/
		
		//3.1
		/*ReadUpdatedInvoiceAnnot readUpdatedInvoiceAnnot = this.appContext.getBean(ReadUpdatedInvoiceAnnot.class);
	   	t  = new Thread(readUpdatedInvoiceAnnot);
		t.start();	*/
		
		//3.2
		/*ReadUpdatedInvoiceManual readUpdatedInvoiceManual = this.appContext.getBean(ReadUpdatedInvoiceManual.class);
	   	t  = new Thread(readUpdatedInvoiceManual);
		t.start();*/
		
		/*Company company = this.appContext.getBean("company",Company.class);
		company.initCompany();*/
		
		/*FillSharedData fsd  = new FillSharedData();
		fsd.start();*/
		
		log.info("GRADEA:{}",invoiceAnnotDao.getGradeA());
		log.info("GRADEB:{}",invoiceAnnotDao.getGradeB());
		
		
	}		

	public void destroy() throws Exception {
       
	}
}
