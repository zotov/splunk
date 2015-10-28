package com.buildua.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class CreateJarFile {
	 ArrayList<String> list =new ArrayList();
	 public static int BUFFER_SIZE = 10240;
	  protected void createJarArchive(File archiveFile, File[] tobeJared, String rootDir) {
	    try {
	      byte buffer[] = new byte[BUFFER_SIZE];
	      // Open archive file
	      FileOutputStream stream = new FileOutputStream(archiveFile);
	      //JarOutputStream out = new JarOutputStream(stream, new Manifest());
	      JarOutputStream out = new JarOutputStream(stream);

	      for (int i = 0; i < tobeJared.length; i++) {
	        if (tobeJared[i] == null || !tobeJared[i].exists()
	            || tobeJared[i].isDirectory())
	          continue; // Just in case...
	     

	        // Add archive entry
	        //String name = tobeJared[i].getName();
	        //if(this.list.contains(name)) {
	        //	continue;
	        //}
	     
	        //this.list.add(name);
	        String name = tobeJared[i].getAbsolutePath();
	        name=name.replaceFirst(rootDir+"(/|\\\\)*","");
	        System.out.println("Adding " + name);
	        
	        JarEntry jarAdd = new JarEntry(name);
	        jarAdd.setTime(tobeJared[i].lastModified());
	        out.putNextEntry(jarAdd);

	        // Write file to archive
	        FileInputStream in = new FileInputStream(tobeJared[i]);
	        while (true) {
	          int nRead = in.read(buffer, 0, buffer.length);
	          if (nRead <= 0)
	            break;
	          out.write(buffer, 0, nRead);
	        }
	        in.close();
	      }

	      out.close();
	      stream.close();
	      System.out.println("Adding completed OK");
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      System.out.println("Error: " + ex.getMessage());
	    }
	  }
}
