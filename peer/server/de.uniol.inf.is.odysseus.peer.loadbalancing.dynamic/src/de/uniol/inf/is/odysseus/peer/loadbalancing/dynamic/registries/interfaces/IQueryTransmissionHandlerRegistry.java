package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQueryTransmissionHandler;

/**
 * Registry for Query Transmission Handlers
 * @author Carsten Cordes
 *
 */
public interface IQueryTransmissionHandlerRegistry {
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void bindTransmissionHandler(IQueryTransmissionHandler serv);
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void unbindTransmissionHandler(IQueryTransmissionHandler serv);
	
	/**
	 * Gets Transmission Handler with specified name 
	 * @param name Name to look for
	 * @return Transmission Handler with specified Name or null if none is found.
	 */
	public IQueryTransmissionHandler getTransmissionHandler(String name);
	
	/**
	 * Checks if particular Transmission Handler is bound by regsitry.
	 * @param name Name to look for.
	 * @return true if Transmission Handler with name is bound
	 */
	public boolean isTransmissionHandlerBound(String name);
	
	/**
	 * Returns list of Transmission Handler names bound by registry
	 * @return list of Transmission Handler names bound by registry
	 */
	public Set<String> getRegisteredTransmissionHandlers();
}
