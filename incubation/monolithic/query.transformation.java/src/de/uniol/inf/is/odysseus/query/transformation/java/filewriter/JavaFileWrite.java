package de.uniol.inf.is.odysseus.query.transformation.java.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.mainstructure.JavaMainstructure;
import de.uniol.inf.is.odysseus.query.transformation.java.shell.commands.ExecuteShellComand;
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
	private Set<String> importList = new HashSet<String>();
	private List<String> copyJars = new ArrayList<String>();

	public JavaFileWrite(String fileName, TransformationParameter parameter, Set<String> importList){
		this.fileName = fileName;
		this.path = parameter.getTempDirectory();
		this.mainStructure = new JavaMainstructure();
		this.parameter = parameter;
		this.createOdysseusJar = new CreateOdysseusJar();
		this.importList = importList;
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
		
		
		/* 
		 * old
		 * 
		if(parameter.isGenerateOdysseusJar()){
			createOdysseusJar.createOdysseusJar(parameter);
		}
		*/
		
		//generate build.xml
		GenerateAntBuildFile buildFileGenerator = new GenerateAntBuildFile();
	
		File buildFile = new File(path+"\\build.xml");
		buildFile.createNewFile();
		
		FileWriter buildWriter = new FileWriter(buildFile); 
		buildWriter.write(buildFileGenerator.getCodeForAntBuild(parameter));
		buildWriter.flush();
		buildWriter.close();
		
		
		generateJarExport(importList,parameter.getTempDirectory());
	
		File projectFile = new File(path+"\\.classpath");
		projectFile.createNewFile();
		
		FileWriter buildProjectWriter = new FileWriter(projectFile); 
		buildProjectWriter.write(getCodeForProjectFile());
		buildProjectWriter.flush();
		buildProjectWriter.close();
		
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
	
	
	private String getCodeForProjectFile(){
		StringBuilder code = new StringBuilder();
		
		
		code.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		code.append("\n");
		code.append("<classpath>");
		code.append("\n");
		code.append("<classpathentry kind=\"src\" path=\"src\"/>");
		code.append("\n");
		code.append("<classpathentry exported=\"true\" kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.7\"/>");
		code.append("\n");
		code.append("<classpathentry kind=\"lib\" path=\"lib\"/>");
		code.append("\n");
		
		code.append("<classpathentry kind=\"lib\" path=\"lib/slf4j-log4j12-1.6.4.jar\"/>");
		code.append("\n");
		code.append("<classpathentry kind=\"lib\" path=\"lib/slf4j-api-1.6.4.jar\"/>");
		code.append("\n");
		code.append("<classpathentry kind=\"lib\" path=\"lib/log4j-1.2.16.jar\"/>");
		code.append("\n");
		
		
		for(String imporJar : copyJars){
			code.append("<classpathentry kind=\"lib\" path=\"lib/"+imporJar+"\"/>");
			code.append("\n");
		}
		
	
		code.append("classpathentry kind=\"output\" path=\"bin\"/>");
		code.append("\n");
		code.append("</classpath>");
		code.append("\n");

		
		return code.toString();
	}
	
	
	private  void generateJarExport(Set<String> importList ,String tempPath){
		Map<String,String> bundles = new HashMap<String, String>();
		//get needed bundle
		for(String neededClass : importList){
			String path;
			try {
				Bundle neededBundel = org.osgi.framework.FrameworkUtil.getBundle(Class.forName(neededClass));
				if(neededBundel != null){				
					URL entryss = neededBundel.getEntry(".project");
					
					//neededBundel.getHeaders().get("Require-Bundle");
					System.out.println("Bundle:" +neededBundel.getSymbolicName());
				
					path = FileLocator.toFileURL(entryss).getPath();
					bundles.put(path.replaceFirst("/", "").replace(".project",""), neededBundel.getSymbolicName());
					
					System.out.println("Bundle-Path:"+path.replaceFirst("/", ""));
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
		}
		
		System.out.println("");
		for (Map.Entry<String, String> entry : bundles.entrySet())
		{

		    String path = entry.getKey().replace("/", "\\");
		    String name = entry.getValue().replace(".", "");
		    String[] volume = path.split(":"); 
		    
		    System.out.println(volume[0]+":");
		    System.out.println("cd "+path);
		    System.out.println("xcopy "+path+"src\\* "+path+"bin /s /e /c /y");
		    System.out.println("jar cvf "+name+".jar *.properties lib/*.jar -C bin .");
		        
		    String[] commands = new String[3];
		    commands[0] = "cmd";
		    commands[1] = "/c";
            /* Command to execute */
		    commands[2] = volume[0]+": && cd "+path+" && xcopy "+path+"src\\* "+path+"bin /s /e /c /y && jar cvf "+name+".jar *.properties *.jar -C bin . && cd "+path+" && xcopy "+name+".jar "+tempPath+"\\lib /Y";
		    
            ExecuteShellComand.excecuteCommand(commands);
            
            copyJars.add(name+".jar");
		}
	
	}
	

}
