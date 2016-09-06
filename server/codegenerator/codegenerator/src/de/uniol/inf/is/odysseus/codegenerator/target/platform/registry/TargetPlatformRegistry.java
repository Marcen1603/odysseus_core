package de.uniol.inf.is.odysseus.codegenerator.target.platform.registry;

import java.util.HashMap;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.target.platform.ITargetPlatform;

/**
 * Registry for targetPlatforms for the codegenerator 
 * 
 * @author MarcPreuschaft
 *
 */
public class TargetPlatformRegistry {
	private static Logger LOG = LoggerFactory.getLogger(TargetPlatformRegistry.class);
	
	// targetPlatforms for codegenerator
	static Map<String, ITargetPlatform> supportedTargetPlattforms = new HashMap<String, ITargetPlatform>();

	
	/**
	 * registers a new targetPlatform (OSGI method)
	 * 
	 * @param targetPlatform
	 */
	public static void registerTargetPlatform(ITargetPlatform targetPlatform){
		
		if(!supportedTargetPlattforms.containsKey(targetPlatform.getTargetPlatformName().toLowerCase())){
			supportedTargetPlattforms.put(targetPlatform.getTargetPlatformName().toLowerCase(), targetPlatform);
			LOG.info("Target platform "+targetPlatform.getTargetPlatformName() +" added!");
		}else{
			LOG.info("Targetplatform "+targetPlatform.getTargetPlatformName() +" already added!");
		}
	}
	
	
	/**
	 * unregisters a existing targetPlatform (OSGI method)
	 * 
	 * @param targetPlatform
	 */
	public static void unregisterTargetPlatform(ITargetPlatform targetPlatform){
		
		if(supportedTargetPlattforms.containsKey(targetPlatform.getTargetPlatformName().toLowerCase())){
			supportedTargetPlattforms.remove(targetPlatform.getTargetPlatformName().toLowerCase());
		}else{
			LOG.info("No targetplatform "+targetPlatform.getTargetPlatformName() +" found!");
		}
	}
	
	/**
	 * return a targetPlatform instanz for a given platformName
	 * 
	 * @param platformName
	 * @return targetPlatform
	 */
	public static ITargetPlatform getTargetPlatform(String platformName){
		if(supportedTargetPlattforms.containsKey(platformName.toLowerCase())){
			return supportedTargetPlattforms.get(platformName.toLowerCase());
		}else{
			LOG.info("No targetplatform "+platformName +" found!");
			return null;
		}	
	}
	
	/**
	 * return a keySet of all registed targetPlatforms
	 * 
	 * @return keySet of targetPlatforms
	 */
	public static Set<String> getAllTargetPlatform(){
			return supportedTargetPlattforms.keySet();
	}
	
	/**
	 * checks if the name of a targetPlatform is valid
	 * 
	 * @param targetPlatform
	 * @return 
	 */
	public static boolean existTargetPlatform(String targetPlatform){
		if(supportedTargetPlattforms.containsKey(targetPlatform.toLowerCase())){
			return true;
		}else{
			return false;
		}
	}
}
