/*
 * The MIT License (MIT)
 * Copyright (c) 2016, Zotov Denys Vladimirovich, [Ukraine, Chernihiv, zotov.denys@gmail.com]
 */
package com.buildua.maven.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JspFile.
 *
 * @author Denys Zotov (http://soft-industry.com)
 */
public class JspFile  {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(JspFile.class);
	
	/** The file. */
	File file;
	
	/** The parent jsp file. */
	JspFile parentJspFile;
	
	/** The child jsp file list. */
	private List<JspFile> childJspFileList = new ArrayList<JspFile>();

	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.file = file;
	}	

	/**
	 * Gets the child jsp file list.
	 *
	 * @return the child jsp file list
	 */
	public List<JspFile> getChildJspFileList() {
		return childJspFileList;
	}

	/**
	 * Sets the child jsp file list.
	 *
	 * @param childJspFileList the new child jsp file list
	 */
	public void setChildJspFileList(List<JspFile> childJspFileList) {
		this.childJspFileList = childJspFileList;
	}
	
	public void addChildJspFile(JspFile jspFile) {
		if(!this.childJspFileList.contains(jspFile)) {
		    log.info("add child:{}, parent:{}", jspFile.file.getName(), this.file.getName());
			jspFile.setParentJspFile(this);
		    this.childJspFileList.add(jspFile);
		}
	}

	/**
	 * Gets the parent jsp file.
	 *
	 * @return the parent jsp file
	 */
	public JspFile getParentJspFile() {
		return parentJspFile;
	}

	/**
	 * Sets the parent jsp file.
	 *
	 * @param parentJspFile the new parent jsp file
	 */
	public void setParentJspFile(JspFile parentJspFile) {
		this.parentJspFile = parentJspFile;
	}

	/**
	 * Adds the child jsp file.
	 *
	 * @param jspFile the jsp file
	 */
	/*public void addChildJspFile(final JspFile jspFile) {
		
		log.info("add child:{}, parent:{}",jspFile.file.getName(),this.file.getName());
		jspFile.setParentJspFile(this);
		this.childJspFileList.add(jspFile);
	}*/
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		//return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		return this.file!=null?this.file.getPath()+"\r\n":"null";
	}
	
	


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {		
		
		if(o == null || !(o instanceof JspFile)) {
			return false;
		}
		
		JspFile jspFile = (JspFile)o;	
		return (this.file.getAbsolutePath().toString().compareToIgnoreCase(jspFile.getFile().getAbsolutePath().toString()) == 0);
	}

		
	
}
