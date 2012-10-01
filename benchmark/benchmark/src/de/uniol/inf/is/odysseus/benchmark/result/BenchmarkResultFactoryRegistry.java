package de.uniol.inf.is.odysseus.benchmark.result;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkResultFactoryRegistry {

	static Logger logger = LoggerFactory
			.getLogger(BenchmarkResultFactoryRegistry.class);

	private static Map<String, IBenchmarkResultFactory<?>> entries = new HashMap<String, IBenchmarkResultFactory<?>>();

	static void addEntry(IBenchmarkResultFactory<?> entry) {
		String name = entry.getName();
		if (entries.containsKey(name)) {
			logger.error("BenchmarkResultFactory " + name
					+ " already registered! Ignoring new entry");
		}else{
			entries.put(name, entry);
		}
	}
	
	static IBenchmarkResultFactory<?> getEntry(String name){
		return entries.get(name);
	}

}
