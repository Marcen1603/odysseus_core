package de.uniol.inf.is.odysseus.query.transformation.java.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.mainstructure.JavaMainstructure;


public class JavaFileWrite {
	
	private String fileName;
	private File file;
	private FileWriter writer;
	private String path;
	private JavaMainstructure mainStructure;

	public JavaFileWrite(String fileName, TransformationParameter parameter){
		this.fileName = fileName;
		this.path = parameter.getTempDirectory();
		this.mainStructure = new JavaMainstructure();
	}
	
	public void createFile() throws IOException{
		StringBuilder absolutePath = new StringBuilder();
		absolutePath.append(path);
		absolutePath.append("\\");
		absolutePath.append(fileName);
		
		file = new File(absolutePath.toString());
		 // creates the file
		// creates a FileWriter Object
		file.createNewFile();
		writer = new FileWriter(file); 
	}
	
	public void writeImports(Set<String> importList) throws IOException{
		//sort the imports for nice look
		TreeSet<String> sortList = new TreeSet<String>( Collections.reverseOrder() );
		sortList.addAll(importList);
	
		for(String imp : sortList){
			 writer.write("import "+imp+";\n"); 
		}
	}
	
	
	public void writeClassTop() throws IOException{
		 writer.write(mainStructure.getClassTop()); 
	}
	
	public void writeBody(String bodyText) throws IOException{
		 writer.write(bodyText); 
	}
	
	
	public void writeBottom() throws IOException{
		//Write imports
		 writer.write(mainStructure.getClassBottom()); 
		 writer.flush();
		 writer.close();
	}
	
	

}
