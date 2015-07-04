package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;




public class ReadJavaImports {
	
	public static List<String> getImportsFromFileWithKnownLocation(String fileFullPath){
		
		List<String> importList = new ArrayList<String>();
	
		    JavaDocBuilder builder = new JavaDocBuilder();
		    try {
				builder.addSource(new FileReader( fileFullPath  ));
			    JavaSource src = builder.getSources()[0];
			    String[] imports = src.getImports();

			    for ( String imp : imports )
			    {
			        System.out.println(imp);
			        importList.add(imp);
			    }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    return importList;	
	}
	

	public static List<String> getImportsFromFileWithUnknownLocation(String fileName, String odysseusPath){
		
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
		
		return getImportsFromFileWithKnownLocation(fileSearch.getResult().get(0));
		
	}

}
