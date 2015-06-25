package de.uniol.inf.is.odysseus.query.transformation.python.filewrite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;


public class PythonFileWrite {
	
	
	private String fileName;
	private File file;
	private FileWriter writer;
	private String path;

	public PythonFileWrite(String fileName, TransformationParameter parameter){
		this.fileName = fileName;
		this.path = parameter.getTempDirectory();
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
	
	public void writeBody(String bodyText) throws IOException{
		 writer.write(bodyText); 
	}

	
	public void closeFile() throws IOException{
		 writer.flush();
		 writer.close();
	}
}
