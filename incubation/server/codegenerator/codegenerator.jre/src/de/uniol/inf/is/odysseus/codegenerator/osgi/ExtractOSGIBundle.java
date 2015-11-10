package de.uniol.inf.is.odysseus.codegenerator.osgi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.codegenerator.utils.ExecuteShellComand;
import de.uniol.inf.is.odysseus.codegenerator.utils.Utils;
import de.uniol.inf.is.odysseus.core.Activator;

/**
 * This class extract all needed OSGi-Bundles form the odysseus code path.
 * This extract only works if odysseus starts from eclipse!
 * 
 * @author MarcPreuschaft
 *
 */
public class ExtractOSGIBundle {
	
	private static Logger LOG = LoggerFactory.getLogger(ExtractOSGIBundle.class);
	
	private boolean EXTRACT_SOURCE = false;
	
	private Map<String,String> requiredBundleByBundle = new HashMap<String,String>();
	private Map<String,String> bundlesToCopy = new HashMap<String, String>();
	private List<String> onlyJarFilesToCopy = new ArrayList<String>();
	
	public ExtractOSGIBundle(){
	
	}
	
	public ExtractOSGIBundle(boolean extractSource){
		EXTRACT_SOURCE  = extractSource;
	}

	
	public Set<String> extractOSGIBundle(Set<String> importList , String targetDirectory, String copyToDirectory){
		
		//resolve import 
		resolveBundles(importList);
	
		//resolve imports required by bundles
		resolveRequiredBundlesByBundle();
		
		return copyBundles(targetDirectory, copyToDirectory);	
	}
	
	
	/**
	 * find the bundle from a given class
	 * resolved bundles (complex) added to bundlesToCopy list
	 * resolved bundles (only jar files) added to onlyJarFilesToCopy list
	 * 
	 * @param importList
	 */
	private void resolveBundles(Set<String> importList){
		//get needed bundle
				for(String neededClass : importList){
					String path;
					try {
						//resolve the bundle
						Bundle neededBundel = org.osgi.framework.FrameworkUtil.getBundle(Class.forName(neededClass));
					
						//if bundle not null
						if(neededBundel != null){			
							//check if the detected bundle needs a different bundle
							List<RequireBundleModel> requiredBundeList = getRequireBundleList(neededBundel.getHeaders().get("Require-Bundle"));
						
							//add all bundles to requiredBundleByBundle list
							for(RequireBundleModel  bundle: requiredBundeList){
								requiredBundleByBundle.put(bundle.getName(), bundle.getVersion());
							}
							
							//get bundle classPath to find *.jar files
							String bundleClassPathValue = neededBundel.getHeaders().get("Bundle-ClassPath");
							
							//only a jar file
							if(bundleClassPathValue != null && bundleClassPathValue.contains(".jar")){
								
								List<String> jarList = Lists.newArrayList(Splitter.on(",").split(neededBundel.getHeaders().get("Bundle-ClassPath")));
								
								//resolve path to jar file
								String pathNeu = neededBundel.getLocation().replaceFirst("reference:file:/", "");
								
								//add all jar files to the onlyJarFilesToCopy list
								for(String jarFile :jarList){
									if(jarFile.contains(".jar")){
										onlyJarFilesToCopy.add(pathNeu+jarFile);
									}
								
								}
							}
							
							//search for the .project file 
							URL projectUrlFile = neededBundel.getEntry(".project");
							//get the path to the .project file
							path = FileLocator.toFileURL(projectUrlFile).getPath();
							
							//add bundle to copy list
							bundlesToCopy.put(path.replaceFirst("/", "").replace(".project",""), neededBundel.getSymbolicName());
							
							//write log info 
							LOG.info("Bundle: " +neededBundel.getSymbolicName() +" found, in path "+path.replaceFirst("/", "").replace(".project",""));
							
						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

	
	/**
	 * resolve the required bundles from a bundle
	 * 
	 */
	public void resolveRequiredBundlesByBundle(){
		for (Map.Entry<String, String> bundleString : requiredBundleByBundle.entrySet())
		{
			//iterate over all active bundles 
			for(Bundle entry : Activator.getBundleContext().getBundles()){
				
				if(bundleString.getKey().equals(entry.getSymbolicName())){
					//get bundle classPath
					String bundleClassPathValue =entry.getHeaders().get("Bundle-ClassPath");
					
					//if the classPath contains .jar 
					if(bundleClassPathValue != null && bundleClassPathValue.contains(".jar")){
						
						//get all jar files
						List<String> jarList = Lists.newArrayList(Splitter.on(",").split(entry.getHeaders().get("Bundle-ClassPath")));
						
						String path = entry.getLocation().replaceFirst("reference:file:/", "");
						
						//add all jar files to the onlyJarFilesToCopy list
						for(String jarFile :jarList){
							if(jarFile.contains(".jar")){
								onlyJarFilesToCopy.add(path+jarFile);
							}
						
						}
					
					}else{
						//complex bundle
						String path = entry.getLocation();
						bundlesToCopy.put(path.replaceFirst("reference:file:/", ""), entry.getSymbolicName());
					}
					
			
				}
				
			}
			
		}
	}
	
	/**
	 * copy the detected OSGi-Bundles to the targetDirectory
	 * 
	 * @param targetDirectory
	 * @param copyToDirectory
	 * @return
	 */
	private  Set<String> copyBundles(String targetDirectory,String copyToDirectory){
		Set<String> copiedJars = new HashSet<String>();
		
		
		//copy complex bundles
		for (Map.Entry<String, String> entry : bundlesToCopy.entrySet())
		{
		    String path = entry.getKey().replace("/", File.separator);

		    String name = entry.getValue();
	
		    String[] commands = new String[3];  
		    File filePath = new File(path+File.separator+"src");
		    if(filePath.exists() && filePath.isDirectory()) { 
		    	
		    
		    switch (Utils.getOS()) {
		    	//copy command for windows
	            case WINDOWS:
	        	    String[] volume = path.split(":"); 
	    		  
	      		    commands[0] = "cmd";
	      		    commands[1] = "/c";
	                  /* Command to execute */
	      		    
	      		    if(EXTRACT_SOURCE){
	      		    	commands[2] = volume[0]+": && cd "+path+" && xcopy "+path+"src\\* "+path+"bin /s /e /c /y && jar -cvf \""+targetDirectory+"/"+copyToDirectory +"/"+name+".jar\" *.properties -C bin .";
	      		    }else{ 
	      			    commands[2] = volume[0]+": && cd "+path+" && jar -cvf \""+targetDirectory+"/"+copyToDirectory +"/"+name+".jar\" *.properties -C bin .";
	      		    }
	      		    
	                break;
	            //copy command for linux
	            case LINUX:
	          	
	      		    commands[0] = "sh";
	      		    commands[1] = "-c";
	                  /* Command to execute */
	      		    
	      		    if(EXTRACT_SOURCE){
	      		    	commands[2] = "cd "+path+" && cp -r "+path+"src/* "+path+"bin && jar cvf "+targetDirectory+"/"+copyToDirectory +"/"+name+".jar *.properties  -C bin .";
	      		    }else{ 
	      			    commands[2] = "cd "+path+" && jar -cvf "+targetDirectory+"/"+copyToDirectory +"/"+name+".jar *.properties -C bin .";
	      		    }
	            	break;
	            	
	             //copy command for mac
	            case MAC:
	         
	      		    commands[0] = "sh";
	      		    commands[1] = "-c";
	                  /* Command to execute */
	      		    
	      		    if(EXTRACT_SOURCE){
	      		    	commands[2] = "cd "+path+" && cp -r "+path+"src/* "+path+"bin && jar cvf "+targetDirectory+"/"+copyToDirectory +"/"+name+".jar *.properties -C bin .";
	      		    }else{ 
	      			    commands[2] = "cd "+path+" && jar cvf "+targetDirectory+"/"+copyToDirectory +"/"+name+".jar *.properties -C bin .";
	      		    }
	            	break;
	            default:
	            	break;
	         	
		    }
		    
		    //execute the command
		    ExecuteShellComand.excecuteCommand(commands,false);
            
		    //add jar file to the copied jar list
            copiedJars.add(name+".jar");
		    }
		}
		
		//copy jar files
		for(String filePath : onlyJarFilesToCopy){
			
			String fileName = new File(filePath).getName();
			String path = filePath.replace(fileName, "");
			String[] volume = filePath.split(":"); 
			
		    String[] commands = new String[3];
		
		    switch (Utils.getOS()) {
			//copy command for windows
            case WINDOWS:
                commands[0] = "cmd";
    		    commands[1] = "/c";
    		    
    		    commands[2] = volume[0]+": && cd "+path+" && copy \""+filePath+"\" \""+targetDirectory+"/"+copyToDirectory+"/"+fileName+"\""; 
        	
                break;
            //copy command for linux
            case LINUX:
                commands[0] = "sh";
    		    commands[1] = "-c";
    		    
    		    commands[2] = volume[0]+": && cd "+path+" && cp \""+filePath+"\" \""+targetDirectory+"/"+copyToDirectory+"/"+fileName+"\""; 
      		
            	
            	break;
            //copy command for mac
            case MAC:
                commands[0] = "sh";
    		    commands[1] = "-c";
    		    
    		    commands[2] = volume[0]+": && cd "+path+" && cp \""+filePath+"\" \""+targetDirectory+"/"+copyToDirectory+"/"+fileName+"\""; 
      		 
            	break;
            default:
            	break;
         	
	    }
		   //execute copy command
		    ExecuteShellComand.excecuteCommand(commands, false);
		    
		    //add jars to copied jar list
		    copiedJars.add(fileName);
		}
	
		return copiedJars;
	}
	
	/**
	 * extract important bundle infos from the requiredBundleString
	 * then create a new RequireBundleModel Object. 
	 * The resolveRequiredBundlesByBundle() function use this information
	 * to copy the bundles.
	 * 
	 * @param requiredBundleString
	 * @return
	 */
	private List<RequireBundleModel> getRequireBundleList(String requiredBundleString){
		List<RequireBundleModel> requireBundleList = new ArrayList<RequireBundleModel>();
		
		if(requiredBundleString != null){
			//splitt the string by ,
			String[] temp = requiredBundleString.split(",");
			
			for(String bundleString : temp){
				
				//if the string contains ; -> special version needed
				if(bundleString.contains(";")){
					String[] bundleInfo = bundleString.split(";");
					
					if(bundleInfo.length == 2){
						requireBundleList.add(new RequireBundleModel(bundleInfo[0],bundleInfo[1].replace("bundle-version=", "").replace("\"", "")));
					}
				
				}else{
					requireBundleList.add(new RequireBundleModel(bundleString, null));
				}
			}
		}
		return requireBundleList;
	}
}
