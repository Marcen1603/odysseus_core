package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

public class ReadJavaImports {
	
	public static Set<String> getImportsFromFileWithKnownLocation(String fileFullPath,String packageName){
		
		 Set<String> importList = new HashSet<String>();
	
		 JavaProjectBuilder builder = new JavaProjectBuilder();
		    try {
				builder.addSource(new FileReader( fileFullPath  ));
			    JavaSource src = builder.getSources().iterator().next();
			   
			
			    if( src.getPackage().equals(packageName)){
				    List<String> imports = src.getImports();
	
				    for ( String imp : imports )
				    {
				        System.out.println(imp);
				        importList.add(imp);
				    }
			    }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    return importList;	
	}
	

	public static Set<String> getImportsFromFileWithUnknownLocation(String fileName,String packageName, String odysseusPath){
		
		FileSearch fileSearch = new FileSearch(fileName, odysseusPath);
		fileSearch.searchDirectory();
		int count = fileSearch.getResult().size();
		
		if(count ==0){
		    System.out.println("\nNo result found!");
		}else{
		    System.out.println("\nFound " + count + " result!\n");
		    for (String matched : fileSearch.getResult()){
		    	System.out.println("Found : " + matched);
		  
		    	
		    }
		}
		
		return getImportsFromFileWithKnownLocation(fileSearch.getResult().get(0),packageName);
	}

	
	public static String getImportForFile(String fileFullPath){
		String importString = "";
		
	    JavaProjectBuilder builder = new JavaProjectBuilder();
	    try {
			builder.addSource(new FileReader( fileFullPath  ));
		    JavaSource src = builder.getSources().iterator().next();
		  
		   JavaClass clazzes = src.getClasses().get(0);
	
		    importString = src.getPackage().getName()+"."+clazzes.getName();
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return importString;
	}
}
