package de.uniol.inf.is.odysseus.net.recovery;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

public interface IRecoveryAllocator extends INamedInterface {

	/**
	 * Allocates a given collection of {@link ILogicalQueryPart}s to given
	 * nodes.
	 * 
	 * @param queryParts
	 *            The collection of {@link ILogicalQueryPart}s to allocate.
	 * @param query
	 *            The complete {@link ILogicalQuery} of <code>queryParts</code>,
	 *            so all {@link ILogicalQueryPart}s must belong to the same
	 *            {@link ILogicalQuery}.
	 * @param knownRemoteNodes
	 *            A collection of all known nodes. This collection does not
	 *            contain the local node.
	 * @param localNodeID
	 *            The local node.
	 * @return A mapping of nodes to {@link ILogicalQueryPart}s, indicating
	 *         which {@link ILogicalQueryPart} has been allocated to which node.
	 * @throws QueryPartAllocationException
	 *             if any error occurs.
	 */
	public Map<ILogicalQueryPart, OdysseusNodeID> allocate(Collection<ILogicalQueryPart> queryParts,
			ILogicalQuery query, Collection<OdysseusNodeID> knownRemoteNodes, OdysseusNodeID localNodeID)
			throws QueryPartAllocationException;

	/**
	 * Reallocates a given mapping of nodes to {@link ILogicalQueryPart}s.
	 * <br />
	 * This method may be used, if a first allocation
	 * ({@link #allocate(Collection, ILogicalQuery, Collection, OdysseusNodeID)})
	 * fails.
	 * 
	 * @param previousAllocationMap
	 *            A previous mapping of nodes to {@link ILogicalQueryPart}s,
	 *            indicating which {@link ILogicalQueryPart} has been allocated
	 *            to which node.
	 * @param faultPeers
	 *            A collection of nodes, where the first allocation failed.
	 * @param knownRemoteNodes
	 *            A collection of all known nodes. This collection does not
	 *            contain the local node.
	 * @param localNodeID
	 *            The local node.
	 * @return A mapping of nodes to {@link ILogicalQueryPart}s, indicating
	 *         which {@link ILogicalQueryPart} has been allocated to which node.
	 * @throws QueryPartAllocationException
	 *             if any error occurs.
	 */
	public Map<ILogicalQueryPart, OdysseusNodeID> reallocate(
			Map<ILogicalQueryPart, OdysseusNodeID> previousAllocationMap, Collection<OdysseusNodeID> faultNodes,
			Collection<OdysseusNodeID> knownRemoteNodes, OdysseusNodeID localNodeID)
			throws QueryPartAllocationException;
}
