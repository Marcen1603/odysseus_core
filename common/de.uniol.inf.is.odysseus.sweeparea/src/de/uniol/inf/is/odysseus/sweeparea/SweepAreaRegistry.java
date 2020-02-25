package de.uniol.inf.is.odysseus.sweeparea;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class SweepAreaRegistry {
	static Logger logger = LoggerFactory.getLogger(SweepAreaRegistry.class);

	public static final String LEFTINPUTSCHEMA = "SweepAreaRegistry.LEFTSCHEMA";
	public static final String RIGHTINPUTSCHEMA = "SweepAreaRegistry.RIGHTSCHEMA";
	public static final String PREDICATE = "SweepAreaRegistry.PREDICATE";
	
	static private Map<String, ISweepArea<?>> areas = new HashMap<String, ISweepArea<?>>();

	public SweepAreaRegistry(){
		// Default Constructor
		logger.trace("Created new SweepAreaRegistry");
	}
	
	public void register(ISweepArea<?> area) {
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

	public void remove(ISweepArea<?> area) {
		logger.trace("Remove area " + area.getName());
		areas.remove(area.getName().toLowerCase());
	}

	@Deprecated
	// use getSweepArea(String name, OptionMap options)
	public static ISweepArea<?> getSweepArea(String name)
			throws InstantiationException, IllegalAccessException {
		return getSweepArea(name, null);
	}
	
	public static ISweepArea<?> getSweepArea(String name, OptionMap options, SDFSchema leftSchema, SDFSchema rightSchema, IPredicate<?> predicate)
			throws InstantiationException, IllegalAccessException {
		OptionMap clonedOptions = new OptionMap(options);
		clonedOptions.setOption(LEFTINPUTSCHEMA, leftSchema);
		clonedOptions.setOption(RIGHTINPUTSCHEMA, rightSchema);
		clonedOptions.setOption(PREDICATE, predicate);
		return getSweepArea(name, clonedOptions);
	}
	
	public static ISweepArea<?> getSweepArea(String name, OptionMap options)
			throws InstantiationException, IllegalAccessException {
		ISweepArea<?> a = areas.get(name.toLowerCase());
		if (a != null) {
			return a.newInstance(options);
		}
		return null;
	}

	static public ImmutableList<String> getAreaNames() {
		return ImmutableList.copyOf(areas.keySet());
	}

}
