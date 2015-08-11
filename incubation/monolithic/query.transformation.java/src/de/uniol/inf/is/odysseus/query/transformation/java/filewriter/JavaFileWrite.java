package de.uniol.inf.is.odysseus.query.transformation.java.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.Activator;
import de.uniol.inf.is.odysseus.query.transformation.java.shell.commands.ExecuteShellComand;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.UnZip;


//TODO add mac support
public class JavaFileWrite {
	
	
	private String fileName;
	private File file;
	private FileWriter writer;
	private String tempPath;
	private TransformationParameter transformationParameter;
	private Set<String> importList = new HashSet<String>();
	private List<String> copyJars = new ArrayList<String>();
	private String osgiBindCode;
	private String bodyCode;
	private String startCode;
	
	private static Logger LOG = LoggerFactory.getLogger(JavaFileWrite.class);
	
	
	public JavaFileWrite(String fileName, TransformationParameter transformationParameter, Set<String> importList, String osgiBindCode ,String bodyCode,String startCode){
		this.fileName = fileName;
		this.tempPath = transformationParameter.getTempDirectory();
		this.transformationParameter = transformationParameter;
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
		
		Bundle bundle = Activator.getContext().getBundle();
	
		
		URL fileURL = bundle.getEntry("templates/java/JavaProject.zip");
		File file = null;
		try {
		    file = new File(FileLocator.resolve(fileURL).toURI());
		}catch (IOException | URISyntaxException e1) {
		    e1.printStackTrace();
		}
		
		if(file != null){
			unZip.unZipIt(file.getAbsolutePath(),tempPath);
		}else{
			LOG.error("Project file not found!");
		}
    	
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
			
			//sort importList for nice look 
			TreeSet<String> sortList = new TreeSet<String>( Collections.reverseOrder() );
			sortList.addAll(importList);
			
			StringTemplate javaMain = new StringTemplate("java","javaMain");
			javaMain.getSt().add("importList", sortList);
			javaMain.getSt().add("osgiBindCode", osgiBindCode);
			javaMain.getSt().add("bodyCode", bodyCode);
			javaMain.getSt().add("startCode", startCode);
			
			writer.write(javaMain.getSt().render()); 
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	private void createBuildScript(){

		File buildFile = new File(tempPath+"\\build.xml");
		try {
			buildFile.createNewFile();
			FileWriter buildWriter = new FileWriter(buildFile); 
			
			StringTemplate javaAntBuildTemplate = new StringTemplate("java","javaAntBuild");
			javaAntBuildTemplate.getSt().add("copyJars", copyJars);
			
			buildWriter.write(javaAntBuildTemplate.getSt().render());
			buildWriter.flush();
			buildWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}
	
	private void createClassFile(){
		File classpathFile = new File(tempPath+"\\.classpath");
		
		try {
			classpathFile.createNewFile();
			FileWriter buildProjectWriter = new FileWriter(classpathFile); 
			
			StringTemplate javaJarList = new StringTemplate("java","javaClasspath");
			javaJarList.getSt().add("jarList", copyJars);
	
			buildProjectWriter.write(javaJarList.getSt().render());
			
			buildProjectWriter.flush();
			buildProjectWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void copyOdysseusJar(){
		generateJarExport(importList,transformationParameter.getTempDirectory());
	}
	
	
	/*
	 * TODO only works with eclipse odysseus?
	 */
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
