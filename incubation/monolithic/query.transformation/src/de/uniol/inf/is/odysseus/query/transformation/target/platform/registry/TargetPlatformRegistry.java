package de.uniol.inf.is.odysseus.query.transformation.target.platform.registry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.query.transformation.target.platform.ITargetPlatform;

public class TargetPlatformRegistry {
	private static Logger LOG = LoggerFactory.getLogger(TargetPlatformRegistry.class);
	
	static Map<String, ITargetPlatform> supportedTargetPlattforms = new HashMap<String, ITargetPlatform>();

	public static void registerTargetPlatform(ITargetPlatform targetPlatform){
		
		if(!supportedTargetPlattforms.containsKey(targetPlatform.getTargetPlatformName().toLowerCase())){
			supportedTargetPlattforms.put(targetPlatform.getTargetPlatformName().toLowerCase(), targetPlatform);
		}else{
			LOG.debug("Target platform "+targetPlatform.getTargetPlatformName() +" already added!");
		}
	}
	
	public static void unregisterTargetPlatform(ITargetPlatform targetPlatform){
		
		if(supportedTargetPlattforms.containsKey(targetPlatform.getTargetPlatformName().toLowerCase())){
			supportedTargetPlattforms.remove(targetPlatform.getTargetPlatformName().toLowerCase());
		}else{
			LOG.debug("No target platform "+targetPlatform.getTargetPlatformName() +" found!");
		}
	}
	
	
	
	public static ITargetPlatform getTargetPlatform(String platformName){
		if(supportedTargetPlattforms.containsKey(platformName.toLowerCase())){
			return supportedTargetPlattforms.get(platformName.toLowerCase());
		}else{
			LOG.debug("No target platform "+platformName +" found!");
			return null;
		}	
	}
}
