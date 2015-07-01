package de.uniol.inf.is.odysseus.query.transformation.java.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.mainstructure.JavaMainstructure;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.GenerateAntBuildFile;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.UnZip;


public class JavaFileWrite {
	
	private String fileName;
	private File file;
	private FileWriter writer;
	private String path;
	private JavaMainstructure mainStructure;
	TransformationParameter parameter;
	private CreateOdysseusJar createOdysseusJar;

	public JavaFileWrite(String fileName, TransformationParameter parameter){
		this.fileName = fileName;
		this.path = parameter.getTempDirectory();
		this.mainStructure = new JavaMainstructure();
		this.parameter = parameter;
		this.createOdysseusJar = new CreateOdysseusJar();
	}
	
	public void createFile() throws IOException{
		StringBuilder absolutePath = new StringBuilder();
		absolutePath.append(path);
		absolutePath.append("\\src\\main\\");
		absolutePath.append(fileName);
		
		UnZip unZip = new UnZip();
    	unZip.unZipIt(parameter.getOdysseusPath()+"\\incubation\\monolithic\\query.transformation.java\\templates\\"+"JavaProject.zip",parameter.getTempDirectory());
		

		file = new File(absolutePath.toString());
		 // creates the file
		// creates a FileWriter Object
		file.createNewFile();
		writer = new FileWriter(file); 
		
		//create lib directory
		File dir = new File(path+"\\lib");
		dir.mkdir();
		
		if(parameter.isGenerateOdysseusJar()){
			createOdysseusJar.createOdysseusJar(parameter);
		}
		
		//generate build.xml
		GenerateAntBuildFile buildFileGenerator = new GenerateAntBuildFile();
	
		File buildFile = new File(path+"\\build.xml");
		buildFile.createNewFile();
		
		FileWriter buildWriter = new FileWriter(buildFile); 
		buildWriter.write(buildFileGenerator.getCodeForAntBuild(parameter));
		buildWriter.flush();
		buildWriter.close();
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
	
	public void writePackage() throws IOException{
		 writer.write(mainStructure.getPackage()); 
	}
	
	
	
	

}
