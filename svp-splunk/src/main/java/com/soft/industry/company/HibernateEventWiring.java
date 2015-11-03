package com.soft.industry.company;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateEventWiring {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CountryListener listener;

    @PostConstruct
    public void registerListeners() {
        final EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory)
                .getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_LOAD)
                .appendListener((PostLoadEventListener) listener);
    }

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public CountryListener getListener() {
		return listener;
	}

	public void setListener(CountryListener listener) {
		this.listener = listener;
	}
    
    
}
