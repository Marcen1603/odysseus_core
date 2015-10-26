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
	
		resolveBundles(importList);
	
		resolveRequiredBundlesByBundle();
		
		return copyBundles( targetDirectory, copyToDirectory);	
	}
	
	

	private List<RequireBundleModel> getRequireBundleList(String requiredBundleString){
		List<RequireBundleModel> requireBundleList = new ArrayList<RequireBundleModel>();
		
		if(requiredBundleString != null){
			String[] temp = requiredBundleString.split(",");
			
			for(String bundleString : temp){
				
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
	

	
	private void resolveBundles(Set<String> importList){
		//get needed bundle
				for(String neededClass : importList){
					String path;
					try {
						Bundle neededBundel = org.osgi.framework.FrameworkUtil.getBundle(Class.forName(neededClass));
					
						if(neededBundel != null){				
							URL entryss = neededBundel.getEntry(".project");
												
							List<RequireBundleModel> requiredBundeList = getRequireBundleList(neededBundel.getHeaders().get("Require-Bundle"));
						
							for(RequireBundleModel  bundle: requiredBundeList){
								requiredBundleByBundle.put(bundle.getName(), bundle.getVersion());
							}
							
						
							String bundleClassPathValue = neededBundel.getHeaders().get("Bundle-ClassPath");
							
							if(bundleClassPathValue != null && bundleClassPathValue.contains(".jar")){
								//only a jar file
								List<String> jarList = Lists.newArrayList(Splitter.on(",").split(neededBundel.getHeaders().get("Bundle-ClassPath")));
								
								String pathNeu = neededBundel.getLocation().replaceFirst("reference:file:/", "");
								
								for(String jarFile :jarList){
									if(jarFile.contains(".jar")){
										onlyJarFilesToCopy.add(pathNeu+jarFile);
									}
								
								}
							}
							

							path = FileLocator.toFileURL(entryss).getPath();
							bundlesToCopy.put(path.replaceFirst("/", "").replace(".project",""), neededBundel.getSymbolicName());
							
							LOG.info("Bundle: " +neededBundel.getSymbolicName() +" found, in path "+path.replaceFirst("/", "").replace(".project",""));
							
						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

	
	public void resolveRequiredBundlesByBundle(){
		for (Map.Entry<String, String> bundleString : requiredBundleByBundle.entrySet())
		{

			for(Bundle entry : Activator.getBundleContext().getBundles()){
				if(bundleString.getKey().equals(entry.getSymbolicName())){
					String bundleClassPathValue =entry.getHeaders().get("Bundle-ClassPath");
					
					if(bundleClassPathValue != null && bundleClassPathValue.contains(".jar")){
						//only a jar file
	
						List<String> jarList = Lists.newArrayList(Splitter.on(",").split(entry.getHeaders().get("Bundle-ClassPath")));
						
		
						String path = entry.getLocation().replaceFirst("reference:file:/", "");
						
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
	
	
	private  Set<String> copyBundles(String targetDirectory,String copyToDirectory){
		Set<String> copiedJars = new HashSet<String>();
		
		
		for (Map.Entry<String, String> entry : bundlesToCopy.entrySet())
		{
		    String path = entry.getKey().replace("/", File.separator);

		    String name = entry.getValue();
	
		    String[] commands = new String[3];  
		    File filePath = new File(path+File.separator+"src");
		    if(filePath.exists() && filePath.isDirectory()) { 
		    	
		    
		    switch (Utils.getOS()) {
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
		    
		    
		    ExecuteShellComand.excecuteCommand(commands,false);
            
            copiedJars.add(name+".jar");
		    }
		}
		
		
		for(String filePath : onlyJarFilesToCopy){
			
			String fileName = new File(filePath).getName();
			String path = filePath.replace(fileName, "");
			String[] volume = filePath.split(":"); 
			
		    String[] commands = new String[3];
		
		    switch (Utils.getOS()) {
            case WINDOWS:
                commands[0] = "cmd";
    		    commands[1] = "/c";
    		    
    		    commands[2] = volume[0]+": && cd "+path+" && copy \""+filePath+"\" \""+targetDirectory+"/"+copyToDirectory+"/"+fileName+"\""; 
        	
                break;
            case LINUX:
                commands[0] = "sh";
    		    commands[1] = "-c";
    		    
    		    commands[2] = volume[0]+": && cd "+path+" && cp \""+filePath+"\" \""+targetDirectory+"/"+copyToDirectory+"/"+fileName+"\""; 
      		
            	
            	break;
            case MAC:
                commands[0] = "sh";
    		    commands[1] = "-c";
    		    
    		    commands[2] = volume[0]+": && cd "+path+" && cp \""+filePath+"\" \""+targetDirectory+"/"+copyToDirectory+"/"+fileName+"\""; 
      		 
            	break;
            default:
            	break;
         	
	    }
		   
		    ExecuteShellComand.excecuteCommand(commands, false);
		    
		    copiedJars.add(fileName);
   
		}
	
		return copiedJars;
	}
}
