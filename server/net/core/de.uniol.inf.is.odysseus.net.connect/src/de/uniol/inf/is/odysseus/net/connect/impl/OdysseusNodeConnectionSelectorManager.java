package de.uniol.inf.is.odysseus.net.connect.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionSelector;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionSelectorManager;

public class OdysseusNodeConnectionSelectorManager implements IOdysseusNodeConnectionSelectorManager {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeConnectionSelectorManager.class);
	
	private final Map<String, IOdysseusNodeConnectionSelector> selectorMap = Maps.newHashMap();
	
	// called by OSGi-DS
	public void bindOdysseusNodeConnectionSelector(IOdysseusNodeConnectionSelector serv) {
		String selectorName = serv.getClass().getSimpleName();
		if( !selectorMap.containsKey(selectorName)) {
			selectorMap.put(selectorName, serv);
			
			LOG.info("Added odysseus node connection selector {}", serv);
		}
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeConnectionSelector(IOdysseusNodeConnectionSelector serv) {
		String selectorName = serv.getClass().getSimpleName();
		if( selectorMap.containsKey(selectorName)) {
			selectorMap.remove(selectorName);
			
			LOG.info("Removed odysseus node connection selector {}", serv);
		}
	}

	@Override
	public ImmutableCollection<String> getSelectorNames() {
		return ImmutableList.copyOf(selectorMap.keySet());
	}

	@Override
	public Optional<IOdysseusNodeConnectionSelector> getSelector(String selectorName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(selectorName), "Selectorname must not be null or empty!");

		return Optional.fromNullable(selectorMap.get(selectorName));
	}
	
}
