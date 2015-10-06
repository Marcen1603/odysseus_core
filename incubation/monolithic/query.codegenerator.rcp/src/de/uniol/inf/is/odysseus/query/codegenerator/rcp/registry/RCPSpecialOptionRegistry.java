package de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry;

import java.util.HashMap;
import java.util.Map;

public class RCPSpecialOptionRegistry {
	
	static Map<String, IRCPSpecialOption> rcpSpeicalOptionList= new HashMap<String, IRCPSpecialOption>();
	
	
	
	public static void registerRCPSpeicalOption(IRCPSpecialOption specialOption){
		
		if(!rcpSpeicalOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpeicalOptionList.put(specialOption.getTargetPlatform().toLowerCase(), specialOption);
		}
		
	}
	
	
	public static void unregisterRCPSpeicalOption(IRCPSpecialOption specialOption){
		
		if(rcpSpeicalOptionList.containsKey(specialOption.getTargetPlatform().toLowerCase())){
			rcpSpeicalOptionList.remove(specialOption.getTargetPlatform().toLowerCase());
		}
		
		
	}
	
	
	public static IRCPSpecialOption getRCPSpeicalOption(String targetPlatform){
		
		if(rcpSpeicalOptionList.containsKey(targetPlatform.toLowerCase())){
			return rcpSpeicalOptionList.get(targetPlatform.toLowerCase());
			
		}else{
			return null;
		}
		
	}

}
