package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;


public interface ICommunicatorChooserRegistry {
	public void bindCommunicatorChooser(ICommunicatorChooser serv);
	public void unbindCommunicatorChooser(ICommunicatorChooser serv);
	public ICommunicatorChooser getCommunicatorChooser(String name);
	public boolean isCommunicatorChooserBound(String name);
	public Set<String> getRegisteredCommunicatorChoosers();

}
