package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;


/**
 * Registry for Communicator Choosers
 * @author Carsten Cordes
 *
 */
public interface ICommunicatorChooserRegistry {
	/**
	 * used by OSGI
	 * @param serv Instance
	 */
	public void bindCommunicatorChooser(ICommunicatorChooser serv);
	/**
	 * used by OSGi
	 * @param serv Instance
	 */
	public void unbindCommunicatorChooser(ICommunicatorChooser serv);
	
	/**
	 * Get Communicator Chooser by name
	 * @param name Name to look for
	 * @return Instance of Communicator chooser that fits name or null
	 */
	public ICommunicatorChooser getCommunicatorChooser(String name);
	
	/**
	 * Checks if Communicator Choser with specified name is bound.
	 * @param name Name to look for.
	 * @return true if Communciator Chooser with specified name is bound.
	 */
	public boolean isCommunicatorChooserBound(String name);
	
	/**
	 * Returns list of registered Communicator Chooser Names.
	 * @return list of registered Communciator Chooser Names
	 */
	public Set<String> getRegisteredCommunicatorChoosers();

}
