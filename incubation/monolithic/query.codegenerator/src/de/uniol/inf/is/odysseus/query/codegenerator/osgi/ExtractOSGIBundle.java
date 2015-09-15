package de.uniol.inf.is.odysseus.query.codegenerator.osgi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.query.codegenerator.utils.ExecuteShellComand;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.Utils;


//TODO mac, unix support?
public class ExtractOSGIBundle {
	
	private static boolean DEBUG_MODUS = true;
	
	public static List<String> extractOSGIBundle(Set<String> importList ,String tempPath, String copyToFolder){
		
		List<String> copyJars = new ArrayList<String>();
		
		
		//Map<String,String> requiredBundleByBundle = new HashMap<String,String>();
		//List<String> onlyJarBundles = new ArrayList<String>();
		
		
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
				
					//List<RequireBundleModel> sss = getRequireBundleList(neededBundel.getHeaders().get("Require-Bundle"));
					
					/*
					for(RequireBundleModel  bundle: sss){
					
						requiredBundleByBundle.put(bundle.getName(), bundle.getVersion());
					}
					*/
	
					path = FileLocator.toFileURL(entryss).getPath();
		
					bundles.put(path.replaceFirst("/", "").replace(".project",""), neededBundel.getSymbolicName());
					
					System.out.println("Bundle-Path:"+path.replaceFirst("/", ""));
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		/*
		for (Map.Entry<String, String> bundleString : requiredBundleByBundle.entrySet())
		{
	
		    System.out.println(bundleString);
	
			for(Bundle entry : Activator.getBundleContext().getBundles()){
				//if(bundleString.getKey().equals(entry.getSymbolicName()) && bundleString.getValue().equals(entry.getVersion().toString())){
				if(bundleString.getKey().equals(entry.getSymbolicName())){
					String bundleClassPathValue =entry.getHeaders().get("Bundle-ClassPath");
					
					if(bundleClassPathValue != null && bundleClassPathValue.contains(".jar")){
						//only a jar file
						System.out.println("Only Jar copy");
						String path = entry.getLocation()+entry.getHeaders().get("Bundle-ClassPath");
						onlyJarBundles.add(path.replaceFirst("reference:file:/", ""));
						
					}else{
						//complex bundle
						String path = entry.getLocation();
						bundles.put(path.replaceFirst("reference:file:/", ""), entry.getSymbolicName());
					}
					
			
				}
				
			}
			
		}
		*/
	
	
		for (Map.Entry<String, String> entry : bundles.entrySet())
		{

			//TODO unix mac support...
		    String path = entry.getKey().replace("/", File.separator);
		 
		    String name = entry.getValue().replace(".", "");
	
		    String[] commands = new String[3];  
		
		    switch (Utils.getOS()) {
	            case WINDOWS:
	            	
	        	    String[] volume = path.split(":"); 
	    		    
	    		    System.out.println(volume[0]+":");
	    		    System.out.println("cd "+path);
	    		    System.out.println("xcopy "+path+"src\\* "+path+"bin /s /e /c /y");
	    		    System.out.println("jar cvf "+name+".jar *.properties lib/*.jar -C bin .");
	            	 
	      		    commands[0] = "cmd";
	      		    commands[1] = "/c";
	                  /* Command to execute */
	      		    
	      		    if(DEBUG_MODUS){
	      		    	commands[2] = volume[0]+": && cd "+path+" && xcopy "+path+"src\\* "+path+"bin /s /e /c /y && jar cvf "+tempPath+"\\"+copyToFolder +"\\"+name+".jar *.properties *.jar -C bin .";
	      		    }else{ 
	      			    commands[2] = volume[0]+": && cd "+path+" && jar cvf "+tempPath+"\\"+copyToFolder +"\\"+name+".jar *.properties *.jar -C bin .";
	      		    }
	      		    
	                break;
	            case LINUX:
	          	
	      		    commands[0] = "sh";
	      		    commands[1] = "-c";
	                  /* Command to execute */
	      		    
	      		    if(DEBUG_MODUS){
	      		    	commands[2] = "cd "+path+" && cp -r "+path+"src/* "+path+"bin && jar cvf "+tempPath+"/"+copyToFolder +"/"+name+".jar *.properties *.jar -C bin .";
	      		    }else{ 
	      			    commands[2] = "cd "+path+" && jar cvf "+tempPath+"/"+copyToFolder +"/"+name+".jar *.properties *.jar -C bin .";
	      		    }
	            	break;
	            case MAC:
	         
	      		    commands[0] = "sh";
	      		    commands[1] = "-c";
	                  /* Command to execute */
	      		    
	      		    if(DEBUG_MODUS){
	      		    	commands[2] = "cd "+path+" && cp -r "+path+"src/* "+path+"bin && jar cvf "+tempPath+"/"+copyToFolder +"/"+name+".jar *.properties *.jar -C bin .";
	      		    }else{ 
	      			    commands[2] = "cd "+path+" && jar cvf "+tempPath+"/"+copyToFolder +"/"+name+".jar *.properties *.jar -C bin .";
	      		    }
	            	break;
	            default:
	            	break;
	         	
		    }
		    
		    
		    ExecuteShellComand.excecuteCommand(commands);
            
            copyJars.add(name+".jar");
		}
		
		/*
		for(String filePath : onlyJarBundles){
			
			String fileName = new File(filePath).getName();
			String path = filePath.replace(fileName, "");
			String[] volume = filePath.split(":"); 
			
		    String[] commands = new String[3];
		    commands[0] = "cmd";
		    commands[1] = "/c";
		    
		    commands[2] = volume[0]+": && cd "+path+" && copy \""+filePath+"\" "+tempPath+"\\*"; 
		    ExecuteShellComand.excecuteCommand(commands);
		}
		
		*/

		
		return copyJars;
	}
	
	
	public static List<RequireBundleModel> getRequireBundleList(String requiredBundleString){
		List<RequireBundleModel> requireBundleList = new ArrayList<RequireBundleModel>();
		
		String[] temp = requiredBundleString.split(",");
		
		for(String bundleString : temp){
			String[] bundleInfo = bundleString.split(";");
			
			if(bundleInfo.length == 2){
				requireBundleList.add(new RequireBundleModel(bundleInfo[0],bundleInfo[1].replace("bundle-version=", "").replace("\"", "")));
			}
			
		}
		
		
		return requireBundleList;
	}
	
	

	


}
