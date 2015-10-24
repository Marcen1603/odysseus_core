package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;

/***
 * Interface for implementing custom communicator choosers for dynamic loadbalancing
 * @author Carsten Cordes
 *
 */
public interface ICommunicatorChooser extends INamedInterface {
	
	
	/**
	 * Gets Name of Communicator Chooser.
	 * @see de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface#getName()
	 */
	public String getName();
	
	/** 
	 * Chooses commincators for list of Queries.
	 * @param queryIds List of query IDs for which communicator should be chosen.
	 * @param session current Session
	 * @return Mapping between queryID and chosen Communciator
	 */
	public HashMap<Integer,ILoadBalancingCommunicator>  chooseCommunicators(List<Integer> queryIds, ISession session);
	
	/**
	 * Chooses communicator for a single Query.
     * @param queryId Query ID for which communicator should be chosen.
	 * @param session current Session
	 * @return Communicator that should be used for this Query.
	 */
	public ILoadBalancingCommunicator chooseCommunicator(int queryID, ISession session);
}
