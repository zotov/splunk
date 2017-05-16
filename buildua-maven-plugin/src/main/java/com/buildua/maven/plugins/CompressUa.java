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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class CompressUa.
 *
 * @author Denys Zotov (http://soft-industry.com)
 */
public class CompressUa {//mvn install -DskipTests
	

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(CompressUa.class);
	
	//Log log = new SystemStreamLog();
	
	private String targetDir = "/home/denis/Projects/map.org.ua/git/projects/Map-Info/target";
	
	private String app_name="request-manager";
	
	/** The src war name. */
	private String srcWarName = app_name + ".war";

	/** The dst war name. */
	private String dstWarName = app_name + ".war"; //+ "-compress.war";

	/** The tmp dir. */
	private String tmpDir = targetDir + "/tmp";

	/** The copy package path. */
	private String copyPackagePath = targetDir + "/" + app_name + "/WEB-INF";

	/** The src war path. */
	private String srcWarPath = targetDir;

	/** The dst war path. */
	private String dstWarPath = targetDir + "/compress-war/";
	
	/** The page path dir. */
	private String pagePathDir = targetDir + "/" + app_name + "/WEB-INF/pages";

	/** The union css dir path. */
	private String unionCssDirPath = targetDir + "/tmp/WEB-INF/resources/css/";

	/** The union js dir path. */
	private String unionJsDirPath = targetDir + "/tmp/WEB-INF/resources/js/";

	/** The css reference. */
	private  String cssReference = "<link href=\"${resourceUrl}/css/#\" rel=\"stylesheet\" type=\"text/css\" />";
	
	private  String varCssPattern = "^var[a-zA-Z_0-9 ]+=[ '\"]+.+[ '\"]+;$";
	
	/** The js reference. */
	private  String jsReference = "<script src=\"${resourceUrl}/js/#\" type=\"text/javascript\"></script>";

	/** The resource file pattern. */
	private  String resourceFilePattern = ".+(src=|href=).+(\\.js|\\.css).+";

	/** The css resource file pattern. */
	private  String cssResourceFilePattern = ".+(href=).+(\\.css).+";

	/** The js resource file pattern. */
	private  String jsResourceFilePattern = ".+(src=).+(\\.js).+";
	
	/** The jsp file pattern. */
	private  String jspIncludeFilePattern = ".*<%@include.+\\.jsp.*%>.*";
	
	private String jspCommentPattern="^<%--.+--%>$";
		
	//private String cssImageRefePattern="url *\\(\"(\\.\\.\\/)+";
	
	/** The resource root dir. */
	private  String resourceRootDir = "WEB-INF";

	/** The compressor name. */
	private  String compressorName = "yuicompressor-2.4.8.jar";

	/** The replace src. */
	private boolean replaceSrcWar = false;
	
	/** The replace src. */
	private boolean removeTmpDir = false;

	/** The delete ur. */
	private boolean deleteUR = false;
	
	/** The use file. */
	/*compress file*/
	private boolean compressResource =  true;// false;
	
	private boolean resetReferencesToImagesInCss = true;

	/** The use min file. */
	private boolean useMinFile = false;
	
	/** The union jsp. */
	private boolean unionJsp = false;
	
	/** The trim jps. */
	private boolean trimJsp =  false;
		
	/** The resources compress. */
	private String[][] resourcesCompress={}; // {{result.js, script1.js,
	// script2.js,
	// script3.js,...},{result.css,
	// style1.css, styel2.css,...}}

	/**  app variables *. */
	private File compressFile;

	/** The css file map. */
	// root path, css file
	private Map<String, List<File>> cssFileMap = new HashMap<String, List<File>>();
	
	/** The js file map. */
	// root path, js file
	private Map<String, List<File>> jsFileMap = new HashMap<String, List<File>>();
	
	/** The jsp file map. */
	
	private Map<String,File> jspFileMap = new HashMap<String, File>();
	
	private Map<String, List<File>> jspResFileMap = new HashMap<String, List<File>>();

	/** The jsp inner file map. */	
	private Map<String, JspFile> jspInnerFileMap = new HashMap<String, JspFile>();
	
	/** The jsp ur file map. */
	// root path, jsp file with union references
	private Map<String, List<File>> jspURFileMap = new HashMap<String, List<File>>();
	
	/** The css ur file list. */
	// css union resources
	private Set<File> cssURFileList = new HashSet<File>();
	
	/** The js ur file list. */
	// js union resources
	private Set<File> jsURFileList = new HashSet<File>();

	/** The resource map file. */
	private Map<String, Map<String, List<String>>> resourceMapFile = new HashMap<String, Map<String, List<String>>>();

	/**
	 * Execute.
	 *
	 * @throws Exception the exception
	 */
	public void execute() throws Exception {	
		
		initialization();
		
		log.info("Compress start...");
		
		File dir = new File(tmpDir);
		FileUtils.deleteQuietly(dir);
		if (!dir.exists() && !dir.mkdir()) {
			log.error("Cannot create tmp dir:" + tmpDir);
			throw new Exception("Cannot create tmp dir:" + tmpDir);
		} else {
			log.info("clear tmp dir:{}", dir.getAbsolutePath());
		}
		

		File dstWarFile = new File(dstWarPath + File.separator + dstWarName);
		File srcWarFile = new File(srcWarPath + File.separator + srcWarName);

		try {

			if (dstWarFile.exists()) {
				boolean result = dstWarFile.delete();
				log.info("clear dst dir:{}, result:{}", dstWarFile.getAbsoluteFile(), result);
			}
			if(!dstWarFile.getParentFile().exists()) {
			   Files.createDirectories(Paths.get(dstWarFile.getParentFile().getAbsolutePath()));	
			}
			dstWarFile.createNewFile();

			if (!srcWarFile.exists()) {
				log.error("Error src war file not exist:"+ srcWarFile.getAbsolutePath());
				throw new Exception("Error src war file not exist:" + srcWarFile.getAbsolutePath());
			} else if (!dstWarFile.exists()) {
				log.error("Error dst war file not exist:" + dstWarFile.getAbsolutePath());
				throw new Exception("Error dst war file not exist:"	+ srcWarFile.getAbsolutePath());
			}

			FileUtils.copyFileToDirectory(srcWarFile, dir);

			log.info("Read file:" + srcWarFile.getAbsolutePath());
			this.createCompressJarFile(srcWarFile, dstWarFile);
			removeTmpDir();
		} catch (IOException ex) {
			log.error("execute", ex);
		}
	}

	/**
	 * Creates the compress jar file.
	 *
	 * @param srcFile the src file
	 * @param dstFileCompress the dst file compress
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createCompressJarFile(final File srcFile, final File dstFileCompress) throws IOException {

		JarFile srcJarFile = new JarFile(srcFile); // new JarFile(srcJarFile);

		try {
			JarOutputStream compressJarOutputStream = new JarOutputStream(
					new FileOutputStream(dstFileCompress));
			try {
				// Copy original jar file to the temporary one.
				Enumeration<JarEntry> jarEntries = srcJarFile.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry entry = jarEntries.nextElement();
					if (entry.getName().contains("META-INF")) {
						InputStream entryInputStream = srcJarFile.getInputStream(entry);
						compressJarOutputStream.putNextEntry(entry);
						IOUtils.copy(entryInputStream, compressJarOutputStream);
					}
				}

				File copyDir = new File(copyPackagePath);
				if (!copyDir.exists()) {
					throw new IOException("dir not exists:"	+ copyDir.getAbsolutePath());
				}
			
				// Copy resource from srcDir
				String rootFolderName = FilenameUtils.getBaseName(copyPackagePath);
				List<File> fileList = (List<File>) FileUtils.listFilesAndDirs(copyDir, FileFileFilter.FILE, DirectoryFileFilter.DIRECTORY);

				for (File file : fileList) {
					if (file.isFile() && !this.storeForCompress(file, rootFolderName, new JspFile())) {
						
						String path = file.getAbsolutePath();
						log.info("read file from jar, path={}", path);
						
						String name = path.substring(path.indexOf(rootFolderName), path.length());
						JarEntry entry = new JarEntry(name);
						compressJarOutputStream.putNextEntry(entry);
						log.debug("add entry:" + name);

						InputStream entryInputStream = new FileInputStream(file);
						IOUtils.copy(entryInputStream, compressJarOutputStream);

						compressJarOutputStream.flush();
					}
				}

				log.debug("jspFileMap size:{},:{}", jspFileMap.size(), jspFileMap.values().toArray());
				log.debug("jspWRFileMap size:{]", jspResFileMap.size());
				log.info("jspInnerFileMap size:{}", jspInnerFileMap.size());
				log.debug("jspFileMap size:" + jsFileMap.size());
				log.debug("cssFileMap size:" + cssFileMap.size());				
							
				this.unionJspResourceMap();

				this.compressResource();
				
				this.replaceJspUNResources();
												
				this.processJspMap(compressJarOutputStream);
				
				this.copyUNRtoJar(compressJarOutputStream, false);				
				
				log.info("Compress end successful: " + (replaceSrcWar ? srcFile.getAbsolutePath() : dstFileCompress.getAbsolutePath()));

			} catch (Exception ex) {
				log.error("createCompressJarFile", ex);
				compressJarOutputStream.putNextEntry(new JarEntry("stub"));
			} finally {
				IOUtils.closeQuietly(compressJarOutputStream);
			}

		} finally {
			if (srcJarFile != null) {
				srcJarFile.close();
			}
			if (replaceSrcWar) {
				FileUtils.deleteQuietly(srcFile);
				dstFileCompress.renameTo(srcFile);
			}
		}
	}

	/**
	 * Initialization.
	 *
	 * @throws Exception the exception
	 */
	private void initialization() throws Exception {

		log.debug("resourcesCompress:" + resourcesCompress);
		/*
		 * if(resourcesCompress!=null) { for(int i=0; i <
		 * resourcesCompress.length;i++) { //log.info("[" + i + "]" +
		 * this.resourcesCompress[i][0]); for(int j = 0; j <
		 * resourcesCompress[i].length;j++) { if(j == 0) {
		 * log.info(this.resourcesCompress[i][j]); } else { log.info(" " +
		 * this.resourcesCompress[i][j]); } } } }
		 */

		if (StringUtils.isEmpty(srcWarName)) {
			throw new Exception("Error srcWarName has not set!");
		}

		if (StringUtils.isEmpty(dstWarPath)) {
			dstWarPath = "target/tmp";
		}

		if (StringUtils.isEmpty(copyPackagePath)) {
			copyPackagePath = "target/" + srcWarName + "/WEB-INF";
		}

		if (!srcWarName.toLowerCase().contains(".war")) {
			srcWarName += ".war";
		}

		if (StringUtils.isEmpty(dstWarName)) {
			dstWarName = srcWarName.toLowerCase().replace(".war", "");
			dstWarName += ".war";
		} else if (!dstWarName.toLowerCase().contains(".war")) {
			dstWarName += ".war";
		}
	}

	/**
	 * Store for compress.
	 *
	 * @param file the file
	 * @param rootFolderName the root folder name
	 * @param jspFile the jsp file
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/* make list of .jsp that contains *.js, *.css files, .css, .js files */
	public boolean storeForCompress(final File file, final String rootFolderName, final JspFile jspFile)
			throws IOException {

		String fileExt = FilenameUtils.getExtension(file.getName()).toLowerCase();
		String filePath = FilenameUtils.getFullPath(file.getAbsolutePath());
		
		if ("css".equals(fileExt)) {
			if (this.cssFileMap.containsKey(filePath)) {
				this.cssFileMap.get(filePath).add(file);
			} else {
				ArrayList<File> fileList = new ArrayList<File>();
				fileList.add(file);
				this.cssFileMap.put(filePath, fileList);
			}					
			
			
            if(this.resetReferencesToImagesInCss) {
			   this.resetReferencesToImagesInCss(file);
			}
            
            this.copyToTmpDir(file, rootFolderName);
			
			return true;
		}

		if ("js".equals(fileExt)) {
			if (this.jsFileMap.containsKey(filePath)) {
				this.jsFileMap.get(filePath).add(file);
			} else {
				ArrayList<File> fileList = new ArrayList<File>();
				fileList.add(file);
				this.jsFileMap.put(filePath, fileList);
			}
			this.copyToTmpDir(file, rootFolderName);
			return true;
		}

		//look for jsp with resources ".js" or ".css"
		if ("jsp".equals(fileExt)) {
			List<String> fileLines = FileUtils.readLines(file, "UTF-8");
			for (String line : fileLines) {
				if (line.toLowerCase().matches(resourceFilePattern)) {

					this.makesJspFileUnResource(file, rootFolderName);

					if (this.jspResFileMap.containsKey(filePath)) {
						this.jspResFileMap.get(filePath).add(file);
					} else {
						ArrayList<File> fileList = new ArrayList<File>();
						fileList.add(file);
						this.jspResFileMap.put(filePath, fileList);
					}
					this.copyToTmpDir(file, rootFolderName);
					this.getResourceFileNameOrdered(file);
					return true;
				}
			}
		}	
		
		if (this.unionJsp && "jsp".equals(fileExt)) {	
		   //log.info("jsp:{}",file.getName());
		   this.copyToTmpDir(file, rootFolderName);
		   processChildJspFile( rootFolderName,filePath,file,jspFile);	
		   if(!this.jspFileMap.containsKey(filePath)) {
			   this.jspFileMap.put(file.getAbsolutePath(), file);
		   }
		   return true;
		}
		
		return false;
	}
	

	/**
	 * Process child jsp file.
	 *
	 * @param rootFolderName the root folder name
	 * @param parentFilePath the parent file path
	 * @param file the file
	 * @param jspFile the jsp file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void processChildJspFile(final String rootFolderName,
									final String parentFilePath, 
									final File file, 
									final JspFile jspFile) throws IOException {
		
		
		List<String> fileLines = FileUtils.readLines(file, "UTF-8");
		
		boolean detectJspFile = false;
		//boolean addTojspInnerFileMap = false;
		for (String line : fileLines) {
			String line_=line.trim().toLowerCase();
			
			if(!line_.matches(jspCommentPattern) && line_.matches(jspIncludeFilePattern)) {
				
				    if(!detectJspFile) {				    	
				       log.info("detect inner jsp:{}, file:{}", line, FilenameUtils.getName(file.getAbsolutePath()));
				       detectJspFile=true;
				    }
				  							
					File jspChildrenFile = getJspChildJspFileFromTag(line, file);
					
					if(jspFile.file ==  null) {
					   jspFile.setFile(file);
					}					
					
					//if(jspFile.file!=null && !getJspFileInInnerMap(jspFile.file.getAbsolutePath(), jspFile)) {
					if(jspFile.file != null && !isJspFileInInnerMap(jspFile.file.getAbsolutePath(), jspFile)) {
					   jspInnerFileMap.put(jspFile.getFile().getAbsolutePath().toString(), jspFile);
					   //this.copyToTmpDir(file, rootFolderName);		
					   log.info("add parent jsp file:{}", jspFile.file.getAbsolutePath());
					} 
																			
					JspFile childJspFile = new JspFile();
					//childJspFile.setFile(new File(fullPath));
					childJspFile.setFile(jspChildrenFile);	
				    childJspFile.setParentJspFile(jspFile);
				    jspFile.addChildJspFile(childJspFile);					
					 //this.copyToTmpDir(file, rootFolderName);				    
				    processChildJspFile(rootFolderName, parentFilePath, childJspFile.getFile(), childJspFile);	
			}
		}		
	}
	
	
	
	/**
	 * Checks if is jsp file in inner map.
	 *
	 * @param filePath the file path
	 * @param jspFile_ the jsp file_
	 * @return true, if is jsp file in inner map
	 */
	private boolean isJspFileInInnerMap(String filePath, JspFile jspFile_) {
		boolean result = false;
		for(Iterator<String> iterator = jspInnerFileMap.keySet().iterator(); iterator.hasNext();) {
			JspFile jspFile = jspInnerFileMap.get(iterator.next());
			if(isJspFileHavePath(filePath, jspFile)) {				
			  result = true;
			  break;
		    }			
		}		
		return result;
	}
		
	
	/**
	 * Checks if is jsp file have path.
	 *
	 * @param filePath the file path
	 * @param jspFile the jsp file
	 * @return true, if is jsp file have path
	 */
	private boolean isJspFileHavePath(String filePath, JspFile jspFile) {
		
		if(jspFile.file == null) {
			return true;			
		}
		if(jspFile.file.getAbsolutePath().equals(filePath)) {
			return true;
		}
		
		if(jspFile.getChildJspFileList()==null || jspFile.getChildJspFileList().size() < 1) {
			return false;
		}
		
		for(JspFile jspFile_:jspFile.getChildJspFileList()) {
		   if(isJspFileHavePath(filePath, jspFile_)) {
		    	return true;
		    }			
		}		
		return false;
    }
		
	
	/**
	 * Process jsp map.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void unionJspResourceMap() throws IOException {

		List<String> cssResourceList = new ArrayList<String>();
		List<String> jsResourceList = new ArrayList<String>();
		for(String dirPath : jspResFileMap.keySet()) {
			List<File> jspFileList = jspResFileMap.get(dirPath);
			for(File jspFile : jspFileList) {
				boolean addUnJS = true;
				boolean addUnCss = true;
				File fileUNR = this.getJspUNRFile(FilenameUtils.getFullPath(jspFile.getAbsolutePath()),
												  FilenameUtils.getBaseName(jspFile.getAbsolutePath()),
												  resourceRootDir);
				List<String> fileStringList = FileUtils.readLines(jspFile);

				for (String fileString : fileStringList) {
					int index1 = 0;
					int index2 = 0;

					if (fileString.matches(cssResourceFilePattern)) {
						index1 = fileString.indexOf("/");
						index2 = fileString.indexOf(".css") + 4;
						cssResourceList.add(fileString.substring(index1, index2));
						if (addUnCss) {
							addUnCss = false;
							this.addDeclarationOfUnResource(fileUNR, FilenameUtils.getBaseName(jspFile.getName()), "css", fileString);
						}
						continue;
					}

					if (fileString.matches(jsResourceFilePattern)) {
						index1 = fileString.indexOf("/");
						index2 = fileString.indexOf(".js") + 3;
						jsResourceList.add(fileString.substring(index1, index2));
						if (addUnJS) {
							addUnJS = false;
							this.addDeclarationOfUnResource(fileUNR, FilenameUtils.getBaseName(jspFile.getName()), "js", "");
						}
						continue;
					}
					FileUtils.write(fileUNR, fileString + "\n\r", true);
				}
				log.debug("jsp file:"+jspFile.getAbsolutePath());
				log.debug("css files:"+cssResourceList);
				log.debug("js files:"+jsResourceList);
				this.addResourceContentToUnionResource(jspFile,	this.cssFileMap, cssResourceList, ".css", unionCssDirPath);
				this.addResourceContentToUnionResource(jspFile, this.jsFileMap,	jsResourceList, ".js", unionJsDirPath);
				cssResourceList.clear();
				jsResourceList.clear();
			}
		}
	}

	/**
	 * Copy to tmp dir.
	 *
	 * @param file the file
	 * @param rootFolderName the root folder name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/* store resources to tmp directory t */
	private File copyToTmpDir(final File file, final String rootFolderName) throws IOException {
		
		File tmpPackageDir = this.getTempDir(file, rootFolderName);
		if (!tmpPackageDir.exists()) {
			if (!tmpPackageDir.mkdirs()) {
				throw new IOException("cannto create dir:"	+ tmpPackageDir.getAbsolutePath());
			}
		}
		File tmpFile = FileUtils.getFile(tmpPackageDir, file.getName());

		FileUtils.copyFile(file, tmpFile);
		
		log.debug("copy to :"+tmpFile);
		return tmpFile;
	}
	
	private File getTempDir(final File file, final String rootFolderName) {
		String filePackagePath = file.getAbsolutePath();
		filePackagePath = filePackagePath.substring(filePackagePath.indexOf(rootFolderName), filePackagePath.length());
		File tmpPackageDir = new File(tmpDir + File.separator + FilenameUtils.getPath(filePackagePath));
		return tmpPackageDir;		
	}

	/**
	 * Makes jsp file un resource.
	 *
	 * @param file the file
	 * @param rootFolderName the root folder name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/*
	 * create tmp jsp file with one reference on union .css, .js, and store it
	 * File object in map
	 */
	private void makesJspFileUnResource(final File file,
			final String rootFolderName) throws IOException {
		String filename = file.getName().replace(".jsp", "UNR.jsp");

		String filePackagePath = file.getAbsolutePath();
		filePackagePath = filePackagePath.substring(filePackagePath.indexOf(rootFolderName), filePackagePath.length());
		File tmpPackageDir = new File(tmpDir + File.separator + FilenameUtils.getPath(filePackagePath));
		if (!tmpPackageDir.exists()) {
			if (!tmpPackageDir.mkdirs()) {
				throw new IOException("cannot create dir:" + tmpPackageDir.getAbsolutePath());
			}
		}

		File fileUn = new File(tmpPackageDir.getAbsolutePath() + File.separator	+ filename);
		if (fileUn.exists()) {
			FileUtils.deleteQuietly(fileUn);
		}
		boolean result = fileUn.createNewFile();
		String basedir = FilenameUtils.getFullPath(fileUn.getAbsolutePath());
		if (this.jspURFileMap.containsKey(basedir)) {
			this.jspURFileMap.get(basedir).add(fileUn);
		} else {
			List<File> fileList = new ArrayList<File>();
			fileList.add(fileUn);
			this.jspURFileMap.put(basedir, fileList);
		}

		log.debug("create fileUn to " + result + ":" + fileUn.getAbsolutePath());
	}

	/**
	 * Gets the resource file name ordered.
	 *
	 * @param file the file
	 * @return the resource file name ordered
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/* make map with total .css, .js. resource path */
	private void getResourceFileNameOrdered(final File file) throws IOException {
		List<String> fileStrings = FileUtils.readLines(file);
		String pageResourceBaseDir = file.getAbsolutePath().replace(
				pagePathDir, "");
		pageResourceBaseDir = FilenameUtils.getPath(pageResourceBaseDir);
		Map<String, List<String>> resourceList = new TreeMap<String, List<String>>();
		List<String> cssResourceList = new ArrayList<String>();
		List<String> jsResourceList = new ArrayList<String>();
		for (String line : fileStrings) {
			if (line.matches(cssResourceFilePattern)) {
				int index1 = line.indexOf("/css");
				int index2 = line.indexOf(".css", index1);
				if (index1 < 0 || index2 < 0) {
					continue;
				}
				String resourcePath = line.substring(index1, index2 + 4);
				cssResourceList.add(resourcePath);
				log.debug("add resource:"+resourcePath);
			}
			if (line.matches(jsResourceFilePattern)) {
				int index1 = line.indexOf("/js");
				int index2 = line.indexOf(".js", index1);
				if (index1 < 0 || index2 < 0) {
					continue;
				}
				String resourcePath = line.substring(index1, index2 + 3);
				jsResourceList.add(resourcePath);
				log.debug("add resource:"+resourcePath);
			}
		}
		resourceList.put("css", cssResourceList);
		log.debug(pageResourceBaseDir + ":css size:" + cssResourceList.size());
		resourceList.put("js", jsResourceList);
		log.debug(pageResourceBaseDir + ":js size:{}" + jsResourceList.size());

		this.resourceMapFile.put(pageResourceBaseDir, resourceList);
	}

	/**
	 * Adds the resource content to union resource.
	 *
	 * @param jspFile the jsp file
	 * @param resourceFileMap the resource file map
	 * @param resourceList the resource list
	 * @param ext the ext
	 * @param unionResourceDir the union resource dir
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/*
	 * create union resource file .css, .js and stroe File object in set for
	 * css, js
	 */
	private void addResourceContentToUnionResource(final File jspFile,
												   final Map<String, List<File>> resourceFileMap,
												   final List<String> resourceList,
												   final String ext,
												   final String unionResourceDir) throws IOException {

		String unionResourceFileName = FilenameUtils.getBaseName(jspFile.getName()) + ext;
		File unionResourceFile = new File(unionResourceDir + "/" + unionResourceFileName);
		if (unionResourceFile.exists()) {
			unionResourceFile.delete();
		}
		unionResourceFile.createNewFile();

		for (String jspResource : resourceList) {
			boolean endLoop = false;
			for (List<File> resourceFileList : resourceFileMap.values()) {
				for (File resourceFile : resourceFileList) {
					if (resourceFile.getAbsolutePath().contains(jspResource)) {
						FileUtils.writeStringToFile(unionResourceFile, "\r\n/* " + resourceFile.getAbsolutePath() + "*/\r\n", true);						
						FileUtils.writeByteArrayToFile(unionResourceFile, FileUtils.readFileToByteArray(resourceFile), true);
						
						if (ext.equals(".js")) {
							if (!jsURFileList.contains(unionResourceFile)) {
								this.jsURFileList.add(unionResourceFile);
							}
							
						}
						if (ext.equals(".css")) {
							if (!this.cssURFileList.contains(unionResourceFile)) {
								//log.info("add union-Resource file={}", unionResourceFile.getAbsolutePath());
								this.cssURFileList.add(unionResourceFile);
							}						
						}					
						endLoop = true;
						break;
					}
				}
				if (endLoop) {
					endLoop = false;
					break;
				}
			}
		}
		log.debug("union *" + ext + " resource:"+unionResourceFile);
	}

	/**
	 * Gets the jsp unr file.
	 *
	 * @param basedir the basedir
	 * @param baseFileName the base file name
	 * @param resourceRootDir the resource root dir
	 * @return the jsp unr file
	 */
	/* get jsp union file by basefilename */
	File getJspUNRFile(final String basedir, final String baseFileName,
			final String resourceRootDir) {
		String relativePath;
		int index = basedir.indexOf(resourceRootDir);
		if (index == 0) {
			throw new IllegalArgumentException("dir: " + resourceRootDir
					+ " not found in:" + basedir);
		}

		relativePath = basedir.substring(index, basedir.length());

		for (String path : this.jspURFileMap.keySet()) {
			if (path.contains(relativePath)) {
				List<File> fileList = this.jspURFileMap.get(path);
				for (File file : fileList) {
					if (FilenameUtils.getBaseName(file.getAbsolutePath())
							.equals((baseFileName + "UNR"))) {
						return file;
					}
				}
			}
		}
		throw new IllegalArgumentException("filename: " + baseFileName	+ " not found");
	}

	/**
	 * Adds the declaration of un resource.
	 *
	 * @param file the file
	 * @param filename the filename
	 * @param type the type
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/* write reference to resource by pattern */
	public void addDeclarationOfUnResource(final File file,
										   final String filename,
										   final String type, 
										   final String fileString) throws IOException {
		
		String refer = "";
		if (type.toLowerCase().equals("css")) {
			refer = cssReference.replace("#", filename + (useMinFile ? "-min." : ".") + type);
			
			if(fileString.trim().matches(varCssPattern)) {
				refer = fileString.substring(0, fileString.indexOf("=")+1) + "'" + refer + "';"; 
			}
		} else {
			refer = jsReference.replace("#", filename + (useMinFile ? "-min." : ".") + type);
		}
		FileUtils.write(file, refer + "\n\r", true);
	}

	/**
	 * Replace jsp un resources.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/* replace content of union tmp jsp file to current jsp file */
	public void replaceJspUNResources()
			throws IOException {
		for (List<File> fileList : this.jspURFileMap.values()) {
			for (File file : fileList) {
				String path = FilenameUtils.getFullPath(file.getAbsolutePath());
				String filename = file.getName().replace("UNR.jsp", ".jsp");
				File srcJsp = new File(path + File.separator + filename);
				FileUtils.copyFile(file, srcJsp);
				if (deleteUR) {
					file.delete();
				}
			}
		}
	}

	/**
	 * Copy un rto jar.
	 *
	 * @param compressJarOutputStream the compress jar output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/* copy union resource to jar */
	public void copyUNRtoJar(final JarOutputStream compressJarOutputStream, final boolean skipJspUr)	throws IOException {
		
		//writeJspWithURtoJar(compressJarOutputStream);

		for(File file : this.cssURFileList) {
			log.info("write union resource to file:{}", file.getAbsolutePath());
			this.writeEntryToJar(file, compressJarOutputStream, ".css");
		}

		for(File file : this.jsURFileList) {
			this.writeEntryToJar(file, compressJarOutputStream, ".js");
		}
		
		if(!skipJspUr) {
		   this.writeJspWithURtoJar(compressJarOutputStream);
		}
	}
	
	
	/* copy union resource to jar */
	public void removeTmpDir()	throws IOException {		
		if(this.removeTmpDir) {
		   File  file = new File(this.tmpDir);
		   FileUtils.deleteQuietly(file);  
		}		
	} 
	
	
	/**
	 * Write jsp with u rto jar.
	 *
	 * @param compressJarOutputStream the compress jar output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeJspWithURtoJar(final JarOutputStream compressJarOutputStream) throws IOException {
		for (List<File> fileList : this.jspURFileMap.values()) {
			for (File file : fileList) {
				
				String path = file.getAbsolutePath();
				String srcPath = path.replace("UNR.jsp", ".jsp");
				File fileSrc = new File(srcPath);
				// String name = File.separator +
				// path.substring(path.indexOf(rootName),path.length());
				// String name =
				// path.substring(path.indexOf(RESOURCE_ROOT_DIR),path.length());
				// name=name.replace("UNR.jsp", ".jsp");
				// File fileSrc=new File(name);
				this.writeEntryToJar(fileSrc, compressJarOutputStream, null);
			}
		}
	}
		

	/**
	 * Write entry to jar.
	 *
	 * @param srcFile the src file
	 * @param compressJarOutputStream the compress jar output stream
	 * @param ext_ the ext_
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/* write entry to jar */
	private void writeEntryToJar(final File srcFile,
			                     final JarOutputStream compressJarOutputStream,
			                     final String ext_)	throws IOException {
		
		String path = srcFile.getAbsolutePath();
		String ext = "." + FilenameUtils.getExtension(srcFile.getName());
		
		if (useMinFile && ext_ != null && ext.equals(ext_)) {
			path = path.replace(ext, "-min" + ext);
		}
		
		String nameEntry = path.substring(path.indexOf(resourceRootDir), path.length());
		JarEntry entry = new JarEntry(nameEntry);
		compressJarOutputStream.putNextEntry(entry);

		InputStream entryInputStream = new FileInputStream(new File(path));
		IOUtils.copy(entryInputStream, compressJarOutputStream);

		compressJarOutputStream.flush();
		log.info("add entry:" + nameEntry);
	}
			
	
	private void processJspMap(final JarOutputStream compressJarOutputStream) throws IOException {
		if(!this.unionJsp) {
			return;
		}
        StringBuffer stringBuffer = new StringBuffer();
		for(String filepath : jspInnerFileMap.keySet()) {
			JspFile jspFile = jspInnerFileMap.get(filepath);
			this.readContentJspFile(jspFile, stringBuffer);
			this.writeEntryToJar(jspFile.file, stringBuffer.toString().getBytes(), compressJarOutputStream);
			
			log.info("store parentjsp file:{}, length:{}", jspFile.file, stringBuffer.length());
			stringBuffer.delete(0, stringBuffer.length());
		}
		
		for(File srcFile: this.jspFileMap.values()) {
			byte[]data = IOUtils.toByteArray(new FileInputStream(srcFile));
		    this.writeEntryToJar(srcFile, data, compressJarOutputStream);
		    log.info("store jsp fileName:{}, length:{}", srcFile.getName(), data.length);
		}
	}
	
	
	private String trimJspString(final String line) {
		
		if(line.indexOf("<%--")> -1 || line.indexOf("<!--")> -1) {
			log.debug("find '<%--' or '<!--' ");
		}
		String trimLine = line.trim().replaceAll("(<%--.+--%>)", "")
				 					 .replaceAll("(<!--.+-->)", "")
				                    // .replace("<%.*", "")
				                    // .replace(".*%>","")
				                    // .replace("<!--*+", "")
				                    // .replace(".*-->","")
				                     .replace("[0x0D-0x0A]", "");
	    return trimLine;
	}


	private void writeEntryToJar(final File srcFile,
			                     final byte[] content,
			                     final JarOutputStream compressJarOutputStream) throws IOException {

		String path = srcFile.getAbsolutePath();

		String nameEntry = path.substring(path.indexOf(resourceRootDir), path.length());
		JarEntry entry = new JarEntry(nameEntry);
		compressJarOutputStream.putNextEntry(entry);

		compressJarOutputStream.write(content);
		compressJarOutputStream.flush();
		log.info("add entry:" + nameEntry);
	}


	private void readContentJspFile(final JspFile jspFile, final StringBuffer stringBuff) throws IOException {
				
        if(jspFile.file == null || !jspFile.file.exists()) {
        	throw new IOException("File not found, jspFile:" + jspFile.file);
        }
        
        log.info("read content of file:{}",jspFile.file.getName());
               
		List<String> fileLines = this.readJspContentFromTmpDir(jspFile.file, tmpDir,FilenameUtils.getBaseName(copyPackagePath));
	
		for(String line : fileLines) {	
			
			String line_ = line.trim().toLowerCase();
			
			if (!line_.matches(jspCommentPattern) && line_.matches(jspIncludeFilePattern)) {	
			   File jspChildFile = getJspChildJspFileFromTag(line, jspFile.file);			  
			   JspFile jspChildrenFile= this.getChildrenJspFile(jspFile,  jspChildFile.getAbsolutePath());
			   
			   if(!jspFile.file.equals(jspChildrenFile.file)) {
			      readContentJspFile(jspChildrenFile, stringBuff);
			   }			   
			} else {
				if(stringBuff.indexOf(line) > -1) {
					continue;
				}
				if(this.trimJsp) {
					if(jspFile.file.getName().equals("index.jsp")) {
						log.info("index.jsp");
					}
				   String trimLine = this.trimJspString(line);
				   stringBuff.append(trimLine);	
				} else {
				   stringBuff.append(line + "\r\n");	
				}
		       
		    }		
		}
		
		if(this.jspFileMap.containsKey(jspFile.file.getAbsolutePath())) {
			this.jspFileMap.remove(jspFile.file.getAbsolutePath());
			log.info("remove processed file:{}", jspFile.file.getName());
		}
		
	}
	
	private List<String> readJspContentFromTmpDir(final File file, final String pathTmpDir, final String rootDir) throws IOException {
		String path = file.getAbsolutePath();
		int index1 = path.indexOf(rootDir);
		String tmpFilePath = pathTmpDir + File.separator + path.substring(index1);
		
		File tmpFile =  new File(tmpFilePath);
		//if(tmpFile.exists()) {
        return FileUtils.readLines(tmpFile, "UTF-8");
		//}
		
		//return  FileUtils.readLines(file, "UTF-8");
	}


	private JspFile getChildrenJspFile(JspFile parentJspFile, String filepath) {
		
		if(parentJspFile.file == null) {
			return null;
		}
		
		for(JspFile jspFile: parentJspFile.getChildJspFileList()) {
			
		   if(jspFile.file.getAbsolutePath().equals(filepath)) {
			  return jspFile; 
		   }
		   
		   if(jspFile.getChildJspFileList()!= null) {
			  JspFile jsp_ = getChildrenJspFile(jspFile,filepath);
			  if( jsp_ != null) {
				  return jsp_;
			  }
		   }
		}	
		return null;
	}

		
	private File getJspChildJspFileFromTag(final String jspIncludeTag, final File file) {
		
		if(jspIncludeTag.trim().matches(jspCommentPattern)){
			return null;
		}
		
		String filePath = file.getAbsolutePath();
		
		int index1 = jspIncludeTag.indexOf("WEB-INF");
		int index2 = jspIncludeTag.indexOf("%", index1);		
		log.info("getJspChildJspFileFromTag, jspIncludeTag={}, index1={}, index={}, filePath={}", jspIncludeTag.trim(), index1, index2,  filePath);
		String jspPath = jspIncludeTag.substring(index1, index2).replace("\"","").trim();
		String fullPath = filePath.substring(0,filePath.indexOf("WEB-INF")) + jspPath;	
		return new File(fullPath);
	}
	
	
	/**
	 * Compress resource.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void compressResource() throws IOException {
		
		if(!this.compressResource) {
			return;
		}

		InputStream in = CompressUa.class.getClassLoader().getResourceAsStream(compressorName);
		byte[] compressFileContentn = IOUtils.toByteArray(in);
		IOUtils.closeQuietly(in);

		compressFile = new File(compressorName);
		FileUtils.writeByteArrayToFile(compressFile, compressFileContentn);

		for (File jsFile : jsURFileList) {
			this.compressResource(jsFile, compressFileContentn, ".js");
		}

		/*if (compressFile.exists()) {
			compressFile.delete();
		}*/

		for (File cssFile : cssURFileList) {
			this.compressResource(cssFile, compressFileContentn, ".css");
		}

		/*if (compressFile.exists()) {
			compressFile.delete();
		}*/

	}

	/**
	 * Compress resource.
	 *
	 * @param jsFile the js file
	 * @param compressFileContentn the compress file contentn
	 * @param ext the ext
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException 
	 */
	private void compressResource(final File resourceFile,
							   	  final byte[] compressFileContentn,
							   	  final String ext) throws IOException {
		String baseDir;
        
		baseDir = FilenameUtils	.getFullPath(resourceFile.getAbsolutePath().toString());
        log.info("try compress resourceFile={}, compressFile={}", resourceFile.getAbsolutePath(), compressFile.getAbsolutePath());
		if (!compressFile.getAbsolutePath().contains(baseDir)) {
			//if (compressFile.exists()) {
			//	compressFile.delete();
			//}
			compressFile = new File(baseDir + File.separator + compressorName);
			if (!compressFile.exists()) {
				 compressFile.createNewFile();
				 FileUtils.writeByteArrayToFile(compressFile, compressFileContentn);
			}
		}

		String javaRuntime = "java -jar \"" + compressFile.getAbsolutePath()
										+ "\" --type " + FilenameUtils.getExtension(resourceFile.getName())
										+ " -o \"" + resourceFile.getAbsolutePath().replace(ext, (this.useMinFile ? "-min" : "") + ext)
										+ "\" --charset UTF-8 " + " -v \"" + resourceFile.getAbsolutePath() +"\" ";
		
		//javaRuntime = "java -jar \"" + compressFile.getAbsolutePath() +"\"";
		
		//javaRuntime=java -jar "../js/yuicompressor-2.4.8.jar" 
		//           --type js -o "../js/reportFooter-min.js" 
		//           --charset UTF-8  -v ".../js/reportFooter.js" 
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", compressFile.getAbsolutePath(),
				                       							"--type",  FilenameUtils.getExtension(resourceFile.getName()),
				                       							"-o", resourceFile.getAbsolutePath().replace(ext, (this.useMinFile ? "-min" : "") + ext),
				                       							"--charset", "UTF-8",
				                       							"-v", resourceFile.getAbsolutePath());
		Process process = pb.start();
		
		log.info("javaRuntime=" + javaRuntime);
		//Process process = Runtime.getRuntime().exec(javaRuntime);
		
		InputStream inError = null;	
		InputStream in = null;	
		byte[] resultData =null;
		int sizeBuffer=1024;
		
		try {
			 in = process.getInputStream();
			 inError = process.getErrorStream(); 
			 long startTime=System.currentTimeMillis();
			 while(!process.waitFor(100, TimeUnit.MILLISECONDS)) {			
				/*in = process.getInputStream();
				if(in!=null) {
					int avialableData = in.available();
					log.info("avialabel data size={}", avialableData);
					
					if(avialableData > 0) {
						sizeBuffer=avialableData;
						
					} else {
						sizeBuffer=1024;
					}
					resultData = new byte[sizeBuffer];
					//in.read(resultData);	
					in.read();
					log.info("resultData lenght={}", resultData!=null?resultData.length:null);
				}
				//resultData = IOUtils.toByteArray(in);
				//inError = process.getErrorStream();
				//byte[] result = IOUtils.toByteArray(inError);
				 */
				
				resultData = IOUtils.toByteArray(inError);
				log.info("input stream length={} ", (resultData!=null?resultData.length:null));
				long endTime = System.currentTimeMillis() - startTime;
				log.info("process={} working {}ms ...", process, endTime);			
			}
		} catch (Exception ex) {
			log.error("error wait of process={}", process, ex.getMessage());
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(inError);
		}
		
		log.info("process exit value={}\r\n", process!=null?process.exitValue():null);	
    }

	
	private void resetReferencesToImagesInCss(final File cssFile) throws IOException {
		if(!cssFile.exists()) {
			log.warn("Warn, resetReferencesToImagesInCss file is not exist={}", cssFile.getAbsolutePath());
			return;
		}
		List<String> strList = FileUtils.readLines(cssFile);
		List<String> strList_ = new ArrayList<String>();
		String line_="";
	    for( String line: strList) {	    	
	    	if(line.contains("url")) {//line.matches(.+url *\\\\(\\" *(\\\\.\\\\.\\\\/)+.+)
	    	   line_=line.replaceAll("(\\.\\.\\/)+", "../");	    	  
	    	   strList_.add(line_);
	    	   continue;
	    	}
	    	strList_.add(line);
	    }
		
	    FileUtils.writeLines(cssFile, strList_, false);
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
  
	
	public boolean getReplaceSrcWar() {
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
	 * Checks if is delete ur.
	 *
	 * @return true, if is delete ur
	 */
	public boolean isDeleteUR() {
		return deleteUR;
	}

	/**
	 * Sets the delete ur.
	 *
	 * @param deleteUR the new delete ur
	 */
	public void setDeleteUR(boolean deleteUR) {
		this.deleteUR = deleteUR;
	}

	/**
	 * Checks if is use min file.
	 *
	 * @return true, if is use min file
	 */
	public boolean isUseMinFile() {
		return useMinFile;
	}

	/**
	 * Sets the use min file.
	 *
	 * @param useMinFile the new use min file
	 */
	public void setUseMinFile(boolean useMinFile) {
		this.useMinFile = useMinFile;
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
	 * Gets the js reference.
	 *
	 * @return the js reference
	 */
	public String getJsReference() {
		return jsReference;
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
	 * Gets the css resource file pattern.
	 *
	 * @return the css resource file pattern
	 */
	public String getCssResourceFilePattern() {
		return cssResourceFilePattern;
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
	 * Gets the resource root dir.
	 *
	 * @return the resource root dir
	 */
	public String getResourceRootDir() {
		return resourceRootDir;
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
	 * Sets the css reference.
	 *
	 * @param cssReference the new css reference
	 */
	public void setCssReference(String cssReference) {
		this.cssReference = cssReference;
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
	 * Sets the resource file pattern.
	 *
	 * @param resourceFilePattern the new resource file pattern
	 */
	public void setResourceFilePattern(String resourceFilePattern) {
		this.resourceFilePattern = resourceFilePattern;
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
	 * Sets the js resource file pattern.
	 *
	 * @param jsResourceFilePattern the new js resource file pattern
	 */
	public void setJsResourceFilePattern(String jsResourceFilePattern) {
		this.jsResourceFilePattern = jsResourceFilePattern;
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
	 * Sets the compressor name.
	 *
	 * @param compressorName the new compressor name
	 */
	public void setCompressorName(String compressorName) {
		this.compressorName = compressorName;
	}

	public boolean isRemoveTmpDir() {
		return removeTmpDir;
	}

	public void setRemoveTmpDir(boolean removeTmpDir) {
		this.removeTmpDir = removeTmpDir;
	}

	public boolean isUnionJsp() {
		return unionJsp;
	}

	public void setUnionJsp(boolean unionJsp) {
		this.unionJsp = unionJsp;
	}

	public boolean isTrimJsp() {
		return trimJsp;
	}

	public void setTrimJsp(boolean trimJsp) {
		this.trimJsp = trimJsp;
	}

}