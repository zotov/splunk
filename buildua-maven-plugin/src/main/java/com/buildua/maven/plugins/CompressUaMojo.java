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

@Mojo(name = "compress", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class CompressUaMojo extends AbstractMojo {

	private Log log;
	
	final  String appName="server-node";
	
	@Parameter( defaultValue = "${build.finalName}",name = "srcWarName", readonly = true, required = true )
    private String srcWarName;
	
	@Parameter( defaultValue = "${build.finalName}",name = "dstWarName", readonly = false, required = false )
    private String dstWarName;		
	
	@Parameter( defaultValue = "target/tmp",name = "tmpDir", readonly = true, required = false )
    private String tmpDir;
	
	@Parameter( defaultValue = "target/" + appName + "/WEB-INF",name = "copyPackagePath", readonly = true, required = false )
    private String copyPackagePath;
		
	@Parameter( defaultValue = "target", name = "srcWarPath", readonly = true, required = false )
    private String srcWarPath;	
	
	@Parameter(defaultValue = "target/tmp", name = "dstWarPath", readonly = true, required = false )
    private String dstWarPath;
		
	@Parameter(defaultValue = "target/"+appName+"/WEB-INF/pages", name = "pagePathDir", readonly = true, required = false )
	private String pagePathDir;
	
	@Parameter(defaultValue = "target/"+appName+"/WEB-INF/resources/css", name = "unionCssDirPath", readonly = true, required = false )
	private String unionCssDirPath;
	
	@Parameter(defaultValue = "target/"+appName+"/WEB-INF/resources/js", name = "unionJsDirPath", readonly = true, required = false )
	private String unionJsDirPath;
	
	@Parameter(defaultValue = "<link href=\"${resourceUrl}/css/#\" rel=\"stylesheet\" type=\"text/css\" />", name = "cssReference", readonly = true, required = false )
	private String cssReference;
	 
	@Parameter(defaultValue = "<script src=\"${resourceUrl}/js/#\" type=\"text/javascript\"></script>", name = "jsReference", readonly = true, required = false )
	private String jsReference;
	   
	@Parameter(defaultValue = ".+(src=|href=).+(\\.js|\\.css).+", name = "resourceFilePattern", readonly = true, required = false )
	private String resourceFilePattern;
	   
	@Parameter(defaultValue = ".+(href=).+(\\.css).+", name = "cssResourceFilePattern", readonly = true, required = false )
	private  String cssResourceFilePattern;
	
	@Parameter(defaultValue = ".+(src=).+(\\.js).+", name = "jsResourceFilePattern", readonly = true, required = false )
	private String jsResourceFilePattern;
	
	@Parameter( defaultValue = "WEB-INF",name = "resourceRootDir", readonly = true, required = false )
    private String resourceRootDir;
    
    @Parameter( defaultValue = "yuicompressor-2.4.8.jar",name = "compressorName", readonly = true, required = false )
    private String compressorName;

    @Parameter( defaultValue = "false", name = "replaceSrc", readonly = true, required = false)
    private boolean replaceSrc;
    
    @Parameter( defaultValue = "false", name = "deleteUR", readonly = true, required = false)
    boolean deleteUR;
	
    @Parameter( defaultValue = "true",name = "useMinFile", readonly = true, required = false)
	boolean useMinFile;
		
	@Parameter(readonly = true, required = false )
	private String[][] resourcesCompress; //{{result.js, script1.js, script2.js, script3.js,...},{result.css, style1.css, styel2.css,...}}
		
	/** App variable **/
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
        ca.setReplaceSrc(replaceSrc);
        ca.setDeleteUR(deleteUR);
        ca.setUseMinFile(useMinFile);
        ca.setResourcesCompress(resourcesCompress);
	}

	public String getCopyPackagePath() {
		return copyPackagePath;
	}

	public void setCopyPackagePath(String copyPackagePath) {
		this.copyPackagePath = copyPackagePath;
	}	

	public String getSrcWarName() {
		return srcWarName;
	}

	public void setSrcWarName(String srcWarName) {
		this.srcWarName = srcWarName;
	}

	public String getSrcWarPath() {
		return srcWarPath;
	}

	public void setSrcWarPath(String srcWarPath) {
		this.srcWarPath = srcWarPath;
	}

	public boolean isReplaceSrc() {
		return replaceSrc;
	}

	public void setReplaceSrc(boolean replaceSrc) {
		this.replaceSrc = replaceSrc;
	}

	public String getDstWarName() {
		return dstWarName;
	}
	
	public void setDstWarName(String dstWarName) {
		this.dstWarName = dstWarName;
	}

	public String getDstWarPath() {
		return dstWarPath;
	}

	public void setDstWarPath(String dstWarPath) {
		this.dstWarPath = dstWarPath;
	}
	
	/*public String getResourceCompressDir() {
		return resourceCompressDir;
	}

	public void setResourceCompressDir(String resourceCompressDir) {
		this.resourceCompressDir = resourceCompressDir;
	}*/

	public String getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public String[][] getResourcesCompress() {
		return resourcesCompress;
	}

	public void setResourcesCompress(String[][] resourcesCompress) {
		this.resourcesCompress = resourcesCompress;
	}

	public String getPagePathDir() {
		return pagePathDir;
	}

	public void setPagePathDir(String pagePathDir) {
		this.pagePathDir = pagePathDir;
	}

	public String getUnionCssDirPath() {
		return unionCssDirPath;
	}

	public void setUnionCssDirPath(String unionCssDirPath) {
		this.unionCssDirPath = unionCssDirPath;
	}

	public String getUnionJsDirPath() {
		return unionJsDirPath;
	}

	public void setUnionJsDirPath(String unionJsDirPath) {
		this.unionJsDirPath = unionJsDirPath;
	}

	public String getCssReference() {
		return cssReference;
	}

	public void setCssReference(String cssReference) {
		this.cssReference = cssReference;
	}

	public String getJsReference() {
		return jsReference;
	}

	public void setJsReference(String jsReference) {
		this.jsReference = jsReference;
	}

	public String getResourceFilePattern() {
		return resourceFilePattern;
	}

	public void setResourceFilePattern(String resourceFilePattern) {
		this.resourceFilePattern = resourceFilePattern;
	}

	public String getCssResourceFilePattern() {
		return cssResourceFilePattern;
	}

	public void setCssResourceFilePattern(String cssResourceFilePattern) {
		this.cssResourceFilePattern = cssResourceFilePattern;
	}

	public String getJsResourceFilePattern() {
		return jsResourceFilePattern;
	}

	public void setJsResourceFilePattern(String jsResourceFilePattern) {
		this.jsResourceFilePattern = jsResourceFilePattern;
	}

	public String getResourceRootDir() {
		return resourceRootDir;
	}

	public void setResourceRootDir(String resourceRootDir) {
		this.resourceRootDir = resourceRootDir;
	}

	public String getCompressorName() {
		return compressorName;
	}

	public void setCompressorName(String compressorName) {
		this.compressorName = compressorName;
	}
		

	
}