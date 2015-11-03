package com.soft.industry.company;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soft.industry.dao.InvoiceDao;
import com.soft.industry.entity.Country;

@Component
public class Company {
	
	
	private static final Logger log = LoggerFactory.getLogger(Company.class);
	@Autowired
	InvoiceDao invoiceAnnotDao;
	
	
	public void initCompany() {
		List<Country> list =  this.invoiceAnnotDao.getCountries();
		log.info(Arrays.toString(list.toArray()));
	}
}
