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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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


public class CompressUa {
	
	private static final Logger log = LoggerFactory.getLogger(CompressUa.class);
	
    private String srcWarName="server-node";
    	
    private String dstWarName="server-node-compress";
	
    private boolean replaceSrc=false;
		
	private String tmpDir="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target/tmp";
	
	private String copyPackagePath="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target/server-node/WEB-INF";
		
	private String srcWarPath="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target";
		
	private String dstWarPath="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target/tmp";
	
	private String jspPageRootDirPath="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target/server-node/WEB-INF/pages";
	
	private String pagePathDir="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target/server-node/WEB-INF/pages";

	private String unionCssDirPath="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target/tmp/WEB-INF/resources/css/";
	
	private String unionJsDirPath="/home/denis/Projects/PLUGIN/workspace/mvn-build-project-plugin/server-node/target/tmp/WEB-INF/resources/js/";
	
    private final String RESOURCE_ROOT_DIR="WEB-INF";
        
    private final String cssReference="<link href=\"${resourceUrl}/css/#\" rel=\"stylesheet\" type=\"text/css\" />";
    
    private final String jsReference="<script src=\"${resourceUrl}/js/#\" type=\"text/javascript\"></script>";

	
	private String[][] resourcesCompress; //{{result.js, script1.js, script2.js, script3.js,...},{result.css, style1.css, styel2.css,...}}
		
	//root path, css file
	private Map<String,List<File>>cssFileMap=new HashMap<String,List<File>>();
	//root path, js file
	private Map<String,List<File>>jsFileMap=new HashMap<String,List<File>>();
	//root path, jsp file
	private Map<String,List<File>>jspFileMap=new HashMap<String,List<File>>();
	//root path, 
	private Map<String,List<File>>jspURFileMap=new HashMap<String,List<File>>();
	
	private Set<File>cssURFileList=new HashSet<File>();
	
	private Set<File>jsURFileList=new HashSet<File>();
	
	boolean deleteUNR=false;
	
	private Map<String,Map<String,List<String>>>resourceMapFile=new HashMap<String,Map<String,List<String>>>();
	
	private final String resourceFilePattern=".+(src=|href=).+(\\.js|\\.css).+";
	private final String cssResourceFilePattern=".+(href=).+(\\.css).+";
	private final String jsResourceFilePattern=".+(src=).+(\\.js).+";
	
	public void execute() throws Exception {
		 
		initialization();
				
		log.info("Compress start");
				
		File dir = new File(tmpDir);
		if(!dir.exists() && !dir.mkdir()) {
		   log.error("Cannot create tmp dir:" + tmpDir);
		   throw new Exception("Cannot create tmp dir:" + tmpDir);
		}
				
		File dstWarFile = new File(dstWarPath + File.separator + this.dstWarName);
		File srcWarFile = new File(srcWarPath + File.separator + srcWarName);
				
		try {
			
			if(dstWarFile.exists()){
				dstWarFile.delete();
			}
			dstWarFile.createNewFile();
			
			if (!srcWarFile.exists()) {
				log.error("Error src war file not exist:" + srcWarFile.getAbsolutePath());
				throw new Exception("Error src war file not exist:" + srcWarFile.getAbsolutePath());
			} else if (!dstWarFile.exists()) {
				log.error("Error dst war file not exist:" + dstWarFile.getAbsolutePath());
				throw new Exception("Error dst war file not exist:" + srcWarFile.getAbsolutePath());
			} 
			
			FileUtils.copyFileToDirectory(srcWarFile, dir);
										
			log.info("Read file:" + srcWarFile.getAbsolutePath());
			this.createCompressJarFile(srcWarFile, dstWarFile);
			log.info("Compress end successful: " + (this.replaceSrc?srcWarFile.getAbsolutePath():dstWarFile.getAbsolutePath()));
	   } catch (IOException ex) {
			log.error("execute",ex);
	   }
    }
	
	

	public void createCompressJarFile(final File srcFile,
			                          final File dstFileCompress) throws IOException {
				
		JarFile srcJarFile = new JarFile(srcFile); // new JarFile(srcJarFile);
		
		try {
			JarOutputStream compressJarOutputStream = new JarOutputStream(new FileOutputStream(dstFileCompress));
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
				if(!copyDir.exists()) {
					throw new IOException("dir not exists:" + copyDir.getAbsolutePath());
				}

				// Copy resource from srcDir
				String rootFolderName = FilenameUtils.getBaseName(copyPackagePath);
				List<File> fileList = (List<File>) FileUtils.listFilesAndDirs(copyDir, FileFileFilter.FILE,	DirectoryFileFilter.DIRECTORY);

				for (File file : fileList) {
					if (file.isFile() && !this.storeForCompress(file,rootFolderName)) {
						String path = file.getAbsolutePath();
						//String name = File.separator + path.substring(path.indexOf(rootName),path.length());
						String name = path.substring(path.indexOf(rootFolderName),path.length());
						JarEntry entry = new JarEntry(name);
						compressJarOutputStream.putNextEntry(entry);
						log.info("add entry:" + name);

						InputStream entryInputStream = new FileInputStream(file);
						IOUtils.copy(entryInputStream, compressJarOutputStream);
						
						compressJarOutputStream.flush();
					}
				}
				
				log.info("jspFileMap size:"+this.jspFileMap.size());
				log.info("jsFileMap size:"+this.jsFileMap.size());
				log.info("cssFileMap size:"+this.cssFileMap.size());
				
				this.processJspMap(jspFileMap);				
				
				this.copyUNRtoJar(compressJarOutputStream);
				
			} catch (Exception ex) {
				log.error("createCompressJarFile",ex);
				compressJarOutputStream.putNextEntry(new JarEntry("stub"));
			} finally {
				IOUtils.closeQuietly(compressJarOutputStream);
			}

		} finally {
			if(srcJarFile!=null) {
			   srcJarFile.close();
			}
			
			if(this.replaceSrc) {
				FileUtils.deleteQuietly(srcFile);
				dstFileCompress.renameTo(srcFile);				
			}
		}
	}
		

	private void initialization() throws Exception {
		
	
				
		/*log.info(this.resourceCompressDir);
		
		log.info("resourcesCompress:" + resourcesCompress);
		if(resourcesCompress!=null) {
			for(int i=0; i < resourcesCompress.length;i++) {
				//log.info("[" + i + "]" + this.resourcesCompress[i][0]);		
				for(int j = 0; j < resourcesCompress[i].length;j++) {
					if(j == 0) {
					   log.info(this.resourcesCompress[i][j]);
					} else {
					   log.info(" " + this.resourcesCompress[i][j]);
					}
				}
			}		
		}*/
		
		if(StringUtils.isEmpty(this.srcWarName)){
			throw new Exception("Error srcWarName has not set!");
		}
		
		if(StringUtils.isEmpty(dstWarPath)){
			this.dstWarPath="target/tmp";
		}
		
		if(StringUtils.isEmpty(copyPackagePath)){
			this.copyPackagePath="target/"+srcWarName+"/WEB-INF";
		}		
		
		if(!srcWarName.toLowerCase().contains(".war")) {
			srcWarName+=".war";
		}
		
		if(StringUtils.isEmpty(dstWarName)) {
			dstWarName=srcWarName.toLowerCase().replace(".war","");		
		    dstWarName+=".war";
		} else if(!dstWarName.toLowerCase().contains(".war")) {
		    dstWarName+=".war";
	   }		
	}	
	
	
	public boolean storeForCompress(final File file, final String rootFolderName) throws IOException {
		
		String fileExt=FilenameUtils.getExtension(file.getName()).toLowerCase();
		String filePath=FilenameUtils.getFullPath(file.getAbsolutePath());
		if("css".equals(fileExt)) {
		   if(this.cssFileMap.containsKey(filePath)) {
			  this.cssFileMap.get(filePath).add(file);
    	   } else {
			  ArrayList<File> fileList=new ArrayList<File>();
			  fileList.add(file);
			  this.cssFileMap.put(filePath, fileList);
		   }		   
		   this.copyToTmpDir(file, rootFolderName);
		   return true;
		}
		
		if("js".equals(fileExt)) {
			if(this.jsFileMap.containsKey(filePath)){
			   this.jsFileMap.get(filePath).add(file);
			} else {
			   ArrayList<File> fileList=new ArrayList<File>();
			   fileList.add(file);
			   this.jsFileMap.put(filePath, fileList);
			}
			this.copyToTmpDir(file, rootFolderName);
			return true;
		}
		
		if("jsp".equals(fileExt)) {
		   List<String> fileLines= FileUtils.readLines(file, "UTF-8");
		   for(String line: fileLines) {
			   if(line.toLowerCase().matches(this.resourceFilePattern)) {
				   
				   this.makesJspFileUnResource(file, rootFolderName);
				   
				   if(this.jspFileMap.containsKey(filePath)){
					   this.jspFileMap.get(filePath).add(file);
					} else {
					   ArrayList<File> fileList=new ArrayList<File>();
					   fileList.add(file);
					   this.jspFileMap.put(filePath, fileList);
					}
				    this.copyToTmpDir(file, rootFolderName);
				    this.getResourceFileNameOrdered(file, pagePathDir);
				   return true;
			   }
		   }
		}		
		return false;
	}
	
	private void copyToTmpDir(final File file,final String rootFolderName) throws IOException {
		   String filePackagePath= file.getAbsolutePath();
		   filePackagePath = filePackagePath.substring(filePackagePath.indexOf(rootFolderName),filePackagePath.length());
		   File tmpPackageDir= new File(tmpDir + File.separator + FilenameUtils.getPath(filePackagePath));
		   if(!tmpPackageDir.exists()) {
			   if(!tmpPackageDir.mkdirs()){
				   throw new IOException("cannto create dir:"+tmpPackageDir.getAbsolutePath());
			   }
		   }		 
		   File tmpFile=FileUtils.getFile(tmpPackageDir, file.getName());
		   
		   FileUtils.copyFile(file, tmpFile);
		   log.info("copy to :{}",tmpFile);
	}
	
	private void makesJspFileUnResource(final File file, final String rootFolderName)throws IOException {
		   String filename=file.getName().replace(".jsp", "UNR.jsp");
		   
		   String filePackagePath= file.getAbsolutePath();
		   filePackagePath = filePackagePath.substring(filePackagePath.indexOf(rootFolderName),filePackagePath.length());
		   File tmpPackageDir= new File(tmpDir + File.separator + FilenameUtils.getPath(filePackagePath));
		   if(!tmpPackageDir.exists()) {
			  if(!tmpPackageDir.mkdirs()) {
				  throw new IOException("cannot create dir:"+tmpPackageDir.getAbsolutePath());
			  }
		   }		 
		 		   
		   File fileUn  = new File(tmpPackageDir.getAbsolutePath()  + File.separator + filename);
		   if(fileUn.exists()) {
			   FileUtils.deleteQuietly(fileUn);
		   }
		   boolean  result = fileUn.createNewFile();
		   String basedir=FilenameUtils.getFullPath(fileUn.getAbsolutePath());
		   if(this.jspURFileMap.containsKey(basedir)) {
			  this.jspURFileMap.get(basedir).add(fileUn); 
		   } else {
			   List<File> fileList=new ArrayList<File> ();
			   fileList.add(fileUn);
			   this.jspURFileMap.put(basedir, fileList);
		   }
		   
		   log.info("create fileUn to {}:{}",result,fileUn.getAbsolutePath());
	}
	
	private void getResourceFileNameOrdered(final File file, final String pageDirPath) throws IOException {
		List<String> fileStrings=FileUtils.readLines(file);
		String pageResourceBaseDir=file.getAbsolutePath().replace(pageDirPath, "");
		pageResourceBaseDir=FilenameUtils.getPath(pageResourceBaseDir);
		Map<String, List<String>> resourceList=new TreeMap<String,List<String>>();
		List<String>cssResourceList=new ArrayList<String>();
		List<String>jsResourceList=new ArrayList<String>();
		for(String line:fileStrings) {
		    if(line.matches(this.cssResourceFilePattern)) {
			   int index1=line.indexOf("/css");
			   int index2=line.indexOf(".css", index1);
			   if(index1 < 0 || index2 < 0) {
				   continue;
			   }
			   String resourcePath=line.substring(index1,index2+4);
			   cssResourceList.add(resourcePath);
			   log.info("add resource:{}",resourcePath);
		    }
		    if(line.matches(this.jsResourceFilePattern)) {
			   int index1=line.indexOf("/js");
			   int index2=line.indexOf(".js", index1);
			   if(index1 < 0 || index2 < 0) {
				   continue;
			   }
			   String resourcePath=line.substring(index1,index2 + 3);
			   jsResourceList.add(resourcePath);
			   log.info("add resource:{}",resourcePath);
			}
		}
		resourceList.put("css",cssResourceList);
		log.info("{}:css size:{}",pageResourceBaseDir,cssResourceList.size());
		resourceList.put("js",jsResourceList);
		log.info("{}:js size:{}",pageResourceBaseDir,jsResourceList.size());
		 
		this.resourceMapFile.put(pageResourceBaseDir, resourceList);		
	}
	
	public void processJspMap(final Map<String,List<File>>jspFileMap) throws IOException {
		List<String> cssResourceList=new ArrayList<String>();
		List<String> jsResourceList=new ArrayList<String>();
		for(String dirPath:jspFileMap.keySet()) {
			List<File> jspFileList=jspFileMap.get(dirPath);
			for(File jspFile:jspFileList) {
			   boolean addUnJS=true;
			   boolean addUnCss=true;
			   File fileUNR = this.getJspUNRFile(FilenameUtils.getFullPath(jspFile.getAbsolutePath()), FilenameUtils.getBaseName(jspFile.getAbsolutePath()));
			   List<String> fileStringList = FileUtils.readLines(jspFile);
			   
			   for(String fileString:fileStringList) {
				  int index1=0;
				  int index2=0;
				  
				  if(fileString.matches(cssResourceFilePattern)) {
					  index1=fileString.indexOf("/");
					  index2=fileString.indexOf(".css")+4;
					  cssResourceList.add(fileString.substring(index1,index2));
     				  if(addUnCss) {
						  addUnCss=false;
						  this.addDeclarationOfUnResource(fileUNR,FilenameUtils.getBaseName(jspFile.getName()),"css");
					  }					 
					  continue;
				  }
				 
				  if(fileString.matches(jsResourceFilePattern)) {
					 index1=fileString.indexOf("/");
					 index2=fileString.indexOf(".js")+3;
					 jsResourceList.add(fileString.substring(index1,index2));
					 if(addUnJS) {
						  addUnJS=false;
						  this.addDeclarationOfUnResource(fileUNR,FilenameUtils.getBaseName(jspFile.getName()),"js");
					 }
					 continue;
				  }	
				  FileUtils.write(fileUNR, fileString+"\n\r",true);
			   }
			   log.info("jsp file:{}",jspFile.getAbsolutePath());
			   log.info("css files:{}",cssResourceList);
			   log.info("js files:{}",jsResourceList);	
			   this.makeUnionResource(jspFile,this.cssFileMap, cssResourceList,".css",this.unionCssDirPath);
			   this.makeUnionResource(jspFile,this.jsFileMap, jsResourceList,".js",this.unionJsDirPath);
			   cssResourceList.clear();
			   jsResourceList.clear();
			}
		}
		
		this.replaceUNResources();
	}
	
	private void makeUnionResource(final File jspFile,final Map<String, List<File>> resourceFileMap,final List<String>resourceList,final String ext,final String resourceRoot) throws IOException {
		
		String unionResourceFileName=FilenameUtils.getBaseName(jspFile.getName()) + ext;
		File unionResourceFile=new File(resourceRoot+unionResourceFileName);
		if(unionResourceFile.exists()) {
		    unionResourceFile.delete();
		}
		unionResourceFile.createNewFile();
		for(String jspCssResource:resourceList) {
			boolean endLoop=false;
		    for(List<File> cssFileList: resourceFileMap.values()){
			    for(File cssFile:cssFileList) {
			   	    if(cssFile.getAbsolutePath().contains(jspCssResource)) {			   	    
					   FileUtils.writeStringToFile(unionResourceFile, "\r\n//# "+cssFile.getAbsolutePath()+"\r\n",true);						
					   FileUtils.writeByteArrayToFile(unionResourceFile, FileUtils.readFileToByteArray(cssFile), true);
					   if(ext.equals(".js")) {
						   this.jsURFileList.add(unionResourceFile);
					   }
					   if(ext.equals(".css")) {
						   this.cssURFileList.add(unionResourceFile);
					   }
					   endLoop=true;
					   break;
					}			   	    
				}
			    if(endLoop) {
			    	endLoop=false;
			    	break;
			    }
			}
		}	
		log.info("union *{} resource:{}",ext,unionResourceFile);
	}
	
	File getJspUNRFile(final String basedir,final String baseFileName) {
		String relativePath;
		int index=basedir.indexOf(RESOURCE_ROOT_DIR);
		if(index == 0) {
			throw new IllegalArgumentException("dir: " + RESOURCE_ROOT_DIR + " not found in:" + basedir);
		}
		
		relativePath=basedir.substring(index,basedir.length());
		  
		for(String path:this.jspURFileMap.keySet()) {
			if(path.contains(relativePath)) {
			    List<File> fileList=this.jspURFileMap.get(path);
				for(File file:fileList) {
					if(FilenameUtils.getBaseName(file.getAbsolutePath()).equals((baseFileName+"UNR"))) {
						return file;
					}
				}
			}
		}
		throw new IllegalArgumentException("filename: " + baseFileName + " not found");
	}
	
	public void addDeclarationOfUnResource(final File file,final String filename,final String type) throws IOException {
		 String refer="";
		 
		if(type.toLowerCase().equals("css")) {
		   refer=this.cssReference.replace("#",filename + "." + type);
		} else {
		   refer=this.jsReference.replace("#",filename + "." + type);	
		}
		FileUtils.write(file, refer+"\n\r",true);		
	}
	
	public void replaceUNResources() throws IOException {
		for(List<File> fileList:this.jspURFileMap.values()) {
			for(File file: fileList) {
				String path=FilenameUtils.getFullPath(file.getAbsolutePath());
				String filename=file.getName().replace("UNR.jsp", ".jsp");
				File srcJsp=new File(path + File.separator + filename);
				FileUtils.copyFile(file, srcJsp);
				if(this.deleteUNR) {
				   file.delete();
				}
			}
		}
	}
	
	public void copyUNRtoJar(final JarOutputStream compressJarOutputStream) throws IOException {
		
		for(List<File> fileList:this.jspURFileMap.values()) {
			for(File file: fileList) {
				String path = file.getAbsolutePath();
				
				String srcPath=path.replace("UNR.jsp", ".jsp");
				File fileSrc=new File(srcPath);
				//String name = File.separator + path.substring(path.indexOf(rootName),path.length());
				//String name = path.substring(path.indexOf(RESOURCE_ROOT_DIR),path.length());
				//name=name.replace("UNR.jsp", ".jsp");
				//File fileSrc=new File(name);
				this.writeEntryToJar( fileSrc, compressJarOutputStream);
			}
		}
		
		for(File file:this.cssURFileList) {
		  this.writeEntryToJar(file, compressJarOutputStream);			
		}
		
		for(File file:this.jsURFileList) {
		  this.writeEntryToJar(file, compressJarOutputStream);			
		}
		
	}
	
	private void writeEntryToJar(final File srcFile,final JarOutputStream compressJarOutputStream) throws IOException {
		String path = srcFile.getAbsolutePath();
		String nameEntry = path.substring(path.indexOf(RESOURCE_ROOT_DIR),path.length());
		JarEntry entry = new JarEntry(nameEntry);
		compressJarOutputStream.putNextEntry(entry);
		

		InputStream entryInputStream = new FileInputStream(srcFile);
		IOUtils.copy(entryInputStream, compressJarOutputStream);
		
		compressJarOutputStream.flush();
		log.info("add entry:" + nameEntry);
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

	public String getJspPageRootDirPath() {
		return jspPageRootDirPath;
	}

	public void setJspPageRootDirPath(String jspPageRootDirPath) {
		this.jspPageRootDirPath = jspPageRootDirPath;
	}
	
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
	

	/*public String[] getResourcesCompress() {
		return resourcesCompress;
	}

	public void setResourcesCompress(String[] resourcesCompress) {
		this.resourcesCompress = resourcesCompress;
	}*/
	
}