/*
 * The MIT License (MIT)
 * Copyright (c) 2016, Zotov Denys Vladimirovich, [Ukraine, Chernihiv, zotov.denys@gmail.com]
 */
package com.buildua.maven.plugins;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * The Class MainApp.
 *
 * @author Denys Zotov (http://soft-industry.com)
 */
public class MainApp {	
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	
	/**
	 * The main method.
	 *
	 * @param arg0 the arguments
	 * @throws Exception the exception
	 */
	public static void main(final String[] arg0) throws Exception {
	   CompressUa cum = new CompressUa();
	   log.info("cum:{}",cum);
	   cum.execute();		 
	}
	
}