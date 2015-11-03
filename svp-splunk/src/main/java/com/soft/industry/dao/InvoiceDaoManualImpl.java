package com.soft.industry.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Session.LockRequest;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soft.industry.entity.Company;
import com.soft.industry.entity.Country;
import com.soft.industry.entity.GRADEA;
import com.soft.industry.entity.GRADEB;
import com.soft.industry.entity.Invoice;
import com.soft.industry.entity.InvoiceEnt;
import com.soft.industry.thread.annotation.ReadRec;
import com.soft.industry.thread.annotation.SaveRec;
import com.soft.industry.thread.annotation.Semaphore;
import com.soft.industry.thread.annotation.UpdateRec;

@Repository("invoiceDaoManualImpl")
public class InvoiceDaoManualImpl implements InvoiceDao {

	private static final Logger log = LoggerFactory.getLogger(InvoiceDaoManualImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;

	public int getInvoiceCount(Class<?> classInst) {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		Criteria criteria = session.createCriteria(classInst).setLockMode(LockMode.PESSIMISTIC_READ);
		List<Invoice> list = criteria.list();
		Number count = list.size();
		t.commit();
		session.close();		
		return count.intValue();
	}
	
	@Override
	public Invoice getInvoiceByIdUn(Class<?> classInst, long id) {
		return this.getInvoice(classInst, id, LockMode.NONE);
	}

	@Override
	public Invoice getInvoiceByIdCom(Class<?> classInst,  long id) {
		return this.getInvoice(classInst, id, LockMode.READ);
	}
	
	@Override
	public Invoice getInvoiceByIdComOP(Class<?> classInst,  long id) {
		return this.getInvoice(classInst, id, LockMode.OPTIMISTIC);
	}
	
	@Override
	public Invoice getInvoiceByIdComOPFI(Class<?> classInst,  long id) {
		return this.getInvoice(classInst, id, LockMode.OPTIMISTIC_FORCE_INCREMENT);
	}

	@Override
	public Invoice getInvoiceByIdRep(Class<?> classInst,  long id) {
		return this.getInvoice(classInst, id, LockMode.PESSIMISTIC_READ);
	}

	@Override
	public Invoice getInvoiceByIdSer(Class<?> classInst,  long id) {
		return this.getInvoice(classInst, id, LockMode.PESSIMISTIC_WRITE);
	}
		
	@Override
	public Invoice getInvoiceByIdSerFI(Class<?> classInst,  long id) {
		return this.getInvoice(classInst, id, LockMode.PESSIMISTIC_FORCE_INCREMENT);
	}
	
	public Invoice getInvoice(Class<?> classInst,long id, final LockMode lockMode) {
		Invoice invoice = null;
		Session session = null;
		Transaction t = null;
		try {
			session = sessionFactory.openSession();
			t = session.beginTransaction();
			invoice = (Invoice) session.get(classInst, id,new LockOptions(lockMode));
		} catch(Exception ex) {
			log.error("Error getInvoice, id:{},error mess:{}",id, ex.getMessage());
		} finally {
			try{
				if(t!=null){
				  t.commit();
				}
				if(session!=null){
				   session.close();
				}
			} catch(Exception ex) {
				log.error("Error getInvoice finally, id:{}, error mess:{}",id, ex.getMessage());
			}
		}
		return invoice;
	}	

	public int getInvoiceCountUn(final Class<?>classInst) {
	       return this.getInvoiceCount(classInst, LockMode.NONE);	  
	}

	public int getInvoiceCountCom(final Class<?>classInst) {
		 return this.getInvoiceCount(classInst, LockMode.READ);	  
	}
	
	public int getInvoiceCountComOP(final Class<?>classInst) {
		 return this.getInvoiceCount(classInst, LockMode.OPTIMISTIC);	  
	}
	
	public int getInvoiceCountComOPFI(final Class<?>classInst) {
		return this.getInvoiceCount(classInst, LockMode.OPTIMISTIC_FORCE_INCREMENT);	  
	}

	public int getInvoiceCountRep(final Class<?>classInst) {
		return this.getInvoiceCount(classInst, LockMode.PESSIMISTIC_READ);	 
	}

	public int getInvoiceCountSer(final Class<?>classInst) {
		return this.getInvoiceCount(classInst, LockMode.PESSIMISTIC_WRITE);	 
	}
	
	public int getInvoiceCountSerFI(final Class<?>classInst) {
		return this.getInvoiceCount(classInst, LockMode.PESSIMISTIC_FORCE_INCREMENT);	 
	}

	public int getInvoiceCountSerVer(final Class<?>classInst, final LockMode lockMode) {
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
			Criteria criteria = session.createCriteria(classInst,"invoice").
								setLockMode(lockMode);
			
			@SuppressWarnings("unchecked")
			List<Invoice> invoiceList =  criteria.list();
			int count = invoiceList==null?0:invoiceList.size();
			session.flush();
			
		t.commit();
		session.close();
		return count;
	}
	
	public int getInvoiceCount(final Class<?>classInst, LockMode lockmode) {
		    int count =0;
		    Session session = sessionFactory.openSession();
		    Transaction t = session.beginTransaction();
		    try {
				 Criteria criteria = session.createCriteria(classInst,"invoice").setLockMode(lockmode);
					
				  @SuppressWarnings("unchecked")
				  List<Invoice> invoiceList =  criteria.list();
				  count = invoiceList==null?0:invoiceList.size();
			      session.flush();
				  t.commit();
		     } finally {
		    	if(session!=null) {
		    		 session.close(); 		
		    	}
		    }
		   
		return count;
	}

	public void saveInvoiceDef(Invoice invoice,final long delay)throws InterruptedException {
		 this.saveInvoice(invoice, delay);
	}
	
	public void saveInvoiceSer(Invoice invoice,final long delay)throws InterruptedException {
		 this.saveInvoice(invoice, delay);
	}
	
	// TODO Auto-generated method stub
	/*public void saveInvoiceSer(final Invoice invoice,final long delay) throws InterruptedException {
	
		 Session session = sessionFactory.openSession();
	     Transaction t =	session.beginTransaction();
	     LockRequest lockReq =   session.buildLockRequest(new LockOptions(LockMode.PESSIMISTIC_WRITE));
	     
		 log.info("saveInvoiceManualSer:{}",delay);
		 Semaphore.decrement(delay);
				 
		 session.save(invoice);	
		 lockReq.lock(invoice); 
		 
		 SaveRec.releasDelay();
		 ReadRec.releasAll();
				 
		 Thread.sleep(delay);
		 //session.save(invoice);	
		 session.flush();
		
		 t.commit();		
		 session.close();		
	}*/
	
	// TODO Auto-generated method stub
	/*public void saveInvoiceSerVer(final Invoice invoice,final long delay) throws InterruptedException {
		
			 Session session = sessionFactory.openSession();
		     Transaction t =  session.beginTransaction();
		     		    		    
		     //Criteria criteria =  session.createCriteria(InvoiceNum.class).setLockMode(LockMode.PESSIMISTIC_FORCE_INCREMENT);
			 Semaphore.decrement(delay);
			 SaveRec.releasDelay();
			 log.info("saveInvoiceManualSerVer:{}",delay);
			 
			 //criteria.list();
			 
			 session.save(invoice);
			 session.buildLockRequest(new LockOptions(LockMode.PESSIMISTIC_FORCE_INCREMENT)).lock("InvoiceNum", invoice);
			 //lockReq.lock(invoice);
			 			
			 ReadRec.releasAll();
									 
			 Thread.sleep(delay);
			 //session.save(invoice);
			 session.flush();
			
			 t.commit();		
			 session.close();		
	}*/
	
	public void saveInvoice(final Invoice invoice, final long delay) {

		Session session = sessionFactory.openSession();
		
		Transaction t = session.beginTransaction();
		
		try {
			
			session.save(invoice);
			Semaphore.decrement(delay);
			SaveRec.releasDelay();
			ReadRec.releasAll();

			log.info("saveInvoice:{}, {}", invoice.getId(),delay);
			Thread.sleep(delay);
			
			session.flush();
			t.commit();
		} 
		catch (Exception ex) {
		    if (t != null) { 
		    	t.rollback();
		    }
		    log.error("Error invoice:{}, {}",invoice,ex.getMessage());
		}
		finally {
			if(session != null) {
			   session.close();
			}
		}
	}

	/*public void persistInvoiceUn(Invoice invoice,final long delay) {
		// TODO Auto-generated method stub
		
	}

	public void persistInvoiceSer(Invoice invoice,final long delay) {
		// TODO Auto-generated method stub
		
	}*/

	public void updateInvoice(Invoice invoice) {
      
		
	}

	public void insertInvoice(Invoice invoice) {
		// TODO Auto-generated method stub
		
	}

	public void deletInvoice(int id) {
		// TODO Auto-generated method stub
	}
	
	public Invoice updateInvoiceUn(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id, delay,"updateInvoiceUn:{}",LockMode.NONE);
	}

	public Invoice updateInvoiceCom(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id, delay,"updateInvoiceCom:{}",LockMode.READ);	
	}
	
	public Invoice updateInvoiceComOP(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id, delay,"updateInvoiceComOp:{}",LockMode.OPTIMISTIC);	
	}
	
	public Invoice updateInvoiceComOPFI(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id, delay,"updateInvoiceComOPFI:{}",LockMode.OPTIMISTIC_FORCE_INCREMENT);	
	}

	public Invoice updateInvoiceRep(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id, delay,"updateInvoiceRep:{}",LockMode.PESSIMISTIC_READ);	
	}
	
	public Invoice updateInvoiceSer(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id, delay,"updateInvoiceMunSer:{}",LockMode.PESSIMISTIC_WRITE);	
	}
	
	public Invoice updateInvoiceSerFI(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id, delay,"updateInvoiceMunSer:{}",LockMode.PESSIMISTIC_FORCE_INCREMENT);	
	}	
	
	private Invoice update(Class<?> classInst, long id,final long delay,final String message,final LockMode lockMode)throws InterruptedException {
		Session session = null;
	    Transaction t = null;
	    Invoice invoice = null;
	    try{
	    	session = sessionFactory.openSession();
		    t = session.beginTransaction();
			invoice = (Invoice) session.get(classInst, id,new LockOptions(lockMode));
			String comment = getComment(invoice.getComment(),delay);
			invoice.setComment(comment);
			session.update(invoice);
						
			Semaphore.decrement(delay);
			UpdateRec.releasDelay();
			UpdateRec.releasAll();
						
			log.info(message,comment);
			ReadRec.releasAll();
			Thread.sleep(delay);
			session.flush();
			t.commit();
	    }
	    catch(Exception ex) {
	       log.error("Error update {}, id:{}, {}",classInst,id,ex.getMessage());
	        if (t != null) { 
			   	t.rollback();
			}
	    }finally{
	       if(session!=null) {
	   	       session.close();
	       }
	    }
		 return invoice;
	}
	
	private String getComment(final String comment, final long delay) {
		if(comment.contains("+")) {
			return Thread.currentThread().getName() + ", "+ delay + " :-";
		} else {
			return Thread.currentThread().getName() + ", "+ delay + " :+";
		}
	}

	@Override
	public Invoice getInvoice(Class<?> classInst) {
		Session session = null;
		Transaction t = null;
		try {
			 session = sessionFactory.openSession();
			 t =  session.beginTransaction();
			 Criteria criteria = session.createCriteria(classInst, "invoice");
			 criteria.addOrder(Order.desc("invoice.id"));
			 if (criteria.list().size() > 0) {
			 	return (Invoice) criteria.list().get(0);
			 }
			 session.flush();
			 t.begin();
		} 
        catch (Exception ex) {
			log.error("Error update {}, {}", classInst, ex.getMessage());
			if (t != null) {
				t.rollback();
			}
		} finally {
			if (t != null) {
				t.commit();
			}
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public List<Country> getCountries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GRADEA getGradeA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GRADEB getGradeB() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
