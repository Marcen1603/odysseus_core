package de.uniol.inf.is.odysseus.benchmark.result;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkResultFactoryRegistry {

	static Logger logger = LoggerFactory
			.getLogger(BenchmarkResultFactoryRegistry.class);

	private static Map<String, IBenchmarkResultFactory<?>> entries = new HashMap<String, IBenchmarkResultFactory<?>>();

	static public void addEntry(IBenchmarkResultFactory<?> entry) {
		String name = entry.getName();
		if (entries.containsKey(name.toLowerCase())) {
			logger.error("BenchmarkResultFactory " + name
					+ " already registered! Ignoring new entry");
		}else{
			entries.put(name, entry);
			logger.debug("adding new BenchmarkResultFactory "+name);
		}
	}
	
	static public IBenchmarkResultFactory<?> getEntry(String name){
		return entries.get(name.toLowerCase());
	}
	
	static public void removeEntry(IBenchmarkResultFactory<?> entry){
		String name = entry.getName();
		if (!entries.containsKey(name.toLowerCase())) {
			logger.error("BenchmarkResultFactory " + name
					+ " not registered! Ignoring removal call.");
		}else{
			entries.remove(name.toLowerCase());
		}
	}

}
