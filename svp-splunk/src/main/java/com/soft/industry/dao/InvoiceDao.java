package com.soft.industry.dao;

import java.util.List;

import org.hibernate.LockMode;

import com.soft.industry.entity.Company;
import com.soft.industry.entity.Country;
import com.soft.industry.entity.GRADEA;
import com.soft.industry.entity.GRADEB;
import com.soft.industry.entity.Invoice;


public interface InvoiceDao {
	
	public Invoice getInvoice(final Class<?> classInst);
	public int getInvoiceCountUn(final Class<?> classInst);
	public int getInvoiceCountCom(final Class<?> classInst);
	public int getInvoiceCountComOP(final Class<?> classInst);
	public int getInvoiceCountComOPFI(final Class<?> classInst);
	public int getInvoiceCountRep(final Class<?> classInst);
	public int getInvoiceCountSer(final Class<?> classInst);
	public int getInvoiceCountSerFI(final Class<?> classInst);
		
	public int getInvoiceCount(Class<?> classInst);
	public Invoice getInvoice(final Class<?> classInst,final long id, final LockMode lockMode);
	public Invoice getInvoiceByIdUn(final Class<?> classInst,final long id);
	public Invoice getInvoiceByIdCom(final Class<?> classInst,final long id);
	public Invoice getInvoiceByIdComOP(final Class<?> classInst,final long id);
	public Invoice getInvoiceByIdComOPFI(final Class<?> classInst,final long id);
	public Invoice getInvoiceByIdRep(final Class<?> classInst,final long id);
	public Invoice getInvoiceByIdSer(final Class<?> classInst,final long id);
	public Invoice getInvoiceByIdSerFI(Class<?> classInst,  long id);
	
	public void saveInvoice(final Invoice invoice,final long delay)throws Exception;
	public void saveInvoiceDef(final Invoice invoice,final long delay)throws Exception;
	public void saveInvoiceSer(final Invoice invoice,final long delay)throws Exception;
	
	public void updateInvoice(final Invoice invoice);
	public void insertInvoice(final Invoice invoice);
	public void deletInvoice(final int id);
	
	public Invoice updateInvoiceUn(final Class<?> classInst,final long id,final long delay)throws Exception;
	public Invoice updateInvoiceCom(final Class<?> classInst,final long id,final long delay)throws Exception;
	public Invoice updateInvoiceComOP(Class<?> classInst, long id,final long delay)throws Exception;
	public Invoice updateInvoiceComOPFI(Class<?> classInst, long id,final long delay)throws Exception;
	public Invoice updateInvoiceRep(final Class<?> classInst,final long id,final long delay)throws Exception;
	public Invoice updateInvoiceSer(final Class<?> classInst,final long id,final long delay)throws Exception;
	public Invoice updateInvoiceSerFI(Class<?> classInst, long id,final long delay) throws Exception;
	
	public List<Country> getCountries();
	
	public GRADEA getGradeA();
	public GRADEB getGradeB();
	
}
