package de.uniol.inf.is.odysseus.codegenerator.jre.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.codegenerator.jre.Activator;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.osgi.ExtractOSGIBundle;
import de.uniol.inf.is.odysseus.codegenerator.scheduler.ICScheduler;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.codegenerator.utils.FileHelper;
import de.uniol.inf.is.odysseus.codegenerator.utils.UnZip;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class JavaFileWrite {
	
	
	private String fileName;
	private File file;
	private FileWriter writer;
	private String targetDirectory;
	private TransformationParameter transformationParameter;
	private Set<String> frameworkImportList = new HashSet<String>();
	private Set<String> importList = new HashSet<String>();
	
	private Set<String> copyJars = new HashSet<String>();
	private String osgiBindCode;
	private String bodyCode;
	private String startCode;
	private ICScheduler scheduler;
	
	private Map<ILogicalOperator,Map<String,String>> operatorConfigurationList;
	
	private static Logger LOG = LoggerFactory.getLogger(JavaFileWrite.class);
	
	
	public JavaFileWrite(String fileName, TransformationParameter transformationParameter, Set<String> frameworkImportList,Set<String> importList, String osgiBindCode ,String bodyCode,String startCode,  Map<ILogicalOperator,Map<String,String>> operatorConfigurationList, ICScheduler scheduler){
		this.fileName = fileName;
		this.targetDirectory = transformationParameter.getTargetDirectory();
		this.transformationParameter = transformationParameter;
		this.frameworkImportList = frameworkImportList;
		this.importList = importList;
		this.osgiBindCode = osgiBindCode;
		this.bodyCode = bodyCode;
		this.startCode = startCode;
		this.operatorConfigurationList = operatorConfigurationList;
		this.scheduler = scheduler;
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
		
		//create scheduler file
		createSchedulerFile();
		
		//create operatorconfiguration files
		createOperationConfigurationFiles();
		
		//create build.xml file
		createBuildScript();
		
		//create .classpath file
		createClassFile();
	}
	

	private void createSchedulerFile() {
	
		FileHelper fileHelper = new FileHelper(scheduler.getName()+".java", targetDirectory+"/src/main");
		scheduler.setPackageName("main");
		fileHelper.writeToFile(scheduler.getSchedulerCode());
		
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
			unZip.unZipIt(file.getAbsolutePath(),targetDirectory);
		}else{
			LOG.error("Project file not found!");
		}
    	
	}
	
	private void createMainJavaFile(){
		StringBuilder absolutePath = new StringBuilder();
		absolutePath.append(targetDirectory);
		absolutePath.append("/src/main/");
		absolutePath.append(fileName);
		
		file = new File(absolutePath.toString());
		 // creates the file
		try {
			file.createNewFile();
			writer = new FileWriter(file); 
			
			//sort importList for nice look 
			TreeSet<String> sortList = new TreeSet<String>( Collections.reverseOrder() );
			sortList.addAll(frameworkImportList);
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
		absolutePath.append(targetDirectory);
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

		File buildFile = new File(targetDirectory+"/build.xml");
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
		File classpathFile = new File(targetDirectory+"/.classpath");
		
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
		copyJars = extractOSGIBundle.extractOSGIBundle(frameworkImportList, transformationParameter.getTargetDirectory(), "lib");
	}
	
	
	private void createOperationConfigurationFiles() {
		FileWriter infoFile = null;
		
		StringBuilder infoFileText = new StringBuilder();

		try {
			 infoFile = new FileWriter(targetDirectory+"/operatorConfigurationInfo.txt");
		
			for (Entry<ILogicalOperator, Map<String, String>> entry : operatorConfigurationList.entrySet())
			{
			    String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(entry.getKey());
			 
			    infoFileText.append(entry.getKey().getName()+" --> "+ operatorVariable +"PO.json\n");
			    
			    Gson gson = new Gson();
			    String json = gson.toJson(entry.getValue());
			   
				FileWriter file = new FileWriter(targetDirectory+"/"+operatorVariable+"PO.json");
				file.write(json);
				file.flush();
				file.close();
			}
			
				infoFile.write(infoFileText.toString());
				infoFile.flush();
				infoFile.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	

}
