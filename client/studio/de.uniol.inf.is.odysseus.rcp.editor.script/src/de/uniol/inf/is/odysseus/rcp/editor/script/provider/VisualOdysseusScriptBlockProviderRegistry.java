package de.uniol.inf.is.odysseus.rcp.editor.script.provider;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProvider;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProviders;

public final class VisualOdysseusScriptBlockProviderRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(VisualOdysseusScriptBlockProviderRegistry.class);
	
	private static VisualOdysseusScriptBlockProviderRegistry instance;
	
	private final Map<String, IVisualOdysseusScriptBlockProvider> providerMap = Maps.newHashMap();

	// called by OSGi-DS
	public void bindVisualOdysseusScriptBlockProvider(IVisualOdysseusScriptBlockProvider serv) {
		providerMap.put(serv.getName(), serv);
	}

	// called by OSGi-DS
	public void unbindVisualOdysseusScriptBlockProvider(IVisualOdysseusScriptBlockProvider serv) {
		providerMap.remove(serv.getName(), serv);
	}

	// called by OSGi-DS
	public void bindVisualOdysseusScriptBlockProviders(IVisualOdysseusScriptBlockProviders serv) {
		for (IVisualOdysseusScriptBlockProvider provider : serv.getProviders()) {
			bindVisualOdysseusScriptBlockProvider(provider);
		}
	}

	// called by OSGi-DS
	public void unbindVisualOdysseusScriptBlockProviders(IVisualOdysseusScriptBlockProviders serv) {
		for (IVisualOdysseusScriptBlockProvider provider : serv.getProviders()) {
			unbindVisualOdysseusScriptBlockProvider(provider);
		}
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}
	
	public static VisualOdysseusScriptBlockProviderRegistry getInstance() {
		return instance;
	}
	
	public Collection<String> getProviderNames() {
		return Lists.newArrayList(providerMap.keySet());
	}
	
	public Optional<IVisualOdysseusScriptBlockProvider> getProvider( String name ) {
		return Optional.fromNullable(providerMap.get(name));
	}
	
	public Optional<IVisualOdysseusScriptBlock> create( String name ) {
		IVisualOdysseusScriptBlockProvider provider = providerMap.get(name);
		if( provider == null ) {
			return Optional.absent();
		}
		
		try {
			IVisualOdysseusScriptBlock block = provider.create();
			return Optional.fromNullable(block);
		} catch( Throwable t ) {
			LOG.error("Could not create visual odysseus script block from provider {}", name, t);
			return Optional.absent();
		}
	}
}
