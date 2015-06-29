package de.uniol.inf.is.odysseus.query.transformation.java.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {

	private File file ;
	private FileWriter writer;
	
	private String absolutePath;
	
	public FileHelper(String fileName, String path){
		StringBuilder tempBuild = new StringBuilder();
		tempBuild.append(path);
		tempBuild.append("\\");
		tempBuild.append(fileName);
		
		this.absolutePath = tempBuild.toString();
		try {
			createFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createFile() throws IOException{
	
		
		file = new File(absolutePath);
		file.createNewFile();
		writer = new FileWriter(file); 
		
	    writer.flush();
	    writer.close();
	}
	
	public void writeToFile(String input){

		FileWriter fw;
		try {
			fw = new FileWriter(absolutePath,true);
			fw.write(input);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
