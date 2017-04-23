/*
 * The MIT License (MIT)
 * Copyright (c) 2016, Zotov Denys Vladimirovich, [Ukraine, Chernihiv, zotov.denys@gmail.com]
 */
package com.buildua.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

/**
 * The Class CompressUaMojo.
 *
 * @author Denys Zotov (http://soft-industry.com)
 */
@Mojo(name = "compress", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class CompressUaMojo extends AbstractMojo {

	/** The log. */
	private Log log;
	
	/** The app name. */
	final  String appName="server-node";
	
	/** The src war name. */
	@Parameter( defaultValue = "${build.finalName}",name = "srcWarName", readonly = true, required = true )
    private String srcWarName;
	
	/** The dst war name. */
	@Parameter( defaultValue = "${build.finalName}",name = "dstWarName", readonly = false, required = false )
    private String dstWarName;		
	
	/** The tmp dir. */
	@Parameter( defaultValue = "target/tmp",name = "tmpDir", readonly = true, required = false )
    private String tmpDir;
	
	/** The copy package path. */
	@Parameter( defaultValue = "target/" + appName + "/WEB-INF",name = "copyPackagePath", readonly = true, required = false )
    private String copyPackagePath;
		
	/** The src war path. */
	@Parameter( defaultValue = "target", name = "srcWarPath", readonly = true, required = false )
    private String srcWarPath;	
	
	/** The dst war path. */
	@Parameter(defaultValue = "target/tmp", name = "dstWarPath", readonly = true, required = false )
    private String dstWarPath;
		
	/** The page path dir. */
	@Parameter(defaultValue = "target/"+appName+"/WEB-INF/pages", name = "pagePathDir", readonly = true, required = false )
	private String pagePathDir;
	
	/** The union css dir path. */
	@Parameter(defaultValue = "target/"+appName+"/WEB-INF/resources/css", name = "unionCssDirPath", readonly = true, required = false )
	private String unionCssDirPath;
	
	/** The union js dir path. */
	@Parameter(defaultValue = "target/"+appName+"/WEB-INF/resources/js", name = "unionJsDirPath", readonly = true, required = false )
	private String unionJsDirPath;
	
	/** The css reference. */
	@Parameter(defaultValue = "<link href=\"${resourceUrl}/css/#\" rel=\"stylesheet\" type=\"text/css\" />", name = "cssReference", readonly = true, required = false )
	private String cssReference;
	 
	/** The js reference. */
	@Parameter(defaultValue = "<script src=\"${resourceUrl}/js/#\" type=\"text/javascript\"></script>", name = "jsReference", readonly = true, required = false )
	private String jsReference;
	   
	/** The resource file pattern. */
	@Parameter(defaultValue = ".+(src=|href=).+(\\.js|\\.css).+", name = "resourceFilePattern", readonly = true, required = false )
	private String resourceFilePattern;
	   
	/** The css resource file pattern. */
	@Parameter(defaultValue = ".+(href=).+(\\.css).+", name = "cssResourceFilePattern", readonly = true, required = false )
	private  String cssResourceFilePattern;
	
	/** The js resource file pattern. */
	@Parameter(defaultValue = ".+(src=).+(\\.js).+", name = "jsResourceFilePattern", readonly = true, required = false )
	private String jsResourceFilePattern;
	
	/** The resource root dir. */
	@Parameter( defaultValue = "WEB-INF",name = "resourceRootDir", readonly = true, required = false )
    private String resourceRootDir;
    
    /** The compressor name. */
    @Parameter( defaultValue = "yuicompressor-2.4.8.jar",name = "compressorName", readonly = true, required = false )
    private String compressorName;

    /** The replace src. */
    @Parameter( defaultValue = "false", name = "replaceSrcWar", readonly = true, required = false)
    private boolean replaceSrcWar;
    
    @Parameter( defaultValue = "false", name = "removeTmpDir", readonly = true, required = false)
	private boolean removeTmpDir;
    
    /** The delete ur. */
    @Parameter( defaultValue = "false", name = "deleteUR", readonly = true, required = false)
    boolean deleteUR;
	
    /** The use min file. */
    @Parameter( defaultValue = "false",name = "useMinFile", readonly = true, required = false)
	boolean useMinFile;
    
    @Parameter( defaultValue = "true",name = "unionJsp", readonly = true, required = false)
   	boolean unionJsp;
    
    @Parameter( defaultValue = "false",name = "trimJsp", readonly = true, required = false)
   	boolean trimJsp;
		
	/** The resources compress. */
	@Parameter(readonly = true, required = false )
	private String[][] resourcesCompress; //{{result.js, script1.js, script2.js, script3.js,...},{result.css, style1.css, styel2.css,...}}
		
	/**  App variable *. */
	private CompressUa ca;
	/*
	private File compressFile;
	
	//root path, css file
	private Map<String,List<File>>cssFileMap=new HashMap<String,List<File>>();
	//root path, js file
	private Map<String,List<File>>jsFileMap=new HashMap<String,List<File>>();
	//root path, jsp file
	private Map<log = new SystemStreamLog();String,List<File>>jspFileMap=new HashMap<String,List<File>>();
	//root path, jsp file with union references
	private Map<String,List<File>>jspURFileMap=new HashMap<String,List<File>>();
	//css union resources
	private Set<File>cssURFileList=new HashSet<File>();
	//js union resources
	private Set<File>jsURFileList=new HashSet<File>();
		
	private Map<String,Map<String,List<String>>>resourceMapFile=new HashMap<String,Map<String,List<String>>>();*/
	
	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		 
		try {
			 log=this.getLog();
			 CompressUa ca = new CompressUa();
			 this.updateCASettings(ca);
			 log.info("updateCASettings successful");
			 ca.execute();
	   } catch (Exception ex) {
			log.error(ex);
	   }
    }	

	
	/**
	 * Update ca settings.
	 *
	 * @param ca the ca
	 */
	private void updateCASettings(final CompressUa ca) {
		ca.setSrcWarName(srcWarName);
		ca.setDstWarName(dstWarName);
		ca.setTmpDir(tmpDir);
		ca.setCopyPackagePath(copyPackagePath);
		ca.setSrcWarPath(srcWarPath);
		ca.setDstWarPath(dstWarPath);
		ca.setPagePathDir(pagePathDir);
		ca.setUnionCssDirPath(unionCssDirPath);
		ca.setUnionJsDirPath(unionJsDirPath);
		ca.setCssReference(cssReference);
		ca.setJsReference(jsReference);
		ca.setResourceFilePattern(cssResourceFilePattern);
		ca.setJsResourceFilePattern(jsResourceFilePattern);
        ca.setCssResourceFilePattern(cssResourceFilePattern);
        ca.setResourceRootDir(resourceRootDir);
        ca.setCompressorName(compressorName);
        ca.setReplaceSrcWar(replaceSrcWar);
        ca.setDeleteUR(deleteUR);
        ca.setUseMinFile(useMinFile);
        ca.setResourcesCompress(resourcesCompress);
        ca.setRemoveTmpDir(removeTmpDir);
        ca.setTrimJsp(trimJsp);
        ca.setUnionJsp(unionJsp);
        //ca.setR
	}

	/**
	 * Gets the copy package path.
	 *
	 * @return the copy package path
	 */
	public String getCopyPackagePath() {
		return copyPackagePath;
	}

	/**
	 * Sets the copy package path.
	 *
	 * @param copyPackagePath the new copy package path
	 */
	public void setCopyPackagePath(String copyPackagePath) {
		this.copyPackagePath = copyPackagePath;
	}	

	/**
	 * Gets the src war name.
	 *
	 * @return the src war name
	 */
	public String getSrcWarName() {
		return srcWarName;
	}

	/**
	 * Sets the src war name.
	 *
	 * @param srcWarName the new src war name
	 */
	public void setSrcWarName(String srcWarName) {
		this.srcWarName = srcWarName;
	}

	/**
	 * Gets the src war path.
	 *
	 * @return the src war path
	 */
	public String getSrcWarPath() {
		return srcWarPath;
	}

	/**
	 * Sets the src war path.
	 *
	 * @param srcWarPath the new src war path
	 */
	public void setSrcWarPath(String srcWarPath) {
		this.srcWarPath = srcWarPath;
	}


	public boolean isReplaceSrcWar() {
		return replaceSrcWar;
	}


	public void setReplaceSrcWar(boolean replaceSrcWar) {
		this.replaceSrcWar = replaceSrcWar;
	}


	/**
	 * Gets the dst war name.
	 *
	 * @return the dst war name
	 */
	public String getDstWarName() {
		return dstWarName;
	}
	
	/**
	 * Sets the dst war name.
	 *
	 * @param dstWarName the new dst war name
	 */
	public void setDstWarName(String dstWarName) {
		this.dstWarName = dstWarName;
	}

	/**
	 * Gets the dst war path.
	 *
	 * @return the dst war path
	 */
	public String getDstWarPath() {
		return dstWarPath;
	}

	/**
	 * Sets the dst war path.
	 *
	 * @param dstWarPath the new dst war path
	 */
	public void setDstWarPath(String dstWarPath) {
		this.dstWarPath = dstWarPath;
	}
	
	/*public String getResourceCompressDir() {
		return resourceCompressDir;
	}

	public void setResourceCompressDir(String resourceCompressDir) {
		this.resourceCompressDir = resourceCompressDir;
	}*/

	/**
	 * Gets the tmp dir.
	 *
	 * @return the tmp dir
	 */
	public String getTmpDir() {
		return tmpDir;
	}

	/**
	 * Sets the tmp dir.
	 *
	 * @param tmpDir the new tmp dir
	 */
	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	/**
	 * Gets the resources compress.
	 *
	 * @return the resources compress
	 */
	public String[][] getResourcesCompress() {
		return resourcesCompress;
	}

	/**
	 * Sets the resources compress.
	 *
	 * @param resourcesCompress the new resources compress
	 */
	public void setResourcesCompress(String[][] resourcesCompress) {
		this.resourcesCompress = resourcesCompress;
	}

	/**
	 * Gets the page path dir.
	 *
	 * @return the page path dir
	 */
	public String getPagePathDir() {
		return pagePathDir;
	}

	/**
	 * Sets the page path dir.
	 *
	 * @param pagePathDir the new page path dir
	 */
	public void setPagePathDir(String pagePathDir) {
		this.pagePathDir = pagePathDir;
	}

	/**
	 * Gets the union css dir path.
	 *
	 * @return the union css dir path
	 */
	public String getUnionCssDirPath() {
		return unionCssDirPath;
	}

	/**
	 * Sets the union css dir path.
	 *
	 * @param unionCssDirPath the new union css dir path
	 */
	public void setUnionCssDirPath(String unionCssDirPath) {
		this.unionCssDirPath = unionCssDirPath;
	}

	/**
	 * Gets the union js dir path.
	 *
	 * @return the union js dir path
	 */
	public String getUnionJsDirPath() {
		return unionJsDirPath;
	}

	/**
	 * Sets the union js dir path.
	 *
	 * @param unionJsDirPath the new union js dir path
	 */
	public void setUnionJsDirPath(String unionJsDirPath) {
		this.unionJsDirPath = unionJsDirPath;
	}

	/**
	 * Gets the css reference.
	 *
	 * @return the css reference
	 */
	public String getCssReference() {
		return cssReference;
	}

	/**
	 * Sets the css reference.
	 *
	 * @param cssReference the new css reference
	 */
	public void setCssReference(String cssReference) {
		this.cssReference = cssReference;
	}

	/**
	 * Gets the js reference.
	 *
	 * @return the js reference
	 */
	public String getJsReference() {
		return jsReference;
	}

	/**
	 * Sets the js reference.
	 *
	 * @param jsReference the new js reference
	 */
	public void setJsReference(String jsReference) {
		this.jsReference = jsReference;
	}

	/**
	 * Gets the resource file pattern.
	 *
	 * @return the resource file pattern
	 */
	public String getResourceFilePattern() {
		return resourceFilePattern;
	}

	/**
	 * Sets the resource file pattern.
	 *
	 * @param resourceFilePattern the new resource file pattern
	 */
	public void setResourceFilePattern(String resourceFilePattern) {
		this.resourceFilePattern = resourceFilePattern;
	}

	/**
	 * Gets the css resource file pattern.
	 *
	 * @return the css resource file pattern
	 */
	public String getCssResourceFilePattern() {
		return cssResourceFilePattern;
	}

	/**
	 * Sets the css resource file pattern.
	 *
	 * @param cssResourceFilePattern the new css resource file pattern
	 */
	public void setCssResourceFilePattern(String cssResourceFilePattern) {
		this.cssResourceFilePattern = cssResourceFilePattern;
	}

	/**
	 * Gets the js resource file pattern.
	 *
	 * @return the js resource file pattern
	 */
	public String getJsResourceFilePattern() {
		return jsResourceFilePattern;
	}

	/**
	 * Sets the js resource file pattern.
	 *
	 * @param jsResourceFilePattern the new js resource file pattern
	 */
	public void setJsResourceFilePattern(String jsResourceFilePattern) {
		this.jsResourceFilePattern = jsResourceFilePattern;
	}

	/**
	 * Gets the resource root dir.
	 *
	 * @return the resource root dir
	 */
	public String getResourceRootDir() {
		return resourceRootDir;
	}

	/**
	 * Sets the resource root dir.
	 *
	 * @param resourceRootDir the new resource root dir
	 */
	public void setResourceRootDir(String resourceRootDir) {
		this.resourceRootDir = resourceRootDir;
	}

	/**
	 * Gets the compressor name.
	 *
	 * @return the compressor name
	 */
	public String getCompressorName() {
		return compressorName;
	}

	/**
	 * Sets the compressor name.
	 *
	 * @param compressorName the new compressor name
	 */
	public void setCompressorName(String compressorName) {
		this.compressorName = compressorName;
	}


	public boolean getRemoveTmpDir() {
		return removeTmpDir;
	}


	public void setRemoveTmpDir(boolean removeTmpDir) {
		this.removeTmpDir = removeTmpDir;
	}
	
}