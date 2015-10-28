package de.uniol.inf.is.odysseus.codegenerator.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileSearch {
	
	  private String fileNameToSearch;
	  private File directory;
	  private List<String> result = new ArrayList<String>();
	 
	  public FileSearch(){
		  
	   }
	  
	  public FileSearch(String fileNameToSearch, String directory){
		  this.fileNameToSearch = fileNameToSearch.trim();
		  this.directory = new File(directory);
	  }

	  public String getFileNameToSearch() {
		return fileNameToSearch;
	  }
	 
	  public void setFileNameToSearch(String fileNameToSearch) {
		this.fileNameToSearch = fileNameToSearch;
	  }
	 
	  public List<String> getResult() {
		return result;
	  }
	  
	  @Deprecated
	  public void searchDirectory() {
		setFileNameToSearch(fileNameToSearch.toLowerCase());
	 
		if (directory.isDirectory()) {
		    search(directory);
		} else {
		    System.out.println(directory.getAbsoluteFile() + " is not a directory!");
		}
	 
	  }
	 
	  private void search(File file) {
	 
		if (file.isDirectory()) {
		  System.out.println("Searching directory ... " + file.getAbsoluteFile());
	            //do you have permission to read this directory?	
		    if (file.canRead()) {
		    
				for (File temp : file.listFiles()) {
				    if (temp.isDirectory()) {
					search(temp);
				    } else {
					if (getFileNameToSearch().equals(temp.getName().toLowerCase())) {			
					    result.add(temp.getAbsoluteFile().toString());
				    }
		 
				}
			    }
	 
		 } else {
			System.out.println(file.getAbsoluteFile() + "Permission Denied");
		 }
	      }
	 
	  }


	

}