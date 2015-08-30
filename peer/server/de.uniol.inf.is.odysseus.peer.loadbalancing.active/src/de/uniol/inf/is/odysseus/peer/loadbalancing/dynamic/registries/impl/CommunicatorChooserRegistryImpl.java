package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ICommunicatorChooserRegistry;

public class CommunicatorChooserRegistryImpl extends AbstractInterfaceRegistry<ICommunicatorChooser> implements ICommunicatorChooserRegistry {

	@Override
	public void bindCommunicatorChooser(ICommunicatorChooser serv) {
		bindInstance(serv);
		
	}

	@Override
	public void unbindCommunicatorChooser(ICommunicatorChooser serv) {
		unbindInstance(serv);
		
	}

	@Override
	public ICommunicatorChooser getCommunicatorChooser(String name) {
		return getInstance(name);
	}

	@Override
	public boolean isCommunicatorChooserBound(String name) {
		return isInstanceBound(name);
	}

	@Override
	public Set<String> getRegisteredCommunicatorChoosers() {
		return getRegisteredInstances();
	}

}
