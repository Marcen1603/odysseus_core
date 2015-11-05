package de.uniol.inf.is.odysseus.codegenerator.rcp.registry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RcpSpecialOptionRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(RcpSpecialOptionRegistry.class);
	static Map<String, IRcpSpecialOption> rcpSpecialOptionList= new HashMap<String, IRcpSpecialOption>();
	
	public static void registerRCPSpecialOption(IRcpSpecialOption specialOption){
		
		if(!rcpSpecialOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpecialOptionList.put(specialOption.getTargetPlatform().toLowerCase(), specialOption);
			LOG.info("RCP specialsOption for "+specialOption.getTargetPlatform() +" added!");
		}else{
			LOG.info("Targetplatform "+specialOption.getTargetPlatform() +" not found!");
		}
		
	}
	
	public static void unregisterRCPSpecialOption(IRcpSpecialOption specialOption){
		
		if(rcpSpecialOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpecialOptionList.remove(specialOption.getTargetPlatform().toLowerCase());
		}else{
			
		}
	}
	
	public static IRcpSpecialOption getRCPSpeicalOption(String targetPlatform){
		if(rcpSpecialOptionList.containsKey(targetPlatform.toLowerCase())){
			return rcpSpecialOptionList.get(targetPlatform.toLowerCase());
			
		}else{
			return null;
		}
	}

}
