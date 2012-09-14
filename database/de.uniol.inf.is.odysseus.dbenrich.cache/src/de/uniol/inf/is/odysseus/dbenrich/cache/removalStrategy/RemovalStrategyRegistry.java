package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

/* Note: Based on a copy of TransportHandlerRegistry */
public class RemovalStrategyRegistry {

	static Logger logger = LoggerFactory
			.getLogger(RemovalStrategyRegistry.class);

	static private Map<String, IRemovalStrategy> strategies = new HashMap<>();

	static public void register(IRemovalStrategy strategy) {
		logger.debug("Register new RemovalStrategy " + strategy.getName());
		if (!strategies.containsKey(strategy.getName().toLowerCase())) {
			strategies.put(strategy.getName().toLowerCase(), strategy);
		} else {
			logger.error("Strategy with name " + strategy.getName()
					+ " already registered");
		}
	}

	static public void remove(IRemovalStrategy strategy) {
		logger.debug("Remove strategy " + strategy.getName());
		strategies.remove(strategy.getName().toLowerCase());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public IRemovalStrategy getInstance(String name, Map cacheStore) {
		IRemovalStrategy ret = strategies.get(name.toLowerCase());
		if (ret != null) {
			return ret.createInstance(cacheStore);
		}
		logger.error("No strategy with name " + name + " found!");
		return null;
	}

	public static ImmutableList<String> getStrategyNames() {
		return ImmutableList.copyOf(strategies.keySet());
	}
}
