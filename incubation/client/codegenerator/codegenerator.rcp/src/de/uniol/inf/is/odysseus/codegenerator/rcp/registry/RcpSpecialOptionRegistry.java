package de.uniol.inf.is.odysseus.codegenerator.rcp.registry;

import java.util.HashMap;
import java.util.Map;

public class RcpSpecialOptionRegistry {
	
	static Map<String, IRcpSpecialOption> rcpSpeicalOptionList= new HashMap<String, IRcpSpecialOption>();
	
	
	public static void registerRCPSpeicalOption(IRcpSpecialOption specialOption){
		
		if(!rcpSpeicalOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpeicalOptionList.put(specialOption.getTargetPlatform().toLowerCase(), specialOption);
		}
		
	}
	
	public static void unregisterRCPSpeicalOption(IRcpSpecialOption specialOption){
		
		if(rcpSpeicalOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpeicalOptionList.remove(specialOption.getTargetPlatform().toLowerCase());
		}
	}
	
	public static IRcpSpecialOption getRCPSpeicalOption(String targetPlatform){
		
		if(rcpSpeicalOptionList.containsKey(targetPlatform.toLowerCase())){
			return rcpSpeicalOptionList.get(targetPlatform.toLowerCase());
			
		}else{
			return null;
		}
		
	}

}
