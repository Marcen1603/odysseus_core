package de.uniol.inf.is.odysseus.sweeparea;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class SweepAreaRegistry {
	static Logger logger = LoggerFactory.getLogger(SweepAreaRegistry.class);

	static private Map<String, ISweepArea<?>> areas = new HashMap<String, ISweepArea<?>>();

	static public void register(ISweepArea<?> area) {
		try {
			logger.trace("Register new area " + area.getName());
			if (!areas.containsKey(area.getName().toLowerCase())) {
				areas.put(area.getName().toLowerCase(), area);
			} else {
				logger.error("area with name " + area.getName()
						+ " already registered");
			}
		} catch (Exception e) {
			logger.error("Cannot register area " + area);
		}
	}

	static public void remove(ISweepArea<?> area) {
		logger.trace("Remove area " + area.getName());
		areas.remove(area.getName().toLowerCase());
	}

	static ISweepArea<?> getSweepArea(String name)
			throws InstantiationException, IllegalAccessException {
		return areas.get(name).getClass().newInstance();
	}

	static public ImmutableList<String> getAreaNames() {
		return ImmutableList.copyOf(areas.keySet());
	}

}
