package de.uniol.inf.is.odysseus.core.infoservice;

import java.util.HashMap;
import java.util.Map;


public class InfoServiceFactory {

	static final Map<String,InfoService> infoServices = new HashMap<>();
	
	static public InfoService getInfoService(Class<?> name){
		return getInfoService(name.getName());
	}	
	
	static public InfoService getInfoService(String name){
		InfoService service = infoServices.get(name);
		if (service == null){
			service = new InfoService(name);
			infoServices.put(name, service);
		}
		return service;
	}
}
