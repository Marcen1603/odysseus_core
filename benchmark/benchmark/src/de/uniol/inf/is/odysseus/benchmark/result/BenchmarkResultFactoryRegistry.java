package de.uniol.inf.is.odysseus.benchmark.result;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.monitoring.IDescriptiveStatistics;

public class BenchmarkResultFactoryRegistry {

	static Logger logger = LoggerFactory.getLogger(BenchmarkResultFactoryRegistry.class);

	private static Map<String, IBenchmarkResultFactory<?>> entries = new HashMap<String, IBenchmarkResultFactory<?>>();

	private static Map<String, IDescriptiveStatistics> statistics = new HashMap<String, IDescriptiveStatistics>();

	static public void addEntry(IBenchmarkResultFactory<?> entry) {
		String name = entry.getName();
		if (entries.containsKey(name.toLowerCase())) {
			logger.error("BenchmarkResultFactory " + name + " already registered! Ignoring new entry");
		} else {
			entries.put(name.toLowerCase(), entry);
			logger.debug("adding new BenchmarkResultFactory " + name);
		}
	}

	static public IBenchmarkResultFactory<?> getEntry(String name) {
		return entries.get(name.toLowerCase());
	}

	static public void removeEntry(IBenchmarkResultFactory<?> entry) {
		String name = entry.getName();
		if (!entries.containsKey(name.toLowerCase())) {
			logger.error("BenchmarkResultFactory " + name + " not registered! Ignoring removal call.");
		} else {
			entries.remove(name.toLowerCase());
		}
	}

	public static void addStatistic(String name, IDescriptiveStatistics stats) {
		String internalName = name.toLowerCase();
		if (statistics.containsKey(internalName)) {
			logger.error("BenchmarkResultFactory knows already a statistics type " + name + "! Ignoring new entry");
		} else {
			statistics.put(internalName, stats);
		}
	}

	public static IDescriptiveStatistics getStatistic(String name) {
		return statistics.get(name.toLowerCase());
	}

	public static void removeStatistics(String name) {
		String internalName = name.toLowerCase();
		if (statistics.containsKey(internalName)) {
			statistics.remove(internalName);
		} else {
			logger.error("Statistic type for BenchmarkResultFactories not registered!");
		}
	}

}
