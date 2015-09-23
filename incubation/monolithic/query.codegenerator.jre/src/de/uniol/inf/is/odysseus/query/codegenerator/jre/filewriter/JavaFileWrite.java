package de.uniol.inf.is.odysseus.query.codegenerator.jre.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.codegenerator.executor.ICExecutor;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.Activator;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.osgi.ExtractOSGIBundle;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.FileHelper;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.UnZip;

public class JavaFileWrite {
	
	
	private String fileName;
	private File file;
	private FileWriter writer;
	private String tempPath;
	private TransformationParameter transformationParameter;
	private Set<String> importList = new HashSet<String>();
	private Set<String> copyJars = new HashSet<String>();
	private String osgiBindCode;
	private String bodyCode;
	private String startCode;
	private ICExecutor executor;
	
	private Map<ILogicalOperator,Map<String,String>> operatorConfigurationList;
	
	private static Logger LOG = LoggerFactory.getLogger(JavaFileWrite.class);
	
	
	public JavaFileWrite(String fileName, TransformationParameter transformationParameter, Set<String> importList, String osgiBindCode ,String bodyCode,String startCode,  Map<ILogicalOperator,Map<String,String>> operatorConfigurationList, ICExecutor executor){
		this.fileName = fileName;
		this.tempPath = transformationParameter.getTempDirectory();
		this.transformationParameter = transformationParameter;
		this.importList = importList;
		this.osgiBindCode = osgiBindCode;
		this.bodyCode = bodyCode;
		this.startCode = startCode;
		this.operatorConfigurationList = operatorConfigurationList;
		this.executor = executor;
	}

	public void createProject() throws IOException{
		//unzip project template
		unzipProjectTemplate();
		
		//create and copy odysseus jars
		copyOdysseusJar();
		
		//create main.java file
		createMainJavaFile();
		
		//create Utils.java file
		createUtilsJavaFile();
		
		//create executor file
		createExecutorFile();
		
		//create operatorconfiguration files
		createOperationConfigurationFiles();
		
		//create build.xml file
		createBuildScript();
		
		//create .classpath file
		createClassFile();
	}
	

	private void createExecutorFile() {
	
		FileHelper fileHelper = new FileHelper(executor.getName()+".java", tempPath+"/src/main");
		fileHelper.writeToFile(executor.getExecutorCode());
		
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
		absolutePath.append("/src/main/");
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
	
	
	private void createUtilsJavaFile(){
		StringBuilder absolutePath = new StringBuilder();
		absolutePath.append(tempPath);
		absolutePath.append("/src/main/");
		absolutePath.append("Utils.java");
		
		file = new File(absolutePath.toString());
		 // creates the file
		try {
			file.createNewFile();
			writer = new FileWriter(file); 
			
			StringTemplate javaUtils = new StringTemplate("java","javaUtils");
			javaUtils.getSt().add("packageName", "main");
			writer.write(javaUtils.getSt().render()); 
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void createBuildScript(){

		File buildFile = new File(tempPath+"/build.xml");
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
		File classpathFile = new File(tempPath+"/.classpath");
		
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
		ExtractOSGIBundle extractOSGIBundle =  new ExtractOSGIBundle();
		copyJars = extractOSGIBundle.extractOSGIBundle(importList, transformationParameter.getTempDirectory(), "lib");
	}
	
	
	private void createOperationConfigurationFiles() {
		FileWriter infoFile = null;
		
		try {
			 infoFile = new FileWriter(tempPath+"/operatorConfigurationInfo.txt");
		
			for (Entry<ILogicalOperator, Map<String, String>> entry : operatorConfigurationList.entrySet())
			{
			    String operatorVariable = JreCodegeneratorStatus.getInstance().getVariable(entry.getKey());
			  
			    
			    Gson gson = new Gson();
			    String json = gson.toJson(entry.getValue());
			    
				infoFile.write(entry.getKey().getName()+" --> "+ operatorVariable +"\n");
					
				FileWriter file = new FileWriter(tempPath+"/"+operatorVariable+"PO.json");
				file.write(json);
				file.flush();
				file.close();
			}
			
				infoFile.flush();
				infoFile.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	

}
