package de.uniol.inf.is.odysseus.query.transformation.java.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	
	
	public void writeClassTop() throws IOException{
		//Write imports
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
