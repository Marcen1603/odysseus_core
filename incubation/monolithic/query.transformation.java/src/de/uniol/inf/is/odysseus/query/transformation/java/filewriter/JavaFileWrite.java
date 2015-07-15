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
	private String tempPath;
	private JavaMainstructure mainStructure;
	TransformationParameter parameter;
	private Set<String> importList = new HashSet<String>();
	private List<String> copyJars = new ArrayList<String>();
	private String osgiBindCode ;
	private String bodyCode;
	private String startCode;
	
	
	public JavaFileWrite(String fileName, TransformationParameter parameter, Set<String> importList, String osgiBindCode ,String bodyCode,String startCode){
		this.fileName = fileName;
		this.tempPath = parameter.getTempDirectory();
		this.mainStructure = new JavaMainstructure();
		this.parameter = parameter;
		this.importList = importList;
		this.osgiBindCode = osgiBindCode;
		this.bodyCode = bodyCode;
		this.startCode = startCode;
		
	}

	public void createProject() throws IOException{
		//unzip project template
		unzipProjectTemplate();
		
		//create and copy odysseus jars
		copyOdysseusJar();
		
		//create main.java file
		createMainJavaFile();
		
		//create build.xml file
		createBuildScript();
		
		//create .classpath file
		createClassFile();
	}
	
	
	private void unzipProjectTemplate(){
		UnZip unZip = new UnZip();
    	unZip.unZipIt(parameter.getOdysseusPath()+"\\incubation\\monolithic\\query.transformation.java\\templates\\"+"JavaProject.zip",tempPath);
		
	}
	
	private void createMainJavaFile(){
		StringBuilder absolutePath = new StringBuilder();
		absolutePath.append(tempPath);
		absolutePath.append("\\src\\main\\");
		absolutePath.append(fileName);
		
		file = new File(absolutePath.toString());
		 // creates the file
		try {
			file.createNewFile();
			writer = new FileWriter(file); 
			
				writePackage();
				writeImports();
					writeClassTop();
					writeBody(osgiBindCode);
					writeBody(bodyCode);
					writeBody(startCode);
				writeBottom();
				
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	private void createBuildScript(){
		//generate build.xml
		GenerateAntBuildFile buildFileGenerator = new GenerateAntBuildFile();
	
		File buildFile = new File(tempPath+"\\build.xml");
		try {
			buildFile.createNewFile();
			FileWriter buildWriter = new FileWriter(buildFile); 
			buildWriter.write(buildFileGenerator.getCodeForAntBuild(parameter, copyJars  ));
			buildWriter.flush();
			buildWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}
	
	private void createClassFile(){
		File projectFile = new File(tempPath+"\\.classpath");
		
		try {
			projectFile.createNewFile();
			FileWriter buildProjectWriter = new FileWriter(projectFile); 
			buildProjectWriter.write(getCodeForClassPathFile());
			buildProjectWriter.flush();
			buildProjectWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void copyOdysseusJar(){
		generateJarExport(importList,parameter.getTempDirectory());
	}
	
	private void writeImports() throws IOException{
		//sort the imports for nice look
		TreeSet<String> sortList = new TreeSet<String>( Collections.reverseOrder() );
		sortList.addAll(importList);
	
		for(String imp : sortList){
			 writer.write("import "+imp+";\n"); 
		}
	}
	
	private void writeClassTop() throws IOException{
		 writer.write(mainStructure.getClassTop()); 
	}
	
	private void writeBody(String bodyText) throws IOException{
		 writer.write(bodyText); 
	}
	
	private void writeBottom() throws IOException{
		 writer.write(mainStructure.getClassBottom()); 
	}
	
	private void writePackage() throws IOException{
		 writer.write(mainStructure.getPackage()); 
	}
	
	
	private String getCodeForClassPathFile(){
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
		
	
		code.append("classpathentry kind=\"output\" path=\"build\"/>");
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
