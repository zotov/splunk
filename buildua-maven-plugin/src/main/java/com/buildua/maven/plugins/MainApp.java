package com.buildua.maven.plugins;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class MainApp {	
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	
	public static void main(final String[] arg0) throws Exception {
	   CompressUa cum = new CompressUa();
	   log.info("cum:{}",cum);
	   cum.execute();
		 
	}
	
}