package de.uniol.inf.is.odysseus.datadictionary;

import java.util.HashMap;
import java.util.Map;

public class DataDictionaryFactory {

	static private Map<String, IDataDictionary> cache = new HashMap<String, IDataDictionary>();
	
	static public IDataDictionary getDefaultDataDictionary(String name){
		IDataDictionary ret = cache.get(name);
		if (ret == null){
			ret = new DataDictionary();
			cache.put(name, ret);
		}
		return ret;
	}
	
}
