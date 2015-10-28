package com.buildua.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	@Parameter( defaultValue = "${build.finalName}",name = "srcWarName", readonly = true, required = true )
    private String srcWarName;
	
	@Parameter( defaultValue = "${build.finalName}",name = "dstWarName", readonly = false, required = false )
    private String dstWarName;
	
	@Parameter( defaultValue = "false",name = "replaceSrc", readonly = true, required = false )
    private boolean replaceSrc;
	
	
	@Parameter( defaultValue = "target/tmp",name = "tmpDir", readonly = true, required = false )
    private String tmpDir;
	
	@Parameter( defaultValue = "target/server-node/WEB-INF",name = "copyPackagePath", readonly = true, required = false )
    private String copyPackagePath;
	
	@Parameter( defaultValue = "target", name = "srcWarPath", readonly = true, required = false )
    private String srcWarPath;
	
	
	@Parameter( name = "dstWarPath", readonly = true, required = false )
    private String dstWarPath;
	
	@Parameter(defaultValue = "target/server-node/WEB-INF/pages", name = "resourceCompressDir", readonly = true, required = false )
	private String resourceCompressDir;
	
	@Parameter(readonly = true, required = false )
	private String[][] resourcesCompress; //{{result.js, script1.js, script2.js, script3.js,...},{result.css, style1.css, styel2.css,...}}
		
	private CompressUa ca;
	
	/*private Map<String,List<File>>cssFileMap=new HashMap<String,List<File>>();
	
	private Map<String,List<File>>jsFileMap=new HashMap<String,List<File>>();
	
	private Map<String,List<File>>jspFileMap=new HashMap<String,List<File>>();*/
		
	private Map<String,List<File>>cssFileMap;
	
	private Map<String,List<File>>jsFileMap;
	
	private Map<String,List<File>>jspFileMap;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		 
		initialization();
				
		log.info("Compress start");
				
		File dir = new File(tmpDir);
		if(!dir.exists() && !dir.mkdir()) {
		   log.error("Cannot create tmp dir:" + tmpDir);
		   throw new MojoExecutionException("Cannot create tmp dir:" + tmpDir);
		}
				
		File dstWarFile = new File(dstWarPath + File.separator + this.dstWarName);
		File srcWarFile = new File(srcWarPath + File.separator + srcWarName);
		
		
		try {
			dstWarFile.createNewFile();
			
			if (!srcWarFile.exists()) {
				log.error("Error src war file not exist:" + srcWarFile.getAbsolutePath());
				throw new MojoExecutionException("Error src war file not exist:" + srcWarFile.getAbsolutePath());
			} else if (!dstWarFile.exists()) {
				log.error("Error dst war file not exist:" + dstWarFile.getAbsolutePath());
				throw new MojoExecutionException("Error dst war file not exist:" + srcWarFile.getAbsolutePath());
			} 
			
			FileUtils.copyFileToDirectory(srcWarFile, dir);
										
			log.info("Read file:" + srcWarFile.getAbsolutePath());
			this.createCompressJarFile(srcWarFile, dstWarFile, copyPackagePath );
			log.info("Compress end successful: " + (this.replaceSrc?srcWarFile.getAbsolutePath():dstWarFile.getAbsolutePath()));
	   } catch (IOException ex) {
			ex.printStackTrace();
			log.error(ex);
	   }
    }
	
	

	public void createCompressJarFile(final File srcFile,
			                          final File dstFileCompress, 
			                          final String copyDirPath) throws IOException {
				
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
				
				File copyDir = new File(copyDirPath);
				if(!copyDir.exists()) {
					throw new IOException("dir not exists:"+copyDir.getAbsolutePath());
				}

				// Copy resource from srcDir
				String rootName = FilenameUtils.getBaseName(copyDirPath);
				List<File> fileList = (List<File>) FileUtils.listFilesAndDirs(copyDir, FileFileFilter.FILE,	DirectoryFileFilter.DIRECTORY);

				for (File file : fileList) {
					//if (file.isFile() && !this.storeForCompress(file)) {
					if (file.isFile()) {
						String path = file.getAbsolutePath();
						String name = File.separator + path.substring(path.indexOf(rootName),path.length());
						JarEntry entry = new JarEntry(name);
						compressJarOutputStream.putNextEntry(entry);
						log.debug("add entry:" + name);

						InputStream entryInputStream = new FileInputStream(file);
						if(FilenameUtils.getExtension(file.getName()).toLowerCase().equals("jsp")) {
						  byte[] data = IOUtils.toByteArray(entryInputStream);
						  log.info("filename:"+file.getName());
						  log.info("filename:"+file.getName()+", size:"+(data !=null ?data.length:"null"));
						  IOUtils.write(data, compressJarOutputStream);
						} else {
						  IOUtils.copy(entryInputStream, compressJarOutputStream);
						}
						compressJarOutputStream.flush();
					}
				}
				
				log.info("jspFileMap size:"+this.jspFileMap.size());
				log.info("jsFileMap size:"+this.jsFileMap.size());
				log.info("cssFileMap size:"+this.cssFileMap.size());
				
			} catch (Exception ex) {
				log.error(ex);
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
	
	
	

	private void initialization() throws MojoExecutionException {
		
		
		this.ca = new CompressUa();
		
		log = this.getLog();
				
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
			if(StringUtils.isEmpty(ca.getSrcWarName())) {
				throw new MojoExecutionException("Error srcWarName has not set!");	
			}
			this.srcWarName=ca.getSrcWarName();
		}
		
		if(StringUtils.isEmpty(dstWarPath)) {
			if(StringUtils.isEmpty(ca.getDstWarPath())) {
			   this.dstWarPath="target/tmp";	
			} else {
			   this.dstWarPath=ca.getDstWarPath();
			}
		}
		
		if(StringUtils.isEmpty(copyPackagePath)) {
			if(StringUtils.isEmpty(ca.getDstWarPath())) {
			   this.copyPackagePath="target/"+srcWarName+"/WEB-INF";
			} else {
			   this.copyPackagePath=ca.getCopyPackagePath();
			}
		}
						
		
		if(!srcWarName.toLowerCase().contains(".war")) {
			srcWarName+=".war";
		}
		
		if(StringUtils.isEmpty(dstWarName)) {
			dstWarName=srcWarName.toLowerCase().replace(".war","");		
		    dstWarName+="-compress.war";
		} else if(!dstWarName.toLowerCase().contains(".war")) {
		    dstWarName+="-compress.war";
	   }		
	}	
	
	
	public boolean storeForCompress(final File file) throws IOException {
		
		String fileExt=FilenameUtils.getExtension(file.getName()).toLowerCase();
		String filePath=FilenameUtils.getFullPath(file.getAbsolutePath());
		if("css".equals(fileExt)) {
		   if(this.cssFileMap.containsKey(filePath)){
			  this.cssFileMap.get(filePath).add(file);
		   } else {
			  ArrayList<File> fileList=new ArrayList<File>();
			  fileList.add(file);
			  this.cssFileMap.put(filePath, fileList);
		   }
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
			return true;
		}
		
		/*if("jsp".equals(fileExt)) {
		   List<String> fileLines= FileUtils.readLines(file, "UTF-8");
		   for(String line: fileLines) {
			   if(line.toLowerCase().matches(".*(src=|href=).+(\\.js|\\.css).+")){
				   if(this.jspFileMap.containsKey(filePath)){
					   this.jspFileMap.get(filePath).add(file);
					} else {
					   ArrayList<File> fileList=new ArrayList<File>();
					   fileList.add(file);
					   this.jspFileMap.put(filePath, fileList);
					}
				   return true;
			   }
		   }
		}		*/
		return false;
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
	
	public String getResourceCompressDir() {
		return resourceCompressDir;
	}

	public void setResourceCompressDir(String resourceCompressDir) {
		this.resourceCompressDir = resourceCompressDir;
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