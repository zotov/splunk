package com.soft.industry.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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

@Repository("invoiceAnnotDao")
public class InvoiceDaoAnnotIml implements InvoiceDao {
	
	private static final Logger log = LoggerFactory.getLogger(InvoiceDaoAnnotIml.class);
	
	@Autowired
	SessionFactory sessionFactory;

    //	@Transactional(readOnly=true,isolation=Isolation.READ_COMMITTED)
	public int getInvoiceCount(Class<?> classInst) {
		Session session = sessionFactory.getCurrentSession();
		//Criteria criteria = session.createCriteria(classInst).setProjection(Projections.rowCount());
		//Number count = (Number) criteria.uniqueResult();
		Criteria criteria = session.createCriteria(classInst);
		List<Invoice> list = criteria.list();
		return list != null ? list.size() : 0;
	}

	@Override
	@Transactional(readOnly=true, isolation = Isolation.READ_UNCOMMITTED)
	public Invoice getInvoiceByIdUn(Class<?> classInst, long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Invoice) session.get(classInst, id);
	}

	@Override
	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public Invoice getInvoiceByIdCom(Class<?> classInst, long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Invoice) session.get(classInst, id);
	}
	
	@Override
	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public Invoice getInvoiceByIdComOP(Class<?> classInst, long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Invoice) session.get(classInst, id);
	}	
	
	@Override
	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public Invoice getInvoiceByIdComOPFI(Class<?> classInst, long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Invoice) session.get(classInst, id);
	}

	@Override
	@Transactional(readOnly=true, isolation = Isolation.REPEATABLE_READ)
	public Invoice getInvoiceByIdRep(Class<?> classInst, long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Invoice) session.get(classInst, id);
	}

	@Override
	@Transactional(readOnly=true, isolation = Isolation.SERIALIZABLE)
	public Invoice getInvoiceByIdSer(Class<?> classInst, long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Invoice) session.get(classInst, id);
	}
	
	public Invoice getInvoice(final Class<?> classInst, long id, final LockMode lockMode) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(classInst,"invoice");
		criteria.add(Restrictions.gt("id", id));
		@SuppressWarnings("unchecked")
		List<Invoice> list = criteria.list();
		return list.get(0);
	}	

	public void updateInvoice(Invoice invoice) {
		// TODO Auto-generated method stub
	}

	public void insertInvoice(Invoice invoice) {
				
	}

	public void deletInvoice(int id) {
		// TODO Auto-generated method stub
	}

	@Transactional
	public Invoice getInvoice(final Class<?> classInst) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(classInst,"invoice");
		criteria.addOrder(Order.desc("invoice.id"));
		if(criteria.list().size() > 0) {
			return (Invoice) criteria.list().get(0);
		}
		return null;
	}

	@Transactional(readOnly=true, isolation = Isolation.READ_UNCOMMITTED)
	public int getInvoiceCountUn(final Class<?>classInst) {
		return this.getInvoiceCount(classInst);
	}
	
	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public int getInvoiceCountCom(final Class<?>classInst) {
		return this.getInvoiceCount(classInst);
	}
	
	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public int getInvoiceCountComOP(final Class<?>classInst) {
		return 0;
	}
	
	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	public int getInvoiceCountComOPFI(final Class<?>classInst) {
		return 0;
	}

	@Transactional(readOnly=true, isolation = Isolation.REPEATABLE_READ)
	public int getInvoiceCountRep(final Class<?>classInst) {
		return this.getInvoiceCount(classInst);
	}
	
	@Transactional(readOnly=true, isolation = Isolation.SERIALIZABLE)
	public int getInvoiceCountSer(final Class<?>classInst) {
		return this.getInvoiceCount(classInst);
	}
	
	
	@Transactional(readOnly=true, isolation = Isolation.SERIALIZABLE)
	public int getInvoiceCountSerFI(final Class<?>classInst) {
		return 0;
	}
	
	@Transactional(readOnly=true, isolation = Isolation.SERIALIZABLE)
	public int getInvoiceCountSerVer(final Class<?>classInst,final LockMode lockMode) {
		return this.getInvoiceCount(classInst);
	}	
	
	
	
	@Transactional(isolation=Isolation.DEFAULT)
	public void saveInvoiceDef(Invoice invoice,final long delay) throws InterruptedException  {
		this.saveInvoice(invoice, "saveInvoiceDef, id:{}, delay:{}", delay);
	}
		
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void saveInvoiceSer(Invoice invoice,final long delay) throws InterruptedException {
		this.saveInvoice(invoice, "saveInvoiceSer, id:{}, delay:{}", delay);
	}	
	
    public void saveInvoice(final Invoice invoice,final String message, final long delay) throws InterruptedException {
    	Semaphore.decrement(delay);
		Session session = sessionFactory.getCurrentSession();
		session.save(invoice);
		SaveRec.releasDelay();
		ReadRec.releasAll();
		log.info(message,invoice.getId(),delay);
		Thread.sleep(delay);
	}
	
	public void saveInvoice(Invoice invoice,final long delay) {
		
	}
	
	/*@Transactional
	public void persistInvoiceUn(Invoice invoice,final long delay) throws InterruptedException   {
		log.info("persistInvoiceUn:{}",delay);
		Session session = sessionFactory.getCurrentSession();
		Thread.sleep(delay);
		session.persist(invoice);	
	}
		

	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void persistInvoiceSer(Invoice invoice,final long delay) throws InterruptedException   {
		log.info("persistInvoiceSer:{}",delay);
		Session session = sessionFactory.getCurrentSession();
		Thread.sleep(delay);
		session.persist(invoice);
	}*/

	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	public Invoice updateInvoiceUn(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id,"updateInvoice un :{},{}", delay);
	}

	@Transactional(isolation=Isolation.READ_COMMITTED)
	public Invoice updateInvoiceCom(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id,"updateInvoice com :{},{}", delay);
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public Invoice updateInvoiceComOP(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id,"updateInvoice com:{},{}", delay);
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public Invoice updateInvoiceComOPFI(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id,"updateInvoice com:{},{}", delay);
	}

	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public Invoice updateInvoiceRep(Class<?> classInst, long id,final long delay) throws InterruptedException {
		return this.update(classInst, id,"updateInvoice rep:{},{}", delay);
	}

	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Invoice updateInvoiceSer(Class<?> classInst, long id,final long delay) throws InterruptedException {
    	return this.update(classInst, id,"updateInvoice ser:{},{}", delay);
	}
	
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Invoice updateInvoiceSerFI(Class<?> classInst, long id,final long delay) throws InterruptedException {
    	return this.update(classInst, id,"updateInvoice ser:{},{}", delay);
	}
	
	private Invoice update(Class<?> classInst, long id,String message,final long delay)throws InterruptedException {
	
		Session session = sessionFactory.getCurrentSession();
		
		Invoice invoice = (Invoice) session.get(classInst, id);
		String comment = getComment(invoice.getComment(),delay);
		invoice.setComment(comment);
		session.update(invoice);
		
		Semaphore.decrement(delay);
		UpdateRec.releasDelay();
		ReadRec.releasAll();
		
		log.info(message,comment,delay);
		Thread.sleep(delay);
		return invoice;
	}
	
	private String getComment(final String comment, final long delay) {
		if(comment.contains("+")) {
			return Thread.currentThread().getName() + ":-";
		} else {
			return Thread.currentThread().getName() + ":+";
		}
	}

	@Override
	public Invoice getInvoiceByIdSerFI(Class<?> classInst, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Country> getCountries() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Country.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<Country> list = criteria.list();
		return list;
	}

	@Transactional(readOnly=true, isolation = Isolation.READ_UNCOMMITTED)
	@Override
	public GRADEA getGradeA() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(GRADEA.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<GRADEA> list = criteria.list();
		return list!=null&&list.size()>0?list.get(0):null;
	}

	@Transactional(readOnly=true, isolation = Isolation.READ_COMMITTED)
	@Override
    public GRADEB getGradeB() {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(GRADEB.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<GRADEB> list = criteria.list();
		return list!=null&&list.size()>0?list.get(0):null;
	}
	
}
