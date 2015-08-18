package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;


public class ReadJavaImports {

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
