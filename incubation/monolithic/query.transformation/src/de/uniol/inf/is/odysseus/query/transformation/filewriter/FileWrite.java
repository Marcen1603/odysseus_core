package de.uniol.inf.is.odysseus.query.transformation.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationCompilerParameter;
import de.uniol.inf.is.odysseus.query.transformation.mainstructure.ITransformationMainstructure;
import de.uniol.inf.is.odysseus.query.transformation.mainstructure.registry.QueryTransformationMainstructureRegistry;

public class FileWrite {
	
	private String fileName;
	private File file;
	private FileWriter writer;
	private String path;
	private ITransformationMainstructure mainStructure;
	
	public FileWrite(String fileName, TransformationCompilerParameter parameter){
		this.fileName = fileName;
		this.mainStructure = QueryTransformationMainstructureRegistry.getOperatorTransformation(parameter.getProgramLanguage());
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
