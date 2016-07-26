package de.uniol.inf.is.odysseus.net.recovery.roundrobinallocator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.net.recovery.IRecoveryAllocator;

/**
 * The abstract round robin allocator holds the round robin algorithm. <br />
 * The nodes considered for allocation may differ for concrete implementations.
 * 
 * @author Simon Kuespert
 */
public abstract class AbstractRoundRobinAllocator implements IRecoveryAllocator {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRoundRobinAllocator.class);

	/**
	 * The {@link IOdysseusNodeManager}.
	 */
	private static IOdysseusNodeManager nodeManager;

	/**
	 * Binds an {@link IOdysseusNodeManager}.
	 */
	public static void bindNodeManager(IOdysseusNodeManager manager) {
		nodeManager = manager;
	}

	/**
	 * Removes the binding for an {@link IOdysseusNodeManager}.
	 */
	public static void unbindNodeManager(IOdysseusNodeManager manager) {
		if (manager == nodeManager) {
			nodeManager = null;
		}
	}

	@Override
	public Map<ILogicalQueryPart, OdysseusNodeID> allocate(Collection<ILogicalQueryPart> queryParts,
			ILogicalQuery query, Collection<OdysseusNodeID> knownRemoteNodes, OdysseusNodeID localNodeID)
			throws QueryPartAllocationException {

		LOG.debug("Begin round robin allocation");

		if (nodeManager == null) {

			throw new QueryPartAllocationException("No node manager set");

		}

		List<OdysseusNodeID> nodeIDs = determineConsideredNodeIDs(knownRemoteNodes, localNodeID);
		if (LOG.isDebugEnabled()) {

			logNodes(nodeIDs);

		}

		if (nodeIDs == null || nodeIDs.isEmpty()) {

			throw new QueryPartAllocationException("There are no nodes to be considered for round robin");

		}

		return roundRobinImpl(queryParts, nodeIDs);

	}

	@Override
	public Map<ILogicalQueryPart, OdysseusNodeID> reallocate(
			Map<ILogicalQueryPart, OdysseusNodeID> previousAllocationMap, Collection<OdysseusNodeID> faultyNodes,
			Collection<OdysseusNodeID> knownRemoteNodes, OdysseusNodeID localNodeID)
			throws QueryPartAllocationException {

		LOG.debug("Begin round robin reallocation");

		if (nodeManager == null) {

			throw new QueryPartAllocationException("No node manager set");

		}

		List<OdysseusNodeID> nodeIDs = determineConsideredNodeIDs(knownRemoteNodes, localNodeID);
		nodeIDs.removeAll(faultyNodes);
		if (LOG.isDebugEnabled()) {

			logNodes(nodeIDs);

		}

		if (nodeIDs.isEmpty()) {

			throw new QueryPartAllocationException(
					"There are no nodes left to be considered for round robin in reallocation");

		}

		return roundRobinImpl(previousAllocationMap.keySet(), nodeIDs);

	}

	/**
	 * The concrete round robin algorithm.
	 * 
	 * @param queryParts
	 *            A collection of query parts to allocate.
	 * @param nodeIDs
	 *            A list of all nodes considered for allocation.
	 * @return A mapping of node ids to {@link ILogicalQueryPart} (1:N mapping).
	 */
	private static Map<ILogicalQueryPart, OdysseusNodeID> roundRobinImpl(Collection<ILogicalQueryPart> queryParts,
			List<OdysseusNodeID> nodeIDs) {

		int nodeIDIndex = 0;
		Map<ILogicalQueryPart, OdysseusNodeID> allocationMap = Maps.newHashMap();

		for (ILogicalQueryPart queryPart : queryParts) {

			if (LOG.isDebugEnabled()) {

				LOG.debug("Allocating query part {}", queryPart);

				if (!queryPart.getAvoidingQueryParts().isEmpty()) {

					LOG.debug("Avoiding query parts");
					for (ILogicalQueryPart avoidedQueryPart : queryPart.getAvoidingQueryParts()) {

						LOG.debug("\t{}", avoidedQueryPart);

					}

				}

			}

			Collection<OdysseusNodeID> nonAllowedNodes = determineAvoidedNodes(queryPart, allocationMap);
			if (!nonAllowedNodes.isEmpty()) {

				LOG.debug("Avoiding nodes for this query part:");
				logNodes(nonAllowedNodes);

			}

			int tries = 0;
			int tempNodeIDIndex = nodeIDIndex;
			OdysseusNodeID chosenNodeID = null;

			do {

				if (tries >= nodeIDs.size()) {

					// checked all possibilities
					chosenNodeID = null;
					break;

				}

				tempNodeIDIndex = (tempNodeIDIndex + 1) % nodeIDs.size();
				chosenNodeID = nodeIDs.get(tempNodeIDIndex);
				tries++;

			} while (nonAllowedNodes.contains(chosenNodeID));

			if (chosenNodeID == null) {

				LOG.debug("Could not allocate to a node that is not avoided...");
				nodeIDIndex = (nodeIDIndex + 1) % nodeIDs.size();
				chosenNodeID = nodeIDs.get(nodeIDIndex);

			} else {

				LOG.debug("Could allocate to non-avoided node");
				nodeIDIndex = tempNodeIDIndex;

			}

			LOG.debug("Allocated to node {} for query part {}", nodeManager.getNode(chosenNodeID), queryPart);
			allocationMap.put(queryPart, chosenNodeID);

		}

		LOG.debug("RoundRobinAllocation finished");
		return allocationMap;

	}

	/**
	 * Log the names of given nodes.
	 * 
	 * @param nodeIDs
	 *            The given nodes.
	 */
	private static void logNodes(Collection<OdysseusNodeID> nodeIDs) {

		if (nodeIDs == null || nodeIDs.isEmpty()) {

			LOG.debug("\t<empty>");
			return;

		}

		for (OdysseusNodeID nodeID : nodeIDs) {
			LOG.debug("\t{}", nodeManager.getNode(nodeID));
		}
	}

	/**
	 * Determine all nodes to be avoided for a given {@link ILogicalQueryPart}.
	 * 
	 * @param queryPart
	 *            The given {@link ILogicalQueryPart}.
	 * @param allocationMap
	 *            The allocation map (peers to query parts).
	 * @return A list of all nodes to avoid for <code>queryPart</code>.
	 */
	private static Collection<OdysseusNodeID> determineAvoidedNodes(ILogicalQueryPart queryPart,
			Map<ILogicalQueryPart, OdysseusNodeID> allocationMap) {

		Collection<OdysseusNodeID> avoidedNodes = Lists.newArrayList();

		for (ILogicalQueryPart avoidedPart : queryPart.getAvoidingQueryParts()) {

			OdysseusNodeID avoidedNodeID = allocationMap.get(avoidedPart);

			if (avoidedNodeID != null && !avoidedNodes.contains(avoidedNodeID)) {

				avoidedNodes.add(avoidedNodeID);

			}

		}

		return avoidedNodes;

	}

	/**
	 * Determine all nodes, the round robin allocator can use.
	 * 
	 * @param knownRemoteNodes
	 *            A collection of all known remote nodes by ID.
	 * @param localNodeID
	 *            The ID of the local node.
	 * @return A list of node ids, the round robin allocator can use for
	 *         allocation.
	 */
	protected abstract List<OdysseusNodeID> determineConsideredNodeIDs(Collection<OdysseusNodeID> knownRemoteNodes,
			OdysseusNodeID localNodeID);

}