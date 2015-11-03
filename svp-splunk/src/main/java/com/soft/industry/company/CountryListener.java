package com.soft.industry.company;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultPostLoadEventListener;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.soft.industry.entity.Country;
@Component
public class CountryListener implements PostLoadEventListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*@Override
    public void postLoad(final LoadEvent event, final LoadType loadType) throws HibernateException {
        if(Country.class.getName().equals(event.getEntityClassName())){
        	Country entity = (Country) event.getResult();
            entity.buildDepartments();
            }   // if
    }*/

	@Override
	public void onPostLoad(PostLoadEvent event) {
		 if(Country.class.getName().equals(event.getEntity().getClass().getName())){
	        	Country entity = (Country) event.getEntity();
	            entity.buildDepartments();
	     }   
		
	}
}

/** The Constant log. */
/*
 * private static final Logger log =
 * LoggerFactory.getLogger(CountryListener.class);
 * 
 * @PostLoad public void postLoad(Country country) {
 * log.info("country:{} ",country); } }
 */