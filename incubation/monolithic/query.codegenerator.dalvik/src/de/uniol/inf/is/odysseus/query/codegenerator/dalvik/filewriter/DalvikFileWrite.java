package de.uniol.inf.is.odysseus.query.codegenerator.dalvik.filewriter;

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
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.codegenerator.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.dalvik.Activator;
import de.uniol.inf.is.odysseus.query.codegenerator.executor.ICExecutor;
import de.uniol.inf.is.odysseus.query.codegenerator.dalvik.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.osgi.ExtractOSGIBundle;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.FileHelper;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.UnZip;


//TODO add mac support
public class DalvikFileWrite {
	
	
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
	private ICExecutor executor;
	
	private Map<ILogicalOperator,Map<String,String>> operatorConfigurationList;
	
	private static Logger LOG = LoggerFactory.getLogger(DalvikFileWrite.class);
	
	
	public DalvikFileWrite(String fileName, TransformationParameter transformationParameter, Set<String> importList, String osgiBindCode ,String bodyCode,String startCode,  Map<ILogicalOperator,Map<String,String>> operatorConfigurationList, ICExecutor executor){
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
	
		createMainActivityFragmentFile();
		
		//create Utils.java file
		createUtilsJavaFile();
		
		//create executor file
		createExecutorFile();
		
		//create operatorconfiguration files
		createOperationConfigurationFiles();
	}
	

	private void createExecutorFile() {
	
		FileHelper fileHelper = new FileHelper(executor.getName()+".java", tempPath+"\\app\\src\\main\\java\\com\\app\\odysseus\\odysseustest");
		fileHelper.writeToFile(executor.getExecutorCode());
		
	}

	private void unzipProjectTemplate(){
		UnZip unZip = new UnZip();
		
		Bundle bundle = Activator.getContext().getBundle();
	
		
		URL fileURL = bundle.getEntry("templates/dalvik/AndroidProject.zip");
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
	
	private void createMainActivityFragmentFile(){
		StringBuilder absolutePath = new StringBuilder();
		absolutePath.append(tempPath);
		absolutePath.append("\\app\\src\\main\\java\\com\\app\\odysseus\\odysseustest\\");
		absolutePath.append(fileName);
		
		file = new File(absolutePath.toString());
		 // creates the file
		try {
			file.createNewFile();
			writer = new FileWriter(file); 
			
			//sort importList for nice look 
			TreeSet<String> sortList = new TreeSet<String>( Collections.reverseOrder() );
			sortList.addAll(importList);
			
			
			StringTemplate dalviMainActivityFragment = new StringTemplate("dalvik","dalvikMainActivityFragment");
			dalviMainActivityFragment.getSt().add("importList", sortList);
			dalviMainActivityFragment.getSt().add("osgiBindCode", osgiBindCode);
			dalviMainActivityFragment.getSt().add("bodyCode", bodyCode);
			dalviMainActivityFragment.getSt().add("startCode", startCode);
			
			writer.write(dalviMainActivityFragment.getSt().render()); 
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	private void createUtilsJavaFile(){
		StringBuilder absolutePath = new StringBuilder();
		absolutePath.append(tempPath);
		absolutePath.append("\\app\\src\\main\\java\\com\\app\\odysseus\\odysseustest\\");
		absolutePath.append("Utils.java");
		
		file = new File(absolutePath.toString());
		 // creates the file
		try {
			file.createNewFile();
			writer = new FileWriter(file); 
			
			StringTemplate dalvikUtils = new StringTemplate("dalvik","dalvikUtils");
			dalvikUtils.getSt().add("packageName", "com.app.odysseus.odysseustest");
			writer.write(dalvikUtils.getSt().render()); 
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void copyOdysseusJar(){
		copyJars = ExtractOSGIBundle.extractOSGIBundle(importList, transformationParameter.getTempDirectory(), "app\\libs");
	}
	
	
	private void createOperationConfigurationFiles() {

	}
	
	
	

}
