package de.uniol.inf.is.odysseus.codegenerator.rcp.registry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry for special options for the targetPlatforms
 * 
 * @author MarcPreuschaft
 *
 */
public class RcpSpecialOptionRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(RcpSpecialOptionRegistry.class);
	
	//list of special options
	static Map<String, IRcpSpecialOption> rcpSpecialOptionList= new HashMap<String, IRcpSpecialOption>();
	
	/**
	 * registers a new rcpSpecialOption (OSGI method)
	 * 
	 * @param specialOption
	 */
	public static void registerRCPSpecialOption(IRcpSpecialOption specialOption){
		
		if(!rcpSpecialOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpecialOptionList.put(specialOption.getTargetPlatform().toLowerCase(), specialOption);
			LOG.info("RCP specialsOption for "+specialOption.getTargetPlatform() +" added!");
		}else{
			LOG.info("Targetplatform "+specialOption.getTargetPlatform() +" not found!");
		}
		
	}
	
	/**
	 * unregisters a rcpSpecialOption (OSGI method)
	 * 
	 * @param specialOption
	 */
	public static void unregisterRCPSpecialOption(IRcpSpecialOption specialOption){
		
		if(rcpSpecialOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpecialOptionList.remove(specialOption.getTargetPlatform().toLowerCase());
		}else{
			
		}
	}
	
	/**
	 * return a special option for a given targetPlatform name
	 * 
	 * @param targetPlatform
	 * @return
	 */
	public static IRcpSpecialOption getRCPSpeicalOption(String targetPlatform){
		if(rcpSpecialOptionList.containsKey(targetPlatform.toLowerCase())){
			return rcpSpecialOptionList.get(targetPlatform.toLowerCase());
		}else{
			return null;
		}
	}

}
